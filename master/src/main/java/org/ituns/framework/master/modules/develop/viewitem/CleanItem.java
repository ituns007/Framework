package org.ituns.framework.master.modules.develop.viewitem;

import org.ituns.framework.master.mvvm.viewitem.MVVMItem;

public class CleanItem extends MVVMItem {
    public static final int TYPE_TITLE = 1;
    public static final int TYPE_CACHE = 2;

    public static final int ACTION_CACHE = 1;

    public CleanItem(int type, int action) {
        super(type, action);
    }

    public static CleanItem title(String name) {
        CleanItem item = new CleanItem(TYPE_TITLE, 0);
        item.put("name", name);
        return item;
    }

    public static CleanItem cache(String name) {
        CleanItem item = new CleanItem(TYPE_CACHE, ACTION_CACHE);
        item.put("name", name);
        return item;
    }

    public static CleanItem cache(int type, String name, String cache) {
        CleanItem item = new CleanItem(TYPE_CACHE, ACTION_CACHE);
        item.put("type", type);
        item.put("name", name);
        item.put("cache", cache);
        return item;
    }
}
