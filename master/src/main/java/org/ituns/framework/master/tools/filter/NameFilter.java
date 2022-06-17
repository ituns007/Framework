package org.ituns.framework.master.tools.filter;

public class NameFilter extends BaseFilter {
    private static final String INPUT = "^[\u4E00-\u9FA5\u00B7\u2022\u2027\uFF65\u30FB\u1427\u22C5\u2219\u0387]{1,20}$";
    private static final String VERIFY = "^[\u4E00-\u9FA5\u00B7\u2022\u2027\uFF65\u30FB\u1427\u22C5\u2219\u0387]{2,20}$";

    @Override
    protected String filter(String text) {
        if(text.matches(INPUT)) {
            return null;
        }
        return "";
    }

    public static boolean verify(String name) {
        return name.matches(VERIFY);
    }
}
