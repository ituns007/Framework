package org.ituns.framework.master.widgets.recycler.manager;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

public class LinearManager extends LinearLayoutManager {
    private boolean isLayoutRTL = false;

    public LinearManager(Context context) {
        super(context);
    }

    public LinearManager(Context context, boolean layoutRtl) {
        super(context);
        this.isLayoutRTL = layoutRtl;
    }

    public LinearManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean isLayoutRTL() {
        return isLayoutRTL || super.isLayoutRTL();
    }
}
