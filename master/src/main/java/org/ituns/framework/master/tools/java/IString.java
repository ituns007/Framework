package org.ituns.framework.master.tools.java;

import android.text.Editable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class IString {
    public static final String TEMPLETE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static int length(String s) {
        return s == null ? 0 : s.length();
    }

    public static boolean isNull(String str) {
        return str == null;
    }

    public static boolean notNull(String str) {
        return str != null;
    }

    public static boolean isEmpty(String str) {
        return isNull(str) || str.length() == 0;
    }

    public static boolean notEmpty(String str) {
        return notNull(str) && str.length() > 0;
    }

    public static boolean isEquals(String a, String b) {
        if(isNull(a) && isNull(b)) {
            return true;
        }
        return notNull(a) && notNull(b) && a.equals(b);
    }

    public static boolean notEquals(String a, String b) {
        if(notNull(a) && notNull(b)) {
            return !a.equals(b);
        }
        return !(isNull(a) && isNull(b));
    }

    public static String nullByDefault(String str, String defaultValue) {
        return notNull(str) ? str : defaultValue;
    }

    public static String emptyByDefault(String str, String defaultValue) {
        return notEmpty(str) ? str : defaultValue;
    }

    public static boolean isNull(String... array) {
        if(array == null || array.length == 0) {
            return true;
        }

        for(String str : array) {
            if(notNull(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean notNull(String... array) {
        if(array == null || array.length == 0) {
            return false;
        }

        for(String str : array) {
            if(isNull(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(String... array) {
        if(array == null || array.length == 0) {
            return true;
        }

        for(String str : array) {
            if(notEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean notEmpty(String... array) {
        if(array == null || array.length == 0) {
            return false;
        }

        for(String str : array) {
            if(isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEquals(String... array) {
        if(array == null || array.length == 0) {
            return false;
        }

        String first = array[0];
        for (String str : array) {
            if(notEquals(first, str)) {
                return false;
            }
        }
        return true;
    }

    //思路待定
//    public static boolean notEquals(String... array) {
//        if(array == null || array.length == 0) {
//            return false;
//        }
//        return true;
//    }


    public static String valueOf(String str) {
        return str == null ? "" : str;
    }

    public static String valueOf(CharSequence str) {
        return str == null ? "" : str.toString();
    }

    public static String valueOf(Editable editable) {
        return editable == null ? "" : editable.toString();
    }

    public static String randomString(int length) {
        return randomString(TEMPLETE, length);
    }

    public static String randomString(String template, int length) {
        if (notEmpty(template) && length > 0) {
            Random random = new Random();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                int number = random.nextInt(template.length());
                builder.append(template.charAt(number));
            }
            return builder.toString();
        }
        return "";
    }

    public static boolean match(String str, String... matches) {
        if(matches == null || matches.length == 0) {
            return false;
        }

        for(String match : matches) {
            if(isEquals(str, match)) {
                return true;
            }
        }
        return false;
    }

    public static boolean notMatch(String str, String... matches) {
        if(matches == null || matches.length == 0) {
            return true;
        }

        for(String match : matches) {
            if(isEquals(str, match)) {
                return false;
            }
        }
        return true;
    }

    public static <T extends CharSequence> String join(T delimiter, T... elements) {
        if(elements != null) {
            return join(delimiter, new ArrayList<>(Arrays.asList(elements)));
        }
        return "";
    }

    public static <T extends CharSequence> String join(T delimiter, List<T> elements) {
        StringBuilder builder = new StringBuilder();
        if(IList.notEmpty(elements)) {
            for(CharSequence element : elements) {
                if(builder.length() > 0) builder.append(delimiter);
                builder.append(element);
            }
        }
        return builder.toString();
    }
}
