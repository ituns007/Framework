package org.ituns.framework.master.tools.android;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import org.ituns.framework.master.service.logcat.Logcat;

public class IWindow {

    public static void statusBar(Activity activity, boolean isLight, int color) {
        Window window = activity.getWindow();
        window.setStatusBarColor(color);
        WindowInsetsControllerCompat controller = windowController(window);
        if(controller != null) {
            controller.setAppearanceLightStatusBars(isLight);
        }
    }

    public static void navigationBar(Activity activity, boolean isLight, int color) {
        Window window = activity.getWindow();
        window.setNavigationBarColor(color);
        WindowInsetsControllerCompat controller = windowController(window);
        if(controller != null) {
            controller.setAppearanceLightNavigationBars(isLight);
        }
    }

    private static WindowInsetsControllerCompat windowController(Window window) {
        return WindowCompat.getInsetsController(window, window.getDecorView());
    }
}
