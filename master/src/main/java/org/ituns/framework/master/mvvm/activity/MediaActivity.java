package org.ituns.framework.master.mvvm.activity;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.mvvm.media.MediaCallback;
import org.ituns.framework.master.mvvm.media.MediaFiles;
import org.ituns.framework.master.mvvm.media.MediaPhotos;
import org.ituns.framework.master.mvvm.media.MediaType;
import org.ituns.framework.master.mvvm.media.MediaVideos;
import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.framework.master.tools.android.IBundle;
import org.ituns.framework.master.tools.network.IUri;
import org.ituns.framework.master.tools.storage.IStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MediaActivity extends MasterActivity implements MediaCallback {
    private static final String TAG = "Media";

    private static final int ACTION_RESULT = 100000001;
    private static final int ACTION_CANCEL = 100000002;
    private static final int ACTION_ERROR = 100000003;

    private final AtomicInteger fileWeight = new AtomicInteger(0);
    private final AtomicInteger photoWeight = new AtomicInteger(0);
    private final AtomicInteger videoWeight = new AtomicInteger(0);

    private final MediaFiles mediaFiles = new MediaFiles(this);
    private final MediaPhotos mediaPhotos = new MediaPhotos(this);
    private final MediaVideos mediaVideos = new MediaVideos(this);

    @Override
    public final void onResult(int code, ArrayList<Uri> uris) {
        postLifecycle(MVVMAction.with(ACTION_RESULT).put("code", code).put("uris", uris));
    }

    @Override
    public final void onCancel(int code, String msg) {
        postLifecycle(MVVMAction.with(ACTION_CANCEL).put("code", code).put("msg", msg));
    }

    @Override
    public final void onError(int code, String msg) {
        postLifecycle(MVVMAction.with(ACTION_ERROR).put("code", code).put("msg", msg));
    }

    @Override
    @CallSuper
    protected void onLifecycle(MVVMAction action) {
        switch (action.code()) {
            case ACTION_RESULT:
                onMediaResult(action.getInt("code"), action.getParcelableArrayList("uris"));
                break;
            case ACTION_CANCEL: {
                onMediaCancel(action.getInt("code"), action.getString("msg"));
                break;
            }
            case ACTION_ERROR: {
                onMediaError(action.getInt("code"), action.getString("msg"));
                break;
            }
        }
    }

    protected void onMediaResult(int requestCode, List<Uri> uris) {}

    protected void onMediaCancel(int requestCode, String msg) {}

    protected void onMediaError(int requestCode, String msg) {}

    protected void selectFile(int requestCode) {
        selectFile(requestCode, new IBundle());
    }

    protected void selectFile(int requestCode, int num) {
        selectFile(requestCode, IBundle.with("mediaNum", num));
    }

    protected void selectFile(int requestCode, int num, String type) {
        selectFile(requestCode, IBundle.with("mediaNum", num).put("mediaType", type));
    }

    protected void selectFile(int requestCode, IBundle bundle) {
        fileWeight.incrementAndGet();
        mediaFiles.selectFile(this, MediaFiles.Params.with(requestCode).putAll(bundle));
    }

    protected void showPhotoDialog(int requestCode) {
        showPhotoDialog(requestCode, new IBundle());
    }

    protected void showPhotoDialog(int requestCode, int num) {
        showPhotoDialog(requestCode, IBundle.with("mediaNum", num));
    }

    protected void showPhotoDialog(int requestCode, IBundle bundle) {
        MediaType.Params params = MediaPhotos.Params.with(requestCode).putAll(bundle);
        mediaPhotos.showPhotoDialog(this, params, new MediaType.OnDialogCallback() {
            @Override
            public void onTakeCamera(Object context, MediaType.Params params) {
                takePhoto(requestCode, bundle);
            }

            @Override
            public void onSelectAlbum(Object context, MediaType.Params params) {
                selectPhoto(requestCode, bundle);
            }
        });
    }

    protected void takePhoto(int requestCode) {
        takePhoto(requestCode, new IBundle());
    }

    protected void takePhoto(int requestCode, IBundle bundle) {
        photoWeight.incrementAndGet();
        mediaPhotos.takePhoto(this, MediaPhotos.Params.with(requestCode).putAll(bundle));
    }

    protected void selectPhoto(int requestCode) {
        selectPhoto(requestCode, new IBundle());
    }

    protected void selectPhoto(int requestCode, int mediaNum) {
        selectPhoto(requestCode, IBundle.with("mediaNum", mediaNum));
    }

    protected void selectPhoto(int requestCode, IBundle bundle) {
        photoWeight.incrementAndGet();
        mediaPhotos.selectPhoto(this, MediaPhotos.Params.with(requestCode).putAll(bundle));
    }

    protected boolean storePhoto(String path) {
        return storePhoto(new File(path));
    }

    protected boolean storePhoto(File file) {
        try {
            return storePhoto(file.getName(), new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean storePhoto(String name, Uri uri) {
        return storePhoto(name, IUri.with(uri).input(this));
    }

    protected boolean storePhoto(String name, InputStream is) {
        return storePhoto(name, IStream.input(is).readBytes());
    }

    protected boolean storePhoto(String name, byte[] bytes) {
        return mediaPhotos.storePhotoSync(this, name, bytes);
    }

    protected void showVideoDialog(int requestCode) {
        showVideoDialog(requestCode, new IBundle());
    }

    protected void showVideoDialog(int requestCode, IBundle bundle) {
        MediaType.Params params = MediaVideos.Params.with(requestCode).putAll(bundle);
        mediaVideos.showVideoDialog(this, params, new MediaType.OnDialogCallback() {
            @Override
            public void onTakeCamera(Object context, MediaType.Params params) {
                takeVideo(requestCode, bundle);
            }

            @Override
            public void onSelectAlbum(Object context, MediaType.Params params) {
                selectVideo(requestCode, bundle);
            }
        });
    }

    protected void takeVideo(int requestCode) {
        takeVideo(requestCode, new IBundle());
    }

    protected void takeVideo(int requestCode, IBundle bundle) {
        videoWeight.incrementAndGet();
        mediaVideos.takeVideo(this, MediaVideos.Params.with(requestCode).putAll(bundle));
    }

    protected void selectVideo(int requestCode) {
        selectVideo(requestCode,  new IBundle());
    }

    protected void selectVideo(int requestCode, int mediaNum) {
        selectVideo(requestCode, IBundle.with("mediaNum", mediaNum));
    }

    protected void selectVideo(int requestCode, IBundle bundle) {
        videoWeight.incrementAndGet();
        mediaVideos.selectVideo(this, MediaVideos.Params.with(requestCode).putAll(bundle));
    }

    @Override
    protected void onSystemPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(fileWeight.get() > 0 && mediaFiles.onRequestPermissionsResult(this, requestCode, permissions, grantResults)) {
            Logcat.e("file permission result");
        } else if(photoWeight.get() > 0 && mediaPhotos.onRequestPermissionsResult(this, requestCode, permissions, grantResults)) {
            Logcat.e("photo permission result");
        } else if(videoWeight.get() > 0 && mediaVideos.onRequestPermissionsResult(this, requestCode, permissions, grantResults)) {
            Logcat.e("video permission result");
        }
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(fileWeight.getAndDecrement() > 0 && mediaFiles.onActivityResult(requestCode, resultCode, data)) {
            Logcat.e("file media result");
        } else if(photoWeight.getAndDecrement() > 0 && mediaPhotos.onActivityResult(requestCode, resultCode, data)) {
            Logcat.e("photo media result");
        } else if(videoWeight.getAndDecrement() > 0 && mediaVideos.onActivityResult(requestCode, resultCode, data)) {
            videoWeight.decrementAndGet();
            Logcat.e("video media result");
        }
    }
}