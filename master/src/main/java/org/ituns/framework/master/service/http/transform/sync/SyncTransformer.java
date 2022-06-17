package org.ituns.framework.master.service.http.transform.sync;

import com.google.gson.Gson;

import org.ituns.framework.master.service.http.resp.HttpResp;
import org.ituns.framework.master.service.http.transform.Transformer;
import org.ituns.network.core.Response;

import java.lang.reflect.Type;

public class SyncTransformer<T extends HttpResp> extends Transformer<T> {
    private final Class<T> mClazz;

    public SyncTransformer(Gson gson, Class<T> clazz) {
        super(gson);
        mClazz = clazz;
    }

    @Override
    protected Class<?> transformType() {
        return mClazz;
    }

    public T parse(Response response) {
        return transform(response);
    }
}