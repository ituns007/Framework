package org.ituns.framework.master.mvvm.adapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.tools.androidx.AlignLiveData;

public abstract class ActionAdapter<T extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<T> implements Observer<MVVMAction> {
    protected final AlignLiveData<MVVMAction> internal = new AlignLiveData<>();
    protected final AlignLiveData<MVVMAction> external = new AlignLiveData<>();

    public ActionAdapter() {
        internal.observeForever(this);
    }

    public final LiveData<MVVMAction> action() {
        return external;
    }

    protected void postInternal(MVVMAction action) {
        internal.postValue(action);
    }

    protected void postExternal(MVVMAction action) {
        external.postValue(action);
    }

    @Override
    public final void onChanged(MVVMAction action) {
        onAction(action);
    }

    protected void onAction(MVVMAction action) {}
}
