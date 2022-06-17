package org.ituns.framework.master.modules.develop.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import org.ituns.framework.master.R;
import org.ituns.framework.master.modules.develop.viewitem.CleanItem;
import org.ituns.framework.master.mvvm.action.MVVMAction;
import org.ituns.framework.master.mvvm.viewholder.MVVMHolder;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;
import org.ituns.framework.master.tools.android.IClick;

public class CleanCacheHolder extends MVVMHolder {
    private final AppCompatTextView nameView;
    private final AppCompatTextView cacheView;

    public CleanCacheHolder(@NonNull View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.name);
        cacheView = itemView.findViewById(R.id.cache);
    }

    @Override
    public void bindData(MVVMItem data) {
        if(data instanceof CleanItem) {
            nameView.setText(data.getString("name"));
            cacheView.setText(data.getString("size"));
            IClick.single(itemView, v -> postExternal(MVVMAction.with(data.action())
                    .put("category", data.getInt("category", 0))
                    .put("desc", data.getString("desc", ""))
                    .put("type", data.getString("type", ""))));
        }
    }
}
