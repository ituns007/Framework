package org.ituns.framework.master.tools.filter;

import android.text.InputFilter;
import android.text.Spanned;

public abstract class BaseFilter implements InputFilter {

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String srcValue = srcValue(source, start, end);
        String destValue = destValue(dest);
        String destStart = destStart(destValue, dstart);
        String destEnd = destEnd(destValue, dend);
        return filter(destStart + srcValue + destEnd);
    }

    protected abstract String filter(String text);

    private String srcValue(CharSequence sequence, int start, int end) {
        if(null == sequence) {
            return "";
        }
        String text = sequence.toString();
        if(text.length() > (end - start)) {
            return text.substring(start, end);
        }
        return text;
    }

    private String destValue(Spanned spanned) {
        return spanned == null ? "" : spanned.toString();
    }

    private String destStart(String destValue, int dstart) {
        try {
            return destValue.substring(0, dstart);
        } catch (Exception e) {
            return "";
        }
    }

    private String destEnd(String destValue, int dend) {
        try {
            return destValue.substring(dend);
        } catch (Exception e) {
            return "";
        }
    }
}
