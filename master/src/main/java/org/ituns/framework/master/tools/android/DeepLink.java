package org.ituns.framework.master.tools.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.MATCH_ALL;

public class DeepLink {
    private static final String TAG = "DeepLink";

    public static boolean resolveApp(Context context, String url, String packageId) {
        try {
            return activityInfo(context, Uri.parse(url), packageId) != null;
        } catch (Exception e) {
            Log.i(TAG, "resolve app exception:", e);
            return false;
        }
    }

    public static boolean openApp(Context context, String url, String packageId) {
        try {
            return openApp(context, Uri.parse(url), packageId);
        } catch (Exception e) {
            Log.i(TAG, "open app with url exception:", e);
            return false;
        }
    }

    public static boolean openApp(Context context, Uri uri, String packageId) {
        try {
            ActivityInfo activityInfo = activityInfo(context, uri, packageId);
            if(activityInfo == null) {
                throw new NullPointerException("activityInfo is null.");
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName(activityInfo.packageName, activityInfo.name);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.i(TAG, "open app with uri exception:", e);
            return false;
        }
    }

    public static ActivityInfo activityInfo(Context context, Uri uri, String packageId) {
        try {
            List<ActivityInfo> activityInfos = activityInfos(context, uri);
            for(ActivityInfo activityInfo : activityInfos) {
                if(activityInfo.packageName.equals(packageId)) {
                    return activityInfo;
                }
            }
            return null;
        } catch (Exception e) {
            Log.i(TAG, "activity info exception:", e);
            return null;
        }
    }

    public static List<ActivityInfo> activityInfos(Context context, Uri uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            ArrayList<ActivityInfo> activityInfos = new ArrayList<>();
            PackageManager packageManager = context.getPackageManager();
            List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, MATCH_ALL);
            for(ResolveInfo info : resolveInfos) {
                ActivityInfo activityInfo = info.activityInfo;
                if(activityInfo == null) {
                    continue;
                }

                //Activity没有对外开放或不可用
                if(!activityInfo.exported || !activityInfo.enabled) {
                    continue;
                }

                activityInfos.add(activityInfo);
            }
            return activityInfos;
        } catch (Exception e) {
            Log.i(TAG, "activity info list exception:", e);
            return new ArrayList<>();
        }
    }
}