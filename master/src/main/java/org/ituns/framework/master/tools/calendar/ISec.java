package org.ituns.framework.master.tools.calendar;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ISec {
    private static final String DOT = "yyyy.MM.dd HH:mm:ss";
    private static final String LINE = "yyyy-MM-dd HH:mm:ss";
    private static final String SLASH = "yyyy/MM/dd HH:mm:ss";
    private static final String CHINESE = "yyyy年MM月dd日 HH时mm分ss秒";

    private final Date date;
    private ISec(Date date) {
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

    public Date sec() {
        return date;
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
            Log.e("ISec", "exception", e);
            return "";
        }
    }

    public static ISec with() {
        return with(new Date());
    }

    public static ISec with(long timeMillis) {
        return with(new Date(timeMillis));
    }

    public static ISec with(Date date) {
        if(date == null) {
            return new ISec(new Date());
        }
        return new ISec(date);
    }

    public static ISec withDot(String str) {
        return withPattern(str, DOT);
    }

    public static ISec withLine(String str) {
        return withPattern(str, LINE);
    }

    public static ISec withSlash(String str) {
        return withPattern(str, SLASH);
    }

    public static ISec withChinese(String str) {
        return withPattern(str, CHINESE);
    }

    public static ISec withPattern(String str, String pattern) {
        try {
            DateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
            return new ISec(format.parse(str));
        } catch (Exception e) {
            Log.e("ISec", "exception", e);
            return new ISec(new Date());
        }
    }
}
