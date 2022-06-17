package org.ituns.framework.master.modules.develop.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import org.ituns.framework.master.R;
import org.ituns.framework.master.modules.develop.viewmodel.DevelopVM;
import org.ituns.framework.master.mvvm.activity.MVVMActivity;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;
import org.ituns.framework.master.service.env.EnvService;
import org.ituns.framework.master.service.env.Environment;

public abstract class DevelopActivity extends MVVMActivity<MVVMItem> {
    private DevelopVM vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fw_activity_develop);
        vm = injectActivityVm(DevelopVM.class);
    }

    public abstract void onTitleInit(LinearLayout layout);

    public abstract void onTitleText(String text);

    public abstract void onEnvChanged(String key, String value);

    public final void requestEnv(String key, Callback callback) {
        requestEnv(EnvService.get().getTag(), key, callback);
    }

    protected void requestEnv(String tag, String key, Callback callback) {
        vm.requestEnv(tag, key, callback);
    }

    public interface Callback {
        void onEnv(Environment env);
    }
}