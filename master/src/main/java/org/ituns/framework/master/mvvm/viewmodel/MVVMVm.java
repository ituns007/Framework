package org.ituns.framework.master.mvvm.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.tools.android.IBundle;
import org.ituns.framework.master.tools.androidx.AlignLiveData;

import java.util.List;

public class MVVMVm<T> extends ViewModel {
    protected int page = 1;
    protected int total = 0;
    protected final int size = 10;
    protected final IBundle intents = new IBundle();
    protected final IBundle arguments = new IBundle();
    protected final AlignLiveData<MVVMAction> action = new AlignLiveData<>();
    protected final MutableLiveData<T> single = new MutableLiveData<>();
    protected final MutableLiveData<List<T>> refresh = new MutableLiveData<>();
    protected final MutableLiveData<List<T>> loadmore = new MutableLiveData<>();

    public void onReady() {}

    public void onRecycle() {}

    public final void setIntents(IBundle bundle) {
        intents.putAll(bundle);
    }

    public final void setArguments(IBundle bundle) {
        intents.putAll(bundle);
    }

    public final LiveData<MVVMAction> action() {
        return action;
    }

    public final LiveData<T> single() {
        return single;
    }

    public final LiveData<List<T>> refresh() {
        return refresh;
    }

    public final LiveData<List<T>> loadmore() {
        return loadmore;
    }

    public void postAction(MVVMAction action) {
        this.action.postValue(action);
    }
}
