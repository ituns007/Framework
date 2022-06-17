package org.ituns.framework.master.modules.picker;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import org.ituns.framework.master.R;
import org.ituns.framework.master.tools.androidx.AlignLiveData;

import java.util.Date;

public final class TimePickerDialog implements OnTimeSelectListener, CustomListener {
    private TimePickerView pickerView;
    private final PickerOptions pickerOptions;
    private final AlignLiveData<Date> pickerLiveData;

    public TimePickerDialog(AppCompatActivity activity) {
        pickerLiveData = new AlignLiveData<>();
        pickerOptions = new PickerOptions(PickerOptions.TYPE_PICKER_TIME);
        pickerOptions.context = activity;
        pickerOptions.timeSelectListener = this;
        pickerOptions.type = new boolean[]{true, true, true, false, false, false};
        pickerOptions.label_year = "年";
        pickerOptions.label_month = "月";
        pickerOptions.label_day = "日";
        pickerOptions.label_hours = "时";
        pickerOptions.label_minutes = "分";
        pickerOptions.label_seconds = "秒";

        pickerOptions.textSizeTitle = 15;
        pickerOptions.textColorTitle = Color.parseColor("#333333");
        pickerOptions.textContentTitle = "选择日期";

        pickerOptions.textColorCancel = Color.parseColor("#ACACAC");
        pickerOptions.textContentCancel = "取消";
        pickerOptions.textColorConfirm = Color.parseColor("#2490F9");
        pickerOptions.textContentConfirm = "确认";
        pickerOptions.textSizeSubmitCancel = 15;

        pickerOptions.textColorOut = Color.parseColor("#ADADAD");
        pickerOptions.textColorCenter = Color.parseColor("#333333");
        pickerOptions.itemsVisibleCount = 15;
        pickerOptions.layoutRes = R.layout.fw_dialog_picker_time;
        pickerOptions.customListener = this;
    }

    public final PickerOptions options() {
        return pickerOptions;
    }

    public final LiveData<Date> liveData() {
        return pickerLiveData;
    }

    public final void show() {
        pickerView = new TimePickerView(pickerOptions);
        pickerView.show();
    }

    @Override
    public void onTimeSelect(Date date, View v) {
        pickerLiveData.postValue(date);
    }

    @Override
    public void customLayout(View view) {
        TextView btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setTextSize(pickerOptions.textSizeSubmitCancel);
        btnCancel.setTextColor(pickerOptions.textColorCancel);
        if(TextUtils.isEmpty(pickerOptions.textContentCancel)) {
            btnCancel.setText("取消");
        } else {
            btnCancel.setText(pickerOptions.textContentCancel);
        }
        btnCancel.setOnClickListener(v -> {
            if (pickerOptions.cancelListener != null) {
                pickerOptions.cancelListener.onClick(v);
            }
            if(pickerView != null) {
                pickerView.dismiss();
                pickerView = null;
            }
        });


        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setTextSize(pickerOptions.textSizeTitle);
        tvTitle.setTextColor(pickerOptions.textColorTitle);
        if(TextUtils.isEmpty(pickerOptions.textContentTitle)) {
            tvTitle.setText("选择日期");
        } else {
            tvTitle.setText(pickerOptions.textContentTitle);
        }


        TextView btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setTextSize(pickerOptions.textSizeSubmitCancel);
        btnSubmit.setTextColor(pickerOptions.textColorConfirm);
        if(TextUtils.isEmpty(pickerOptions.textContentConfirm)) {
            btnSubmit.setText("确定");
        } else {
            btnSubmit.setText(pickerOptions.textContentConfirm);
        }
        btnSubmit.setOnClickListener(v -> {
            if(pickerView != null) {
                pickerView.returnData();
                pickerView.dismiss();
                pickerView = null;
            }
        });
    }
}