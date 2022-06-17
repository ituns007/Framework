package org.ituns.framework.master.service.channel;

import androidx.appcompat.app.AppCompatActivity;

import org.ituns.framework.master.service.BaseConfig;
import org.ituns.framework.master.service.BaseService;
import org.ituns.framework.master.service.channel.alert.AlertObserver;
import org.ituns.framework.master.service.channel.loading.LoadingObserver;
import org.ituns.framework.master.service.channel.toast.ToastObserver;
import org.ituns.framework.master.service.logcat.Logcat;

import java.util.ArrayList;

public class ChannelService extends BaseService {
    public static ChannelService get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final ChannelService INSTANCE = new ChannelService();
    }

    private ChannelConfig mConfig;
    private final ArrayList<ChannelObserver<? extends ChannelData>> mObservers;

    private ChannelService() {
        mObservers = new ArrayList<>();
    }

    @Override
    protected void onServiceCreate(BaseConfig config) {
        if(config instanceof ChannelConfig) {
            Logcat.e("ChannelService create");
            mConfig = (ChannelConfig) config;
            mObservers.add(new AlertObserver());
            mObservers.add(new ToastObserver());
            mObservers.add(new LoadingObserver());
            mObservers.addAll(mConfig.observers());
            super.initializeService();
        }
    }

    @Override
    protected void onServiceInitialize() {
        ChannelConfig config = mConfig;
        if(null != config) {
            Logcat.e("ChannelService initialize");
            for(ChannelObserver<? extends ChannelData> observer : mObservers) {
                observer.registerCallback(config.callback());
            }
        }
    }

    @Override
    protected void onActivityStart(AppCompatActivity activity) {
        ChannelConfig config = mConfig;
        if(null != config) {
            Logcat.e("ChannelService start");
            for(ChannelObserver<? extends ChannelData> observer : mObservers) {
                observer.registerActivity(activity);
            }
        }
    }

    @Override
    protected void onActivityStop(AppCompatActivity activity) {
        ChannelConfig config = mConfig;
        if(null != config) {
            Logcat.e("ChannelService stop");
            for(ChannelObserver<? extends ChannelData> observer : mObservers) {
                observer.unregisterActivity(activity);
            }
        }
    }

    @Override
    protected void onServiceRelease() {
        ChannelConfig config = mConfig;
        if(null != config) {
            Logcat.e("ChannelService release");
            for(ChannelObserver<? extends ChannelData> observer : mObservers) {
                observer.unregisterCallback();
            }
        }
    }

    @Override
    protected void onServiceDestroy() {
        super.releaseService();
        Logcat.e("ChannelService destroy");
        mObservers.clear();
        mConfig = null;
    }

    <T extends ChannelData> void postData(T data) {
        ChannelConfig config = mConfig;
        if(null != config && null != data) {
            for(ChannelObserver<? extends ChannelData> observer : mObservers) {
                observer.postData(data);
            }
        } else {
            Logcat.e("config or data is null.");
        }
    }
}