package com.example.ivanovnv.myfirstapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by IvanovNV on 21.02.2018.
 */

public class User implements Serializable {
    @SerializedName("email")
    private String mEmail;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("name")
    private String mName;
    private boolean mHasSuccessLogin;

    public User(String mEmail, String mName, String mPassword) {
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mName = mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public boolean getHasSuccessLogin() {return mHasSuccessLogin;}

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public void setHasSuccessLogin(boolean mHasSuccessLogin) {
        this.mHasSuccessLogin = mHasSuccessLogin;
    }


    public String getName() {
        return mName;
    }
}
