package org.ituns.framework.master.service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.ituns.framework.master.ContextWrapper;
import org.ituns.framework.master.tools.java.IThread;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseService {
    private static final boolean MAIN_PROCESS = IThread.isMainProcess(ContextWrapper.get());

    public static final int STATE_IDLE = 1;
    public static final int STATE_INIT = 2;
    public static final int STATE_READY = 3;
    protected final AtomicInteger mState = new AtomicInteger(STATE_IDLE);

    public final void createService(BaseConfig config) {
        if(MAIN_PROCESS && mState.compareAndSet(STATE_IDLE, STATE_INIT)) {
            onServiceCreate(config);
        }
    }

    protected abstract void onServiceCreate(BaseConfig config);

    public final void initializeService() {
        if(MAIN_PROCESS && mState.compareAndSet(STATE_INIT, STATE_READY)) {
            onServiceInitialize();
        }
    }

    protected abstract void onServiceInitialize();

    public final void activityCreate(AppCompatActivity activity) {
        if(MAIN_PROCESS && mState.get() == STATE_READY) {
            onActivityCreate(activity);
        }
    }

    protected void onActivityCreate(AppCompatActivity activity) {}

    public final void activityStart(AppCompatActivity activity) {
        if(MAIN_PROCESS && mState.get() == STATE_READY) {
            onActivityStart(activity);
        }
    }

    protected void onActivityStart(AppCompatActivity activity) {}

    public final void activityResume(AppCompatActivity activity) {
        if(MAIN_PROCESS && mState.get() == STATE_READY) {
            onActivityResume(activity);
        }
    }

    protected void onActivityResume(AppCompatActivity activity) {}

    public final void fragmentResume(Fragment fragment) {
        if(MAIN_PROCESS && mState.get() == STATE_READY) {
            onFragmentResume(fragment);
        }
    }

    protected void onFragmentResume(Fragment fragment) {}

    public final void fragmentPause(Fragment fragment) {
        if(MAIN_PROCESS && mState.get() == STATE_READY) {
            onFragmentPause(fragment);
        }
    }

    protected void onFragmentPause(Fragment fragment) {}

    public final void activityPause(AppCompatActivity activity) {
        if(MAIN_PROCESS && mState.get() == STATE_READY) {
            onActivityPause(activity);
        }
    }

    protected void onActivityPause(AppCompatActivity activity) {}

    public final void activityStop(AppCompatActivity activity) {
        if(MAIN_PROCESS && mState.get() == STATE_READY) {
            onActivityStop(activity);
        }
    }

    protected void onActivityStop(AppCompatActivity activity) {}

    public final void activityDestroy(AppCompatActivity activity) {
        if(MAIN_PROCESS && mState.get() == STATE_READY) {
            onActivityDestroy(activity);
        }
    }

    protected void onActivityDestroy(AppCompatActivity activity) {}

    public final void releaseService() {
        if(MAIN_PROCESS && mState.compareAndSet(STATE_READY, STATE_INIT)) {
            onServiceRelease();
        }
    }

    protected abstract void onServiceRelease();

    public final void destroyService() {
        if(MAIN_PROCESS && mState.compareAndSet(STATE_INIT, STATE_IDLE)) {
            onServiceDestroy();
        }
    }

    protected abstract void onServiceDestroy();

    public final void authService(Auth auth) {
        if(MAIN_PROCESS && mState.get() == STATE_READY) {
            onServiceAuth(auth);
        }
    }

    protected void onServiceAuth(Auth auth) {}

    public final void loginService(Login login) {
        if(MAIN_PROCESS && mState.get() == STATE_READY) {
            onServiceLogin(login);
        }
    }

    protected void onServiceLogin(Login login) {}

    public final void logoutService(Logout logout) {
        if(MAIN_PROCESS && mState.get() == STATE_READY) {
            onServiceLogout(logout);
        }
    }

    protected void onServiceLogout(Logout logout) {}

    public interface Auth {
        String token();
    }

    public interface Login {
        String userId();
    }

    public interface Logout {}
}
