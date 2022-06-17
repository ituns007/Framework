package org.ituns.framework.master.service;

import android.content.Context;

public abstract class BaseConfig {
    private Context context;
    private BaseService service;
    private ServiceCallback callback;

    public BaseConfig(Builder builder) {
        this.context = builder.context;
        this.service = createService();
        this.callback = builder.callback;
    }

    protected abstract BaseService createService();

    public final Context context() {
        return context;
    }

    public final BaseService service() {
        return service;
    }

    public final ServiceCallback callback() {
        return callback;
    }

    public static abstract class Builder {
        private Context context;
        private ServiceCallback callback;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder callback(ServiceCallback callback) {
            this.callback = callback;
            return this;
        }
    }
}