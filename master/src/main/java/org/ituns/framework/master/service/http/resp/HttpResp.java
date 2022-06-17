package org.ituns.framework.master.service.http.resp;

public class HttpResp {
    protected int httpCode;
    protected String httpMessage;
    protected byte[] httpBody;

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getHttpMessage() {
        return httpMessage;
    }

    public void setHttpMessage(String httpMessage) {
        this.httpMessage = httpMessage;
    }

    public byte[] getHttpBody() {
        return httpBody;
    }

    public void setHttpBody(byte[] httpBody) {
        this.httpBody = httpBody;
    }
}
