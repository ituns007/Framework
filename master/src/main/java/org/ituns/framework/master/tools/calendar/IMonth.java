package org.ituns.framework.master.tools.calendar;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IMonth {
    private static final String DOT = "yyyy.MM";
    private static final String LINE = "yyyy-MM";
    private static final String SLASH = "yyyy/MM";
    private static final String CHINESE = "yyyy年MM月";

    private final Date date;
    private IMonth(Date date) {
        this.date = date;
    }

    public Date month() {
        return date;
    }

    public IDate date() {
        return IDate.with(date);
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
            Log.e("IMonth", "exception", e);
            return "";
        }
    }

    public static IMonth with() {
        return with(new Date());
    }

    public static IMonth with(long timeMillis) {
        return with(new Date(timeMillis));
    }

    public static IMonth with(Date date) {
        if(date == null) {
            return new IMonth(new Date());
        }
        return new IMonth(date);
    }

    public static IMonth withDot(String str) {
        return withPattern(str, DOT);
    }

    public static IMonth withLine(String str) {
        return withPattern(str, LINE);
    }

    public static IMonth withSlash(String str) {
        return withPattern(str, SLASH);
    }

    public static IMonth withChinese(String str) {
        return withPattern(str, CHINESE);
    }

    public static IMonth withPattern(String str, String pattern) {
        try {
            DateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
            return new IMonth(format.parse(str));
        } catch (Exception e) {
            Log.e("IMonth", "exception", e);
            return new IMonth(new Date());
        }
    }
}
