package org.ituns.framework.master.service.persist.storage;

import android.os.Bundle;

import org.ituns.framework.master.service.logcat.Logcat;

import static org.ituns.framework.master.service.persist.PersistService.TAG;

public class RamStorage {
    private final Bundle bundle = new Bundle();

    public String get(String key) {
        return get(key, "");
    }

    public String get(String key, String defaultValue) {
        return bundle.getString(key, defaultValue);
    }

    public void put(String key, String value) {
        if(key == null || value == null) {
            Logcat.i(TAG, "key or value is null.");
            return;
        }
        bundle.putString(key, value);
    }

    public void release() {
        bundle.clear();
    }
}
