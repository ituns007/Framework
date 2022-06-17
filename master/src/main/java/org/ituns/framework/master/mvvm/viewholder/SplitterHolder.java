package org.ituns.framework.master.mvvm.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.ituns.framework.master.R;
import org.ituns.framework.master.mvvm.viewitem.SplitterItem;
import org.ituns.framework.master.mvvm.viewitem.MVVMItem;
import org.ituns.framework.master.tools.android.IScreen;

public class SplitterHolder extends MVVMHolder {
    private final View divideView;

    public SplitterHolder(@NonNull View itemView) {
        super(itemView);
        divideView = itemView.findViewById(R.id.divide);
    }

    @Override
    public void bindData(MVVMItem data) {
        if(data instanceof SplitterItem) {
            SplitterItem item = (SplitterItem) data;
            itemView.setBackgroundColor(item.backColor());
            divideView.setBackgroundColor(item.faceColor());
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) divideView.getLayoutParams();
            layoutParams.height = IScreen.dp2px(itemView.getContext(), item.height());
            layoutParams.leftMargin = IScreen.dp2px(itemView.getContext(), item.leftMargin());
            layoutParams.rightMargin = IScreen.dp2px(itemView.getContext(), item.rightMargin());
            divideView.setLayoutParams(layoutParams);
        }
    }
}
