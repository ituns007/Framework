package org.ituns.framework.master.widgets.input;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import org.ituns.framework.master.widgets.round.RoundEditText;

public class HolderEditText extends AppCompatEditText {
    private TextWatcher textWatcher;

    public HolderEditText(@NonNull Context context) {
        super(context);
    }

    public HolderEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HolderEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextWatcher(TextWatcher watcher) {
        if(textWatcher != null) {
            removeTextChangedListener(textWatcher);
        }

        textWatcher = watcher;

        if(textWatcher != null) {
            addTextChangedListener(textWatcher);
        }
    }
}
