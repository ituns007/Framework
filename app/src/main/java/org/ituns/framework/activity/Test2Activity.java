package org.ituns.framework.activity;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;

import org.ituns.framework.R;
import org.ituns.framework.master.mvvm.activity.MediaActivity;
import org.ituns.framework.master.service.channel.loading.LoadingProxy;
import org.ituns.framework.master.widgets.browser.TbsBrowserView;

import java.util.HashMap;

public class Test2Activity extends MediaActivity {
    private TbsBrowserView browserView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        browserView = findViewById(R.id.browser);
        Log.e("MC", "TbsVer:" + QbSdk.getTbsVersion(this));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LoadingProxy.show();
        Uri attachUri = Uri.parse("https://cxylfile.oss-accelerate.aliyuncs.com/yl/20220622/web-0ce0d7d3-bb53-490e-89a8-bd292c72c903.docx");
        browserView.generateFileFromUrl("temp.docx", attachUri).observe(Test2Activity.this, file -> {
            LoadingProxy.hide();
//            try {
//                browserView.prepareBrowserView(file);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        browserView.destroy();
    }

    public static void load(Activity activity) {
        Intent intent = new Intent(activity, Test2Activity.class);
        activity.startActivity(intent);
    }
}