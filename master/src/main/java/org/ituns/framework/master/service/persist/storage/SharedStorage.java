package org.ituns.framework.master.service.persist.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.ituns.framework.master.service.logcat.Logcat;

import static org.ituns.framework.master.service.persist.PersistService.TAG;

public class SharedStorage {
    private static final String SHARED_NAME = "ituns_storage_shared";

    public static final String GLOBAL_TOKEN = "ituns_global_token";
    public static final String GLOBAL_USERID = "ituns_global_userid";

    private SharedPreferences mShared;

    public SharedStorage(Context context) {
        mShared = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
    }

    public void auth(String token) {
        putGlobal(GLOBAL_TOKEN, token);
    }

    public void login(String userId) {
        putGlobal(GLOBAL_USERID, userId);
    }

    public void logout() {
        removeGlobal(GLOBAL_USERID);
        removeGlobal(GLOBAL_TOKEN);
    }

    public String getUser(String key) {
        String userId = readUserId();
        return getGlobal(composeKey(userId, key));
    }

    public String getUser(String key, String defaultValue) {
        String userId = readUserId();
        return getGlobal(composeKey(userId, key), defaultValue);
    }

    public void putUser(String key, String value) {
        String userId = readUserId();
        putGlobal(composeKey(userId, key), value);
    }

    public void removeUser(String key) {
        String userId = readUserId();
        removeGlobal(composeKey(userId, key));
    }

    public String getGlobal(String key) {
        return getGlobal(key, "");
    }

    public String getGlobal(String key, String defaultValue) {
        if(TextUtils.isEmpty(key)) {
            Logcat.i(TAG, "key is empty.");
            return defaultValue;
        }

        SharedPreferences shared = mShared;
        if(shared == null) {
            Logcat.i(TAG, "shared is null.");
            return defaultValue;
        }

        return shared.getString(key, defaultValue);
    }

    public void putGlobal(String key, String value) {
        SharedPreferences shared = mShared;
        if(shared == null) {
            Logcat.i(TAG, "shared is null.");
            return;
        }
        shared.edit().putString(key, value).apply();
    }

    public void removeGlobal(String key) {
        SharedPreferences shared = mShared;
        if(shared == null) {
            Logcat.i(TAG, "shared is null.");
            return;
        }
        shared.edit().remove(key).apply();
    }

    private String readUserId() {
        return getGlobal(GLOBAL_USERID);
    }

    private String composeKey(String key, String name) {
        return TextUtils.isEmpty(key) ? name : key + "_" + name;
    }

    public void release() {
        mShared = null;
    }
}
