package org.ituns.framework.master.mvvm.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;
import org.ituns.framework.master.tools.androidx.AlignLiveData;

public abstract class MVVMHolder extends RecyclerView.ViewHolder {
    private AlignLiveData<MVVMAction> internal;
    private AlignLiveData<MVVMAction> external;

    public MVVMHolder(@NonNull View itemView) {
        super(itemView);
    }

    protected void postInternal(MVVMAction action) {
        if (internal != null) {
            internal.postValue(action);
        }
    }

    public void setInternal(AlignLiveData<MVVMAction> internal) {
        this.internal = internal;
    }

    protected void postExternal(MVVMAction action) {
        if (external != null) {
            external.postValue(action);
        }
    }

    public void setExternal(AlignLiveData<MVVMAction> external) {
        this.external = external;
    }

    @Deprecated
    protected void postAction(MVVMAction action) {
        if (external != null) {
            external.postValue(action);
        }
    }

    public abstract void bindData(MVVMItem data);
}
