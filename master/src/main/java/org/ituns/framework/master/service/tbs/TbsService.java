package org.ituns.framework.master.service.tbs;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsDownloader;
import com.tencent.smtt.sdk.TbsListener;

import org.checkerframework.checker.units.qual.A;
import org.ituns.framework.master.service.BaseConfig;
import org.ituns.framework.master.service.BaseService;
import org.ituns.framework.master.service.logcat.Logcat;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TbsService extends BaseService implements TbsListener, QbSdk.PreInitCallback {
    public static TbsService get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final TbsService INSTANCE = new TbsService();
    }

    private TbsConfig mConfig;
    private final AtomicInteger downloadCount = new AtomicInteger(0);
    private TbsService() {}

    @Override
    protected void onServiceCreate(BaseConfig config) {
        if(config instanceof TbsConfig) {
            mConfig = (TbsConfig) config;
            super.initializeService();
        }
    }

    @Override
    protected void onServiceInitialize() {
        TbsConfig config = mConfig;
        if(config == null) {
            Logcat.d("config is null.");
            return;
        }

        //TBS内核冷启动优化
        HashMap<String, Object> map = new HashMap<>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        QbSdk.initTbsSettings(map);
        //移动网络下载TBS内核
        QbSdk.setDownloadWithoutWifi(true);
        //下载TBS内核回调
        QbSdk.setTbsListener(this);
        //初始化TBS
        if(QbSdk.canLoadX5(config.context())) {
            QbSdk.preInit(config.context(), this);
        } else {
            Logcat.e("tbs need download");
            downloadCount.set(0);
            TbsDownloader.startDownload(config.context());
        }
    }

    @Override
    public void onDownloadProgress(int i) {
        Logcat.d("tbs download progress:" + i);
    }

    @Override
    public void onDownloadFinish(int i) {
        Logcat.d("tbs download finish:" + i);
        TbsConfig config = mConfig;
        if(config != null && downloadCount.get() < 5 && i > 100) {
            Logcat.e("tbs download again");
            downloadCount.incrementAndGet();
            TbsDownloader.startDownload(config.context());
        }
    }

    @Override
    public void onInstallFinish(int i) {
        Logcat.d("tbs install finish:" + i);
        TbsConfig config = mConfig;
        if(config != null && i == 200) {
            QbSdk.preInit(config.context(), this);
        }
    }

    @Override
    public void onCoreInitFinished() {
        Logcat.d("tbs core init finish");
    }

    @Override
    public void onViewInitFinished(boolean b) {
        Logcat.d("tbs view init finish:" + b);
    }

    @Override
    protected void onServiceRelease() {}

    @Override
    protected void onServiceDestroy() {
        super.releaseService();
        mConfig = null;
    }
}
