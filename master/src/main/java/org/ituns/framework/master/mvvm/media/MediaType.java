package org.ituns.framework.master.mvvm.media;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.util.SparseIntArray;

import androidx.core.content.FileProvider;

import org.ituns.framework.master.tools.android.IBundle;
import org.ituns.framework.master.tools.storage.IStorage;

import java.io.File;

public class MediaType {
    protected static final int CODE_MEDIA_UNIT = 100000000;

    protected final MediaCallback mediaCallback;
    protected final SparseArray<Params> sparseParamsArray = new SparseArray<>();

    public MediaType(MediaCallback mediaCallback) {
        this.mediaCallback = mediaCallback;
    }

    protected int requestCode(int code) {
        return code % CODE_MEDIA_UNIT;
    }

    public static Uri createPhotoUri(Context context) {
        File cacheFile = createPhotoFile(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return createCameraUriForQ(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createCameraUriForN(context, cacheFile);
        } else {
            return Uri.fromFile(cacheFile);
        }
    }

    public static File createPhotoFile(Context context) {
        File directory = IStorage.cache(context).file("photos");
        directory.mkdirs();
        return new File(directory, "fw_photo_" + System.currentTimeMillis() + ".jpg");
    }

    public static Uri createVideoUri(Context context) {
        File cacheFile = createVideoFile(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return createCameraUriForQ(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createCameraUriForN(context, cacheFile);
        } else {
            return Uri.fromFile(cacheFile);
        }
    }

    public static File createVideoFile(Context context) {
        File directory = IStorage.cache(context).file("videos");
        directory.mkdirs();
        return new File(directory, "fw_video_" + System.currentTimeMillis() + ".mp4");
    }

    private static Uri createCameraUriForQ(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    private static Uri createCameraUriForN(Context context, File file) {
        String authorith = context.getPackageName() + ".fileprovider";
        return FileProvider.getUriForFile(context, authorith, file);
    }

    public static final class Params extends IBundle {
        private final int code;

        private Params(int code) {
            this.code = code;
        }

        public int code() {
            return code;
        }

        public static Params with(int code) {
            return new Params(code);
        }
    }

    public interface OnDialogCallback {
        void onTakeCamera(Object context, Params params);
        void onSelectAlbum(Object context, Params params);
    }
}
