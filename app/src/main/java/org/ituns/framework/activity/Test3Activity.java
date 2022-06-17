package org.ituns.framework.activity;

import android.app.Activity;
import android.content.Intent;
import android.webkit.WebView;

import org.ituns.framework.master.modules.browser.WebBrowserActivity;

public class Test3Activity extends WebBrowserActivity<Object> {

    @Override
    protected void onBrowserInit(WebView webView) {
        super.onBrowserInit(webView);
    }

    @Override
    protected void onBrowserContent(WebView webView) {
        webView.loadUrl("http://www.baidu.com/");
    }

    public static void load(Activity activity) {
        Intent intent = new Intent(activity, Test3Activity.class);
        activity.startActivity(intent);
    }
}
