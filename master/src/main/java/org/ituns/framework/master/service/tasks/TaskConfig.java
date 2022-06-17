package org.ituns.framework.master.service.tasks;

import android.content.Context;

import org.ituns.framework.master.service.BaseConfig;
import org.ituns.framework.master.service.BaseService;
import org.ituns.framework.master.service.ServiceCallback;

public class TaskConfig extends BaseConfig {

    public TaskConfig(Builder builder) {
        super(builder);
    }

    @Override
    protected BaseService createService() {
        return TaskService.get();
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

        public TaskConfig build() {
            return new TaskConfig(this);
        }
    }
}
