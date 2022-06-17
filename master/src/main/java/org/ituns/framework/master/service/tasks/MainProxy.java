package org.ituns.framework.master.service.tasks;

import org.ituns.android.concurrent.MainTask;
import org.ituns.framework.master.service.logcat.Logcat;

public class MainProxy {
    private static final String TAG = "MainProxy";

    public static boolean post(Runnable runnable) {
        MainTask mainTask = TaskService.get().main();
        if(mainTask == null) {
            Logcat.i(TAG, "main task is null.");
            return false;
        }
        return mainTask.post(runnable);
    }

    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        MainTask mainTask = TaskService.get().main();
        if(mainTask == null) {
            Logcat.i(TAG, "main task is null.");
            return false;
        }
        return mainTask.postDelayed(runnable, delayMillis);
    }

    public static boolean postSingleDelayed(Runnable runnable, long delayMillis) {
        MainTask mainTask = TaskService.get().main();
        if(mainTask == null) {
            Logcat.i(TAG, "main task is null.");
            return false;
        }
        return mainTask.postSingleDelayed(runnable, delayMillis);
    }

    public static void removeCallbacks(Runnable runnable) {
        MainTask mainTask = TaskService.get().main();
        if(mainTask == null) {
            Logcat.i(TAG, "main task is null.");
            return;
        }
        mainTask.removeCallbacks(runnable);
    }
}
