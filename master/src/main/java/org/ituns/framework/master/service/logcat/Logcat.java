package org.ituns.framework.master.service.logcat;

import org.ituns.android.logcat.Level;

public class Logcat {

    public static void v(String msg) {
        LogService.get().print(Level.VERBOSE, "", msg, null, 2);
    }

    public static void v(String tag, String msg) {
        LogService.get().print(Level.VERBOSE, tag, msg, null, 2);
    }

    public static void d(String msg) {
        LogService.get().print(Level.DEBUG, "", msg, null, 2);
    }

    public static void d(String tag, String msg) {
        LogService.get().print(Level.DEBUG, tag, msg, null, 2);
    }

    public static void i(String msg) {
        LogService.get().print(Level.INFO, "", msg, null, 2);
    }

    public static void i(String tag, String msg) {
        LogService.get().print(Level.INFO, tag, msg, null, 2);
    }

    public static void i(String tag, String msg, int depth) {
        int stackDepth = depth < 0 ? 2 : depth + 2;
        LogService.get().print(Level.INFO, tag, msg, null, stackDepth);
    }

    public static void i(String msg, Throwable throwable) {
        LogService.get().print(Level.INFO, "", msg, throwable, 2);
    }

    public static void i(String msg, Throwable throwable, int depth) {
        int stackDepth = depth < 0 ? 2 : depth + 2;
        LogService.get().print(Level.INFO, "", msg, throwable, stackDepth);
    }

    public static void i(String tag, String msg, Throwable throwable) {
        LogService.get().print(Level.INFO, tag, msg, throwable, 2);
    }

    public static void w(String msg) {
        LogService.get().print(Level.WARN, "", msg, null, 2);
    }

    public static void w(String tag, String msg) {
        LogService.get().print(Level.WARN, tag, msg, null, 2);
    }

    public static void e(String msg) {
        LogService.get().print(Level.ERROR, "", msg, null, 2);
    }

    public static void e(String tag, String msg) {
        LogService.get().print(Level.ERROR, tag, msg, null, 2);
    }

    public static void e(Throwable tr) {
        LogService.get().print(Level.ERROR, "", "", tr, 2);
    }

    public static void e(String tag, Throwable tr) {
        LogService.get().print(Level.ERROR, tag, "", tr, 2);
    }

    public static void e(String tag, String msg, Throwable tr) {
        LogService.get().print(Level.ERROR, tag, msg, tr, 2);
    }
}