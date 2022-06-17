package org.ituns.framework.master.modules.develop.viewitem;

import org.ituns.framework.master.mvvm.viewitem.MVVMItem;
import org.ituns.framework.master.service.env.Environment;

public class EnvDetailItem extends MVVMItem {
    public static final int TYPE_TITLE = 1;
    public static final int TYPE_CONFIG = 2;
    public static final int TYPE_CURRENT = 3;

    public static final int ACTION_CONFIG = 1;
    public static final int ACTION_CURRENT = 2;

    public EnvDetailItem(int type, int action) {
        super(type, action);
    }

    public static EnvDetailItem title(String title) {
        EnvDetailItem item = new EnvDetailItem(TYPE_TITLE, 0);
        item.put("title", title);
        return item;
    }

    public static EnvDetailItem config(String key, Environment.Config config) {
        EnvDetailItem item = new EnvDetailItem(TYPE_CONFIG, ACTION_CONFIG);
        item.put("key", key);
        if(config != null) {
            item.put("name", config.getName());
            item.put("value", config.getValue());
        }
        return item;
    }

    public static EnvDetailItem current(String key, String value) {
        EnvDetailItem item = new EnvDetailItem(TYPE_CURRENT, ACTION_CURRENT);
        item.put("key", key);
        item.put("value", value);
        return item;
    }
}
