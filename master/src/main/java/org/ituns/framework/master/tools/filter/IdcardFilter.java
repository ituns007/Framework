package org.ituns.framework.master.tools.filter;

public class IdcardFilter extends BaseFilter {
    private static final String INPUT = "^[\\dxX]{1,18}$";
    private static final String VERIFY = "(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";

    @Override
    protected String filter(String text) {
        if(text.matches(INPUT)) {
            return null;
        }
        return "";
    }

    public static boolean verify(String idcard) {
        return idcard.matches(VERIFY);
    }
}
