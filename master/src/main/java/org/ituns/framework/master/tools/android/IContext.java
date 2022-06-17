package org.ituns.framework.master.tools.android;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public class IContext {
    private static final String TAG = "IContext";

    public static Context applicationContext(Context context) {
        if(context instanceof Application) {
            return context;
        }
        return context == null ? null : context.getApplicationContext();
    }

    public static int versionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return (int)packageInfo.getLongVersionCode();
            } else {
                return packageInfo.versionCode;
            }
        } catch (Exception e) {
            Log.i(TAG, "version code exception:", e);
            return 1;
        }
    }

    public static String versionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            Log.i(TAG, "version name exception:", e);
            return "1.0.0";
        }
    }

    public static float dimen(Context context, int resId) {
        return context.getResources().getDimension(resId);
    }

    public static float dimenPixel(Context context, int resId) {
        return context.getResources().getDimensionPixelSize(resId);
    }
}
