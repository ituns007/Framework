package org.ituns.framework.master.service.persist;

import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.framework.master.service.persist.storage.RamStorage;

public class RamProxy {
    private static final String TAG = "CacheProxy";

    public static String get(String key) {
        return get(key, "");
    }

    public static String get(String key, String defaultValue) {
        RamStorage cache = PersistService.get().ram();
        if(cache == null) {
            Logcat.i(TAG, "global cache is null.");
            return defaultValue;
        }
        return cache.get(key, defaultValue);
    }

    public static void put(String key, String value) {
        RamStorage cache = PersistService.get().ram();
        if(cache == null) {
            Logcat.i(TAG, "global cache is null.");
            return;
        }
        cache.put(key, value);
    }
}
