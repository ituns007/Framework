package org.ituns.framework.master.service.channel.loading;

import androidx.appcompat.app.AppCompatActivity;

import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;

import org.ituns.framework.master.service.channel.ChannelObserver;
import org.ituns.framework.master.service.logcat.Logcat;

public class LoadingObserver extends ChannelObserver<LoadingData> {
    private TipDialog dialog;

    @Override
    public void onChanged(LoadingData data) {
        AppCompatActivity activity = mActivity != null ? mActivity.get() : null;
        if(activity == null || data == null) {
            Logcat.e("activity or data is null.");
            return;
        }

        if(data.isShow()) {
            show(activity, data);
        } else {
            hide(activity);
        }
    }

    private void show(AppCompatActivity activity, LoadingData data) {
        dialog = WaitDialog.build(activity)
                .setCancelable(false)
                .setMessage(data.text())
                .setTheme(data.theme())
                .setTipTime(data.duration());
        dialog.show();
    }

    private void hide(AppCompatActivity activity) {
        if(dialog != null && dialog.isShow) {
            dialog.doDismiss();
            dialog = null;
        }
    }

    @Override
    protected boolean isMatchedData(Object data) {
        return data instanceof LoadingData;
    }
}
