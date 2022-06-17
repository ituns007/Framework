package org.ituns.framework.master.service.tasks;

import org.ituns.android.concurrent.AsyncTask;
import org.ituns.framework.master.service.logcat.Logcat;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class AsyncProxy {
    private static final String TAG = "AsyncProxy";

    public static boolean execute(Runnable runnable) {
        AsyncTask asyncTask = TaskService.get().async();
        if(asyncTask == null) {
            Logcat.i(TAG, "async task is null.");
            return false;
        }
        return asyncTask.execute(runnable);
    }

    public static Future<?> submit(Runnable runnable) {
        AsyncTask asyncTask = TaskService.get().async();
        if(asyncTask == null) {
            Logcat.i(TAG, "async task is null.");
            return null;
        }
        return asyncTask.submit(runnable);
    }

    public static <T> Future<T> submit(Runnable runnable, T result) {
        AsyncTask asyncTask = TaskService.get().async();
        if(asyncTask == null) {
            Logcat.i(TAG, "async task is null.");
            return null;
        }
        return asyncTask.submit(runnable, result);
    }

    public static <T> Future<T> submit(Callable<T> callable) {
        AsyncTask asyncTask = TaskService.get().async();
        if(asyncTask == null) {
            Logcat.i(TAG, "async task is null.");
            return null;
        }
        return asyncTask.submit(callable);
    }
}
