package org.ituns.framework.master.tools.storage;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class IStorage {

    public static Files files(Context context) {
        return new Files(context);
    }

    public static Cache cache(Context context) {
        return new Cache(context);
    }

    public static class Files {
        private Context context;

        public Files(Context context) {
            this.context = context;
        }

        public String path(String name) {
            File file = file(name);
            if(file != null) {
                return file.getAbsolutePath();
            }
            return null;
        }

        public File file(String name) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                File file = externalFilesDir(name);
                if(file != null) {
                    return new File(file, name);
                }
            }
            return new File(internalFilesDir(), name);
        }

        private File externalFilesDir(String name) {
            return context.getExternalFilesDir(name);
        }

        private File internalFilesDir() {
            return context.getFilesDir();
        }
    }

    public static class Cache {
        private Context context;

        public Cache(Context context) {
            this.context = context;
        }

        public String path(String name) {
            File file = file(name);
            if(file != null) {
                return file.getAbsolutePath();
            }
            return null;
        }

        public File file(String name) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                File file = externalCacheDir();
                if(file != null) {
                    return new File(file, name);
                }
            }
            return new File(internalCacheDir(), name);
        }

        private File externalCacheDir() {
            return context.getExternalCacheDir();
        }

        private File internalCacheDir() {
            return context.getCacheDir();
        }
    }
}