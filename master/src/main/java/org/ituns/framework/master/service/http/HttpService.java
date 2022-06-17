package org.ituns.framework.master.service.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.ituns.framework.master.service.BaseConfig;
import org.ituns.framework.master.service.BaseService;
import org.ituns.framework.master.service.http.resp.HttpResp;
import org.ituns.framework.master.service.http.transform.async.AsyncCallback;
import org.ituns.framework.master.service.http.transform.async.AsyncTransformer;
import org.ituns.framework.master.service.http.transform.sync.SyncTransformer;
import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.network.core.Client;
import org.ituns.network.core.Code;
import org.ituns.network.core.Request;
import org.ituns.network.core.Response;
import org.ituns.network.okhttp.stable.OkHttpClient;
import org.ituns.network.okhttp.stable.OkHttpConfig;

public class HttpService extends BaseService {
    private static final String TAG = "HttpService";

    public static HttpService get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final HttpService INSTANCE = new HttpService();
    }

    private Gson mGson;
    private Client mClient;
    private HttpConfig mConfig;

    private HttpService() {}


    @Override
    protected void onServiceCreate(BaseConfig config) {
        if(config instanceof HttpConfig) {
            mConfig = (HttpConfig) config;
            super.initializeService();
        }
    }

    @Override
    protected void onServiceInitialize() {
        HttpConfig config = mConfig;
        if(null == config) {
            return;
        }

        mGson = new GsonBuilder()
                .setLenient()
                .create();

        mClient = new OkHttpClient(new OkHttpConfig.Builder(config.context())
                .timeOut(config.timeout())
                .logger(new HttpLog(config.debug()))
                .build());
    }

    @Override
    protected void onServiceRelease() {
        Client client = mClient;
        if(client != null) {
            client.release();
            mClient = null;
        }
        mGson = null;
    }

    @Override
    protected void onServiceDestroy() {
        super.releaseService();
        mConfig = null;
    }

    <T extends HttpResp> T request(Request request, Class<T> clazz) {
        SyncTransformer<T> transformer = new SyncTransformer<>(mGson, clazz);

        Client client = mClient;
        if(client == null) {
            Logcat.i(TAG, "http client is null.");
            return transformer.parse(new Response.Builder()
                    .request(request)
                    .code(Code.FAIL_REQ)
                    .message("http client is null.")
                    .build());
        }

        Response response = client.requestSync(request);
        return transformer.parse(response);
    }

    <T extends HttpResp> void request(Request request, AsyncCallback<T> callback) {
        AsyncTransformer<T> transformer = new AsyncTransformer<T>(mGson, callback);

        Client client = mClient;
        if(client == null) {
            Logcat.i(TAG, "http client is null.");
            transformer.onResponse(new Response.Builder()
                    .request(request)
                    .code(Code.FAIL_REQ)
                    .message("http client is null.")
                    .build());
            return;
        }

        client.requestAsync(request, transformer);
    }
}
