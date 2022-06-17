package org.ituns.framework.master.service.env;

import android.content.Context;

import org.ituns.framework.master.service.BaseConfig;
import org.ituns.framework.master.service.BaseService;

public class EnvConfig extends BaseConfig {
    private final String tag;
    private final String def;
    private final String local;

    public EnvConfig(Builder builder) {
        super(builder);
        this.tag = builder.tag;
        this.def = builder.def;
        this.local = builder.local;
    }

    public String tag() {
        return tag;
    }

    public String def() {
        return def;
    }

    public String local() {
        return local;
    }

    @Override
    protected BaseService createService() {
        return EnvService.get();
    }

    public static class Builder extends BaseConfig.Builder {
        private String tag;
        private String def;
        private String local;

        public Builder(Context context) {
            super(context);
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder def(String def) {
            this.def = def;
            return this;
        }

        public Builder local(String local) {
            this.local = local;
            return this;
        }

        public EnvConfig build() {
            return new EnvConfig(this);
        }
    }
}