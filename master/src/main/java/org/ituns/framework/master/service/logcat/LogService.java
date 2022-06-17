package org.ituns.framework.master.service.logcat;

import android.util.Log;

import org.ituns.android.logcat.Level;
import org.ituns.android.logcat.LoggerClient;
import org.ituns.android.logcat.LoggerConfig;
import org.ituns.framework.master.service.BaseConfig;
import org.ituns.framework.master.service.BaseService;

public class LogService extends BaseService {
    private static final String TAG = "LogService";

    public static LogService get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final LogService INSTANCE = new LogService();
    }

    private LogConfig mConfig;
    private LoggerClient mClient;
    private LogService() {}

    @Override
    protected void onServiceCreate(BaseConfig config) {
        if(config instanceof LogConfig) {
            mConfig = (LogConfig) config;
            super.initializeService();
        }
    }

    @Override
    protected void onServiceInitialize() {
        LogConfig config = mConfig;
        if(config == null) {
            Log.i(TAG, "log config is null.");
            return;
        }

        LoggerClient client = mClient;
        if(client == null) {
            mClient = new LoggerClient(new LoggerConfig.Builder(config.context())
                    .debug(config.debug())
                    .tag(config.tag())
                    .level(config.level())
                    .build());
        }
    }

    @Override
    protected void onServiceRelease() {
        LoggerClient client = mClient;
        if(client != null) {
            client.release();
            mClient = null;
        }
    }

    @Override
    protected void onServiceDestroy() {
        super.releaseService();
        mConfig = null;
    }

    void print(Level level, String tag, String msg, Throwable throwable, int depth) {
        LoggerClient client = mClient;
        if(client == null) {
            Log.i(TAG, "logger client is null.");
            return;
        }
        client.print(level, tag, msg, throwable, depth);
    }
}
