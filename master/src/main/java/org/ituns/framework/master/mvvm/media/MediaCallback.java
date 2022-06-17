package org.ituns.framework.master.mvvm.media;

import android.net.Uri;

import java.util.ArrayList;

public interface MediaCallback {
    void onResult(int code, ArrayList<Uri> uris);
    void onCancel(int code, String msg);
    void onError(int code, String msg);
}
