package org.ituns.framework.master.service.tasks;

import org.ituns.android.concurrent.AsyncTask;
import org.ituns.android.concurrent.BackTask;
import org.ituns.android.concurrent.MainTask;
import org.ituns.framework.master.service.BaseConfig;
import org.ituns.framework.master.service.BaseService;

public class TaskService extends BaseService {
    public static TaskService get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final TaskService INSTANCE = new TaskService();
    }

    private MainTask mMainTask;
    private BackTask mBackTask;
    private AsyncTask mAsyncTask;
    private TaskService() {}

    @Override
    protected void onServiceCreate(BaseConfig config) {
        super.initializeService();
    }

    @Override
    protected void onServiceInitialize() {
        MainTask mainTask = mMainTask;
        if(mainTask == null) {
            mainTask = new MainTask();
            mMainTask = mainTask;
        }
        BackTask backTask = mBackTask;
        if(backTask == null) {
            backTask = new BackTask();
            mBackTask = backTask;
        }
        AsyncTask asyncTask = mAsyncTask;
        if(asyncTask == null) {
            asyncTask = new AsyncTask();
            mAsyncTask = asyncTask;
        }
    }

    @Override
    protected void onServiceRelease() {
        MainTask mainTask = mMainTask;
        if(mainTask != null) {
            mainTask.release();
            mMainTask =  null;
        }
        BackTask backTask = mBackTask;
        if(backTask != null) {
            backTask.release();
            mBackTask =  null;
        }
        AsyncTask asyncTask = mAsyncTask;
        if(asyncTask != null) {
            asyncTask.release();
            mAsyncTask =  null;
        }
    }

    @Override
    protected void onServiceDestroy() {
        super.releaseService();
    }

    BackTask back() {
        return mBackTask;
    }

    MainTask main() {
        return mMainTask;
    }

    AsyncTask async() {
        return mAsyncTask;
    }
}
