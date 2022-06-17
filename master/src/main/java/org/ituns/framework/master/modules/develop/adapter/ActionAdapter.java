package org.ituns.framework.master.modules.develop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ituns.framework.master.R;
import org.ituns.framework.master.modules.develop.viewholder.ActionHolder;
import org.ituns.framework.master.modules.develop.viewitem.ActionItem;
import org.ituns.framework.master.mvvm.adapter.MVVMAdapter;
import org.ituns.framework.master.mvvm.viewholder.MVVMHolder;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;

public class ActionAdapter extends MVVMAdapter<MVVMItem> {

    @Override
    protected MVVMHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        switch (viewType) {
            case ActionItem.TYPE_ITEM: {
                View itemView = inflater.inflate(R.layout.fw_adapter_action, parent, false);
                return new ActionHolder(itemView);
            }
        }
        return null;
    }
}