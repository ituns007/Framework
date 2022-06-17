package org.ituns.framework.master.mvvm.action;

import org.ituns.framework.master.tools.android.IBundle;

public class MVVMAction extends IBundle {
    public static final int LOADING = 100001001;
    public static final int DISMISS = 100001002;
    public static final int MESSAGE = 100001003;

    private final int code;

    protected MVVMAction(int code) {
        this.code = code;
    }

    public static MVVMAction with(int code) {
        return new MVVMAction(code);
    }

    public int code() {
        return code;
    }
}