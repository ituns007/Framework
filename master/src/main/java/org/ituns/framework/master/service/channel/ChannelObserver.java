package org.ituns.framework.master.service.channel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import org.ituns.framework.master.service.ServiceCallback;
import org.ituns.framework.master.service.logcat.Logcat;
import org.ituns.framework.master.tools.androidx.AlignLiveData;

import java.lang.ref.WeakReference;

public abstract class ChannelObserver<T extends ChannelData> implements Observer<T> {
    protected ServiceCallback mCallback;
    protected final AlignLiveData<T> mLiveData;
    protected WeakReference<AppCompatActivity> mActivity;

    public ChannelObserver() {
        mLiveData = new AlignLiveData<>();
    }

    public final void registerCallback(ServiceCallback callback) {
        mCallback = callback;
    }

    public final void unregisterCallback() {
        mCallback = null;
    }

    public final void registerActivity(AppCompatActivity activity) {
        if(null == activity) {
            Logcat.e("activity is null.");
            return;
        }

        if(null != mActivity) {
            AppCompatActivity oldActivity = mActivity.get();
            if(null != oldActivity) {
                mLiveData.removeObservers(oldActivity);
                Logcat.e("Remove observers:" + oldActivity.getClass().getName());
            }
            mActivity.clear();
        }

        mActivity = new WeakReference<>(activity);
        mLiveData.observe(activity, this);
        Logcat.e("Observe activity:" + activity.getClass().getName());
    }

    public final void unregisterActivity(AppCompatActivity activity) {
        if(null == activity) {
            Logcat.e("activity is null.");
            return;
        }

        if(null != mActivity && mActivity.get() == activity) {
            mLiveData.removeObservers(activity);
            Logcat.e("Remove observers:" + activity.getClass().getName());
            mActivity.clear();
            mActivity = null;
        }
    }

    public final void postData(Object data) {
        if(isMatchedData(data)) {
            mLiveData.postValue((T) data);
            Logcat.e("Observer:" + getClass().getName() + " Match class:" + data.getClass().getName());
        }
    }

    protected abstract boolean isMatchedData(Object data);
}
