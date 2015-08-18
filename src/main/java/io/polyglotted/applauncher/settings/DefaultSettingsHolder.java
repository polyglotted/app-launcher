package io.polyglotted.applauncher.settings;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@RequiredArgsConstructor
public class DefaultSettingsHolder implements SettingsHolder {

    private final Config config;

    public DefaultSettingsHolder() {
        this.config = ConfigFactory.load();
    }

    private static final InvocationHandler MethodProxyInvocationHandler = methodProxyInvocationHandler();
    private static final Map<Class<?>, PropertyType<?>> DefaultPropertyTypes = ImmutableMap.<Class<?>, PropertyType<?>>builder()
            .put(int.class, new IntegerPropertyType())
            .put(Integer.class, new IntegerPropertyType())
            .put(long.class, new LongPropertyType())
            .put(Long.class, new LongPropertyType())
            .put(boolean.class, new BooleanPropertyType())
            .put(Boolean.class, new BooleanPropertyType())
            .put(double.class, new DoublePropertyType())
            .put(Double.class, new DoublePropertyType())
            .put(String.class, new StringPropertyType())
            .build();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T proxy(Class<T> configurationInterface) {
        try {
            if (!configurationInterface.isInterface()) {
                throw new SettingsException(configurationInterface.getName() + " is not an interface");
            } else if (!Modifier.isPublic(configurationInterface.getModifiers())) {
                throw new SettingsException(configurationInterface.getName() + " is not public");
            } else if (!configurationInterface.isAnnotationPresent(Settings.class)) {
                throw new SettingsException(configurationInterface.getName() + " must be annotated with @Settings annotation");
            }

            Class<?> proxyClass = Proxy.getProxyClass(configurationInterface.getClassLoader(), configurationInterface);
            InvocationHandlerImpl invocationHandler = buildInvocationHandler(configurationInterface);
            return (T) proxyClass.getConstructor(InvocationHandler.class).newInstance(invocationHandler);

        } catch (SettingsException e) {
            throw e;
        } catch (Exception e) {
            throw new SettingsException(e);
        }
    }

    @Override
    public Properties asProperties(String prefix, boolean includePrefix) {
        final Properties props = new Properties();
        config.entrySet().parallelStream().filter(entry -> entry.getKey().startsWith(prefix)).forEach(entry -> {
            String key = includePrefix ? entry.getKey() : entry.getKey().replace(prefix + ".", "");
            props.put(key, entry.getValue().unwrapped());
        });
        return props;
    }

    @Override
    public boolean hasValue(String name) {
        return config.hasPath(name);
    }

    @Override
    public String stringValue(String name) {
        return config.getString(name);
    }

    @Override
    public int intValue(String name) {
        return config.getInt(name);
    }

    @Override
    public boolean booleanValue(String name) {
        return config.getBoolean(name);
    }

    @Override
    public long longValue(String name) {
        return config.getLong(name);
    }

    @Override
    public double doubleValue(String name) {
        return config.getDouble(name);
    }

    private <T> InvocationHandlerImpl buildInvocationHandler(Class<T> configurationInterface) {
        Map<String, Object> result = new HashMap<>();
        Method[] declaredMethods = configurationInterface.getDeclaredMethods();
        Object classProxy = Proxy.newProxyInstance(configurationInterface.getClassLoader(),
                new Class[]{configurationInterface}, MethodProxyInvocationHandler);

        for (Method method : declaredMethods) {
            if (!method.isAnnotationPresent(Attribute.class)) continue;

            validateParameterTypes(method);
            Object value = resolvePropertyValue(method, classProxy);
            result.put(method.getName(), value);
        }

        if (result.isEmpty()) {
            throw new SettingsException("No properties are defined in @Settings "
                    + configurationInterface.getName());
        }
        return new InvocationHandlerImpl(result);
    }

    @SneakyThrows(Exception.class)
    private Object resolvePropertyValue(Method method, Object classProxy) {
        Attribute propertyAnnotation = method.getAnnotation(Attribute.class);
        PropertyType propertyType = resolvePropertyType(method, propertyAnnotation.optional());
        String propertyName = propertyAnnotation.name();

        Optional value = Optional.empty();
        if (config.hasPath(propertyName)) {
            value = Optional.of(propertyType.getValue(config, propertyName));

        } else if (method.isDefault()) {
            value = Optional.of(method.invoke(classProxy));
        }

        if (!value.isPresent() && !propertyAnnotation.optional()) {
            throw new SettingsException("Missing property " + propertyName +
                    " expected in configuration " + method.getDeclaringClass().getName() + ".");
        }
        return propertyAnnotation.optional() ? value : value.get();
    }

    private PropertyType resolvePropertyType(Method method, boolean optional) {
        Class<?> returnType = method.getReturnType();
        if (optional) {
            returnType = resolveOptionalPropertyType(method, returnType);
        }

        PropertyType propertyType = DefaultPropertyTypes.get(returnType);
        if (propertyType == null) {
            throw new SettingsException("Method " + method.getName() + " of interface " + method.getDeclaringClass().getName() +
                    " annotated with @Attribute has unsupported return type. Only the following types are supported: "
                    + DefaultPropertyTypes.keySet());
        }
        return propertyType;
    }

    private Class<?> resolveOptionalPropertyType(Method method, Class<?> returnType) {
        if (!returnType.isAssignableFrom(Optional.class)) {
            throw new SettingsException("Method " + method.getName() + " of interface "
                    + method.getDeclaringClass().getName() + " annotated with " + Attribute.class.getName()
                    + " with Optional = true but has a return type different from java.util.Optional.");
        }
        Type returnTypeGeneric = method.getGenericReturnType();
        Type[] actualTypeArguments = ((ParameterizedType) returnTypeGeneric).getActualTypeArguments();
        returnType = (Class) actualTypeArguments[0];
        return returnType;
    }

    private void validateParameterTypes(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes != null && parameterTypes.length > 0) {
            throw new SettingsException("Attribute method declaration has parameters: " + method.getName()
                    + " in configuration interface " + method.getDeclaringClass());
        }
    }

    private static InvocationHandler methodProxyInvocationHandler() {
        return (proxy, method, args) -> {
            if (method.isDefault()) {
                final Class<?> declaringClass = method.getDeclaringClass();
                final MethodHandles.Lookup lookup = MethodHandles.publicLookup()
                        .in(declaringClass);

                final Field f = MethodHandles.Lookup.class.getDeclaredField("allowedModes");
                final int modifiers = f.getModifiers();
                if (Modifier.isFinal(modifiers)) { // should be done a single time
                    final Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(f, modifiers & ~Modifier.FINAL);
                    f.setAccessible(true);
                    f.set(lookup, MethodHandles.Lookup.PRIVATE);
                }

                return lookup.unreflectSpecial(method, declaringClass)
                        .bindTo(proxy).invokeWithArguments(args);
            }
            return null;
        };
    }

    private interface PropertyType<T> {
        T getValue(Config source, String name);
    }

    private static class StringPropertyType implements PropertyType<String> {
        public String getValue(Config source, String property) {
            return source.getString(property);
        }
    }

    private static class IntegerPropertyType implements PropertyType<Integer> {
        public Integer getValue(Config source, String property) {
            return source.getInt(property);
        }
    }

    private static class BooleanPropertyType implements PropertyType<Boolean> {
        public Boolean getValue(Config source, String property) {
            return source.getBoolean(property);
        }
    }

    private static class LongPropertyType implements PropertyType<Long> {
        public Long getValue(Config source, String property) {
            return source.getLong(property);
        }
    }

    private static class DoublePropertyType implements PropertyType<Double> {
        public Double getValue(Config source, String property) {
            return source.getDouble(property);
        }
    }

    private static class InvocationHandlerImpl implements InvocationHandler {
        private final Map<String, Object> config;

        private InvocationHandlerImpl(Map<String, Object> config) {
            this.config = config;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return config.get(method.getName());
        }
    }
}

