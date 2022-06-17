package org.ituns.framework.master.modules.develop.viewitem;

import org.ituns.framework.master.mvvm.viewitem.MVVMItem;

public class ActionItem extends MVVMItem {
    public static final int TYPE_ITEM = 1;

    public static final int ACTION_ENV = 1;
    public static final int ACTION_CLEAN = 2;

    public ActionItem(int type, int action) {
        super(type, action);
    }

    public static ActionItem env() {
        ActionItem item = new ActionItem(TYPE_ITEM, ACTION_ENV);
        item.put("name", "环境配置");
        return item;
    }

    public static ActionItem clean() {
        ActionItem item = new ActionItem(TYPE_ITEM, ACTION_CLEAN);
        item.put("name", "垃圾清理");
        return item;
    }
}
