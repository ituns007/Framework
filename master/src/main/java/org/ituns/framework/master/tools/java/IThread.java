package org.ituns.framework.master.tools.java;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Looper;
import android.os.Process;

import org.ituns.framework.master.service.logcat.Logcat;

public class IThread {

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static boolean isMainProcess(Context context) {
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
                if (process.pid == Process.myPid()) {
                    return IString.isEquals(context.getPackageName(), process.processName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void printStackTrace() {
        Thread thread = Thread.currentThread();
        StackTraceElement[] elements = thread.getStackTrace();
        for(StackTraceElement element : elements) {
            Logcat.e(element.toString());
        }
    }
}
