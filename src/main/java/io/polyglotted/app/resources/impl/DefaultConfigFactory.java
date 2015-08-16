package io.polyglotted.app.resources.impl;

import com.google.common.collect.ImmutableMap;
import io.polyglotted.app.resources.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class DefaultConfigFactory implements ConfigFactory {

    private final ValuesProvider sourceProvider;

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
    public <T> T properties(Class<T> configurationInterface) {
        return properties(configurationInterface, Optional.<String>empty());
    }

    @Override
    public <T> T properties(Class<T> configurationInterface, String source) {
        return properties(configurationInterface, Optional.of(source));
    }

    @SuppressWarnings("unchecked")
    private <T> T properties(Class<T> configurationInterface, Optional<String> source) {
        try {
            if (!configurationInterface.isInterface()) {
                throw new ConfigException(configurationInterface.getName() + " is not an interface");
            } else if (!Modifier.isPublic(configurationInterface.getModifiers())) {
                throw new ConfigException(configurationInterface.getName() + " is not public");
            } else if (!configurationInterface.isAnnotationPresent(Config.class)) {
                throw new ConfigException(configurationInterface.getName() + " must be annotated with @Config annotation");
            }

            Class<?> proxyClass = Proxy.getProxyClass(configurationInterface.getClassLoader(), configurationInterface);
            InvocationHandlerImpl invocationHandler = buildInvocationHandler(configurationInterface, source);
            return (T) proxyClass.getConstructor(InvocationHandler.class).newInstance(invocationHandler);

        } catch (ConfigException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigException(e);
        }
    }

    private <T> InvocationHandlerImpl buildInvocationHandler(Class<T> configurationInterface, Optional<String> source) {
        String sourceName = source.isPresent() ? source.get() :
                configurationInterface.getAnnotation(Config.class).source();
        Values values = sourceProvider.values(sourceName);

        Map<String, Object> result = new HashMap<>();
        Method[] declaredMethods = configurationInterface.getDeclaredMethods();
        Object classProxy = Proxy.newProxyInstance(configurationInterface.getClassLoader(),
                new Class[]{configurationInterface}, MethodProxyInvocationHandler);

        for (Method method : declaredMethods) {
            if (!method.isAnnotationPresent(Property.class)) continue;

            validateParameterTypes(method);
            Object value = resolvePropertyValue(values, method, classProxy);
            result.put(method.getName(), value);
        }

        if (result.isEmpty()) {
            throw new ConfigException("No properties are defined in @Config "
                    + configurationInterface.getName());
        }
        return new InvocationHandlerImpl(result);
    }

    @SneakyThrows(Exception.class)
    private Object resolvePropertyValue(Values source, Method method, Object classProxy) {
        Property propertyAnnotation = method.getAnnotation(Property.class);
        PropertyType propertyType = resolvePropertyType(method, propertyAnnotation.optional());
        String propertyName = propertyAnnotation.name();

        Optional value = Optional.empty();
        if (source.hasValue(propertyName)) {
            value = Optional.of(propertyType.getValue(source, propertyName));

        } else if (method.isDefault()) {
            value = Optional.of(method.invoke(classProxy));
        }

        if (!value.isPresent() && !propertyAnnotation.optional()) {
            throw new ConfigException("Missing property " + propertyName +
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
            throw new ConfigException("Method " + method.getName() + " of interface " + method.getDeclaringClass().getName() +
                    " annotated with @Property has unsupported return type. Only the following types are supported: "
                    + DefaultPropertyTypes.keySet());
        }
        return propertyType;
    }

    private Class<?> resolveOptionalPropertyType(Method method, Class<?> returnType) {
        if (!returnType.isAssignableFrom(Optional.class)) {
            throw new ConfigException("Method " + method.getName() + " of interface "
                    + method.getDeclaringClass().getName() + " annotated with " + Property.class.getName()
                    + " with Optionalal = true but has a return type different from fj.data.Optional.");
        }
        Type returnTypeGeneric = method.getGenericReturnType();
        Type[] actualTypeArguments = ((ParameterizedType) returnTypeGeneric).getActualTypeArguments();
        returnType = (Class) actualTypeArguments[0];
        return returnType;
    }

    private void validateParameterTypes(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes != null && parameterTypes.length > 0) {
            throw new ConfigException("Property method declaration has parameters: " + method.getName()
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
        T getValue(Values source, String name);

        T getDefaultValue(String value);
    }

    private static class StringPropertyType implements PropertyType<String> {
        public String getValue(Values source, String property) {
            return source.stringValue(property);
        }

        public String getDefaultValue(String defaultValue) {
            return defaultValue;
        }
    }

    private static class IntegerPropertyType implements PropertyType<Integer> {
        public Integer getValue(Values source, String property) {
            return source.intValue(property);
        }

        public Integer getDefaultValue(String defaultValue) {
            return Integer.parseInt(defaultValue);
        }
    }

    private static class BooleanPropertyType implements PropertyType<Boolean> {
        public Boolean getValue(Values source, String property) {
            return source.booleanValue(property);
        }

        public Boolean getDefaultValue(String defaultValue) {
            return Boolean.parseBoolean(defaultValue);
        }
    }

    private static class LongPropertyType implements PropertyType<Long> {
        public Long getValue(Values source, String property) {
            return source.longValue(property);
        }

        public Long getDefaultValue(String defaultValue) {
            return Long.parseLong(defaultValue);
        }
    }

    private static class DoublePropertyType implements PropertyType<Double> {
        public Double getValue(Values source, String property) {
            return source.doubleValue(property);
        }

        public Double getDefaultValue(String defaultValue) {
            return Double.parseDouble(defaultValue);
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

