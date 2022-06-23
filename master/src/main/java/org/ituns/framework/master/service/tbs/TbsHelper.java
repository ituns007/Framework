package org.ituns.framework.master.service.tbs;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.ituns.framework.master.service.tasks.AsyncProxy;
import org.ituns.framework.master.tools.network.IUri;
import org.ituns.framework.master.tools.storage.IStorage;
import org.ituns.framework.master.tools.storage.IStream;

import java.io.File;
import java.io.FileOutputStream;

public class TbsHelper {

    /**
     * Raw to File 异步
     * @param name
     * @param bytes
     * @return
     */
    public static LiveData<File> fileFromRawAsync(Context context, String name, byte[] bytes) {
        MutableLiveData<File> liveData = new MutableLiveData<>();
        AsyncProxy.execute(() -> liveData.postValue(fileFromRaw(context, name, bytes)));
        return liveData;
    }

    /**
     * Raw to File 同步
     * @param name
     * @param bytes
     * @return
     */
    public static File fileFromRaw(Context context, String name, byte[] bytes) {
        File file = createTempFile(context, name);
        try(FileOutputStream fos = new FileOutputStream(file)) {
            IStream.output(fos).write(bytes);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Uri to File 异步
     * @param name
     * @param uri
     * @return
     */
    public static LiveData<File> fileFromUriAsync(Context context, String name, Uri uri) {
        MutableLiveData<File> liveData = new MutableLiveData<>();
        AsyncProxy.execute(() -> liveData.postValue(fileFromUri(context, name, uri)));
        return liveData;
    }

    /**
     * Uri to File 同步
     * @param name
     * @param uri
     * @return
     */
    public static File fileFromUri(Context context, String name, Uri uri) {
        File file = createTempFile(context, name);
        try(FileOutputStream fos = new FileOutputStream(file)) {
            IStream.output(fos).write(IUri.with(uri).input(context));
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建临时文件
     * @param name
     * @return
     */
    private static File createTempFile(Context context, String name) {
        File tmpDir = IStorage.files(context).file("tbstmp");
        if(!tmpDir.exists()) tmpDir.mkdirs();
        return new File(tmpDir, name);
    }
}