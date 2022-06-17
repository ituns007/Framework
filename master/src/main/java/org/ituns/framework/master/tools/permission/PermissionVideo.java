package org.ituns.framework.master.tools.permission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

public class PermissionVideo extends Permission {

    @Override
    protected String[] permissions() {
        return new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }

    @Override
    protected String deniedMessage() {
        return "使用该功能，需要开启相机、录音、存储权限，请手动设置开启权限";
    }

    @Override
    public void onRequestPermissionsResult(AppCompatActivity activity,
                                           int requestCode,
                                           String[] permissions,
                                           int[] grantResults,
                                           Callback callback) {
        int weight = 0;
        int len = Math.min(permissions.length, grantResults.length);
        for(int i = 0; i < len; i++) {
            if(TextUtils.equals(permissions[i], Manifest.permission.CAMERA)) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    weight += 1;
                }
            } else if(TextUtils.equals(permissions[i], Manifest.permission.RECORD_AUDIO)) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    weight += 2;
                }
            } else if(TextUtils.equals(permissions[i], Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    weight += 4;
                }
            }
        }

        if(weight < 7) {
            if(!callback.onDenied(requestCode)) {
                showDeniedDialog(activity);
            }
        } else {
            callback.onGranted(requestCode);
        }
    }

    private PermissionVideo() {}

    public static PermissionVideo get() {
        return PermissionVideo.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final PermissionVideo INSTANCE = new PermissionVideo();
    }
}
