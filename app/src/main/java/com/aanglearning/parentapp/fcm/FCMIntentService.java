package com.aanglearning.parentapp.fcm;

import android.app.IntentService;
import android.content.Intent;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.AuthApi;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FCMIntentService extends IntentService {

    public FCMIntentService() {
        super("FCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AuthApi api = ApiClient.getAuthorizedClient().create(AuthApi.class);

        Call<Void> queue = api.updateFcmToken(SharedPreferenceUtil.getAuthorization(getApplicationContext()));
        queue.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    SharedPreferenceUtil.fcmTokenSaved(App.getInstance());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

}
