package org.ituns.framework.master.service.logcat;

import android.content.Context;

import org.ituns.android.logcat.Level;
import org.ituns.framework.master.service.BaseConfig;
import org.ituns.framework.master.service.BaseService;
import org.ituns.framework.master.service.ServiceCallback;

public class LogConfig extends BaseConfig {
    private boolean debug;
    private String tag;
    private Level level;

    public LogConfig(Builder builder) {
        super(builder);
        this.debug = builder.debug;
        this.tag = builder.tag;
        this.level = builder.level;
    }

    @Override
    protected BaseService createService() {
        return  LogService.get();
    }

    public boolean debug() {
        return debug;
    }

    public String tag() {
        return tag;
    }

    public Level level() {
        return level;
    }

    public static class Builder extends BaseConfig.Builder {
        private boolean debug;
        private String tag;
        private Level level;

        public Builder(Context context) {
            super(context);
        }

        @Override
        public Builder callback(ServiceCallback callback) {
            super.callback(callback);
            return this;
        }

        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder level(Level level) {
            this.level = level;
            return this;
        }

        public LogConfig build() {
            return new LogConfig(this);
        }
    }
}
