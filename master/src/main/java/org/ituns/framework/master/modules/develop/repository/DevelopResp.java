package org.ituns.framework.master.modules.develop.repository;

import org.ituns.framework.master.service.env.Environment;
import org.ituns.framework.master.service.http.resp.HttpResp;

public class DevelopResp extends HttpResp {
    private int code;
    private Environment data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Environment getData() {
        return data;
    }

    public void setData(Environment data) {
        this.data = data;
    }
}
