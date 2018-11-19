package wbh.finanzapp.util;

import wbh.finanzapp.business.ProfileBean;

public class ProfileMemory {

    private static ProfileBean curProfileBean;

    public static ProfileBean getCurProfileBean() {
        return curProfileBean;
    }

    public static void setCurProfileBean(ProfileBean curProfileBean) {
        ProfileMemory.curProfileBean = curProfileBean;
    }
}
