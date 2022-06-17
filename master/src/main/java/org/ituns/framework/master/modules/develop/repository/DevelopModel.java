package org.ituns.framework.master.modules.develop.repository;

import org.ituns.framework.master.mvvm.model.MVVMData;
import org.ituns.framework.master.mvvm.model.MVVMModel;
import org.ituns.network.core.Request;

public class DevelopModel extends MVVMModel<DevelopResp> {

    public void requestData(String tag, String key, Callback<DevelopResp> callback) {
        requestParam("tag", tag);
        requestParam("key", key);
        requestAsync(callback);
    }

    @Override
    protected String onHost() {
        return "http://static.ituns.org/";
    }

    @Override
    protected String onPath(MVVMData data) {
        String tag = data.getString("tag", "");
        String key = data.getString("key", "");
        return "configs/" + tag + "/" + key + ".json";
    }

    @Override
    protected Request onRequest(String url, MVVMData data) {
        return Request.get()
                .url(url)
                .build();
    }

    @Override
    protected DevelopResp onRequestSyncComplete(MVVMData data, DevelopResp resp) {
        return resp;
    }

    @Override
    protected DevelopResp onRequestAsyncComplete(MVVMData data, DevelopResp resp) {
        return resp;
    }
}
