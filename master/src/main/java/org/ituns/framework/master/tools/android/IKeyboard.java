package org.ituns.framework.master.tools.android;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Field;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.inputmethod.InputMethodManager.SHOW_FORCED;

public class IKeyboard {

    public static boolean isShowing(Context context) {
        Object service = context.getSystemService(INPUT_METHOD_SERVICE);
        if(service instanceof InputMethodManager) {
            return ((InputMethodManager) service).isActive();
        }
        return false;
    }

    public static void show(Context context, EditText view) {
        Object service = context.getSystemService(INPUT_METHOD_SERVICE);
        if(service instanceof InputMethodManager) {
            view.requestFocus();
            ((InputMethodManager) service).showSoftInput(view, SHOW_FORCED);
        }
    }

    public static void hide(Activity activity) {
        hide(activity, activity.getCurrentFocus());
    }

    public static void hide(Activity activity, View view) {
        if(view == null) {
            return;
        }

        Object service = activity.getSystemService(INPUT_METHOD_SERVICE);
        if(service instanceof InputMethodManager) {
            view.clearFocus();
            ((InputMethodManager) service).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void fixMemoryLeak(Context context) {
        Object service = context.getSystemService(INPUT_METHOD_SERVICE);
        if(service instanceof InputMethodManager) {
            clearLastSrvView(service);
        }
    }

    private static void clearLastSrvView(Object service) {
        try {
            Class<?> clazz = InputMethodManager.class;
            Field field = clazz.getDeclaredField("mLastSrvView");
            field.setAccessible(true);
            field.set(service, null);
        } catch (Exception e) {}
    }
}
