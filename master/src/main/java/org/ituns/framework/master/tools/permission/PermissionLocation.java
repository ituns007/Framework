package org.ituns.framework.master.tools.permission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

public class PermissionLocation extends Permission {

    @Override
    protected String[] permissions() {
        return new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
    }

    @Override
    protected String deniedMessage() {
        return "使用该功能，需要开启定位权限，请手动设置开启权限";
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
            if(TextUtils.equals(permissions[i], Manifest.permission.ACCESS_FINE_LOCATION)) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    weight += 1;
                }
            } else if(TextUtils.equals(permissions[i], Manifest.permission.ACCESS_COARSE_LOCATION)) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    weight += 2;
                }
            }
        }

        if(weight < 3) {
            if(!callback.onDenied(requestCode)) {
                showDeniedDialog(activity);
            }
        } else {
            callback.onGranted(requestCode);
        }
    }

    private PermissionLocation() {}

    public static PermissionLocation get() {
        return PermissionLocation.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final PermissionLocation INSTANCE = new PermissionLocation();
    }
}
