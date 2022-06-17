package org.ituns.framework.master.modules.develop.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import org.ituns.framework.master.R;
import org.ituns.framework.master.modules.develop.viewitem.ActionItem;
import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.mvvm.viewholder.MVVMHolder;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;
import org.ituns.framework.master.tools.android.IClick;

public class ActionHolder extends MVVMHolder {
    private final AppCompatTextView nameView;

    public ActionHolder(@NonNull View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.name);
    }

    @Override
    public void bindData(MVVMItem data) {
        if(data instanceof ActionItem) {
            nameView.setText(data.getString("name", ""));
            IClick.single(itemView, v -> postExternal(MVVMAction.with(data.action())));
        }
    }
}
