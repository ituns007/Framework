package org.ituns.framework.master.widgets.recycler.manager;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;

public class GridManager extends GridLayoutManager {
    private boolean isLayoutRTL = false;

    public GridManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public GridManager(Context context, int spanCount, boolean layoutRtl) {
        super(context, spanCount);
        this.isLayoutRTL = layoutRtl;
    }

    public GridManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public GridManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean isLayoutRTL() {
        return isLayoutRTL || super.isLayoutRTL();
    }
}
