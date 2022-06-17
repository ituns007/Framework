package org.ituns.framework.master.service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ServiceManager {
    public static ServiceManager get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final ServiceManager INSTANCE = new ServiceManager();
    }

    private final ArrayList<BaseConfig> mConfig = new ArrayList<>();
    private ServiceManager() {}

    public void applicationCreate(ServiceConfig config) {
        if(config != null) {
            mConfig.clear();
            mConfig.addAll(config.configs());
            for(BaseConfig c : mConfig) {
                c.service().createService(c);
            }
        }
    }

    public void activityCreate(AppCompatActivity activity) {
        for(BaseConfig c : mConfig) {
            c.service().activityCreate(activity);
        }
    }

    public void activityStart(AppCompatActivity activity) {
        for(BaseConfig c : mConfig) {
            c.service().activityStart(activity);
        }
    }

    public void activityResume(AppCompatActivity activity) {
        for(BaseConfig c : mConfig) {
            c.service().activityResume(activity);
        }
    }

    public void fragmentResume(Fragment fragment) {
        for(BaseConfig c : mConfig) {
            c.service().fragmentResume(fragment);
        }
    }

    public void fragmentPause(Fragment fragment) {
        for(BaseConfig c : mConfig) {
            c.service().fragmentPause(fragment);
        }
    }

    public void activityPause(AppCompatActivity activity) {
        for(BaseConfig c : mConfig) {
            c.service().activityPause(activity);
        }
    }

    public void activityStop(AppCompatActivity activity) {
        for(BaseConfig c : mConfig) {
            c.service().activityStop(activity);
        }
    }

    public void activityDestroy(AppCompatActivity activity) {
        for(BaseConfig c : mConfig) {
            c.service().activityDestroy(activity);
        }
    }

    public void applicationLowMemory() {
        for(BaseConfig c : mConfig) {
            c.service().destroyService();
        }
    }

    public void applicationAuth(BaseService.Auth auth) {
        for(BaseConfig c : mConfig) {
            c.service().authService(auth);
        }
    }

    public void applicationLogin(BaseService.Login login) {
        for(BaseConfig c : mConfig) {
            c.service().loginService(login);
        }
    }

    public void applicationLogout(BaseService.Logout logout) {
        for(BaseConfig c : mConfig) {
            c.service().logoutService(logout);
        }
    }
}