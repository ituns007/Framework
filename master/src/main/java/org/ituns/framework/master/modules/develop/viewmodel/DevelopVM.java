package org.ituns.framework.master.modules.develop.viewmodel;

import org.ituns.framework.master.modules.develop.activity.DevelopActivity;
import org.ituns.framework.master.modules.develop.repository.DevelopModel;
import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;
import org.ituns.framework.master.mvvm.viewmodel.MVVMVm;
import org.ituns.framework.master.service.env.Environment;

import java.util.HashMap;

public class DevelopVM extends MVVMVm<MVVMItem> {
    private final DevelopModel developModel = new DevelopModel();
    private final HashMap<String, Environment> envMap = new HashMap<>();

    public void requestEnv(String tag, String key, DevelopActivity.Callback callback) {
        if(envMap.containsKey(key)) {
            callback.onEnv(envMap.get(key));
        } else {
            requestRemoteEnv(tag, key, callback);
        }
    }

    private void requestRemoteEnv(String tag, String key, DevelopActivity.Callback callback) {
        postAction(MVVMAction.with(MVVMAction.LOADING));
        developModel.requestData(tag, key, resp -> {
            Environment env = null;
            if(resp != null) {
                env = resp.getData();
            }
            if(env != null) {
                envMap.put(key, env);
            }
            callback.onEnv(env);
            postAction(MVVMAction.with(MVVMAction.DISMISS));
        });
    }
}
