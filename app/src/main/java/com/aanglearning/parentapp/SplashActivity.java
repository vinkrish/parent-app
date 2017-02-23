package com.aanglearning.parentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aanglearning.parentapp.dashboard.DashboardActivity;
import com.aanglearning.parentapp.login.LoginActivity;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SharedPreferenceUtil.getUser(this).getAuthToken() != "") {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
