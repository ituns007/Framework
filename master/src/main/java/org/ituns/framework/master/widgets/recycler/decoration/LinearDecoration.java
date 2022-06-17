package org.ituns.framework.master.widgets.recycler.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.ituns.framework.master.tools.android.IScreen;

public class LinearDecoration extends RecyclerView.ItemDecoration  {
    public static final int TYPE_ALL = 1;
    public static final int TYPE_START = 2;
    public static final int TYPE_LIMIT = 3;
    public static final int TYPE_CENTER = 4;

    private int type = TYPE_CENTER;
    private float space = 0;
    private float margin = 0;
    private final Rect bounds = new Rect();
    private final Paint backPaint = new Paint();
    private final Paint frontPaint = new Paint();

    private LinearDecoration() {
        backPaint.setAntiAlias(true);
        backPaint.setColor(Color.TRANSPARENT);
        frontPaint.setAntiAlias(true);
        frontPaint.setColor(Color.BLACK);
    }

    public static LinearDecoration all(float space) {
        return all(space, Color.TRANSPARENT);
    }

    public static LinearDecoration all(float space, @ColorInt int front) {
        return all(space, 0, front);
    }

    public static LinearDecoration all(float space, float margin, @ColorInt int front) {
        return all(space, margin, front, Color.TRANSPARENT);
    }

    public static LinearDecoration all(float space, float margin, @ColorInt int front, @ColorInt int back) {
        return with(TYPE_ALL, space, margin, front, back);
    }

    public static LinearDecoration start(float space) {
        return start(space, Color.TRANSPARENT);
    }

    public static LinearDecoration start(float space, @ColorInt int front) {
        return start(space, 0, front);
    }

    public static LinearDecoration start(float space, float margin, @ColorInt int front) {
        return start(space, margin, front, Color.TRANSPARENT);
    }

    public static LinearDecoration start(float space, float margin, @ColorInt int front, @ColorInt int back) {
        return with(TYPE_START, space, margin, front, back);
    }

    public static LinearDecoration limit(float space) {
        return limit(space, Color.TRANSPARENT);
    }

    public static LinearDecoration limit(float space, @ColorInt int front) {
        return limit(space, 0, front);
    }

    public static LinearDecoration limit(float space, float margin, @ColorInt int front) {
        return limit(space, margin, front, Color.TRANSPARENT);
    }

    public static LinearDecoration limit(float space, float margin, @ColorInt int front, @ColorInt int back) {
        return with(TYPE_LIMIT, space, margin, front, back);
    }

    public static LinearDecoration center(float space) {
        return center(space, Color.TRANSPARENT);
    }

    public static LinearDecoration center(float space, @ColorInt int front) {
        return center(space, 0, front);
    }

    public static LinearDecoration center(float space, float margin, @ColorInt int front) {
        return center(space, margin, front, Color.TRANSPARENT);
    }

    public static LinearDecoration center(float space, float margin, @ColorInt int front, @ColorInt int back) {
        return with(TYPE_CENTER, space, margin, front, back);
    }

    public static LinearDecoration with(int type, float space, float margin, @ColorInt int front, @ColorInt int back) {
        LinearDecoration decoration = new LinearDecoration();
        decoration.type = type;
        decoration.space = space;
        decoration.margin = margin;
        decoration.backPaint.setColor(back);
        decoration.frontPaint.setColor(front);
        return decoration;
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            if(manager.getOrientation() == RecyclerView.VERTICAL) {
                drawVertical(canvas, parent);
            } else {
                drawHorizontal(canvas, parent);
            }
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();

        int left = 0;
        int right = parent.getWidth();
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();

            int top = parent.getPaddingTop();
            int bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(left, top, right, bottom);
        }

        final int count = parent.getChildCount();
        final int spacePixel = IScreen.dp2px(parent.getContext(), space);
        final int marginPixel = IScreen.dp2px(parent.getContext(), margin);
        for (int i = 0; i < count; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, bounds);

            if(i == 0 && (type == TYPE_ALL || type == TYPE_START)) {
                int top = bounds.top + Math.round(child.getTranslationY());
                int bottom = top + spacePixel;
                canvas.drawRect(left, top, right, bottom, backPaint);
                canvas.drawRect((left + marginPixel), top, (right - marginPixel), bottom, frontPaint);
            }

            if(i < count - 1 || type == TYPE_ALL || type == TYPE_LIMIT) {
                final int bottom = bounds.bottom + Math.round(child.getTranslationY());
                final int top = bottom - spacePixel;
                canvas.drawRect(left, top, right, bottom, backPaint);
                canvas.drawRect((left + marginPixel), top, (right - marginPixel), bottom, frontPaint);
            }
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();

        int top = 0;
        int bottom = parent.getHeight();
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();

            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, top, right, bottom);
        }

        final int count = parent.getChildCount();
        final int spacePixel = IScreen.dp2px(parent.getContext(), space);
        final int marginPixel = IScreen.dp2px(parent.getContext(), margin);
        for (int i = 0; i < count; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, bounds);

            if(i == 0 && (type == TYPE_ALL || type == TYPE_START)) {
                int left = bounds.left + Math.round(child.getTranslationX());
                int right = left + spacePixel;
                canvas.drawRect(left, top, right, bottom, backPaint);
                canvas.drawRect((left + marginPixel), top, (right - marginPixel), bottom, frontPaint);
            }

            if(i < count - 1 || type == TYPE_ALL || type == TYPE_LIMIT) {
                final int right = bounds.right + Math.round(child.getTranslationX());
                final int left = right - spacePixel;
                canvas.drawRect(left, top, right, bottom, backPaint);
                canvas.drawRect((left + marginPixel), top, (right - marginPixel), bottom, frontPaint);
            }
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            if(manager.getOrientation() == RecyclerView.VERTICAL) {
                verticalItemOffset(outRect, view, parent);
            } else {
                horizontalItemOffset(outRect, view, parent);
            }
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }

    private void verticalItemOffset(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent) {
        int top = 0, bottom = 0;
        RecyclerView.Adapter<?> adapter = parent.getAdapter();
        if(adapter != null) {
            int count = adapter.getItemCount();
            int position = parent.getChildAdapterPosition(view);
            int spacePixel = IScreen.dp2px(parent.getContext(), space);

            if(position == 0 && (type == TYPE_ALL || type == TYPE_START)) {
                top = spacePixel;
            }
            if(position < count - 1 || type == TYPE_ALL || type == TYPE_LIMIT) {
                bottom = spacePixel;
            }
        }
        outRect.set(0, top, 0, bottom);
    }

    private void horizontalItemOffset(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent) {
        int left = 0, right = 0;
        RecyclerView.Adapter<?> adapter = parent.getAdapter();
        if(adapter != null) {
            int count = adapter.getItemCount();
            int position = parent.getChildAdapterPosition(view);
            int spacePixel = IScreen.dp2px(parent.getContext(), space);

            if(position == 0 && (type == TYPE_ALL || type == TYPE_START)) {
                left = spacePixel;
            }
            if(position < count - 1 || type == TYPE_ALL || type == TYPE_LIMIT) {
                right = spacePixel;
            }
        }
        outRect.set(left, 0, right, 0);
    }
}