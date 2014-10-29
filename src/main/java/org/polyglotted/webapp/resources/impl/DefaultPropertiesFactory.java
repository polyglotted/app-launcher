package org.polyglotted.webapp.resources.impl;

import com.google.common.collect.ImmutableMap;
import fj.data.Option;
import lombok.RequiredArgsConstructor;
import org.polyglotted.webapp.resources.*;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class DefaultPropertiesFactory implements PropertiesFactory {

    private final ValuesProvider sourceProvider;

    private final Map<Class<?>, PropertyType<?>> propertyTypes = ImmutableMap.<Class<?>, PropertyType<?>>builder()
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
        return properties(configurationInterface, Option.<String>none());
    }

    @Override
    public <T> T properties(Class<T> configurationInterface, String source) {
        return properties(configurationInterface, Option.some(source));
    }

    @SuppressWarnings("unchecked")
    private <T> T properties(Class<T> configurationInterface, Option<String> source) {
        try {
            if (!configurationInterface.isInterface()) {
                throw new ConfigException(configurationInterface.getName() + " is not an interface.");
            }
            if (!configurationInterface.isAnnotationPresent(Properties.class)) {
                throw new ConfigException("Configuration class must be annotated with " + Properties.class.getName());
            }
            InvocationHandlerImpl invocationHandler = buildInvocationHandler(configurationInterface, source);

            Class<?> proxyClass = Proxy.getProxyClass(configurationInterface.getClassLoader(), new Class[]{configurationInterface});

            return (T) proxyClass.getConstructor(InvocationHandler.class).newInstance(invocationHandler);
        } catch (ConfigException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigException(e);
        }
    }

    private <T> InvocationHandlerImpl buildInvocationHandler(Class<T> configurationInterface, Option<String> source) {
        String sourceName = source.isSome() ? source.some() : configurationInterface.getAnnotation(Properties.class).source();
        Values values = sourceProvider.values(sourceName);

        Map<String, Object> result = new HashMap<>();
        Method[] declaredMethods = configurationInterface.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (!isPropertyMethod(method)) continue;

            validateParameterTypes(method);

            Object value = resolvePropertyValue(values, method);

            result.put(method.getName(), value);
        }
        if (result.isEmpty()) {
            throw new ConfigException("No configuration is defined in configuration interface " + configurationInterface.getName());
        }

        return new InvocationHandlerImpl(result);
    }

    private Object resolvePropertyValue(Values source, Method method) {
        Property propertyAnnotation = method.getAnnotation(Property.class);
        PropertyType propertyType = resolvePropertyType(method, propertyAnnotation.optional());
        String propertyName = propertyAnnotation.name();
        Option value = Option.none();
        if (source.hasValue(propertyName)) {
            value = Option.some(propertyType.getValue(source, propertyName));
        } else if (isDefaultValueDefined(method)) {
            value = Option.some(propertyType.getDefaultValue(method.getAnnotation(DefaultValue.class).value()));
        }
        if (value.isNone() && !propertyAnnotation.optional()) {
            throw new ConfigException("Missing property " + propertyName + " expected in configuration " + method.getDeclaringClass().getName() + ".");
        }
        return propertyAnnotation.optional() ? value : value.some();
    }

    private boolean isDefaultValueDefined(Method method) {
        return method.isAnnotationPresent(DefaultValue.class);
    }

    private boolean isPropertyMethod(Method method) {
        return method.isAnnotationPresent(Property.class);
    }

    private PropertyType resolvePropertyType(Method method, boolean optional) {
        Class<?> returnType = method.getReturnType();
        if (optional) {
            returnType = resolveOptionalPropertyType(method, returnType);
        }
        PropertyType propertyType = propertyTypes.get(returnType);
        if (propertyType == null) {
            throw new ConfigException("Method " + method.getName() + " of interface " + method.getDeclaringClass().getName() +
                    " annotated with " + Property.class.getName() + " has unsupported return type. Following types are supported: " + propertyTypes.keySet());
        }
        return propertyType;
    }

    private Class<?> resolveOptionalPropertyType(Method method, Class<?> returnType) {
        if (!returnType.isAssignableFrom(Option.class)) {
            throw new ConfigException("Method " + method.getName() + " of interface " + method.getDeclaringClass().getName() +
                    " annotated with " + Property.class.getName() + " with optional = true but has a return type different from fj.data.Option.");
        }
        Type returnTypeGeneric = method.getGenericReturnType();
        Type[] actualTypeArguments = ((ParameterizedType) returnTypeGeneric).getActualTypeArguments();
        returnType = (Class) actualTypeArguments[0];
        return returnType;
    }

    private void validateParameterTypes(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes != null && parameterTypes.length > 0) {
            throw new ConfigException("Property method declaration has parameters: " + method.getName() + " in configuration interface " + method.getDeclaringClass());
        }
    }

    private interface PropertyType<T> {
        public T getValue(Values source, String name);

        public T getDefaultValue(String value);
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

