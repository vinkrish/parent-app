package com.aanglearning.parentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aanglearning.parentapp.dao.ChildInfoDao;
import com.aanglearning.parentapp.dashboard.DashboardActivity;
import com.aanglearning.parentapp.login.LoginActivity;
import com.aanglearning.parentapp.model.ChildInfo;
import com.aanglearning.parentapp.util.AppGlobal;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import org.joda.time.LocalDate;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppGlobal.setSqlDbHelper(getApplicationContext());

        LocalDate localDate = new LocalDate();
        SharedPreferenceUtil.saveHomeworkDate(this, localDate.toString());

        if (ChildInfoDao.getChildInfos().size() == 0) {
            SharedPreferenceUtil.logout(this);
            SharedPreferenceUtil.clearProfile(this);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (SharedPreferenceUtil.getUser(this).getAuthToken() != "") {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
