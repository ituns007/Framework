package org.ituns.framework.master.service.env;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ituns.framework.master.service.BaseConfig;
import org.ituns.framework.master.service.BaseService;
import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.framework.master.tools.java.IString;
import org.ituns.framework.master.tools.storage.IStream;

import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class EnvService extends BaseService {
    private static final String SHARED_NAME = "ituns_environment_shared";

    public static EnvService get() {
        return EnvService.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final EnvService INSTANCE = new EnvService();
    }

    private EnvConfig mConfig;
    private SharedPreferences mShared;
    private final HashMap<String, String> defEnv = new HashMap<>();
    private final HashMap<String, Environment> localEnv = new HashMap<>();
    private EnvService() {}

    @Override
    protected void onServiceCreate(BaseConfig config) {
        if(config instanceof EnvConfig) {
            mConfig = (EnvConfig) config;
            super.initializeService();
        }
    }

    @Override
    protected void onServiceInitialize() {
        EnvConfig config = mConfig;
        if(config != null) {
            initializeShared(config.context());
            initializeDefConfig(config.context(), config.def());
            initializeLocalConfig(config.context(), config.local());
        }
    }

    private void initializeShared(Context context) {
        mShared = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
    }

    private void initializeDefConfig(Context context, String def) {
        try {
            defEnv.clear();
            Properties properties = new Properties();
            properties.load(context.getAssets().open(def));
            Enumeration<?> enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement().toString();
                String value = properties.getProperty(key);
                if(IString.notEmpty(key, value)) {
                    defEnv.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeLocalConfig(Context context, String local) {
        try {
            localEnv.clear();
            byte[] bytes = IStream.input(context.getAssets().open(local)).readBytes();
            Type type = new TypeToken<HashMap<String, Environment>>(){}.getType();
            localEnv.putAll( new Gson().fromJson(new String(bytes), type));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onServiceRelease() {
        mShared = null;
        defEnv.clear();
        localEnv.clear();
    }

    @Override
    protected void onServiceDestroy() {
        super.releaseService();
        mConfig = null;
    }

    public String getTag() {
        EnvConfig config = mConfig;
        if(config != null) {
            return config.tag();
        }
        return "";
    }

    @Deprecated
    public String getEnv(String key, String def) {
        return getString(key, def);
    }

    @Deprecated
    public void setEnv(String key, String value) {
        setString(key, value);
    }

    public boolean getBoolean(String key, boolean def) {
        SharedPreferences shared = mShared;
        if(shared == null) {
            Logcat.e("shared is null.");
            return def;
        }

        if(shared.contains(key)) {
            return shared.getBoolean(key, def);
        } else if(defEnv.containsKey(key)) {
            try {
                return Boolean.parseBoolean(defEnv.get(key));
            } catch (Exception e) {
                return def;
            }
        }
        return def;
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences shared = mShared;
        if(shared == null) {
            Logcat.e("shared is null.");
            return;
        }
        shared.edit().putBoolean(key, value).apply();
    }

    public float getFloat(String key, float def) {
        SharedPreferences shared = mShared;
        if(shared == null) {
            Logcat.e("shared is null.");
            return def;
        }

        if(shared.contains(key)) {
            return shared.getFloat(key, def);
        } else if(defEnv.containsKey(key)) {
            try {
                return Float.parseFloat(defEnv.get(key));
            } catch (Exception e) {
                return def;
            }
        }
        return def;
    }

    public void setFloat(String key, float value) {
        SharedPreferences shared = mShared;
        if(shared == null) {
            Logcat.e("shared is null.");
            return;
        }
        shared.edit().putFloat(key, value).apply();
    }

    public int getInt(String key, int def) {
        SharedPreferences shared = mShared;
        if(shared == null) {
            Logcat.e("shared is null.");
            return def;
        }

        if(shared.contains(key)) {
            return shared.getInt(key, def);
        } else if(defEnv.containsKey(key)) {
            try {
                return Integer.parseInt(defEnv.get(key));
            } catch (Exception e) {
                return def;
            }
        }
        return def;
    }

    public void setInt(String key, int value) {
        SharedPreferences shared = mShared;
        if(shared == null) {
            Logcat.e("shared is null.");
            return;
        }
        shared.edit().putInt(key, value).apply();
    }

    public long getLong(String key, long def) {
        SharedPreferences shared = mShared;
        if(shared == null) {
            Logcat.e("shared is null.");
            return def;
        }

        if(shared.contains(key)) {
            return shared.getLong(key, def);
        } else if(defEnv.containsKey(key)) {
            try {
                return Long.parseLong(defEnv.get(key));
            } catch (Exception e) {
                return def;
            }
        }
        return def;
    }

    public void setLong(String key, long value) {
        SharedPreferences shared = mShared;
        if(shared == null) {
            Logcat.e("shared is null.");
            return;
        }
        shared.edit().putLong(key, value).apply();
    }

    public String getString(String key, String def) {
        SharedPreferences shared = mShared;
        if(shared == null) {
            Logcat.e("shared is null.");
            return def;
        }

        if(shared.contains(key)) {
            return shared.getString(key, def);
        } else if(defEnv.containsKey(key)) {
            return defEnv.get(key);
        }
        return def;
    }

    public void setString(String key, String value) {
        SharedPreferences shared = mShared;
        if(shared == null) {
            Logcat.e("shared is null.");
            return;
        }
        shared.edit().putString(key, value).apply();
    }

    public Set<String> getDefKey() {
        return defEnv.keySet();
    }

    public Environment getLocalEnv(String name) {
        return localEnv.get(name);
    }
}
