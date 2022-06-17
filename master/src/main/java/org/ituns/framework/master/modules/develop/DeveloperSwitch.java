package org.ituns.framework.master.modules.develop;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.InputDialog;

import org.ituns.framework.master.modules.develop.activity.DevelopActivity;
import org.ituns.framework.master.service.channel.toast.ToastProxy;
import org.ituns.framework.master.service.persist.SharedProxy;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class DeveloperSwitch {
    private static final String KEY = "ituns_global_develop";

    private long lastTouchTimeMillis = 0;
    private final AtomicInteger counter = new AtomicInteger(0);

    public <T extends DevelopActivity> void touch(AppCompatActivity activity, Class<T> clazz) {
        if("1".equals(SharedProxy.getGlobal(KEY, "0"))) {
            loadDevelopActivity(activity, clazz);
        } else {
            counter(activity, clazz);
        }
    }

    private <T extends DevelopActivity> void counter(AppCompatActivity activity, Class<T> clazz) {
        long currentTimeMillis = System.currentTimeMillis();
        if(currentTimeMillis - lastTouchTimeMillis > 3000) {
            counter.set(0);
        } else {
            counter.incrementAndGet();
        }
        lastTouchTimeMillis = currentTimeMillis;
        showCounterTips(activity, clazz);
    }

    private <T extends DevelopActivity> void showCounterTips(AppCompatActivity activity, Class<T> clazz) {
        if(counter.get() >= 10) {
            showUnlockDialog(activity, clazz);
            counter.set(0);
        } else if(counter.get() > 5) {
            String format = "再点击%d次解锁开发者模式";
            ToastProxy.show(String.format(Locale.getDefault(), format, (10 - counter.get())));
        }
    }

    private <T extends DevelopActivity> void showUnlockDialog(AppCompatActivity activity, Class<T> clazz) {
        InputDialog.build(activity)
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setCancelable(false)
                .setTitle("解锁")
                .setMessage("请输入PIN码解锁开发者模式")
                .setCancelButton("放弃")
                .setOkButton("解锁", (dialog, v, text) -> {
                    if("123456Aa".equals(text)) {
                        SharedProxy.putGlobal(KEY, "1");
                        loadDevelopActivity(activity, clazz);
                    }
                    return false;
                })
                .show();
    }

    private <T extends DevelopActivity> void loadDevelopActivity(AppCompatActivity activity, Class<T> clazz) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);
    }


    public static DeveloperSwitch get() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final DeveloperSwitch INSTANCE = new DeveloperSwitch();
    }

    private DeveloperSwitch() {}

    public interface Callback {
        void onUnlock();
    }
}