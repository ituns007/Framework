package org.ituns.framework.master.mvvm.media;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.BottomMenu;

import org.ituns.framework.master.tools.java.IList;
import org.ituns.framework.master.tools.permission.Callback;
import org.ituns.framework.master.tools.permission.PermissionCamera;
import org.ituns.framework.master.tools.permission.PermissionFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class MediaVideos extends MediaType {
    private static final int CODE_VIDEO_CAMERA = CODE_MEDIA_UNIT * 6;
    private static final int CODE_VIDEO_SINGLE = CODE_MEDIA_UNIT * 7;
    private static final int CODE_VIDEO_MULTI = CODE_MEDIA_UNIT * 8;
    private static final int CODE_VIDEO_LIMIT = CODE_MEDIA_UNIT * 9;

    private Uri FW_CAMERA_VIDEO_URI = null;

    public MediaVideos(MediaCallback mediaCallback) {
        super(mediaCallback);
    }

    public void showVideoDialog(Object context, Params params, OnDialogCallback callback) {
        showVideoDialogWithoutPermission(context, params, (text, index) -> {
            if ("录制".equals(text)) {
                callback.onTakeCamera(context, params);
            } else if ("从相册选择".equals(text)) {
                callback.onSelectAlbum(context, params);
            } else {
                mediaCallback.onError(params.code(), "invalid selected.");
            }
        });
    }

    private void showVideoDialogWithoutPermission(Object context, Params params, OnMenuItemClickListener listener) {
        if(context instanceof Fragment) {
            Fragment fragment = (Fragment) context;
            AppCompatActivity activity = (AppCompatActivity) fragment.requireActivity();
            showVideoDialogWithoutPermission(activity, params, listener);
        } else if(context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            showVideoDialogWithoutPermission(activity, params, listener);
        }  else {
            mediaCallback.onError(params.code(), "context must be Fragment or AppCompatActivity.");
        }
    }

    private void showVideoDialogWithoutPermission(AppCompatActivity activity, Params params, OnMenuItemClickListener listener) {
        AtomicBoolean dismiss = new AtomicBoolean(false);
        BottomMenu.build(activity)
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setMenuTextList(Arrays.asList("录制", "从相册选择"))
                .setOnMenuItemClickListener((text, index) -> {
                    if(dismiss.compareAndSet(false, true)) {
                        listener.onClick(text, index);
                    }
                })
                .setOnCancelButtonClickListener((baseDialog, v) -> {
                    if(dismiss.compareAndSet(false, true)) {
                        mediaCallback.onCancel(params.code(), "click cancel in bottom dialog");
                    }
                    return false;
                })
                .setOnDismissListener(() -> {
                    if(dismiss.compareAndSet(false, true)) {
                        mediaCallback.onCancel(params.code(), "click outside with bottom dialog");
                    }
                })
                .show();
    }

    public void takeVideo(Object context, Params params) {
        takeVideoWithoutPermission(context, params);
    }

    private void takeVideoWithoutPermission(Object context, Params params) {
        if (params.code() <= 0 || 100000000 <= params.code()) {
            mediaCallback.onError(params.code(), "requestCode must between 0 and 100000000.");
            return;
        }

        int requestCode = params.code() + CODE_VIDEO_CAMERA;
        sparseParamsArray.put(requestCode, params);

        if(context instanceof Fragment) {
            Fragment fragment = (Fragment) context;
            PermissionCamera.get().check(fragment, requestCode);
        } else if(context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            PermissionCamera.get().check(activity, requestCode);
        }  else {
            mediaCallback.onError(requestCode(requestCode), "context must be Fragment or AppCompatActivity.");
        }
    }

    public void selectVideo(Object context, Params params) {
        selectVideoWithoutPermission(context, params);
    }

    private void selectVideoWithoutPermission(Object context, Params params) {
        if (params.code() <= 0 || 100000000 <= params.code()) {
            mediaCallback.onError(params.code(), "requestCode must between 0 and 100000000.");
            return;
        }

        int mediaNum = params.getInt("mediaNum", 1);
        int requestCode = mediaNum > 1 ? (params.code() + CODE_VIDEO_MULTI) : (params.code() + CODE_VIDEO_SINGLE);
        sparseParamsArray.put(requestCode, params);

        if(context instanceof Fragment) {
            Fragment fragment = (Fragment) context;
            PermissionFile.get().check(fragment, requestCode);
        } else if(context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            PermissionFile.get().check(activity, requestCode);
        } else {
            mediaCallback.onError(requestCode(requestCode), "context must be Fragment or AppCompatActivity.");
        }
    }

    public boolean onRequestPermissionsResult(Object context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(CODE_VIDEO_CAMERA < requestCode && requestCode < CODE_VIDEO_SINGLE) {
            verifyCameraPermission(context, requestCode, permissions, grantResults);
            return true;
        } else if(CODE_VIDEO_SINGLE < requestCode && requestCode < CODE_VIDEO_LIMIT) {
            verifyFilePermission(context, requestCode, permissions, grantResults);
            return true;
        }
        return false;
    }

    private void verifyCameraPermission(Object context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyCameraPermission(context, requestCode, permissions, grantResults, new Callback() {
            @Override
            public void onGranted(int requestCode) {
                if(CODE_VIDEO_CAMERA < requestCode && requestCode < CODE_VIDEO_SINGLE) {
                    takeVideoWithPermission(context, requestCode);
                }
            }

            @Override
            public boolean onDenied(int requestCode) {
                if(CODE_VIDEO_CAMERA < requestCode && requestCode < CODE_VIDEO_SINGLE) {
                    mediaCallback.onCancel(requestCode(requestCode), "file permission denied.");
                }
                return super.onDenied(requestCode);
            }
        });
    }

    private void verifyCameraPermission(Object context, int requestCode,
                                        @NonNull String[] permissions,
                                        @NonNull int[] grantResults,
                                        Callback callback) {
        if(context instanceof Fragment) {
            Fragment fragment = (Fragment) context;
            AppCompatActivity activity = (AppCompatActivity) fragment.requireActivity();
            PermissionCamera.get().onRequestPermissionsResult(activity, requestCode, permissions, grantResults, callback);
        } else if(context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            PermissionCamera.get().onRequestPermissionsResult(activity, requestCode, permissions, grantResults, callback);
        }  else {
            mediaCallback.onError(requestCode(requestCode), "context must be Fragment or AppCompatActivity.");
        }
    }

    private void verifyFilePermission(Object context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyFilePermission(context, requestCode, permissions, grantResults, new Callback() {
            @Override
            public void onGranted(int requestCode) {
                if(CODE_VIDEO_SINGLE < requestCode && requestCode < CODE_VIDEO_LIMIT) {
                    selectVideoWithPermission(context, requestCode);
                }
            }

            @Override
            public boolean onDenied(int requestCode) {
                if(CODE_VIDEO_SINGLE < requestCode && requestCode < CODE_VIDEO_LIMIT) {
                    mediaCallback.onCancel(requestCode(requestCode), "file permission denied.");
                }
                return super.onDenied(requestCode);
            }
        });
    }

    private void verifyFilePermission(Object context, int requestCode,
                                      @NonNull String[] permissions,
                                      @NonNull int[] grantResults,
                                      Callback callback) {
        if(context instanceof Fragment) {
            Fragment fragment = (Fragment) context;
            AppCompatActivity activity = (AppCompatActivity) fragment.requireActivity();
            PermissionFile.get().onRequestPermissionsResult(activity, requestCode, permissions, grantResults, callback);
        } else if(context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            PermissionFile.get().onRequestPermissionsResult(activity, requestCode, permissions, grantResults, callback);
        }  else {
            mediaCallback.onError(requestCode(requestCode), "context must be Fragment or AppCompatActivity.");
        }
    }

    private void takeVideoWithPermission(Object context, int requestCode) {
        Params params = sparseParamsArray.get(requestCode);
        if(params == null) {
            mediaCallback.onError(requestCode(requestCode), "params is null.");
            return;
        }

        if(context instanceof Fragment) {
            Fragment fragment = (Fragment) context;
            AppCompatActivity activity = (AppCompatActivity) fragment.requireActivity();
            Intent intent = buildVideoIntent(activity, params);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                fragment.startActivityForResult(intent, requestCode);
            } else {
                mediaCallback.onError(requestCode(requestCode), "Can't find activity to open.");
            }
        } else if(context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            Intent intent = buildVideoIntent(activity, params);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivityForResult(intent, requestCode);
            } else {
                mediaCallback.onError(requestCode(requestCode), "Can't find activity to open.");
            }
        } else {
            mediaCallback.onError(requestCode(requestCode), "context must be Fragment or AppCompatActivity.");
        }
    }

    private Intent buildVideoIntent(Activity activity, Params params) {
        FW_CAMERA_VIDEO_URI = createVideoUri(activity);
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FW_CAMERA_VIDEO_URI);
        if(params.containsKey(MediaStore.EXTRA_DURATION_LIMIT)) {
            int duration = params.getInt(MediaStore.EXTRA_DURATION_LIMIT, 0);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, duration);
        }
        if(params.containsKey(MediaStore.EXTRA_VIDEO_QUALITY)) {
            int quality = params.getInt(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, quality);
        }
        if(params.containsKey(MediaStore.EXTRA_SIZE_LIMIT)) {
            long size = params.getLong(MediaStore.EXTRA_SIZE_LIMIT, 0L);
            intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, size);
        }
        return intent;
    }

    private void selectVideoWithPermission(Object context, int requestCode) {
        Params params = sparseParamsArray.get(requestCode);
        if(params == null) {
            mediaCallback.onError(requestCode(requestCode), "params is null.");
            return;
        }

        int mediaNum = params.getInt("mediaNum", 1);
        if(context instanceof Fragment) {
            Fragment fragment = (Fragment) context;
            AppCompatActivity activity = (AppCompatActivity) fragment.requireActivity();
            EasyPhotos.createAlbum(fragment, false,false, GlideEngine.get())
                    .setFileProviderAuthority(activity.getPackageName() + ".fileprovider")
                    .setPuzzleMenu(false)
                    .setCleanMenu(false)
                    .onlyVideo()
                    .setCount(mediaNum)
                    .start(requestCode);
        } else if(context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            EasyPhotos.createAlbum(activity, false,false, GlideEngine.get())
                    .setFileProviderAuthority(activity.getPackageName() + ".fileprovider")
                    .setPuzzleMenu(false)
                    .setCleanMenu(false)
                    .onlyVideo()
                    .setCount(mediaNum)
                    .start(requestCode);
        } else {
            mediaCallback.onError(requestCode(requestCode), "context must be Fragment or AppCompatActivity.");
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (CODE_VIDEO_CAMERA < requestCode && requestCode < CODE_VIDEO_SINGLE) {
                onTakeVideoResult(requestCode(requestCode), data);
                return true;
            } else if (CODE_VIDEO_SINGLE < requestCode && requestCode < CODE_VIDEO_LIMIT) {
                onSelectVideoResult(requestCode(requestCode), data);
                return true;
            }
        } else if(resultCode == Activity.RESULT_CANCELED) {
            if (CODE_VIDEO_CAMERA < requestCode && requestCode < CODE_VIDEO_SINGLE) {
                mediaCallback.onCancel(requestCode(requestCode), "take video cancel");
                return true;
            } else if (CODE_VIDEO_SINGLE < requestCode && requestCode < CODE_VIDEO_LIMIT) {
                mediaCallback.onCancel(requestCode(requestCode), "select video cancel");
                return true;
            }
        }
        return false;
    }

    private void onTakeVideoResult(int requestCode, Intent data) {
        Uri uri = null;
        if (data != null) {
            uri = data.getData();
        }
        if (uri == null) {
            uri = FW_CAMERA_VIDEO_URI;
        }
        if(uri == null) {
            mediaCallback.onError(requestCode, "uri is null.");
        } else {
            ArrayList<Uri> uris = new ArrayList<>(Collections.singletonList(uri));
            mediaCallback.onResult(requestCode, uris);
        }
    }

    private void onSelectVideoResult(int requestCode, Intent data) {
        if(data == null) {
            mediaCallback.onError(requestCode, "data is null.");
            return;
        }

        ArrayList<Photo> photos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
        if(IList.isEmpty(photos)) {
            mediaCallback.onError(requestCode, "photos is empty.");
            return;
        }

        ArrayList<Uri> uris = new ArrayList<>();
        for(Photo photo : photos) {
            uris.add(photo.uri);
        }

        if(IList.isEmpty(uris)) {
            mediaCallback.onError(requestCode, "uris is empty.");
        } else {
            mediaCallback.onResult(requestCode, uris);
        }
    }
}
