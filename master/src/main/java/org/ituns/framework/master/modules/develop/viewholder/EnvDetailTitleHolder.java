package org.ituns.framework.master.modules.develop.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import org.ituns.framework.master.R;
import org.ituns.framework.master.modules.develop.viewitem.EnvDetailItem;
import org.ituns.framework.master.mvvm.viewholder.MVVMHolder;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;

public class EnvDetailTitleHolder extends MVVMHolder {
    private final AppCompatTextView titleView;

    public EnvDetailTitleHolder(@NonNull View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.title);
    }

    @Override
    public void bindData(MVVMItem data) {
        if(data instanceof EnvDetailItem) {
            titleView.setText(data.getString("title", ""));
        }
    }
}