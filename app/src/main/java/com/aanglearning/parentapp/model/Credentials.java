package com.aanglearning.parentapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Credentials {
    @SerializedName("username")
    private String mobileNo;
    @SerializedName("password")
    private String password;
    @SerializedName("authToken")
    private String authToken;
    private ArrayList<ChildInfo> info;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public ArrayList<ChildInfo> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<ChildInfo> info) {
        this.info = info;
    }
}
