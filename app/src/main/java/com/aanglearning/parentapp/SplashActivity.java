package com.aanglearning.parentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aanglearning.parentapp.dao.ChildInfoDao;
import com.aanglearning.parentapp.dashboard.DashboardActivity;
import com.aanglearning.parentapp.login.LoginActivity;
import com.aanglearning.parentapp.model.Credentials;
import com.aanglearning.parentapp.fcm.FCMIntentService;
import com.aanglearning.parentapp.util.AppGlobal;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppGlobal.setSqlDbHelper(getApplicationContext());
        Credentials credentials = SharedPreferenceUtil.getUser(this);

        if(!credentials.getMobileNo().equals("") && !SharedPreferenceUtil.isFcmTokenSaved(this)) {
            startService(new Intent(this, FCMIntentService.class));
        }

        if (ChildInfoDao.getChildInfos().size() == 0) {
            SharedPreferenceUtil.logout(this);
            SharedPreferenceUtil.clearProfile(this);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (!credentials.getAuthToken().equals("")) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
