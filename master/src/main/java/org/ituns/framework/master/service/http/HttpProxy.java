package org.ituns.framework.master.service.http;

import org.ituns.framework.master.service.http.resp.HttpResp;
import org.ituns.framework.master.service.http.transform.async.AsyncCallback;
import org.ituns.network.core.Request;

public class HttpProxy {

    public static <T extends HttpResp> T request(Request request, Class<T> clazz) {
        return HttpService.get().request(request, clazz);
    }

    public static <T extends HttpResp> void request(Request request, AsyncCallback<T> callback) {
        HttpService.get().request(request, callback);
    }
}
