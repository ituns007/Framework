package org.ituns.framework.master.modules.develop.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.ituns.framework.master.R;
import org.ituns.framework.master.modules.develop.viewholder.EnvDetailConfigHolder;
import org.ituns.framework.master.modules.develop.viewholder.EnvDetailCurrentHolder;
import org.ituns.framework.master.modules.develop.viewholder.EnvDetailTitleHolder;
import org.ituns.framework.master.modules.develop.viewitem.EnvDetailItem;
import org.ituns.framework.master.mvvm.adapter.MVVMAdapter;
import org.ituns.framework.master.mvvm.viewholder.MVVMHolder;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;

public class EnvDetailAdapter extends MVVMAdapter<MVVMItem> {

    @Override
    protected MVVMHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        switch (viewType) {
            case EnvDetailItem.TYPE_TITLE: {
                return new EnvDetailTitleHolder(inflater.inflate(R.layout.fw_adapter_env_detail_title, parent, false));
            }
            case EnvDetailItem.TYPE_CONFIG: {
                return new EnvDetailConfigHolder(inflater.inflate(R.layout.fw_adapter_env_detail_config, parent, false));
            }
            case EnvDetailItem.TYPE_CURRENT: {
                return new EnvDetailCurrentHolder(inflater.inflate(R.layout.fw_adapter_env_detail_current, parent, false));
            }
        }
        return null;
    }
}
