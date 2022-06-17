package org.ituns.framework.master.service.tbs;

import android.content.Context;

import org.ituns.framework.master.service.BaseConfig;
import org.ituns.framework.master.service.BaseService;
import org.ituns.framework.master.service.ServiceCallback;

public class TbsConfig extends BaseConfig {

    public TbsConfig(Builder builder) {
        super(builder);
    }

    @Override
    protected BaseService createService() {
        return TbsService.get();
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

        public TbsConfig build() {
            return new TbsConfig(this);
        }
    }
}
