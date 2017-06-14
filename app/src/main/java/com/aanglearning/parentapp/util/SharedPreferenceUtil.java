package com.aanglearning.parentapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
        return childInfo;
    }

    public static void saveAttendanceDate(Context context, String date) {
        SharedPreferences sharedPref = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("attendanceDate", date);
        editor.apply();
    }

    public static String getAttendanceDate(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return sharedPref.getString("attendanceDate", "");
    }
    public static void saveHomeworkDate(Context context, String date) {
        SharedPreferences sharedPref = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("homeworkDate", date);
        editor.apply();
    }

    public static String getHomeworkDate(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("profile", Context.MODE_PRIVATE);
        return sharedPref.getString("homeworkDate", "");
    }

}
