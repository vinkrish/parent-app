package com.aanglearning.parentapp.util;

import android.app.IntentService;
import android.content.Intent;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.BuildConfig;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.ParentApi;
import com.aanglearning.parentapp.model.AppVersion;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VersionIntentService extends IntentService {

    public VersionIntentService() {
        super("VersionIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<AppVersion> queue = api.getAppVersion(BuildConfig.VERSION_CODE, "parent");
        queue.enqueue(new Callback<AppVersion>() {
            @Override
            public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
                if(response.isSuccessful()) {
                    SharedPreferenceUtil.saveAppVersion(App.getInstance(), response.body());
                }
            }

            @Override
            public void onFailure(Call<AppVersion> call, Throwable t) {
            }
        });
    }

}
