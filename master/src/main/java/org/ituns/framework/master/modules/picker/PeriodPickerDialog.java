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

import java.util.Calendar;
import java.util.Date;

public class PeriodPickerDialog extends DialogFragment {
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
    private WheelView syWheelView;
    private WheelView smWheelView;
    private WheelView eyWheelView;
    private WheelView emWheelView;

    private Calendar startTime;
    private Calendar limitTime;
    private PeriodCallback periodCallback;

    public void setCallback(PeriodCallback periodCallback) {
        this.periodCallback = periodCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fw_dialog_picker_period, container, false);
        titleView = view.findViewById(R.id.title);
        cancelView = view.findViewById(R.id.cancel);
        submitView = view.findViewById(R.id.submit);
        syWheelView = view.findViewById(R.id.start_year);
        smWheelView = view.findViewById(R.id.start_month);
        eyWheelView = view.findViewById(R.id.end_year);
        emWheelView = view.findViewById(R.id.end_month);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializePeriodPickerData();
        initializeBottomDialog();
        initializeHeaderView();
        initializeWheelView();
    }

    private void initializePeriodPickerData() {
        startTime = Calendar.getInstance();
        limitTime = Calendar.getInstance();
        Bundle bundle = getArguments();
        if(bundle != null) {
            startTime.setTimeInMillis(bundle.getLong("startTime", System.currentTimeMillis()));
            limitTime.setTimeInMillis(bundle.getLong("limitTime", System.currentTimeMillis()));
        }
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
        int leastYear = 1970;
        int leastMonth = 1;
        int startYear = startTime.get(Calendar.YEAR);
        int startMonth = startTime.get(Calendar.MONTH);
        int limitYear = limitTime.get(Calendar.YEAR);
        int limitMonth = limitTime.get(Calendar.MONTH);

        syWheelView.setLabel("");
        syWheelView.setCyclic(false);
        syWheelView.setAdapter(new YearAdapter(leastYear));
        if(startYear >= leastYear) {
            syWheelView.setCurrentItem(startYear - leastYear);
        } else {
            syWheelView.setCurrentItem(0);
        }
        syWheelView.setOnItemSelectedListener(index -> refreshWheelView());

        smWheelView.setLabel("");
        smWheelView.setCyclic(false);
        smWheelView.setAdapter(new MonthAdapter(leastMonth));
        smWheelView.setCurrentItem(startMonth + 1 - leastMonth);
        smWheelView.setOnItemSelectedListener(index -> refreshWheelView());

        eyWheelView.setLabel("");
        eyWheelView.setCyclic(false);
        eyWheelView.setAdapter(new YearAdapter(startYear));
        if(limitYear >= startYear) {
            eyWheelView.setCurrentItem(limitYear - startYear);
        } else {
            eyWheelView.setCurrentItem(0);
        }
        eyWheelView.setOnItemSelectedListener(index -> refreshWheelView());

        emWheelView.setLabel("");
        emWheelView.setCyclic(false);
        if(startYear < limitYear) {
            emWheelView.setAdapter(new MonthAdapter(leastMonth));
            emWheelView.setCurrentItem(limitMonth + 1 - leastMonth);
        } else {
            emWheelView.setAdapter(new MonthAdapter(startMonth + 1));
            if(limitMonth >= startMonth) {
                emWheelView.setCurrentItem(limitMonth- startMonth);
            } else {
                emWheelView.setCurrentItem(0);
            }
        }
        emWheelView.setOnItemSelectedListener(index -> refreshWheelView());
    }

    private void refreshWheelView() {
        YearData syData = getCurrentYear(syWheelView);
        YearData eyData = getCurrentYear(eyWheelView);
        if(syData != null && eyData != null) {
            YearAdapter yearAdapter = new YearAdapter(syData.year);
            eyWheelView.setAdapter(yearAdapter);
            eyWheelView.setCurrentItem(yearAdapter.indexOf(eyData));

            MonthData smData = getCurrentMonth(smWheelView);
            MonthData emData = getCurrentMonth(emWheelView);
            if(smData != null && emData != null) {
                MonthAdapter monthAdapter = new MonthAdapter(1);
                if(syData.year >= eyData.year) {
                    monthAdapter = new MonthAdapter(smData.month);
                }
                emWheelView.setAdapter(monthAdapter);
                emWheelView.setCurrentItem(monthAdapter.indexOf(emData));
            }
        }
    }

    private YearData getCurrentYear(WheelView wheelView) {
        WheelAdapter<?> adapter = wheelView.getAdapter();
        if(adapter instanceof YearAdapter) {
            YearAdapter yearAdapter = (YearAdapter) adapter;
            return yearAdapter.getItem(wheelView.getCurrentItem());
        }
        return null;
    }

    private MonthData getCurrentMonth(WheelView wheelView) {
        WheelAdapter<?> adapter = wheelView.getAdapter();
        if(adapter instanceof MonthAdapter) {
            MonthAdapter monthAdapter = (MonthAdapter) adapter;
            return monthAdapter.getItem(wheelView.getCurrentItem());
        }
        return null;
    }

    private void clickSubmit() {
        Date startDate = Calendar.getInstance().getTime();
        YearData syData = getCurrentYear(syWheelView);
        MonthData smData = getCurrentMonth(smWheelView);
        if(syData != null && smData != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(syData.year, smData.month - 1, 1, 0, 0, 0);
            startDate = calendar.getTime();
        }

        Date limitDate = Calendar.getInstance().getTime();
        YearData eyData = getCurrentYear(eyWheelView);
        MonthData emData = getCurrentMonth(emWheelView);
        if(eyData != null && emData != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(eyData.year, emData.month - 1, 1, 0, 0, 0);
            limitDate = calendar.getTime();
        }

        if(periodCallback != null) {
            periodCallback.onPeriod(startDate, limitDate);
        }

        dismiss();
    }

    private static class YearData {
        public final int year;

        public YearData(int year) {
            this.year = year;
        }

        @Override
        public String toString() {
            return year + "年";
        }
    }

    private static class YearAdapter implements WheelAdapter<YearData> {
        private final int year;

        public YearAdapter(int year) {
            this.year = year;
        }

        @Override
        public int getItemsCount() {
            return 2100 - year;
        }

        @Override
        public YearData getItem(int index) {
            return new YearData(year + index);
        }

        @Override
        public int indexOf(YearData o) {
            return Math.max(o.year - year, 0);
        }
    }

    private static class MonthData {
        public final int month;

        public MonthData(int month) {
            this.month = month;
        }

        @Override
        public String toString() {
            return month + "月";
        }
    }

    private static class MonthAdapter implements WheelAdapter<MonthData> {
        private final int month;

        public MonthAdapter(int month) {
            this.month = month;
        }

        @Override
        public int getItemsCount() {
            return 12 - month + 1;
        }

        @Override
        public MonthData getItem(int index) {
            return new MonthData(month + index);
        }

        @Override
        public int indexOf(MonthData o) {
            return Math.max(o.month - month, 0);
        }
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

        public PeriodPickerDialog build() {
            PeriodPickerDialog dialog = new PeriodPickerDialog();
            dialog.setArguments(bundle);
            return dialog;
        }

        public PeriodPickerDialog show(FragmentManager fm) {
            PeriodPickerDialog dialog = build();
            dialog.show(fm, "period");
            return dialog;
        }
    }

    public interface PeriodCallback {
        void onPeriod(Date start, Date limit);
    }
}
