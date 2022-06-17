package org.ituns.framework.master.service.persist;

import org.ituns.framework.master.service.BaseConfig;
import org.ituns.framework.master.service.BaseService;
import org.ituns.framework.master.service.persist.storage.DiskStorage;
import org.ituns.framework.master.service.persist.storage.RamStorage;
import org.ituns.framework.master.service.persist.storage.SharedStorage;

public class PersistService extends BaseService {
    public static final String TAG = "PersistLog";

    public static PersistService get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final PersistService INSTANCE = new PersistService();
    }

    private PersistConfig mConfig;
    private RamStorage mRamStorage;
    private DiskStorage mDiskStorage;
    private SharedStorage mSharedStorage;
    private PersistService() {}

    @Override
    protected void onServiceCreate(BaseConfig config) {
        if(config instanceof PersistConfig) {
            mConfig = (PersistConfig) config;
            super.initializeService();
        }
    }

    @Override
    protected void onServiceInitialize() {
        PersistConfig config = mConfig;
        if(config == null) {
            return;
        }

        mRamStorage = new RamStorage();
        mDiskStorage = new DiskStorage(config.context());
        mSharedStorage = new SharedStorage(config.context());
    }

    @Override
    protected void onServiceAuth(Auth auth) {
        SharedStorage shared = mSharedStorage;
        if(shared != null) {
            shared.auth(auth.token());
        }
    }

    @Override
    protected void onServiceLogin(Login login) {
        SharedStorage shared = mSharedStorage;
        if(shared != null) {
            shared.login(login.userId());
        }
    }

    @Override
    protected void onServiceLogout(Logout logout) {
        SharedStorage shared = mSharedStorage;
        if(shared != null) {
            shared.logout();
        }
    }

    @Override
    protected void onServiceRelease() {
        RamStorage ramStorage = mRamStorage;
        if(ramStorage != null) {
            ramStorage.release();
            mRamStorage = null;
        }
        DiskStorage diskStorage = mDiskStorage;
        if(diskStorage != null) {
            diskStorage.release();
            mDiskStorage = null;
        }
        SharedStorage sharedStorage = mSharedStorage;
        if(sharedStorage != null) {
            sharedStorage.release();
            mSharedStorage = null;
        }
    }

    @Override
    protected void onServiceDestroy() {
        super.releaseService();
        mConfig = null;
    }

    RamStorage ram() {
        return mRamStorage;
    }

    DiskStorage disk() {
        return mDiskStorage;
    }

    SharedStorage storage() {
        return mSharedStorage;
    }
}
