package org.ituns.framework.master.service.http.transform.async;

import org.ituns.framework.master.service.http.resp.HttpResp;

import java.lang.reflect.Type;

public abstract class AsyncCallback<T extends HttpResp> {

    public abstract Class<?> transformType();

    public abstract void onComplete(T resp);
}
