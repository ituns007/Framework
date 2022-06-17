package org.ituns.framework.master.tools.android;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

import static android.provider.Settings.Secure.ANDROID_ID;

public class ISettings {

    public static String androidId(Context context) {
        try {
            ContentResolver resolver = context.getContentResolver();
            return Settings.Secure.getString(resolver, ANDROID_ID);
        } catch (Exception e) {
            return "";
        }
    }
}
