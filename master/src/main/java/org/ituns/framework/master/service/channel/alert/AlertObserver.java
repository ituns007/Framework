package org.ituns.framework.master.service.channel.alert;

import androidx.appcompat.app.AppCompatActivity;

import com.kongzue.dialog.v3.TipDialog;

import org.ituns.framework.master.service.channel.ChannelObserver;
import org.ituns.framework.master.service.logcat.Logcat;

public class AlertObserver extends ChannelObserver<AlertData> {

    @Override
    public void onChanged(AlertData data) {
        AppCompatActivity activity = mActivity != null ? mActivity.get() : null;
        if(activity == null || data == null) {
            Logcat.e("activity or data is null.");
            return;
        }

        TipDialog.build(activity)
                .setMessage(data.text())
                .setTheme(data.theme())
                .setCancelable(false)
                .setTip(data.type())
                .show();
    }

    @Override
    protected boolean isMatchedData(Object data) {
        return data instanceof AlertData;
    }
}
