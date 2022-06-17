package org.ituns.framework.master.modules.develop.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import org.ituns.framework.master.R;
import org.ituns.framework.master.modules.develop.viewitem.EnvDetailItem;
import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.mvvm.viewholder.MVVMHolder;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;
import org.ituns.framework.master.tools.android.IClick;

public class EnvDetailCurrentHolder extends MVVMHolder {
    private final AppCompatTextView valueView;

    public EnvDetailCurrentHolder(@NonNull View itemView) {
        super(itemView);
        valueView = itemView.findViewById(R.id.value);
    }

    @Override
    public void bindData(MVVMItem data) {
        if(data instanceof EnvDetailItem) {
            String key = data.getString("key", "");
            String value = data.getString("value", "");
            valueView.setText(value);
            IClick.single(valueView, v -> postExternal(MVVMAction.with(data.action())
                    .put("key", key)
                    .put("value", value)));
        }
    }
}
