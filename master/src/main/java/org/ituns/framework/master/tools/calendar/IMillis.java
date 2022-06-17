package org.ituns.framework.master.tools.calendar;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IMillis {
    private static final String DOT = "yyyy.MM.dd HH:mm:ss SSS";
    private static final String LINE = "yyyy-MM-dd HH:mm:ss SSS";
    private static final String SLASH = "yyyy/MM/dd HH:mm:ss SSS";
    private static final String CHINESE = "yyyy年MM月dd日 HH时mm分ss秒 SSS毫秒";

    private final Date date;
    private IMillis(Date date) {
        this.date = date;
    }

    public IMonth month() {
        return IMonth.with(date);
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

    public Date millis() {
        return date;
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
            Log.e("IMillis", "exception", e);
            return "";
        }
    }

    public static IMillis with() {
        return with(new Date());
    }

    public static IMillis with(long timeMillis) {
        return with(new Date(timeMillis));
    }

    public static IMillis with(Date date) {
        if(date == null) {
            return new IMillis(new Date());
        }
        return new IMillis(date);
    }

    public static IMillis withDot(String str) {
        return withPattern(str, DOT);
    }

    public static IMillis withLine(String str) {
        return withPattern(str, LINE);
    }

    public static IMillis withSlash(String str) {
        return withPattern(str, SLASH);
    }

    public static IMillis withChinese(String str) {
        return withPattern(str, CHINESE);
    }

    public static IMillis withPattern(String str, String pattern) {
        try {
            DateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
            return new IMillis(format.parse(str));
        } catch (Exception e) {
            Log.e("IMillis", "exception", e);
            return new IMillis(new Date());
        }
    }
}
