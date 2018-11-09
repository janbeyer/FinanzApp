package wbh.finanzapp.activity;

import wbh.finanzapp.business.ProfileBean;

public class ActivityMemory {

    private static ProfileBean curProfileBean;

    public static ProfileBean getCurProfileBean() {
        return curProfileBean;
    }

    public static void setCurProfileBean(ProfileBean curProfileBean) {
        ActivityMemory.curProfileBean = curProfileBean;
    }
}
