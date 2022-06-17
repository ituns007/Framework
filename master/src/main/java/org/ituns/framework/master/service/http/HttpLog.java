package org.ituns.framework.master.service.http;

import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.network.core.Logger;

public class HttpLog implements Logger {
    public static final String TAG = "HttpLog";

    private boolean isDebug;
    public HttpLog(boolean debug) {
        this.isDebug = debug;
    }

    @Override
    public void log(String msg) {
        if(isDebug) {
            Logcat.i(TAG, msg, 3);
        }
    }

    @Override
    public void log(Throwable t) {
        if(isDebug) {
            Logcat.i(TAG, t, 3);
        }
    }
}
