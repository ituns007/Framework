package org.ituns.framework.master.modules.browser;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;

import org.ituns.framework.master.R;
import org.ituns.framework.master.modules.media.VideoPreviewActivity;
import org.ituns.framework.master.mvvm.activity.MediaActivity;
import org.ituns.framework.master.service.channel.loading.LoadingProxy;
import org.ituns.framework.master.service.channel.toast.ToastProxy;
import org.ituns.framework.master.widgets.browser.TbsBrowserView;

import java.io.File;

public abstract class TbsBrowserActivity extends MediaActivity {
    private LinearLayout mTitle;
    private TbsBrowserView mBrowser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fw_activity_browser_tbs);
        mTitle = findViewById(R.id.title);
        mBrowser = findViewById(R.id.browser);
        onInitializeTitle(mTitle);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (intents.containsKey("uri") && intents.containsKey("name")){
            LoadingProxy.show();
            String name = intents.getString("name");
            Uri uri = intents.getParcelable("uri");
            mBrowser.generateFileFromUrl(name, uri).observe(this, file -> {
                LoadingProxy.hide();
                refreshBrowserView(file);
            });
        } else if(intents.containsKey("file")) {
            File file = intents.getSerializable("file");
            refreshBrowserView(file);
        } else {
            showExitDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBrowser.destroy();
    }

    protected abstract void onInitializeTitle(LinearLayout layout);

    private void refreshBrowserView(File file) {
        try {
            mBrowser.prepareBrowserView(file);
        } catch (Exception e) {
            ToastProxy.show(e.getMessage());
            finish();
        }
    }

    private void showExitDialog() {
        MessageDialog.build(this)
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setTitle("错误")
                .setMessage("预览文件路径为空")
                .setCancelable(false)
                .setCancelButton("退出", (dialog, v) -> {
                    finish();
                    return false;
                })
                .show();
    }

    public static void showUri(Activity activity, String name, Uri uri) {
        Intent intent = new Intent(activity, TbsBrowserActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("uri", uri);
        activity.startActivity(intent);
    }

    public static void showPath(Activity activity, String path) {
        showFile(activity, new File(path));
    }

    public static void showFile(Activity activity, File file) {
        Intent intent = new Intent(activity, TbsBrowserActivity.class);
        intent.putExtra("file", file);
        activity.startActivity(intent);
    }
}