package org.ituns.framework.master.service.tasks;

import org.ituns.android.concurrent.BackTask;
import org.ituns.framework.master.service.logcat.Logcat;

public class BackProxy {
    private static final String TAG = "BackProxy";

    public static boolean post(Runnable runnable) {
        BackTask backTask = TaskService.get().back();
        if(backTask == null) {
            Logcat.i(TAG, "back task is null.");
            return false;
        }
        return backTask.post(runnable);
    }

    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        BackTask backTask = TaskService.get().back();
        if(backTask == null) {
            Logcat.i(TAG, "back task is null.");
            return false;
        }
        return backTask.postDelayed(runnable, delayMillis);
    }

    public static boolean postSingleDelayed(Runnable runnable, long delayMillis) {
        BackTask backTask = TaskService.get().back();
        if(backTask == null) {
            Logcat.i(TAG, "back task is null.");
            return false;
        }
        return backTask.postSingleDelayed(runnable, delayMillis);
    }

    public static void removeCallbacks(Runnable runnable) {
        BackTask backTask = TaskService.get().back();
        if(backTask == null) {
            Logcat.i(TAG, "back task is null.");
            return;
        }
        backTask.removeCallbacks(runnable);
    }
}
