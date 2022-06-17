package org.ituns.framework.master;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;

import org.ituns.framework.master.service.ServiceConfig;
import org.ituns.framework.master.service.ServiceManager;

public abstract class FrameworkApp extends Application implements CameraXConfig.Provider {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceManager.get().applicationCreate(onServiceConfig());
    }

    protected abstract ServiceConfig onServiceConfig();

    @Override
    public void onLowMemory() {
        ServiceManager.get().applicationLowMemory();
        super.onLowMemory();
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }
}
