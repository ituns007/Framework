package org.ituns.framework.master.tools.java;

import java.util.ArrayList;
import java.util.List;

public class IList {

    public static int length(List<?> list) {
        return list == null ? 0 : list.size();
    }

    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean notEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }

    public static <T> List<T> maxList(List<T> list, int count) {
        if(!isEmpty(list)) {
            if(list.size() > count) {
                return list.subList(0, count);
            }
            return list;
        }
        return new ArrayList<>();
    }
}
