package org.ituns.framework.master.service.channel.toast;

import androidx.appcompat.app.AppCompatActivity;

import org.ituns.framework.master.service.channel.ChannelObserver;
import org.ituns.framework.master.service.channel.alert.AlertData;
import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.framework.master.tools.android.IToast;
import org.ituns.framework.master.tools.java.IString;

public class ToastObserver extends ChannelObserver<ToastData> {

    @Override
    public void onChanged(ToastData data) {
        AppCompatActivity activity = mActivity != null ? mActivity.get() : null;
        if(activity == null || data == null) {
            Logcat.e("activity or data is null.");
            return;
        }

        if(IString.isEmpty(data.text())) {
            Logcat.e("toast text is empty.");
            return;
        }

        if(data.isShort()) {
            IToast.showShort(activity, data.text());
        } else {
            IToast.showLong(activity, data.text());
        }
    }

    @Override
    protected boolean isMatchedData(Object data) {
        return data instanceof ToastData;
    }
}
