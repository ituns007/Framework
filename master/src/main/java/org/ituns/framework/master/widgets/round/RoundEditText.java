package org.ituns.framework.master.widgets.round;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import org.ituns.framework.master.R;
import org.ituns.framework.master.widgets.input.HolderEditText;

public class RoundEditText extends HolderEditText {

    public RoundEditText(@NonNull Context context) {
        this(context, null);
    }

    public RoundEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public RoundEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(attrs != null) {
            int size_width = -1, size_height = -1;
            int solid_color = Color.TRANSPARENT;
            int stroke_color = Color.TRANSPARENT, stroke_width = -1, stroke_dash_gap = 0,
                    stroke_dash_width = 0;
            int corners_radius = -1, corners_radius_tl = 0, corners_radius_tr = 0,
                    corners_radius_bl = 0, corners_radius_br = 0;
            int padding_top = 0, padding_left = 0, padding_right = 0, padding_bottom = 0;
            int gradient_type = -1, gradient_shape = -1, gradient_alpha = 255, gradient_radius = 0,
                    gradient_orientation = -1;
            float gradient_center_x = -1.0f, gradient_center_y = -1.0f;
            boolean gradient_dither = false;

            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundEditText, 0, 0);
            for(int i = 0; i < ta.getIndexCount(); i++) {
                int attr = ta.getIndex(i);
                if(attr == R.styleable.RoundEditText_ret_size_width) {
                    size_width = ta.getDimensionPixelSize(attr, -1);
                } else if(attr == R.styleable.RoundEditText_ret_size_height) {
                    size_height = ta.getDimensionPixelSize(attr, -1);
                } else if(attr == R.styleable.RoundEditText_ret_solid_color) {
                    solid_color = ta.getColor(attr, Color.TRANSPARENT);
                } else if(attr == R.styleable.RoundEditText_ret_stroke_color) {
                    stroke_color = ta.getColor(attr, Color.TRANSPARENT);
                } else if(attr == R.styleable.RoundEditText_ret_stroke_width) {
                    stroke_width = ta.getDimensionPixelSize(attr, -1);
                } else if(attr == R.styleable.RoundEditText_ret_stroke_dash_gap) {
                    stroke_dash_gap = ta.getDimensionPixelSize(attr, 0);
                } else if(attr == R.styleable.RoundEditText_ret_stroke_dash_width) {
                    stroke_dash_width = ta.getDimensionPixelSize(attr, 0);
                } else if(attr == R.styleable.RoundEditText_ret_corners_radius) {
                    corners_radius = ta.getDimensionPixelSize(attr, -1);
                } else if(attr == R.styleable.RoundEditText_ret_corners_radius_tl) {
                    corners_radius_tl = ta.getDimensionPixelSize(attr, -1);
                } else if(attr == R.styleable.RoundEditText_ret_corners_radius_tr) {
                    corners_radius_tr = ta.getDimensionPixelSize(attr, -1);
                } else if(attr == R.styleable.RoundEditText_ret_corners_radius_bl) {
                    corners_radius_bl = ta.getDimensionPixelSize(attr, -1);
                } else if(attr == R.styleable.RoundEditText_ret_corners_radius_br) {
                    corners_radius_br = ta.getDimensionPixelSize(attr, -1);
                } else if(attr == R.styleable.RoundEditText_ret_padding_top) {
                    padding_top = ta.getDimensionPixelSize(attr, 0);
                } else if(attr == R.styleable.RoundEditText_ret_padding_left) {
                    padding_left = ta.getDimensionPixelSize(attr, 0);
                } else if(attr == R.styleable.RoundEditText_ret_padding_right) {
                    padding_right = ta.getDimensionPixelSize(attr, 0);
                } else if(attr == R.styleable.RoundEditText_ret_padding_bottom) {
                    padding_bottom = ta.getDimensionPixelSize(attr, 0);
                } else if(attr == R.styleable.RoundEditText_ret_gradient_type) {
                    gradient_type = ta.getInt(attr, -1);
                } else if(attr == R.styleable.RoundEditText_ret_gradient_alpha) {
                    gradient_alpha = ta.getInt(attr, 255);
                } else if(attr == R.styleable.RoundEditText_ret_gradient_shape) {
                    gradient_shape = ta.getInt(attr, -1);
                } else if(attr == R.styleable.RoundEditText_ret_gradient_dither) {
                    gradient_dither = ta.getBoolean(attr, false);
                } else if(attr == R.styleable.RoundEditText_ret_gradient_radius) {
                    gradient_radius = ta.getDimensionPixelSize(attr, 0);
                } else if(attr == R.styleable.RoundEditText_ret_gradient_center_x) {
                    gradient_center_x = ta.getFloat(attr, -1.0f);
                } else if(attr == R.styleable.RoundEditText_ret_gradient_center_y) {
                    gradient_center_y = ta.getFloat(attr, -1.0f);
                } else if(attr == R.styleable.RoundEditText_ret_gradient_orientation) {
                    gradient_orientation = ta.getInt(attr, -1);
                }
            }
            ta.recycle();

            GradientDrawable drawable = new GradientDrawable();
            drawable.setSize(size_width, size_height);
            drawable.setColor(solid_color);
            drawable.setStroke(stroke_width, stroke_color, stroke_dash_width, stroke_dash_gap);
            if(corners_radius < 0) {
                drawable.setCornerRadii(new float[]{
                        corners_radius_tl, corners_radius_tl,
                        corners_radius_tr, corners_radius_tr,
                        corners_radius_br, corners_radius_br,
                        corners_radius_bl, corners_radius_bl
                });
            } else {
                drawable.setCornerRadius(corners_radius);
            }
            if(0 <= gradient_type && gradient_type <= 2) {
                drawable.setGradientType(gradient_type);
            }
            if(0 <= gradient_alpha && gradient_alpha <= 255) {
                drawable.setAlpha(gradient_alpha);
            }
            if(0 <= gradient_shape && gradient_shape <= 3) {
                drawable.setShape(gradient_shape);
            }
            drawable.setDither(gradient_dither);
            drawable.setGradientRadius(gradient_radius);
            if(0.0f <= Math.min(gradient_center_x, gradient_center_y) &&
                    Math.max(gradient_center_x, gradient_center_y) <= 1.0f) {
                drawable.setGradientCenter(gradient_center_x, gradient_center_y);
            }
            if(0 <= gradient_orientation && gradient_orientation <= 7) {
                setGradientOrientation(drawable, gradient_orientation);
            }
            setBackground(drawable);
        }
    }

    private void setGradientOrientation(GradientDrawable drawable, int gradient_orientation) {
        switch (gradient_orientation) {
            case 0:
                drawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
                break;
            case 1:
                drawable.setOrientation(GradientDrawable.Orientation.TR_BL);
                break;
            case 2:
                drawable.setOrientation(GradientDrawable.Orientation.RIGHT_LEFT);
                break;
            case 3:
                drawable.setOrientation(GradientDrawable.Orientation.BR_TL);
                break;
            case 4:
                drawable.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
                break;
            case 5:
                drawable.setOrientation(GradientDrawable.Orientation.BL_TR);
                break;
            case 6:
                drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
                break;
            case 7:
                drawable.setOrientation(GradientDrawable.Orientation.TL_BR);
                break;
        }
    }
}
