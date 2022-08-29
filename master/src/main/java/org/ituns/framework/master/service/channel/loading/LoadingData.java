package org.ituns.framework.master.service.channel.loading;

import com.kongzue.dialog.util.DialogSettings;

import org.ituns.framework.master.service.channel.ChannelData;

public class LoadingData extends ChannelData {
    private static final int DURATION = 15000;
    private boolean isShow;
    private String text;
    private DialogSettings.THEME theme;
    private int duration;

    public LoadingData(boolean isShow, String text, DialogSettings.THEME theme) {
        this.isShow = isShow;
        this.text = text;
        this.theme = theme;
        this.duration = DURATION;
    }
    public LoadingData(boolean isShow, String text, DialogSettings.THEME theme, int duration) {
        this.isShow = isShow;
        this.text = text;
        this.theme = theme;
        this.duration = duration;
    }

    public boolean isShow() {
        return isShow;
    }

    public String text() {
        return text;
    }

    public DialogSettings.THEME theme() {
        return theme;
    }

    public int duration() {
        return duration;
    }
}
