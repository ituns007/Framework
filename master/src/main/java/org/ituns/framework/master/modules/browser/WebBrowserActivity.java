package org.ituns.framework.master.modules.browser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

import org.ituns.framework.master.R;
import org.ituns.framework.master.mvvm.activity.MediaActivity;
import org.ituns.framework.master.tools.java.IList;

import java.util.HashMap;
import java.util.List;

public abstract class WebBrowserActivity<T> extends MediaActivity {
    private static final int CODE_BROWSER_WEB = 1001001;

    private ValueCallback<Uri[]> mFilePathCallback;
    private LinearLayout mTitleLayout;
    private ProgressBar mProgress;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fw_activity_browser_web);
        mTitleLayout = findViewById(R.id.title);
        mProgress = findViewById(R.id.progress);
        mWebView = findViewById(R.id.browser);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initializeTitle(mTitleLayout);
        initializeProgress(mProgress);
        initializeWebView(mWebView);
        initializeBrowser(mWebView);
        configBrowserContent(mWebView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WebView webView = mWebView;
        if(webView != null) {
            webView.resumeTimers();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebView webView = mWebView;
        if(webView != null) {
            webView.pauseTimers();
            webView.destroy();
            mWebView = null;
        }
    }

    public void pressWebBack() {
        WebView webView = mWebView;
        if(webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void initializeTitle(LinearLayout layout) {
        onTitleInit(layout);
    }

    private void initializeProgress(ProgressBar progressBar) {
        onProgressInit(progressBar);
    }

    private void initializeWebView(WebView webView) {
        WebSettings setting = webView.getSettings();
        setting.setUseWideViewPort(true);//是否支持HTML的“viewport”标签或者使用wide viewport
        setting.setLoadWithOverviewMode(true);//是否允许WebView度超出以概览的方式载入页面,默认false
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//设置布局,会引起WebView的重新布局(relayout),默认值NARROW_COLUMNS
        setting.setMediaPlaybackRequiresUserGesture(false); //自动播放音频autoplay

        setting.setRenderPriority(WebSettings.RenderPriority.HIGH);//线程优先级(在API18以上已废弃。不建议调整线程优先级，未来版本不会支持这样做)
        setting.setEnableSmoothTransition(true);//已废弃,将来会成为空操作（no-op）,设置当panning或者缩放或者持有当前WebView的window没有焦点时是否允许其光滑过渡,若为true,WebView会选择一个性能最大化的解决方案。例如过渡时WebView的内容可能不更新。若为false,WebView会保持精度（fidelity）,默认值false。
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);//重写使用缓存的方式，默认值LOAD_DEFAULT
        setting.setPluginState(WebSettings.PluginState.ON);//在API18以上已废弃。未来将不支持插件,不要使用
        setting.setJavaScriptCanOpenWindowsAutomatically(true);//让JavaScript自动打开窗口,默认false

        //webview 中localStorage无效的解决方法
        setting.setAppCacheMaxSize(1024 * 1024 * 8);//设置应用缓存内容的最大值
        setting.setAppCacheEnabled(true);//应用缓存API是否可用,默认值false,结合setAppCachePath(String)使用
        setting.setAppCachePath(getCacheDir().getAbsolutePath());//设置应用缓存文件的路径
        setting.setDomStorageEnabled(true); //DOM存储API是否可用,默认false
        setting.setDefaultTextEncodingName("UTF-8");
        setting.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        setting.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        setting.setAllowFileAccessFromFileURLs(false); // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        setting.setAllowUniversalAccessFromFileURLs(false); // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false

        setting.setJavaScriptEnabled(true);//设置WebView是否允许执行JavaScript脚本,默认false
        setting.setSupportZoom(false);//WebView是否支持使用屏幕上的缩放控件和手势进行缩放,默认值true
        setting.setBuiltInZoomControls(false);//是否使用内置的缩放机制
        setting.setDisplayZoomControls(false);//使用内置的缩放机制时是否展示缩放控件,默认值true
        setting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        addJavascriptInterface(webView);
        webView.setVerticalScrollBarEnabled(false); //禁用纵向滚动条
        webView.setHorizontalScrollBarEnabled(false); //禁用横向滚动条
        webView.setDownloadListener(new DownloadListenerImpl());
        webView.setWebViewClient(new WebViewClientImpl());
        webView.setWebChromeClient(new WebChromeClientImpl());


        //TODO:delete
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Uri uri = Uri.parse(url);
//                if("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
//                    view.loadUrl(url);
//                    return true;
//                }
//                return false;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                String title = view.getTitle();
//                if(!TextUtils.isEmpty(title)) {
//                    onTitleText(title);
//                }
//
//
//                String script = "javascript:" +
//                        "try {" +
//                        "   if(document.head && !document.getElementById('cx_mc_inject')) {" +
//                        "       var inject = document.createElement('script');" +
//                        "       inject.id='cx_mc_inject';" +
//                        "       inject.src='https://unpkg.com/vconsole@3.11.1/dist/vconsole.min.js';" +
//                        "       document.head.appendChild(inject);" +
//                        "   }" +
//                        "} catch(e) {}";
//                view.loadUrl(script);
//
//                String console = "javascript:var vConsole = new window.VConsole();";
//                view.loadUrl(console);
//            }
//        });
    }

    @SuppressLint("JavascriptInterface")
    private void addJavascriptInterface(WebView webView) {
        HashMap<String, JsInterface> hashMap = onJsInterface();
        if(hashMap == null) {
            return;
        }

        for(String key : hashMap.keySet()) {
            try {
                Object object = hashMap.get(key);
                webView.addJavascriptInterface(object, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeBrowser(WebView webView) {
        onBrowserInit(webView);
    }

    private void configBrowserContent(WebView webView) {
        onBrowserContent(webView);
    }

    private class DownloadListenerImpl implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition,
                                    String mimetype,
                                    long contentLength) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class WebViewClientImpl extends WebViewClient {

        /**
         * 拦截自定义scheme，只支持http或https
         * @param view
         * @param url
         * @return
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            if("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
                view.loadUrl(url);
                return true;
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String title = view.getTitle();
            if(!TextUtils.isEmpty(title)) {
                onTitleText(title);
            }
        }
    }

    private class WebChromeClientImpl extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if(!TextUtils.isEmpty(title)) {
                onTitleText(title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgress.setVisibility(View.GONE);
            } else {
                mProgress.setVisibility(View.VISIBLE);
                mProgress.setProgress(newProgress);
            }
        }

        // For Android >= 5.0
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            showFileChooser(filePathCallback, fileChooserParams);
            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            onBrowserConsole(consoleMessage);
            return false;
        }
    }

    private void showFileChooser(ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        this.mFilePathCallback = filePathCallback;
        onChooseFile(CODE_BROWSER_WEB, fileChooserParams);
    }

    @Override
    @CallSuper
    protected void onMediaResult(int requestCode, List<Uri> uris) {
        super.onMediaResult(requestCode, uris);
        if(IList.isEmpty(uris)) {
            releaseFilePathCallback();
            return;
        }

        if(requestCode == CODE_BROWSER_WEB) {
            if(mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(uris.toArray(new Uri[]{}));
                mFilePathCallback = null;
            }
        }
    }

    @Override
    @CallSuper
    protected void onMediaCancel(int requestCode, String msg) {
        super.onMediaCancel(requestCode, msg);
        if(requestCode == CODE_BROWSER_WEB) {
            releaseFilePathCallback();
        }
    }

    @Override
    @CallSuper
    protected void onMediaError(int requestCode, String msg) {
        super.onMediaError(requestCode, msg);
        if(requestCode == CODE_BROWSER_WEB) {
            releaseFilePathCallback();
        }
    }

    private void releaseFilePathCallback() {
        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
            mFilePathCallback = null;
        }
    }

    protected void onTitleInit(LinearLayout layout) {}

    protected void onTitleText(String title) {}

    protected void onProgressInit(ProgressBar progressBar) {}

    protected void onBrowserInit(WebView webView) {}

    protected void onBrowserContent(WebView webView) {}

    protected void onBrowserConsole(ConsoleMessage message) {}

    protected HashMap<String, JsInterface> onJsInterface() {
        return null;
    }

    protected void onChooseFile(int requestCode, WebChromeClient.FileChooserParams fileChooserParams) {
        selectFile(requestCode);
    }
}