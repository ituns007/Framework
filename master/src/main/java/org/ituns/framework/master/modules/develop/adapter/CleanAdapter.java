package org.ituns.framework.master.modules.develop.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.ituns.framework.master.R;
import org.ituns.framework.master.modules.develop.viewholder.CleanCacheHolder;
import org.ituns.framework.master.modules.develop.viewholder.CleanTitleHolder;
import org.ituns.framework.master.modules.develop.viewitem.CleanItem;
import org.ituns.framework.master.mvvm.adapter.MVVMAdapter;
import org.ituns.framework.master.mvvm.viewholder.MVVMHolder;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;

public class CleanAdapter extends MVVMAdapter<MVVMItem> {

    @Override
    protected MVVMHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        switch (viewType) {
            case CleanItem.TYPE_TITLE: {
                return new CleanTitleHolder(inflater.inflate(R.layout.fw_adapter_clean_title, parent, false));
            }
            case CleanItem.TYPE_CACHE: {
                return new CleanCacheHolder(inflater.inflate(R.layout.fw_adapter_clean_cache, parent, false));
            }
        }
        return null;
    }
}
