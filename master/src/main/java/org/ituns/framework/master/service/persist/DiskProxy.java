package org.ituns.framework.master.service.persist;

import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.framework.master.service.persist.storage.DiskStorage;

import java.io.InputStream;

public class DiskProxy {
    private static final String TAG = "DiskProxy";

    public static String string(String key) {
        DiskStorage storage = PersistService.get().disk();
        if(storage == null) {
            Logcat.i(TAG, "disk storage is null.");
            return null;
        }
        return storage.string(key);
    }

    public static InputStream input(String key) {
        DiskStorage storage = PersistService.get().disk();
        if(storage == null) {
            Logcat.i(TAG, "disk storage is null.");
            return null;
        }
        return storage.input(key);
    }

    public static void put(String key, String file) {
        DiskStorage storage = PersistService.get().disk();
        if(storage == null) {
            Logcat.i(TAG, "disk storage is null.");
            return;
        }
        storage.put(key, file);
    }
}
