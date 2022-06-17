package org.ituns.framework.master.mvvm.viewitem;

import android.graphics.Color;

public class DividerItem extends MVVMItem implements SplitterItem {
    private float height = 0.0f; //dp value
    private int faceColor = Color.TRANSPARENT;
    private int backColor = Color.TRANSPARENT;
    private float leftMargin = 0.0f; //dp value
    private float rightMargin = 0.0f; //dp value

    private DividerItem() {
        super(TYPE_SPLITTER);
    }

    public static DividerItem divide(float height) {
        return divide(height, Color.TRANSPARENT, 0);
    }

    public static DividerItem divide(float height, int color) {
        return divide(height, color, 0);
    }

    public static DividerItem divide(float height, int color, float margin) {
        DividerItem item = new DividerItem();
        item.height = height;
        item.faceColor = color;
        item.leftMargin = margin;
        item.rightMargin = margin;
        return item;
    }

    @Override
    public float height() {
        return height;
    }

    @Override
    public int backColor() {
        return backColor;
    }

    @Override
    public int faceColor() {
        return faceColor;
    }

    @Override
    public float leftMargin() {
        return leftMargin;
    }

    @Override
    public float rightMargin() {
        return rightMargin;
    }

    public DividerItem height(float height) {
        this.height = height;
        return this;
    }

    public DividerItem backColor(int backColor) {
        this.backColor = backColor;
        return this;
    }

    public DividerItem faceColor(int faceColor) {
        this.faceColor = faceColor;
        return this;
    }

    public DividerItem leftMargin(float leftMargin) {
        this.leftMargin = leftMargin;
        return this;
    }

    public DividerItem rightMargin(float rightMargin) {
        this.rightMargin = rightMargin;
        return this;
    }
}
