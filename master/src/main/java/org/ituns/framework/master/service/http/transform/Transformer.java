package org.ituns.framework.master.service.http.transform;

import static org.ituns.framework.master.service.http.HttpLog.TAG;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.ituns.framework.master.service.http.resp.GsonResp;
import org.ituns.framework.master.service.http.resp.HttpResp;
import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.network.core.Code;
import org.ituns.network.core.Response;

import java.io.IOException;
import java.lang.reflect.Type;

public abstract class Transformer<T extends HttpResp> {
    private final Gson mGson;

    public Transformer(Gson gson) {
        mGson = gson;
    }

    protected abstract Class<?> transformType();

    protected T transform(Response response) {
        if(response == null) {
            Logcat.e(TAG, "response is null.");
            return null;
        }

        Class<?> clazz = transformType();
        if(clazz == null) {
            Logcat.e(TAG, "transform type is null.");
            return null;
        }

        int code = response.code();
        String msg = response.message();
        byte[] body = responseBody(response);

        //解析真实响应
        T resp = parseRealResp(clazz, code, msg, body);

        //解析基本响应
        if(resp == null) {
            resp = parseBaseResp(clazz, code, msg, body);
        }

        return resp;
    }

    private T parseRealResp(Class<?> clazz, int code, String msg, byte[] body) {
        T resp = fromJson(body, clazz);
        if(resp != null) {
            resp.setHttpCode(code);
            resp.setHttpMessage(msg);
            resp.setHttpBody(body);
            if(resp instanceof GsonResp) {
                ((GsonResp)resp).setGsonCode(GsonResp.CODE_SUCC);
                ((GsonResp)resp).setGsonMessage("success");
                ((GsonResp)resp).setGsonBody(fromJson(body, JsonElement.class));
            }
        }
        return resp;
    }

    private T parseBaseResp(Class<?> clazz, int code, String msg, byte[] body) {
        try {
            T resp = (T) clazz.newInstance();
            resp.setHttpCode(code);
            resp.setHttpMessage(msg);
            resp.setHttpBody(body);
            if(resp instanceof GsonResp) {
                ((GsonResp)resp).setGsonCode(GsonResp.CODE_SUCC);
                ((GsonResp)resp).setGsonMessage("success");
                ((GsonResp)resp).setGsonBody(fromJson(body, JsonElement.class));
            }
            return resp;
        } catch (Throwable t) {
            Logcat.e(TAG,"parse base resp exception:", t);
            return null;
        }
    }

    private byte[] responseBody(Response response) {
        try {
            return response.body().bytes();
        } catch (Throwable t) {
            Logcat.e(TAG,"response body exception:", t);
            return null;
        }
    }

    private <J> J fromJson(byte[] json, Type type) {
        try {
            return mGson.fromJson(new String(json), type);
        } catch (Throwable t) {
            Logcat.e(TAG,"from json exception:", t);
            return null;
        }
    }
}