package com.aanglearning.parentapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Credentials implements Parcelable {
    @SerializedName("username")
    private String mobileNo;
    @SerializedName("password")
    private String password;
    @SerializedName("authToken")
    private String authToken;
    private ArrayList<ChildInfo> info;

    public Credentials() {
    }

    protected Credentials(Parcel in) {
        mobileNo = in.readString();
        password = in.readString();
        authToken = in.readString();
    }

    public static final Creator<Credentials> CREATOR = new Creator<Credentials>() {
        @Override
        public Credentials createFromParcel(Parcel in) {
            return new Credentials(in);
        }

        @Override
        public Credentials[] newArray(int size) {
            return new Credentials[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mobileNo);
        parcel.writeString(password);
        parcel.writeString(authToken);
    }
}
