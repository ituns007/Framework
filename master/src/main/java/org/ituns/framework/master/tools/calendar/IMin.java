package org.ituns.framework.master.tools.calendar;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IMin {
    private static final String DOT = "yyyy.MM.dd HH:mm";
    private static final String LINE = "yyyy-MM-dd HH:mm";
    private static final String SLASH = "yyyy/MM/dd HH:mm";
    private static final String CHINESE = "yyyy年MM月dd日 HH时mm分";

    private final Date date;
    private IMin(Date date) {
        this.date = date;
    }

    public IMonth month() {
        return IMonth.with(date);
    }

    public IDate date() {
        return IDate.with(date);
    }

    public Date min() {
        return date;
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
            Log.e("IMin", "exception", e);
            return "";
        }
    }

    public static IMin with() {
        return with(new Date());
    }

    public static IMin with(long timeMillis) {
        return with(new Date(timeMillis));
    }

    public static IMin with(Date date) {
        if(date == null) {
            return new IMin(new Date());
        }
        return new IMin(date);
    }

    public static IMin withDot(String str) {
        return withPattern(str, DOT);
    }

    public static IMin withLine(String str) {
        return withPattern(str, LINE);
    }

    public static IMin withSlash(String str) {
        return withPattern(str, SLASH);
    }

    public static IMin withChinese(String str) {
        return withPattern(str, CHINESE);
    }

    public static IMin withPattern(String str, String pattern) {
        try {
            DateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
            return new IMin(format.parse(str));
        } catch (Exception e) {
            Log.e("IMin", "exception", e);
            return new IMin(new Date());
        }
    }
}
