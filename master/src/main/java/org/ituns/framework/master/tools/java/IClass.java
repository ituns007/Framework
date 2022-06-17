package org.ituns.framework.master.tools.java;

import com.google.gson.reflect.TypeToken;

import org.ituns.framework.master.service.logcat.Logcat;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class IClass {
    private static final String TAG = "ICLASS";

    public static void printClassTree(Class<?> clazz) {
        Logcat.i(TAG, "=================================================");
        Logcat.i(TAG, IClass.className(clazz));
        while (clazz != null) {
            Type type = clazz.getGenericSuperclass();
            if(type != null) {
                Logcat.i(TAG, typeName(type));
            }
            clazz = clazz.getSuperclass();
        }
        Logcat.i(TAG, "=================================================");
    }

    public static String className(Class<?> clazz) {
        return clazz != null ? clazz.getName() : null;
    }

    public static String typeName(Type type) {
        StringBuilder builder = new StringBuilder();
        if(type != null) {
            Class<?> clazz = TypeToken.get(type).getRawType();
            builder.append(clazz.getName());
            if(type instanceof ParameterizedType) {
                Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                builder.append(typeName(types));
            }
        }
        return builder.toString();
    }

    public static String typeName(Type[] types) {
        StringBuilder builder = new StringBuilder();
        if(types != null && types.length > 0) {
            builder.append("<");
            for(Type type : types) {
                if(builder.length() > 1) builder.append(",");
                Class<?> clazz = TypeToken.get(type).getRawType();
                builder.append(clazz.getName());
            }
            builder.append(">");
        }
        return builder.toString();
    }

    public static boolean isParameterType(Object object, Class<?> target) {
        return parameterClass(object, target) != null;
    }

    public static Type parameterType(Object object, Class<?> target) {
        return parameterClass(object, target);
    }

    public static Class<?> parameterClass(Object object, Class<?> target) {
        try {
            Class<?> clazz = object.getClass();
            while (clazz != null) {
                Type superType = clazz.getGenericSuperclass();
                if(superType instanceof ParameterizedType) {
                    Type[] argumentTypes = ((ParameterizedType) superType).getActualTypeArguments();
                    for(Type argumentType : argumentTypes) {
                        Class<?> argumentClass = TypeToken.get(argumentType).getRawType();
                        if(target.isAssignableFrom(argumentClass)) {
                            return argumentClass;
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }
            return null;
        } catch (Throwable t) {
            Logcat.i(TAG, "class exception:", t);
            return null;
        }
    }
}
