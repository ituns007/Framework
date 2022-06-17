package org.ituns.framework.master.mvvm.media;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
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

import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.framework.master.tools.java.IList;
import org.ituns.framework.master.tools.permission.Callback;
import org.ituns.framework.master.tools.permission.PermissionCamera;
import org.ituns.framework.master.tools.permission.PermissionFile;
import org.ituns.framework.master.tools.storage.IStream;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class MediaPhotos extends MediaType {
    private static final int CODE_PHOTO_CAMERA = CODE_MEDIA_UNIT * 3;
    private static final int CODE_PHOTO_SINGLE = CODE_MEDIA_UNIT * 4;
    private static final int CODE_PHOTO_MULTI = CODE_MEDIA_UNIT * 5;
    private static final int CODE_PHOTO_LIMIT = CODE_MEDIA_UNIT * 6;

    private Uri FW_CAMERA_PHOTO_URI = null;

    public MediaPhotos(MediaCallback mediaCallback) {
        super(mediaCallback);
    }

    public void showPhotoDialog(Object context, Params params, OnDialogCallback callback) {
        showPhotoDialogWithoutPermission(context, params, (text, index) -> {
            if ("拍照".equals(text)) {
                callback.onTakeCamera(context, params);
            } else if ("从相册选择".equals(text)) {
                callback.onSelectAlbum(context, params);
            } else {
                mediaCallback.onError(params.code(), "invalid selected.");
            }
        });
    }

    private void showPhotoDialogWithoutPermission(Object context, Params params, OnMenuItemClickListener listener) {
        if(context instanceof Fragment) {
            Fragment fragment = (Fragment) context;
            AppCompatActivity activity = (AppCompatActivity) fragment.requireActivity();
            showPhotoDialogWithoutPermission(activity, params, listener);
        } else if(context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            showPhotoDialogWithoutPermission(activity, params, listener);
        }  else {
            mediaCallback.onError(params.code(), "context must be Fragment or AppCompatActivity.");
        }
    }

    private void showPhotoDialogWithoutPermission(AppCompatActivity activity, Params params, OnMenuItemClickListener listener) {
        AtomicBoolean dismiss = new AtomicBoolean(false);
        BottomMenu.build(activity)
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setMenuTextList(Arrays.asList("拍照", "从相册选择"))
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

    public void takePhoto(Object context, Params params) {
        takePhotoWithoutPermission(context, params);
    }

    private void takePhotoWithoutPermission(Object context, Params params) {
        if (params.code() <= 0 || 100000000 <= params.code()) {
            mediaCallback.onError(params.code(), "requestCode must between 0 and 100000000.");
            return;
        }

        int requestCode = params.code() + CODE_PHOTO_CAMERA;
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

    public void selectPhoto(Object context, Params params) {
        selectPhotoWithoutPermission(context, params);
    }

    private void selectPhotoWithoutPermission(Object context, Params params) {
        if (params.code() <= 0 || 100000000 <= params.code()) {
            mediaCallback.onError(params.code(), "requestCode must between 0 and 100000000.");
            return;
        }

        int mediaNum = params.getInt("mediaNum", 1);
        int requestCode = mediaNum > 1 ? (params.code() + CODE_PHOTO_MULTI) : (params.code() + CODE_PHOTO_SINGLE);
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
        if(CODE_PHOTO_CAMERA < requestCode && requestCode < CODE_PHOTO_SINGLE) {
            verifyCameraPermission(context, requestCode, permissions, grantResults);
            return true;
        } else if(CODE_PHOTO_SINGLE < requestCode && requestCode < CODE_PHOTO_LIMIT) {
            verifyFilePermission(context, requestCode, permissions, grantResults);
            return true;
        }
        return false;
    }

    private void verifyCameraPermission(Object context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyCameraPermission(context, requestCode, permissions, grantResults, new Callback() {
            @Override
            public void onGranted(int requestCode) {
                if(CODE_PHOTO_CAMERA < requestCode && requestCode < CODE_PHOTO_SINGLE) {
                    takePhotoWithPermission(context, requestCode);
                }
            }

            @Override
            public boolean onDenied(int requestCode) {
                if(CODE_PHOTO_CAMERA < requestCode && requestCode < CODE_PHOTO_SINGLE) {
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
                if(CODE_PHOTO_SINGLE < requestCode && requestCode < CODE_PHOTO_LIMIT) {
                    selectPhotoWithPermission(context, requestCode);
                }
            }

            @Override
            public boolean onDenied(int requestCode) {
                if(CODE_PHOTO_SINGLE < requestCode && requestCode < CODE_PHOTO_LIMIT) {
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

    private void takePhotoWithPermission(Object context, int requestCode) {
        if(context instanceof Fragment) {
            Fragment fragment = (Fragment) context;
            AppCompatActivity activity = (AppCompatActivity) fragment.requireActivity();
            Intent intent = buildPhotoIntent(activity);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                fragment.startActivityForResult(intent, requestCode);
            } else {
                mediaCallback.onError(requestCode(requestCode), "Can't find activity to open.");
            }
        } else if(context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            Intent intent = buildPhotoIntent(activity);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivityForResult(intent, requestCode);
            } else {
                mediaCallback.onError(requestCode(requestCode), "Can't find activity to open.");
            }
        } else {
            mediaCallback.onError(requestCode(requestCode), "context must be Fragment or AppCompatActivity.");
        }
    }

    private Intent buildPhotoIntent(Activity activity) {
        FW_CAMERA_PHOTO_URI = createPhotoUri(activity);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FW_CAMERA_PHOTO_URI);
        return intent;
    }

    private void selectPhotoWithPermission(Object context, int requestCode) {
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
                    .setVideo(false)
                    .setCount(mediaNum)
                    .start(requestCode);
        } else if(context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            EasyPhotos.createAlbum(activity, false,false, GlideEngine.get())
                    .setFileProviderAuthority(activity.getPackageName() + ".fileprovider")
                    .setPuzzleMenu(false)
                    .setCleanMenu(false)
                    .setVideo(false)
                    .setCount(mediaNum)
                    .start(requestCode);
        } else {
            mediaCallback.onError(requestCode(requestCode), "context must be Fragment or AppCompatActivity.");
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (CODE_PHOTO_CAMERA < requestCode && requestCode < CODE_PHOTO_SINGLE) {
                onTakePhotoResult(requestCode(requestCode), data);
                return true;
            } else if (CODE_PHOTO_SINGLE < requestCode && requestCode < CODE_PHOTO_LIMIT) {
                onSelectPhotoResult(requestCode(requestCode), data);
                return true;
            }
        } else if(resultCode == Activity.RESULT_CANCELED) {
            if (CODE_PHOTO_CAMERA < requestCode && requestCode < CODE_PHOTO_SINGLE) {
                mediaCallback.onCancel(requestCode(requestCode), "take photo cancel");
                return true;
            } else if (CODE_PHOTO_SINGLE < requestCode && requestCode < CODE_PHOTO_LIMIT) {
                mediaCallback.onCancel(requestCode(requestCode), "select photo cancel");
                return true;
            }
        }
        return false;
    }

    private void onTakePhotoResult(int requestCode, Intent data) {
        Uri uri = null;
        if (data != null) {
            uri = data.getData();
        }
        if (uri == null) {
            uri = FW_CAMERA_PHOTO_URI;
        }
        if(uri == null) {
            mediaCallback.onError(requestCode, "uri is null.");
        } else {
            ArrayList<Uri> uris = new ArrayList<>(Collections.singletonList(uri));
            mediaCallback.onResult(requestCode, uris);
        }
    }

    private void onSelectPhotoResult(int requestCode, Intent data) {
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

    public boolean storePhotoSync(Context context, String name, byte[] bytes) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

            String fp = Environment.getExternalStorageDirectory()
                    + File.separator
                    + Environment.DIRECTORY_DOWNLOADS
                    + File.separator
                    + name;

            ContentValues cv = new ContentValues();
            cv.put(MediaStore.Images.ImageColumns.TITLE, name);
            cv.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, name);
            cv.put(MediaStore.Images.ImageColumns.SIZE, bytes.length);
            cv.put(MediaStore.Images.ImageColumns.WIDTH, options.outWidth);
            cv.put(MediaStore.Images.ImageColumns.HEIGHT, options.outHeight);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                cv.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            } else {
                cv.put(MediaStore.Images.ImageColumns.DATA, fp);
            }

            Uri uri = Uri.fromFile(new File(fp));
            ContentResolver cr = context.getContentResolver();
            IStream.output(cr.openOutputStream(uri)).write(bytes);
            if(cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv) == null) {
                int num = cr.update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv,"_display_name=?", new String[]{name});
                Logcat.i("Update Photo Num:" + num);
            }
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
