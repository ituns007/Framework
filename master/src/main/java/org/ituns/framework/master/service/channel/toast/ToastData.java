package org.ituns.framework.master.service.channel.toast;

import org.ituns.framework.master.service.channel.ChannelData;

public class ToastData extends ChannelData {
    private String text;
    private boolean isShort;

    public ToastData(String text, boolean isShort) {
        this.text = text;
        this.isShort = isShort;
    }

    public String text() {
        return text;
    }

    public boolean isShort() {
        return isShort;
    }
}
