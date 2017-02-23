package com.aanglearning.parentapp.util;

import android.app.Activity;
import android.content.Context;

import com.aanglearning.parentapp.sqlite.SqlDbHelper;

/**
 * Created by Vinay on 21-02-2017.
 */

public class AppGlobal {
    private static Activity activity;
    private static SqlDbHelper sqlDbHelper;

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        AppGlobal.activity = activity;
    }

    public static SqlDbHelper getSqlDbHelper() {
        return sqlDbHelper;
    }

    public static void setSqlDbHelper(Context context) {
        AppGlobal.sqlDbHelper = SqlDbHelper.getInstance(context);;
    }
}
