package org.ituns.framework.master.modules.develop.viewitem;

import org.ituns.framework.master.mvvm.viewitem.MVVMItem;

public class EnvCollectItem extends MVVMItem {
    public static final int TYPE_ITEM = 1;

    public static final int ACTION_ITEM = 1;

    public EnvCollectItem(int type, int action) {
        super(type, action);
    }

    public static EnvCollectItem item(String key, String name) {
        EnvCollectItem item = new EnvCollectItem(TYPE_ITEM, ACTION_ITEM);
        item.put("key", key);
        item.put("name", name);
        return item;
    }
}
