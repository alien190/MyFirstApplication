package com.example.ivanovnv.myfirstapplication;

import java.io.Serializable;

/**
 * Created by IvanovNV on 21.02.2018.
 */

public class User implements Serializable {
    private String mLogin;
    private String mPassword;
    private boolean mHasSuccessLogin;

    public User(String mLogin, String mPassword) {
        this.mLogin = mLogin;
        this.mPassword = mPassword;
    }

    public String getmLogin() {
        return mLogin;
    }

    public String getmPassword() {
        return mPassword;
    }

    public boolean getmHasSuccessLogin() {return mHasSuccessLogin;}

    public void setmLogin(String mLogin) {
        this.mLogin = mLogin;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public void setmHasSuccessLogin(boolean mHasSuccessLogin) {
        this.mHasSuccessLogin = mHasSuccessLogin;
    }


}
