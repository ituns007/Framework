package org.ituns.framework.master.tools.android;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class IScreen {

    public static float width(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getRealMetrics(metrics);
        return (float) metrics.widthPixels;
    }

    public static float height(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getRealMetrics(metrics);
        return (float) metrics.heightPixels;
    }

    public static float density(Context context) {
        Resources resources = context.getResources();
        return resources.getDisplayMetrics().density;
    }

    public static int dp2px(Context context, float dp) {
        Resources resources = context.getResources();
        float density = resources.getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public static int px2dp(Context context, float px) {
        Resources resources = context.getResources();
        float density = resources.getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    public static int sp2px(Context context, float sp) {
        Resources resources = context.getResources();
        float density = resources.getDisplayMetrics().scaledDensity;
        return (int) (sp * density + 0.5f);
    }

    public static int px2sp(Context context, float px) {
        Resources resources = context.getResources();
        float density = resources.getDisplayMetrics().scaledDensity;
        return (int) (px / density + 0.5f);
    }
}
