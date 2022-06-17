package org.ituns.framework.master.service.channel.loading;

import com.kongzue.dialog.util.DialogSettings;

import org.ituns.framework.master.service.channel.ChannelProxy;

public class LoadingProxy {

    public static void show() {
        showLight("");
    }

    public static void show(String msg) {
        showLight(msg);
    }

    public static void showDark(String msg) {
        ChannelProxy.postData(new LoadingData(true, msg, DialogSettings.THEME.DARK));
    }

    public static void showLight(String msg) {
        ChannelProxy.postData(new LoadingData(true, msg, DialogSettings.THEME.LIGHT));
    }

    public static void hide() {
        ChannelProxy.postData(new LoadingData(false, "", DialogSettings.THEME.LIGHT));
    }
}
