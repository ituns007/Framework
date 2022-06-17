package org.ituns.framework.master.tools.filter;

public class PhoneFilter extends BaseFilter {
    private static final String INPUT = "^1[\\d]{0,10}$";
    private static final String VERIFY = "^1[\\d]{10}$";

    @Override
    protected String filter(String text) {
        if(text.matches(INPUT)) {
            return null;
        }
        return "";
    }

    public static boolean verify(String phone) {
        return phone.matches(VERIFY);
    }
}
