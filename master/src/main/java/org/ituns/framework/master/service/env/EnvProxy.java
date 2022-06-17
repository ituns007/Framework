package org.ituns.framework.master.service.env;

public class EnvProxy {

    @Deprecated
    public static String getEnv(String key) {
        return getEnv(key, "");
    }

    @Deprecated
    public static String getEnv(String key, String def) {
        return EnvService.get().getEnv(key, def);
    }

    @Deprecated
    public static void setEnv(String key, String value) {
        EnvService.get().setEnv(key, value);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean def) {
        return EnvService.get().getBoolean(key, def);
    }

    public static void setBoolean(String key, boolean value) {
        EnvService.get().setBoolean(key, value);
    }

    public static float getFloat(String key) {
        return getFloat(key, 0.0f);
    }

    public static float getFloat(String key, float def) {
        return EnvService.get().getFloat(key, def);
    }

    public static void setFloat(String key, float value) {
        EnvService.get().setFloat(key, value);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int def) {
        return EnvService.get().getInt(key, def);
    }

    public static void setInt(String key, int value) {
        EnvService.get().setInt(key, value);
    }

    public static long getLong(String key) {
        return getLong(key, 0L);
    }

    public static long getLong(String key, long def) {
        return EnvService.get().getLong(key, def);
    }

    public static void setLong(String key, long value) {
        EnvService.get().setLong(key, value);
    }

    public static String getString(String key) {
        return getString(key, "");
    }

    public static String getString(String key, String def) {
        return EnvService.get().getString(key, def);
    }

    public static void setString(String key, String value) {
        EnvService.get().setString(key, value);
    }
}