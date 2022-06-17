package org.ituns.framework.master.service.persist.storage;

import android.content.Context;

import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.framework.master.tools.android.IContext;
import org.ituns.framework.master.tools.disklru.DiskLruCache;
import org.ituns.framework.master.tools.storage.IStorage;

import java.io.File;
import java.io.InputStream;

import static org.ituns.framework.master.service.persist.PersistService.TAG;

public class DiskStorage {
    private DiskLruCache mDiskLruCache;

    public DiskStorage(Context context) {
        try {
            File cacheFile = IStorage.files(context).file("disklru");
            int appVersion = IContext.versionCode(context);
            int valueCount = 1;
            int maxSize = 1024 * 1024 * 1024; //1GB
            mDiskLruCache = DiskLruCache.open(cacheFile, appVersion, valueCount, maxSize);
        } catch (Exception e) {
            Logcat.i(TAG, "constructor exception:", e);
        }
    }

    public String string(String key) {
        DiskLruCache diskLruCache = mDiskLruCache;
        if(diskLruCache == null) {
            Logcat.i(TAG, "disklru cache is null.");
            return null;
        }

        try {
            DiskLruCache.Snapshot snapshot =  diskLruCache.get(key);
            if(snapshot != null) {
                return snapshot.getString(0);
            }
            return null;
        } catch (Exception e) {
            Logcat.i(TAG, "string exception:", e);
            return null;
        }
    }

    public InputStream input(String key) {
        DiskLruCache diskLruCache = mDiskLruCache;
        if(diskLruCache == null) {
            Logcat.i(TAG, "disklru cache is null.");
            return null;
        }

        try {
            DiskLruCache.Snapshot snapshot =  diskLruCache.get(key);
            if(snapshot != null) {
                return snapshot.getInputStream(0);
            }
            return null;
        } catch (Exception e) {
            Logcat.i(TAG, "input exception:", e);
            return null;
        }
    }

    public void put(String key, String value) {
        DiskLruCache diskLruCache = mDiskLruCache;
        if(diskLruCache == null) {
            Logcat.i(TAG, "disk lru cache is null.");
            return;
        }

        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            editor.set(0, value);
            editor.commit();
            diskLruCache.flush();
        } catch (Exception e) {
            Logcat.i(TAG, "put exception:", e);
        }
    }

    public void release() {
        DiskLruCache diskLruCache = mDiskLruCache;
        if(diskLruCache != null) {
            try {
                diskLruCache.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDiskLruCache = null;
            }
        }
    }
}
