package org.ituns.framework.master.tools.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public abstract class Permission {

    protected abstract String[] permissions();

    public final boolean valid(Context context) {
        for(String permission : permissions()) {
            if(ActivityCompat.checkSelfPermission(context, permission) != PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public final void check(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions(), requestCode);
    }

    public final void check(Fragment fragment, int requestCode) {
        fragment.requestPermissions(permissions(), requestCode);
    }

    public final void requestPermissionsResult(AppCompatActivity activity,
                                               int requestCode,
                                               String[] permissions,
                                               int[] grantResults,
                                               Callback callback) {
        if(permissions() == null || permissions == null || grantResults == null) {
            callback.onError(requestCode);
            return;
        }

        if(permissions().length != permissions.length || permissions().length  != grantResults.length) {
            callback.onError(requestCode);
            return;
        }

        onRequestPermissionsResult(activity, requestCode, permissions, grantResults, callback);
    }

    public abstract void onRequestPermissionsResult(AppCompatActivity activity,
                                                    int requestCode,
                                                    String[] permissions,
                                                    int[] grantResults,
                                                    Callback callback);

    protected abstract String deniedMessage();

    public final void showDeniedDialog(AppCompatActivity activity) {
        showDeniedDialog(activity, null);
    }

    public final void showDeniedDialog(AppCompatActivity activity, OnDialogButtonClickListener listener) {
        showDeniedDialog(activity, false, listener);
    }

    public final void showDeniedDialog(AppCompatActivity activity, boolean cancelable, OnDialogButtonClickListener listener) {
        MessageDialog.build(activity)
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setCancelable(cancelable)
                .setMessage(deniedMessage())
                .setCancelButton("取消", (dialog, v) -> {
                    dialog.doDismiss();
                    if(listener != null) {
                        listener.onClick(dialog, v);
                    }
                    return true;
                })
                .setOkButton("申请", (dialog, v) -> {
                    dialog.doDismiss();
                    loadSettingActivity(activity);
                    return true;
                })
                .show();
    }


    public static void loadSettingActivity(AppCompatActivity activity) {
        Uri packageURI = Uri.parse("package:" + activity.getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        activity.startActivity(intent);
    }
}
