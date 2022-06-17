package org.ituns.framework.master.service.http.transform.async;

import com.google.gson.Gson;

import org.ituns.framework.master.service.http.resp.HttpResp;
import org.ituns.framework.master.service.http.transform.Transformer;
import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.network.core.Callback;
import org.ituns.network.core.Response;

import java.lang.reflect.Type;

import static org.ituns.framework.master.service.http.HttpLog.TAG;

public class AsyncTransformer<T extends HttpResp> extends Transformer<T> implements Callback {
    private final AsyncCallback<T> mCallback;

    public AsyncTransformer(Gson gson, AsyncCallback<T> callback) {
        super(gson);
        mCallback = callback;
    }

    @Override
    protected Class<?> transformType() {
        AsyncCallback<T> callback = mCallback;
        if(callback == null) {
            Logcat.i(TAG, "callback is null.");
            return null;
        }
        return callback.transformType();
    }

    @Override
    public void onResponse(Response response) {
        AsyncCallback<T> callback = mCallback;
        if(callback == null) {
            Logcat.i(TAG, "callback is null.");
            return;
        }
        callback.onComplete(transform(response));
    }
}
