package org.ituns.framework.master.widgets.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import org.ituns.framework.master.R;
import org.ituns.framework.master.tools.android.IScreen;

public class ImageButton extends LinearLayout {
    private AppCompatImageView iconView;
    private AppCompatTextView titleView;

    public ImageButton(Context context) {
        super(context);
        initializeView(context, null);
    }

    public ImageButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeView(context, attrs);
    }

    public ImageButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(context, attrs);
    }

    private void initializeView(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        iconView = new AppCompatImageView(context);
        iconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LayoutParams iconParams = new LayoutParams(
                IScreen.dp2px(context, 22),
                IScreen.dp2px(context, 22));
        iconParams.rightMargin = IScreen.dp2px(context, 3);
        addView(iconView, iconParams);

        titleView = new AppCompatTextView(context);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        titleView.setTextColor(Color.parseColor("#2A55FF"));
        LayoutParams titleParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(titleView, titleParams);

        if(attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ImageButton, 0, 0);
            for(int i = 0; i < ta.getIndexCount(); i++) {
                int attr = ta.getIndex(i);
                if(attr == R.styleable.ImageButton_icon) {
                    iconView.setBackgroundDrawable(ta.getDrawable(attr));
                } else if(attr == R.styleable.ImageButton_iconColor) {
                    iconView.setBackgroundTintList(ColorStateList.valueOf(ta.getColor(attr, Color.BLACK)));
                } else if(attr == R.styleable.ImageButton_text) {
                    titleView.setText(ta.getString(attr));
                } else if(attr == R.styleable.ImageButton_textSize) {
                    float titleSize = ta.getDimensionPixelSize(attr, IScreen.sp2px(context, 18));
                    titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
                } else if(attr == R.styleable.ImageButton_textColor) {
                    titleView.setTextColor(ta.getColor(attr, Color.parseColor("#2A55FF")));
                }
            }
            ta.recycle();
        }
    }

    private float dimenValue(Context context, int id) {
        return context.getResources().getDimension(id);
    }
}
