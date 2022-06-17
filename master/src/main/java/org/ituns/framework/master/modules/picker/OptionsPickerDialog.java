package org.ituns.framework.master.modules.picker;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.view.WheelView;

import org.ituns.framework.master.R;
import org.ituns.framework.master.tools.android.IClick;
import org.ituns.framework.master.tools.android.IScreen;
import org.ituns.framework.master.tools.java.IList;

import java.util.ArrayList;
import java.util.List;

public class OptionsPickerDialog extends DialogFragment {
    private static final String DEFAULT_TITLE = "选择时间";
    private static final float DEFAULT_TITLE_SIZE = 15f;
    private static final int DEFAULT_TITLE_COLOR = Color.parseColor("#333333");
    private static final String DEFAULT_CANCEL = "取消";
    private static final float DEFAULT_CANCEL_SIZE = 15f;
    private static final int DEFAULT_CANCEL_COLOR = Color.parseColor("#ACACAC");
    private static final String DEFAULT_SUBMIT = "确认";
    private static final float DEFAULT_SUBMIT_SIZE = 15f;
    private static final int DEFAULT_SUBMIT_COLOR = Color.parseColor("#2490F9");

    private TextView titleView;
    private TextView cancelView;
    private TextView submitView;
    private WheelView wheelView;

    private Object select;
    private OptionsAdapter adapter;
    private OptionsCallback callback;

    private OptionsPickerDialog() {
        select = null;
        adapter = new OptionsAdapter();
    }

    public void setSelect(Object select) {
        this.select = select;
    }

    public void setOptions(List<Object> options) {
        adapter.setData(options);
    }

    public void setCallback(OptionsCallback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fw_dialog_picker_options, container, false);
        titleView = view.findViewById(R.id.title);
        cancelView = view.findViewById(R.id.cancel);
        submitView = view.findViewById(R.id.submit);
        wheelView = view.findViewById(R.id.options);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeBottomDialog();
        initializeHeaderView();
        initializeWheelView();
    }

    private void initializeBottomDialog() {
        Dialog dialog = getDialog();
        if(dialog != null) {
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(null);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) IScreen.width(requireActivity());
            params.gravity = Gravity.BOTTOM;
            window.setAttributes(params);
        }
    }

    private void initializeHeaderView() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            titleView.setText(bundle.getString("title", DEFAULT_TITLE));
            titleView.setTextSize(bundle.getFloat("titleSize", DEFAULT_TITLE_SIZE));
            titleView.setTextColor(bundle.getInt("titleColor", DEFAULT_TITLE_COLOR));
            cancelView.setText(bundle.getString("cancel", DEFAULT_CANCEL));
            cancelView.setTextSize(bundle.getFloat("cancelSize", DEFAULT_CANCEL_SIZE));
            cancelView.setTextColor(bundle.getInt("cancelColor", DEFAULT_CANCEL_COLOR));
            submitView.setText(bundle.getString("submit", DEFAULT_SUBMIT));
            submitView.setTextSize(bundle.getFloat("submitSize", DEFAULT_SUBMIT_SIZE));
            submitView.setTextColor(bundle.getInt("submitColor", DEFAULT_SUBMIT_COLOR));
        }
        IClick.single(cancelView, v -> dismiss());
        IClick.single(submitView, v -> clickSubmit());
    }

    private void initializeWheelView() {
        wheelView.setLabel("");
        wheelView.setCyclic(false);
        wheelView.setAdapter(adapter);
        wheelView.setCurrentItem(adapter.indexOf(select));
    }

    private void clickSubmit() {
        if(callback != null) {
            select = adapter.getItem(wheelView.getCurrentItem());
            callback.onOptions(select);
        }
        dismiss();
    }

    private static class OptionsAdapter implements WheelAdapter<Object> {
        private final ArrayList<Object> mOptionsData = new ArrayList<>();

        public void setData(List<Object> options) {
            if(IList.notEmpty(options)) {
                mOptionsData.addAll(options);
            }
        }

        @Override
        public int getItemsCount() {
            return mOptionsData.size();
        }

        @Override
        public Object getItem(int index) {
            if(0 <= index && index < mOptionsData.size()) {
                return mOptionsData.get(index);
            }
            return null;
        }

        @Override
        public int indexOf(Object o) {
            int index = mOptionsData.indexOf(o);
            return Math.max(0, index);
        }
    }

    public interface OptionsCallback {
        void onOptions(Object options);
    }

    public static class Builder {
        private final Bundle bundle = new Bundle();

        public Builder title(String title) {
            bundle.putString("title", title);
            return this;
        }

        public Builder titleSize(float size) {
            bundle.putFloat("titleSize", size);
            return this;
        }

        public Builder titleColor(int color) {
            bundle.putInt("titleColor", color);
            return this;
        }

        public Builder titleColor(String color) {
            bundle.putInt("titleColor", Color.parseColor(color));
            return this;
        }

        public Builder cancel(String cancel) {
            bundle.putString("cancel", cancel);
            return this;
        }

        public Builder cancelSize(float size) {
            bundle.putFloat("cancelSize", size);
            return this;
        }

        public Builder cancelColor(int color) {
            bundle.putInt("cancelColor", color);
            return this;
        }

        public Builder cancelColor(String color) {
            bundle.putInt("cancelColor", Color.parseColor(color));
            return this;
        }

        public Builder submit(String submit) {
            bundle.putString("submit", submit);
            return this;
        }

        public Builder submitSize(float size) {
            bundle.putFloat("submitSize", size);
            return this;
        }

        public Builder submitColor(int color) {
            bundle.putInt("submitColor", color);
            return this;
        }

        public Builder submitColor(String color) {
            bundle.putInt("submitColor", Color.parseColor(color));
            return this;
        }

        public Builder startTime(long timeMillis) {
            bundle.putLong("startTime", timeMillis);
            return this;
        }

        public Builder limitTime(long timeMillis) {
            bundle.putLong("limitTime", timeMillis);
            return this;
        }

        public OptionsPickerDialog build() {
            OptionsPickerDialog dialog = new OptionsPickerDialog();
            dialog.setArguments(bundle);
            return dialog;
        }

        public OptionsPickerDialog show(FragmentManager fm) {
            OptionsPickerDialog dialog = build();
            dialog.show(fm, "period");
            return dialog;
        }
    }
}
