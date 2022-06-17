package org.ituns.framework.master.mvvm.viewholder;

import android.content.Context;
import android.view.View;

import org.ituns.framework.master.mvvm.viewitem.MVVMItem;

public class EmptyHolder extends MVVMHolder {

    public EmptyHolder(Context context) {
        super(new View(context));
        itemView.setVisibility(View.GONE);
    }

    @Override
    public void bindData(MVVMItem data) {}
}
