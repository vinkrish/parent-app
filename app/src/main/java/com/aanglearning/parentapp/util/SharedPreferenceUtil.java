package com.aanglearning.parentapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.aanglearning.parentapp.model.AppVersion;
import com.aanglearning.parentapp.model.Authorization;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.model.Credentials;

public class SharedPreferenceUtil {

    public static void saveUser(Context context, Credentials credentials) {
        SharedPreferences sharedPref = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("authToken", credentials.getAuthToken());
        editor.putString("mobileNo", credentials.getMobileNo());
        editor.apply();
    }

    public static Credentials getUser(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        Credentials response = new Credentials();
        response.setAuthToken(sharedPref.getString("authToken", ""));
        response.setMobileNo(sharedPref.getString("mobileNo", ""));
        return response;
    }

    public static void logout(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("authToken", "");
        editor.putString("mobileNo", "");
        editor.apply();
    }

    public static void clearProfile(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", "");
        editor.putBoolean("is_message_recipients_Saved", false);
        editor.apply();
    }

    public static void saveProfile(Context context, ChildInfo childInfo) {
        SharedPreferences sharedPref = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("schoolId", childInfo.getSchoolId());
        editor.putString("schoolName", childInfo.getSchoolName());
        editor.putLong("classId", childInfo.getClassId());
        editor.putString("className", childInfo.getClassName());
        editor.putLong("sectionId", childInfo.getSectionId());
        editor.putString("sectionName", childInfo.getSectionName());
        editor.putLong("studentId", childInfo.getStudentId());
        editor.putString("name", childInfo.getName());
        editor.putString("image", childInfo.getImage());
        editor.apply();
    }

    public static ChildInfo getProfile(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        ChildInfo childInfo = new ChildInfo();
        childInfo.setSchoolId(sharedPref.getLong("schoolId", 0));
        childInfo.setSchoolName(sharedPref.getString("schoolName", ""));
        childInfo.setClassId(sharedPref.getLong("classId", 0));
        childInfo.setClassName(sharedPref.getString("className", ""));
        childInfo.setSectionId(sharedPref.getLong("sectionId", 0));
        childInfo.setSectionName(sharedPref.getString("sectionName", ""));
        childInfo.setStudentId(sharedPref.getLong("studentId", 0));
        childInfo.setName(sharedPref.getString("name", ""));
        childInfo.setImage(sharedPref.getString("image", ""));
        return childInfo;
    }

    public static void updateProfile(Context context, String image) {
        SharedPreferences sharedPref = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("image", image);
        editor.apply();
    }

    public static void saveFcmToken(Context context, String fcmToken) {
        SharedPreferences sharedPref = context.getSharedPreferences("fcm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("fcmToken", fcmToken);
        editor.putBoolean("isSaved", false);
        editor.apply();
    }

    public static Authorization getAuthorization(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("fcm", Context.MODE_PRIVATE);
        Authorization authorization = new Authorization();
        authorization.setFcmToken(sharedPref.getString("fcmToken", ""));
        authorization.setUser(sharedPref.getString("user", ""));
        return authorization;
    }

    public static void saveAuthorizedUser(Context context, String user) {
        SharedPreferences sharedPref = context.getSharedPreferences("fcm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user", user);
        editor.apply();
    }

    public static boolean isFcmTokenSaved(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("fcm", Context.MODE_PRIVATE);
        return sharedPref.getBoolean("isSaved", false);
    }

    public static void fcmTokenSaved(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("fcm", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isSaved", true);
        editor.apply();
    }

    public static boolean isMessageRecipientsSaved(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return sharedPref.getBoolean("is_message_recipients_Saved", false);
    }

    public static void messageRecipientsSaved(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("is_message_recipients_Saved", true);
        editor.apply();
    }

    public static AppVersion getAppVersion(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("ver", Context.MODE_PRIVATE);
        AppVersion appVersion = new AppVersion();
        appVersion.setVersionId(sharedPref.getInt("version_id", 0));
        appVersion.setVersionName(sharedPref.getString("version_name", ""));
        appVersion.setStatus(sharedPref.getString("version_status", ""));
        return appVersion;
    }

    public static void saveAppVersion(Context context, AppVersion appVersion) {
        SharedPreferences sharedPref = context.getSharedPreferences("ver", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("version_id", appVersion.getVersionId());
        editor.putString("version_name", appVersion.getVersionName());
        editor.putString("version_status", appVersion.getStatus());
        editor.apply();
    }

    public static boolean isUpdatePrompted(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("ver", Context.MODE_PRIVATE);
        return sharedPref.getBoolean("is_prompted", false);
    }

    public static void updatePrompted(Context context, boolean isPrompted) {
        SharedPreferences sharedPref = context.getSharedPreferences("ver", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("is_prompted", isPrompted);
        editor.apply();
    }

    public static boolean isNotifiable(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("notify", Context.MODE_PRIVATE);
        return sharedPref.getBoolean("is_notifiable", true);
    }

    public static void setNotifiable(Context context, boolean isNotifiable) {
        SharedPreferences sharedPref = context.getSharedPreferences("notify", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("is_notifiable", isNotifiable);
        editor.apply();
    }

}
