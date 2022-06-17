package org.ituns.framework.master.tools.android;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class IToast {

    public static void showShort(Context context, String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, String msg) {
        show(context, msg, Toast.LENGTH_LONG);
    }

    public static void show(Context context, String msg) {
        show(context, msg, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String msg, int duration) {
        if(context == null) {
            Log.e("IToast", "context is null.");
            return;
        }

        Toast toast = Toast.makeText(context, "", duration);
        toast.setText(msg);
        toast.show();
    }
}
