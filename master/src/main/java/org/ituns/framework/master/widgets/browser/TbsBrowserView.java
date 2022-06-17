package org.ituns.framework.master.widgets.browser;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsReaderView;

import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.framework.master.service.persist.DiskProxy;
import org.ituns.framework.master.service.tasks.BackProxy;
import org.ituns.framework.master.tools.network.IUri;
import org.ituns.framework.master.tools.security.Encrypt;
import org.ituns.framework.master.tools.storage.IStorage;
import org.ituns.framework.master.tools.storage.IStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class TbsBrowserView extends FrameLayout implements TbsReaderView.ReaderCallback {
    private final TbsReaderView mReaderView;

    public TbsBrowserView(@NonNull Context context) {
        this(context, null);
    }

    public TbsBrowserView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TbsBrowserView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mReaderView = new TbsReaderView(context, this);
        addView(mReaderView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {}

    public void destroy() {
        if(mReaderView != null) {
            mReaderView.onStop();
        }
    }

    public void prepareBrowserView(File file) throws Exception {
        if(QbSdk.canLoadX5(getContext())) {
            refreshBrowserView(file);
        } else  {
            throw new Exception("插件初始化中，请稍后重试");
        }
    }

    private void refreshBrowserView(File file) throws Exception {
        if(file != null && file.exists()) {
            String path = file.getAbsolutePath();
            String suffix = path.substring(path.lastIndexOf(".") + 1);
            if(!mReaderView.preOpen(suffix, false)) {
                throw new Exception("不支持的文件格式");
            }

            Bundle bundle = new Bundle();
            bundle.putString("filePath", path);
            bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
            mReaderView.openFile(bundle);
        } else {
            throw new Exception("文件不存在");
        }
    }

    public LiveData<File> generateFileFromUrl(String fileName, Uri uri) {
        MutableLiveData<File> liveData = new MutableLiveData<>();
        BackProxy.post(() -> {
            byte[] bytes = readByteFromUri(uri);
            File file = writeBytesToFile(fileName, bytes);
            liveData.postValue(file);
        });
        return liveData;
    }

    public LiveData<File> generateFileFromBytes(String fileName, byte[] bytes) {
        MutableLiveData<File> liveData = new MutableLiveData<>();
        BackProxy.post(() -> {
            File file = writeBytesToFile(fileName, bytes);
            liveData.postValue(file);
        });
        return liveData;
    }

    /**
     * 读取URI
     * @param uri
     * @return
     */
    private byte[] readByteFromUri(Uri uri) {
        try {
            InputStream stream = IUri.with(uri).input(getContext());
            return IStream.input(stream).readBytes();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 写入到文件
     * @param fileName
     * @param bytes
     * @return
     */
    private File writeBytesToFile(String fileName, byte[] bytes) {
        try {
            File file = IStorage.cache(getContext()).file(fileName);
            IStream.output(new FileOutputStream(file)).write(bytes);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
