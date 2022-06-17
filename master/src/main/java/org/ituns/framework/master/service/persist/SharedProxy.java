package org.ituns.framework.master.service.persist;

import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.framework.master.service.persist.storage.SharedStorage;

public class SharedProxy {
    private static final String TAG = "SharedProxy";

    public static String token() {
        SharedStorage storage = PersistService.get().storage();
        if(storage == null) {
            Logcat.i(TAG, "shared storage is null.");
            return "";
        }
        return storage.getGlobal(SharedStorage.GLOBAL_TOKEN, "");
    }

    public static String userId() {
        SharedStorage storage = PersistService.get().storage();
        if(storage == null) {
            Logcat.i(TAG, "shared storage is null.");
            return "";
        }
        return storage.getGlobal(SharedStorage.GLOBAL_USERID, "");
    }

    public static String getUser(String key) {
        SharedStorage storage = PersistService.get().storage();
        if(storage == null) {
            Logcat.i(TAG, "shared storage is null.");
            return null;
        }
        return storage.getUser(key);
    }

    public static String getUser(String key, String defaultValue) {
        SharedStorage storage = PersistService.get().storage();
        if(storage == null) {
            Logcat.i(TAG, "shared storage is null.");
            return null;
        }
        return storage.getUser(key, defaultValue);
    }

    public static void putUser(String key, String value) {
        SharedStorage storage = PersistService.get().storage();
        if(storage == null) {
            Logcat.i(TAG, "shared storage is null.");
            return;
        }
        storage.putUser(key, value);
    }

    public static void removeUser(String key) {
        SharedStorage storage = PersistService.get().storage();
        if(storage == null) {
            Logcat.i(TAG, "shared storage is null.");
            return;
        }
        storage.removeUser(key);
    }

    public static String getGlobal(String key) {
        SharedStorage storage = PersistService.get().storage();
        if(storage == null) {
            Logcat.i(TAG, "shared storage is null.");
            return null;
        }
        return storage.getGlobal(key);
    }

    public static String getGlobal(String key, String defaultValue) {
        SharedStorage storage = PersistService.get().storage();
        if(storage == null) {
            Logcat.i(TAG, "shared storage is null.");
            return null;
        }
        return storage.getGlobal(key, defaultValue);
    }

    public static void putGlobal(String key, String value) {
        SharedStorage storage = PersistService.get().storage();
        if(storage == null) {
            Logcat.i(TAG, "shared storage is null.");
            return;
        }
        storage.putGlobal(key, value);
    }

    public static void removeGlobal(String key) {
        SharedStorage storage = PersistService.get().storage();
        if(storage == null) {
            Logcat.i(TAG, "shared storage is null.");
            return;
        }
        storage.removeGlobal(key);
    }
}
