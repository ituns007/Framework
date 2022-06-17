package org.ituns.framework.master.tools.recycler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ituns.framework.master.tools.android.IScreen;

public class Decoration extends RecyclerView.ItemDecoration  {
    private int mHeight = 1;
    private int mMargin =  0;
    private int mBackColor = Color.WHITE;
    private int mFrontColor = Color.LTGRAY;
    private final Rect mBounds = new Rect();

    private Decoration() {}

    public static Decoration get() {
        return new Decoration();
    }

    public Decoration back(@ColorInt int color) {
        mBackColor = color;
        return this;
    }

    public Decoration front(@ColorInt int color) {
        mFrontColor = color;
        return this;
    }

    public Decoration height(int height) {
        mHeight = height;
        return this;
    }

    public Decoration margin(int margin) {
        mMargin = margin;
        return this;
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        drawDecoration(canvas, parent);
    }

    private void drawDecoration(Canvas canvas, RecyclerView parent) {
        canvas.save();

        final int left;
        final int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final Paint backPaint = new Paint();
        backPaint.setAntiAlias(true);
        backPaint.setColor(mBackColor);

        final Paint frontPaint = new Paint();
        frontPaint.setAntiAlias(true);
        frontPaint.setColor(mFrontColor);

        final int height = IScreen.dp2px(parent.getContext(), mHeight);
        final int margin = IScreen.dp2px(parent.getContext(), mMargin);

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - height;
            canvas.drawRect(left, top, right, bottom, backPaint);
            canvas.drawRect((left + margin), top, (right - margin), bottom, frontPaint);
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.set(0, 0, 0, IScreen.dp2px(parent.getContext(), mHeight));
    }
}
