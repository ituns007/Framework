package org.ituns.framework.master.mvvm.media;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.ituns.framework.master.tools.java.IList;
import org.ituns.framework.master.tools.java.IString;
import org.ituns.framework.master.tools.permission.Callback;
import org.ituns.framework.master.tools.permission.PermissionFile;

import java.util.ArrayList;

public class MediaFiles extends MediaType {
    private static final int CODE_FILE_SINGLE = CODE_MEDIA_UNIT;
    private static final int CODE_FILE_MULTI = CODE_MEDIA_UNIT * 2;
    private static final int CODE_FILE_LIMIT = CODE_MEDIA_UNIT * 3;

    public MediaFiles(MediaCallback mediaCallback) {
        super(mediaCallback);
    }

    public void selectFile(Fragment fragment, Params params) {
        selectFileWithoutPermission(fragment, params);
    }

    public void selectFile(AppCompatActivity activity, Params params) {
        selectFileWithoutPermission(activity, params);
    }

    private void selectFileWithoutPermission(Object context, Params params) {
        if (params.code() <= 0 || 100000000 <= params.code()) {
            mediaCallback.onError(params.code(), "requestCode must between 0 and 100000000.");
            return;
        }

        int mediaNum = params.getInt("mediaNum", 1);
        int requestCode = mediaNum > 1 ? (params.code() + CODE_FILE_MULTI) : (params.code() + CODE_FILE_SINGLE);
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
        if(CODE_FILE_SINGLE < requestCode && requestCode < CODE_FILE_LIMIT) {
            verifyFilePermission(context, requestCode, permissions, grantResults);
            return true;
        }
        return false;
    }

    private void verifyFilePermission(Object context, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyFilePermission(context, requestCode, permissions, grantResults, new Callback() {
            @Override
            public void onGranted(int requestCode) {
                if(CODE_FILE_SINGLE < requestCode && requestCode < CODE_FILE_LIMIT) {
                    selectFileWithPermission(context, requestCode);
                }
            }

            @Override
            public boolean onDenied(int requestCode) {
                if(CODE_FILE_SINGLE < requestCode && requestCode < CODE_FILE_LIMIT) {
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

    private void selectFileWithPermission(Object context, int requestCode) {
        Params params = sparseParamsArray.get(requestCode);
        if(params == null) {
            mediaCallback.onError(requestCode(requestCode), "params is null.");
            return;
        }

        int mediaNum = params.getInt("mediaNum", 1);
        String mediaType = params.getString("mediaType", "*/*");

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, splitMediaTypes(mediaType));
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, mediaNum > 1);

        if(context instanceof Fragment) {
            Fragment fragment = (Fragment) context;
            if (intent.resolveActivity(fragment.requireContext().getPackageManager()) != null) {
                fragment.startActivityForResult(intent, requestCode);
            } else {
                mediaCallback.onError(requestCode(requestCode), "Can't find activity to open.");
            }
        } else if(context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivityForResult(intent, requestCode);
            } else {
                mediaCallback.onError(requestCode(requestCode), "Can't find activity to open.");
            }
        }  else {
            mediaCallback.onError(requestCode(requestCode), "context must be Fragment or AppCompatActivity.");
        }
    }

    private String[] splitMediaTypes(String mediaType) {
        if(IString.notEmpty(mediaType)) {
            try {
                mediaType = mediaType.replaceAll(" ", "");
                return mediaType.split("&");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new String[]{"*/*"};
    }


    public boolean onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (CODE_FILE_SINGLE < requestCode && requestCode < CODE_FILE_LIMIT) {
                onSelectFileResult(requestCode(requestCode), data);
                return true;
            }
        } else if(resultCode == Activity.RESULT_CANCELED) {
            if (CODE_FILE_SINGLE < requestCode && requestCode < CODE_FILE_LIMIT) {
                mediaCallback.onCancel(requestCode(requestCode), "select file cancel");
                return true;
            }
        }
        return false;
    }

    private void onSelectFileResult(int requestCode, Intent data) {
        if(data == null) {
            mediaCallback.onError(requestCode, "data is null.");
            return;
        }

        ArrayList<Uri> uris = new ArrayList<>();

        Uri uriData = data.getData();
        ClipData clipData = data.getClipData();
        if(clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                uris.add(clipData.getItemAt(i).getUri());
            }
        } else if(uriData != null) {
            uris.add(uriData);
        }

        if(IList.isEmpty(uris)) {
            mediaCallback.onError(requestCode, "uris is empty.");
        } else {
            mediaCallback.onResult(requestCode, uris);
        }
    }
}
