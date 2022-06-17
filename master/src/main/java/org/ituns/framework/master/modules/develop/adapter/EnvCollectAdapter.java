package org.ituns.framework.master.modules.develop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ituns.framework.master.R;
import org.ituns.framework.master.modules.develop.viewholder.EnvCollectHolder;
import org.ituns.framework.master.modules.develop.viewitem.EnvCollectItem;
import org.ituns.framework.master.mvvm.adapter.MVVMAdapter;
import org.ituns.framework.master.mvvm.viewholder.MVVMHolder;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;

public class EnvCollectAdapter extends MVVMAdapter<MVVMItem> {

    @Override
    protected MVVMHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        switch (viewType) {
            case EnvCollectItem.TYPE_ITEM: {
                View itemView = inflater.inflate(R.layout.fw_adapter_env_collect, parent, false);
                return new EnvCollectHolder(itemView);
            }
        }
        return null;
    }
}