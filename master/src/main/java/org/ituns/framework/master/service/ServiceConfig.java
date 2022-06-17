package org.ituns.framework.master.service;

import java.util.ArrayList;

public final class ServiceConfig {
    private final ArrayList<BaseConfig> configs = new ArrayList<>();

    public ServiceConfig(Builder builder) {
        configs.addAll(builder.configs);
    }

    public final ArrayList<BaseConfig> configs() {
        return configs;
    }

    public static final class Builder {
        private final ArrayList<BaseConfig> configs = new ArrayList<>();

        public final Builder add(BaseConfig config) {
            configs.add(config);
            return this;
        }

        public ServiceConfig build() {
            return new ServiceConfig(this);
        }
    }
}