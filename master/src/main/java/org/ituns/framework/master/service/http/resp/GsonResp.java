package org.ituns.framework.master.service.http.resp;

import com.google.gson.JsonElement;

public class GsonResp extends HttpResp {
    public static final int CODE_SUCC = 0;
    public static final int CODE_RESP = 1;
    public static final int CODE_PARSE = 2;
    protected int gsonCode;
    protected String gsonMessage;
    protected JsonElement gsonBody;

    public int getGsonCode() {
        return gsonCode;
    }

    public void setGsonCode(int gsonCode) {
        this.gsonCode = gsonCode;
    }

    public String getGsonMessage() {
        return gsonMessage;
    }

    public void setGsonMessage(String gsonMessage) {
        this.gsonMessage = gsonMessage;
    }

    public JsonElement getGsonBody() {
        return gsonBody;
    }

    public void setGsonBody(JsonElement gsonBody) {
        this.gsonBody = gsonBody;
    }
}
