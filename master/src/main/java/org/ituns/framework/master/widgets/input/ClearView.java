package org.ituns.framework.master.widgets.input;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import org.ituns.framework.master.R;
import org.ituns.framework.master.tools.android.IScreen;

public class ClearView extends RelativeLayout implements View.OnClickListener, TextWatcher {
    private int clearId;
    private AppCompatImageView iconView;

    public ClearView(Context context) {
        super(context);
        initializeView(context, null);
    }

    public ClearView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context, attrs);
    }

    public ClearView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(context, attrs);
    }

    private void initializeView(Context context, AttributeSet attrs) {
        iconView = new AppCompatImageView(context);
        LayoutParams iconParams = new LayoutParams(
                IScreen.dp2px(context, 18),
                IScreen.dp2px(context, 18));
        iconParams.addRule(CENTER_IN_PARENT);
        addView(iconView, iconParams);

        if(attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClearView, 0, 0);
            for(int i = 0; i < ta.getIndexCount(); i++) {
                int attr = ta.getIndex(i);
                if(attr == R.styleable.ClearView_clearId) {
                    clearId = ta.getResourceId(attr, 0);
                } else if(attr == R.styleable.ClearView_clearIcon) {
                    iconView.setBackgroundDrawable(ta.getDrawable(attr));
                }
            }
            ta.recycle();
        }

        setVisibility(GONE);
        setOnClickListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        TextView textView = findInputView(clearId);
        if(textView != null) {
            textView.removeTextChangedListener(this);
            textView.addTextChangedListener(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        TextView textView = findInputView(clearId);
        if(textView != null) {
            textView.removeTextChangedListener(this);
        }
        super.onDetachedFromWindow();
    }

    @Override
    public void onClick(View v) {
        TextView textView = findInputView(clearId);
        if(textView != null) {
            textView.setText("");
        }
    }

    private float dimenValue(Context context, int id) {
        return context.getResources().getDimension(id);
    }

    private TextView findInputView(int id) {
        if(id != 0) {
            ViewParent viewParent = getParent();
            if(viewParent instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) viewParent;
                View view = viewGroup.findViewById(id);
                if(view instanceof TextView) {
                    return (TextView) view;
                }
            }
        }
        return null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(TextUtils.isEmpty(s)) {
            setVisibility(GONE);
        } else {
            TextView textView = findInputView(clearId);
            if(textView != null && textView.isEnabled()) {
                setVisibility(VISIBLE);
            } else {
                setVisibility(GONE);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {}
}
