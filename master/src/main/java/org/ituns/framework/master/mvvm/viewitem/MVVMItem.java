package org.ituns.framework.master.mvvm.viewitem;

import org.ituns.framework.master.tools.android.IBundle;

public class MVVMItem extends IBundle {
    private int type;
    private int action;

    public MVVMItem(int type) {
        this.type = type;
        this.action = 0;
    }

    public MVVMItem(int type, int action) {
        this.type = type;
        this.action = action;
    }

    public int type() {
        return type;
    }

    public int action() {
        return action;
    }
}
