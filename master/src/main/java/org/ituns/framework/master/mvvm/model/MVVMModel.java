package org.ituns.framework.master.mvvm.model;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.ituns.framework.master.service.http.HttpProxy;
import org.ituns.framework.master.service.http.resp.HttpResp;
import org.ituns.framework.master.service.http.transform.async.AsyncCallback;
import org.ituns.framework.master.tools.android.IBundle;
import org.ituns.framework.master.tools.java.IClass;
import org.ituns.network.core.Request;

import java.util.Map;

public abstract class MVVMModel<T extends HttpResp> {
    private final MutableLiveData<T> liveData = new MutableLiveData<>();
    private final ThreadLocal<MVVMData> localData = new ThreadLocal<>();

    public final LiveData<T> liveData() {
        return liveData;
    }

    protected final void requestParam(String key, Object value) {
        MVVMData data = initializeAndGetData();
        data.put(key, value);
    }

    protected final void requestParams(Bundle bundle) {
        MVVMData data = initializeAndGetData();
        data.clear();
        data.putAll(bundle);
    }

    protected final void requestParams(IBundle bundle) {
        MVVMData data = initializeAndGetData();
        data.clear();
        data.putAll(bundle);
    }

    protected final void requestParams(Map<String, Object> map) {
        MVVMData data = initializeAndGetData();
        data.clear();
        data.putAll(map);
    }

    protected final void appendParams(Bundle bundle) {
        MVVMData data = initializeAndGetData();
        data.putAll(bundle);
    }

    protected final void appendParams(IBundle bundle) {
        MVVMData data = initializeAndGetData();
        data.putAll(bundle);
    }

    protected final void appendParams(Map<String, Object> map) {
        MVVMData data = initializeAndGetData();
        data.putAll(map);
    }

    protected final void clearParams() {
        MVVMData data = initializeAndGetData();
        data.clear();
    }

    protected final T requestSync(Class<T> clazz) {
        MVVMData data = initializeAndGetData();
        T resp = requestSync(httpRequest(data), clazz);
        return onRequestSyncComplete(data, resp);
    }

    protected T requestSync(Request request, Class<T> clazz) {
        return HttpProxy.request(request, clazz);
    }

    protected abstract T onRequestSyncComplete(MVVMData data, T resp);

    protected final void requestAsync() {
        requestAsync(null);
    }

    protected final void requestAsync(Callback<T> callback) {
        MVVMData data = initializeAndGetData();
        requestSync(httpRequest(data), callback);
    }

    protected void requestSync(Request request, Callback<T> callback) {
        MVVMData data = initializeAndGetData();
        HttpProxy.request(request, new AsyncCallback<T>() {
            @Override
            public Class<?> transformType() {
                return IClass.parameterClass(MVVMModel.this, HttpResp.class);
            }

            @Override
            public void onComplete(T resp) {
                responseAsync(onRequestAsyncComplete(data, resp), callback);
            }
        });
    }

    protected abstract T onRequestAsyncComplete(MVVMData data, T resp);

    protected final void responseAsync(T resp) {
        responseAsync(resp, null);
    }

    protected final void responseAsync(T resp, Callback<T> callback) {
        if(callback != null) {
            callback.onResp(resp);
        } else {
            liveData.postValue(resp);
        }
    }

    private Request httpRequest(MVVMData data) {
        String url = onHost() + onPath(data);
        return onRequest(url, data);
    }

    protected abstract String onHost();

    protected abstract String onPath(MVVMData data);

    protected abstract Request onRequest(String url, MVVMData data);

    private MVVMData initializeAndGetData() {
        MVVMData data = localData.get();
        if(data == null) {
            data = new MVVMData();
            localData.set(data);
        }
        return data;
    }

    public interface Callback<T> {
        void onResp(T resp);
    }
}
