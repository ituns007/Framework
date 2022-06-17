package org.ituns.framework.master.service.persist;

import android.content.Context;

import org.ituns.framework.master.service.BaseConfig;
import org.ituns.framework.master.service.BaseService;
import org.ituns.framework.master.service.ServiceCallback;

public class PersistConfig extends BaseConfig {
    private PersistConfig(Builder builder) {
        super(builder);
    }

    @Override
    protected BaseService createService() {
        return PersistService.get();
    }

    public static class Builder extends BaseConfig.Builder {
        public Builder(Context context) {
            super(context);
        }

        @Override
        public Builder callback(ServiceCallback callback) {
            super.callback(callback);
            return this;
        }

        public PersistConfig build() {
            return new PersistConfig(this);
        }
    }
}
