package com.example.eindopdracht_backend_ipmroved.TestUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TestUtils {

    public static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T invokePrivateMethod(Object target, String methodName, Object... params) {
        try {
            Class<?>[] paramTypes = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                paramTypes[i] = params[i].getClass();
            }
            Method method = target.getClass().getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return (T) method.invoke(target, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

