package com.ep.joy.bmob.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * author   Joy
 * Date:  2016/7/13.
 * version:  V1.0
 * Description:
 */
public class MyUser extends BmobUser {
    private BmobFile icon;
    private String nikename;

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }

    public String getNikename() {
        return nikename;
    }

    public void setNikename(String nikename) {
        this.nikename = nikename;
    }
}
