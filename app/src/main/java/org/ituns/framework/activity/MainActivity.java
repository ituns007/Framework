package org.ituns.framework.activity;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;

import org.ituns.framework.R;
import org.ituns.framework.master.ContextWrapper;
import org.ituns.framework.master.mvvm.activity.MediaActivity;
import org.ituns.framework.master.service.channel.loading.LoadingProxy;
import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.framework.master.service.tasks.AsyncProxy;
import org.ituns.framework.master.service.tasks.MainProxy;
import org.ituns.framework.master.service.tbs.TbsHelper;
import org.ituns.framework.master.tools.java.IList;
import org.ituns.framework.master.tools.media.IBitmap;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends MediaActivity {
    private static final String[] PERMISSION = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        checkPermissions(123, PERMISSION);
    }

    @Override
    public void onPermissionGrant(int requestCode) {
        super.onPermissionGrant(requestCode);
        if(requestCode == 123) {
            Logcat.e("123");
        }
    }

    public void clickTest1(View view) {
        selectPhoto(123, 1);
    }

    @Override
    protected void onMediaResult(int requestCode, List<Uri> uris) {
        if(IList.isEmpty(uris)) return;
        byte[] bytes = IBitmap.read(ContextWrapper.get(), uris.get(0))
                .memLength(4 * IBitmap.MB).write().bytes();
    }

    public void clickTest2(View view) {
//        Test2Activity.load(this);

        String url = "https://cxylfile.oss-accelerate.aliyuncs.com/yl/20220622/web-0ce0d7d3-bb53-490e-89a8-bd292c72c903.docx";
        Uri attachUri = Uri.parse(url);
        LoadingProxy.show();
        AsyncProxy.execute(() -> {
            File file = TbsHelper.fileFromUri(MainActivity.this, "temp.docx", attachUri);
            LoadingProxy.hide();
            if(file == null) {
                Logcat.e("file is null.");
                return;
            }

            MainProxy.post(() -> {
                HashMap<String, String> params = new HashMap<>();
                params.put("style", "1");
                params.put("local", "true");
//                params.put("topbar_title", "true");
//                params.put("topBarBgColor", "#2490F9");
                int ret = QbSdk.startMiniQBToLoadUrl(MainActivity.this, file.getAbsolutePath(), params, (ValueCallback<String>) s -> {});
                Logcat.e("Result:" + ret);
            });
        });
    }

    public void clickTest3(View view) {
        Test3Activity.load(this);
    }

    public void clickTest4(View view) {
        Test4Activity.load(this);
    }

    public void clickTest5(View view) {
        Test5Activity.load(this);
    }

    public void clickTest6(View view) {
        Test6Activity.load(this);
    }
}