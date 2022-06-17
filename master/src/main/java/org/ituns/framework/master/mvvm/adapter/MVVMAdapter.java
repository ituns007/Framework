package org.ituns.framework.master.mvvm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import org.ituns.framework.master.R;
import org.ituns.framework.master.mvvm.viewholder.SplitterHolder;
import org.ituns.framework.master.mvvm.viewholder.MVVMHolder;
import org.ituns.framework.master.mvvm.viewholder.EmptyHolder;
import org.ituns.framework.master.mvvm.viewitem.SplitterItem;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class MVVMAdapter<T extends MVVMItem> extends ActionAdapter<MVVMHolder> {
    private final ArrayList<T> dataItems = new ArrayList<>();
    private final LinkedList<MVVMItem> viewItems = new LinkedList<>();

    public final void setData(List<T> list) {
        dataItems.clear();
        if(list != null) {
            dataItems.addAll(list);
        }
        refreshViewData();
    }

    public final void addData(List<T> list) {
        if(list != null) {
            dataItems.addAll(list);
            refreshViewData();
        }
    }

    protected final void refreshViewData() {
        prepareDataItems(dataItems);
        viewItems.clear();
        viewItems.addAll(headerViewItems());
        viewItems.addAll(dataItems);
        viewItems.addAll(footerViewItems());
        prepareViewItems(viewItems);
        notifyDataSetChanged();
    }

    protected void prepareDataItems(ArrayList<T> viewItems) {}

    protected List<T> headerViewItems() {
        return new ArrayList<>();
    }

    protected List<T> footerViewItems() {
        return new ArrayList<>();
    }

    protected void prepareViewItems(LinkedList<MVVMItem> viewItems) {}

    @Override
    public int getItemCount() {
        return viewItems.size();
    }

    @Override
    public final int getItemViewType(int position) {
        if(0 <= position && position < viewItems.size()) {
            return viewItems.get(position).type();
        }
        return super.getItemViewType(position);
    }

    public final MVVMItem getItemViewData(int position) {
        if(0 <= position && position < viewItems.size()) {
            return viewItems.get(position);
        }
        return null;
    }

    @NonNull
    @Override
    public final MVVMHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MVVMHolder viewHolder = null;
        if(viewType == SplitterItem.TYPE_SPLITTER) {
            View itemView = inflater.inflate(R.layout.fw_adapter_splitter, parent, false);
            viewHolder = new SplitterHolder(itemView);
        } else {
            viewHolder = createViewHolder(inflater, parent, viewType);
        }
        if(viewHolder == null) {
            viewHolder = new EmptyHolder(parent.getContext());
        }
        viewHolder.setInternal(internal);
        viewHolder.setExternal(external);
        return viewHolder;
    }

    protected abstract MVVMHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType);

    @Override
    public final void onBindViewHolder(@NonNull MVVMHolder holder, int position) {
        if(0 <= position && position < viewItems.size()) {
            holder.bindData(viewItems.get(position));
        }
    }
}
