package org.ituns.framework.master.tools.android;

import android.os.Build;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class ILayout {

    public static void alignParentLeft(ViewGroup.LayoutParams layoutParams) {
        if(layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layoutParams).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
    }

    public static void alignParentTop(ViewGroup.LayoutParams layoutParams) {
        if(layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layoutParams).addRule(RelativeLayout.ALIGN_PARENT_TOP);
        }
    }

    public static void alignParentRight(ViewGroup.LayoutParams layoutParams) {
        if(layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layoutParams).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
    }

    public static void alignParentBottom(ViewGroup.LayoutParams layoutParams) {
        if(layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layoutParams).addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
    }

    public static void alignParentStart(ViewGroup.LayoutParams layoutParams) {
        if(layoutParams instanceof RelativeLayout.LayoutParams) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                ((RelativeLayout.LayoutParams) layoutParams).addRule(RelativeLayout.ALIGN_PARENT_START);
            } else {
                ((RelativeLayout.LayoutParams) layoutParams).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }
        }
    }

    public static void alignParentEnd(ViewGroup.LayoutParams layoutParams) {
        if(layoutParams instanceof RelativeLayout.LayoutParams) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                ((RelativeLayout.LayoutParams) layoutParams).addRule(RelativeLayout.ALIGN_PARENT_END);
            } else {
                ((RelativeLayout.LayoutParams) layoutParams).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }
        }
    }

    public static void marginLeft(ViewGroup.LayoutParams layoutParams, int left) {
        if(layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin = left;
        }
    }

    public static void marginTop(ViewGroup.LayoutParams layoutParams, int top) {
        if(layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = top;
        }
    }

    public static void marginRight(ViewGroup.LayoutParams layoutParams, int right) {
        if(layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = right;
        }
    }

    public static void marginBottom(ViewGroup.LayoutParams layoutParams, int bottom) {
        if(layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = bottom;
        }
    }

    public static void marginStart(ViewGroup.LayoutParams layoutParams, int start) {
        if(layoutParams instanceof ViewGroup.MarginLayoutParams) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                ((ViewGroup.MarginLayoutParams)layoutParams).setMarginStart(start);
            } else {
                ((ViewGroup.MarginLayoutParams)layoutParams).leftMargin = start;
            }
        }
    }

    public static void marginEnd(ViewGroup.LayoutParams layoutParams, int end) {
        if(layoutParams instanceof ViewGroup.MarginLayoutParams) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                ((ViewGroup.MarginLayoutParams)layoutParams).setMarginEnd(end);
            } else {
                ((ViewGroup.MarginLayoutParams)layoutParams).rightMargin = end;
            }
        }
    }
}
