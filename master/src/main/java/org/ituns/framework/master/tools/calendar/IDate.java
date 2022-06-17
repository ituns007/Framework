package org.ituns.framework.master.tools.calendar;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IDate {
    private static final String DOT = "yyyy.MM.dd";
    private static final String LINE = "yyyy-MM-dd";
    private static final String SLASH = "yyyy/MM/dd";
    private static final String CHINESE = "yyyy年MM月dd日";

    private final Date date;
    private IDate(Date date) {
        this.date = date;
    }

    public IMonth month() {
        return IMonth.with(date);
    }

    public Date date() {
        return date;
    }

    public IMin min() {
        return IMin.with(date);
    }

    public ISec sec() {
        return ISec.with(date);
    }

    public IMillis millis() {
        return IMillis.with(date);
    }

    public long timeMillis() {
        return date.getTime();
    }

    public String formatDot() {
        return formatPattern(DOT);
    }

    public String formatLine() {
        return formatPattern(LINE);
    }

    public String formatSlash() {
        return formatPattern(SLASH);
    }

    public String formatChinese() {
        return formatPattern(CHINESE);
    }

    public String formatPattern(String pattern) {
        try {
            DateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
            return format.format(date);
        } catch (Exception e) {
            Log.e("IDate", "exception", e);
            return "";
        }
    }

    public static IDate with() {
        return with(new Date());
    }

    public static IDate with(long timeMillis) {
        return with(new Date(timeMillis));
    }

    public static IDate with(Date date) {
        if(date == null) {
            return new IDate(new Date());
        }
        return new IDate(date);
    }

    public static IDate withDot(String str) {
        return withPattern(str, DOT);
    }

    public static IDate withLine(String str) {
        return withPattern(str, LINE);
    }

    public static IDate withSlash(String str) {
        return withPattern(str, SLASH);
    }

    public static IDate withChinese(String str) {
        return withPattern(str, CHINESE);
    }

    public static IDate withPattern(String str, String pattern) {
        try {
            DateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
            return new IDate(format.parse(str));
        } catch (Exception e) {
            Log.e("IDate", "exception", e);
            return new IDate(new Date());
        }
    }
}