package org.ituns.framework.master.modules.develop.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import org.ituns.framework.master.R;
import org.ituns.framework.master.modules.develop.viewitem.EnvDetailItem;
import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.mvvm.viewholder.MVVMHolder;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;
import org.ituns.framework.master.service.env.EnvProxy;
import org.ituns.framework.master.service.env.EnvService;
import org.ituns.framework.master.tools.android.IClick;
import org.ituns.framework.master.tools.java.IString;

public class EnvDetailConfigHolder extends MVVMHolder {
    private final AppCompatTextView nameView;
    private final AppCompatTextView valueView;
    private final AppCompatImageView iconView;

    public EnvDetailConfigHolder(@NonNull View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.name);
        valueView = itemView.findViewById(R.id.value);
        iconView = itemView.findViewById(R.id.icon);
    }

    @Override
    public void bindData(MVVMItem data) {
        if(data instanceof EnvDetailItem) {
            String key = data.getString("key", "");
            String name = data.getString("name", "");
            String value = data.getString("value", "");

            nameView.setText(name);
            valueView.setText(value);
            if(IString.isEquals(value, EnvProxy.getEnv(key))) {
                iconView.setVisibility(View.VISIBLE);
            } else {
                iconView.setVisibility(View.INVISIBLE);
            }

            IClick.single(itemView, v -> postExternal(MVVMAction.with(data.action())
                    .put("key", key)
                    .put("name", name)
                    .put("value", value)));
        }
    }
}
