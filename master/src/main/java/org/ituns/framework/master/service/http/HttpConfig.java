package org.ituns.framework.master.service.http;

import android.content.Context;

import org.ituns.framework.master.service.BaseConfig;
import org.ituns.framework.master.service.BaseService;
import org.ituns.framework.master.service.ServiceCallback;
import org.ituns.network.core.Config;

public class HttpConfig extends BaseConfig {
    private boolean debug;
    private int timeout;

    private HttpConfig(Builder builder) {
        super(builder);
        this.debug = builder.debug;
        this.timeout = builder.timeout;
    }

    @Override
    protected BaseService createService() {
        return HttpService.get();
    }

    public boolean debug() {
        return debug;
    }

    public int timeout() {
        return timeout;
    }

    public static class Builder extends BaseConfig.Builder {
        private boolean debug;
        private int timeout;

        public Builder(Context context) {
            super(context);
            this.debug = false;
            this.timeout = 30;
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

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public HttpConfig build() {
            return new HttpConfig(this);
        }
    }
}
