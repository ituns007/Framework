package org.ituns.framework.master.service.channel;

import android.content.Context;

import org.ituns.framework.master.service.BaseConfig;
import org.ituns.framework.master.service.BaseService;
import org.ituns.framework.master.service.ServiceCallback;
import org.ituns.framework.master.tools.java.IList;

import java.util.ArrayList;
import java.util.List;

public class ChannelConfig extends BaseConfig {
    private final ArrayList<ChannelObserver<? extends ChannelData>> observers = new ArrayList<>();

    private ChannelConfig(Builder builder) {
        super(builder);
        observers.clear();
        observers.addAll(builder.observers);
    }

    public ArrayList<ChannelObserver<? extends ChannelData>> observers() {
        return observers;
    }

    @Override
    protected BaseService createService() {
        return ChannelService.get();
    }

    public static class Builder extends BaseConfig.Builder {
        private final ArrayList<ChannelObserver<? extends ChannelData>> observers = new ArrayList<>();

        public Builder(Context context) {
            super(context);
        }

        @Override
        public Builder callback(ServiceCallback callback) {
            super.callback(callback);
            return this;
        }

        public Builder addObserver(ChannelObserver<? extends ChannelData> observer) {
            this.observers.add(observer);
            return this;
        }

        public Builder addObservers(List<ChannelObserver<? extends ChannelData>> observers) {
            if(!IList.isEmpty(observers)) {
                this.observers.addAll(observers);
            }
            return this;
        }

        public ChannelConfig build() {
            return new ChannelConfig(this);
        }
    }
}
