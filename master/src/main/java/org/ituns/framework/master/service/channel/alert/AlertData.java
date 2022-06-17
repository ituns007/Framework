package org.ituns.framework.master.service.channel.alert;

import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.TipDialog;

import org.ituns.framework.master.service.channel.ChannelData;

public class AlertData extends ChannelData {
    private String text;
    private TipDialog.TYPE type;
    private DialogSettings.THEME theme;

    public AlertData(String text, TipDialog.TYPE type, DialogSettings.THEME theme) {
        this.text = text;
        this.type = type;
        this.theme = theme;
    }

    public String text() {
        return text;
    }

    public TipDialog.TYPE type() {
        return type;
    }

    public DialogSettings.THEME theme() {
        return theme;
    }
}
