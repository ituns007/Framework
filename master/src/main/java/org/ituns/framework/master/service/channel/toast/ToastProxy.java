package org.ituns.framework.master.service.channel.toast;

import org.ituns.framework.master.service.channel.ChannelProxy;

public class ToastProxy {
    public static void show(String msg) {
        showShort(msg);
    }

    public static void showShort(String msg) {
        ChannelProxy.postData(new ToastData(msg, true));
    }

    public static void showLong(String msg) {
        ChannelProxy.postData(new ToastData(msg, false));
    }
}
