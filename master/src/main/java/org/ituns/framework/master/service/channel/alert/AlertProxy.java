package org.ituns.framework.master.service.channel.alert;

import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.TipDialog;

import org.ituns.framework.master.service.channel.ChannelProxy;

public class AlertProxy {

    public static void showError(String msg) {
        showErrorLight(msg);
    }

    public static void showErrorDark(String msg) {
        ChannelProxy.postData(new AlertData(msg, TipDialog.TYPE.ERROR, DialogSettings.THEME.DARK));
    }

    public static void showErrorLight(String msg) {
        ChannelProxy.postData(new AlertData(msg, TipDialog.TYPE.ERROR, DialogSettings.THEME.LIGHT));
    }

    public static void showWarning(String msg) {
        showWarningLight(msg);
    }

    public static void showWarningDark(String msg) {
        ChannelProxy.postData(new AlertData(msg, TipDialog.TYPE.WARNING, DialogSettings.THEME.DARK));
    }

    public static void showWarningLight(String msg) {
        ChannelProxy.postData(new AlertData(msg, TipDialog.TYPE.WARNING, DialogSettings.THEME.LIGHT));
    }

    public static void showSuccess(String msg) {
        showSuccessLight(msg);
    }

    public static void showSuccessDark(String msg) {
        ChannelProxy.postData(new AlertData(msg, TipDialog.TYPE.SUCCESS, DialogSettings.THEME.DARK));
    }

    public static void showSuccessLight(String msg) {
        ChannelProxy.postData(new AlertData(msg, TipDialog.TYPE.SUCCESS, DialogSettings.THEME.LIGHT));
    }
}
