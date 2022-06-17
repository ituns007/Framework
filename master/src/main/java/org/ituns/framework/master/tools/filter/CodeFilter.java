package org.ituns.framework.master.tools.filter;

public class CodeFilter extends BaseFilter {
    private static final String INPUT = "^[\\d]{0,6}$";
    private static final String VERIFY = "^[\\d]{6}$";

    @Override
    protected String filter(String text) {
        if(text.matches(INPUT)) {
            return null;
        }
        return "";
    }

    public static boolean verify(String code) {
        return code.matches(VERIFY);
    }
}
