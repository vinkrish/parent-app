package com.aanglearning.parentapp.dashboard;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.AuthApi;
import com.aanglearning.parentapp.api.ParentApi;
import com.aanglearning.parentapp.model.Authorization;
import com.aanglearning.parentapp.model.Groups;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 02-04-2017.
 */

class GroupInteractorImpl implements GroupInteractor {
    @Override
    public void getGroups(long userId, final OnFinishedListener listener) {
        ParentApi api = ApiClient.getAuthorizedClient().create(ParentApi.class);

        Call<List<Groups>> classList = api.getGroups(userId);
        classList.enqueue(new Callback<List<Groups>>() {
            @Override
            public void onResponse(Call<List<Groups>> call, Response<List<Groups>> response) {
                if(response.isSuccessful()) {
                    listener.onGroupsReceived(response.body());
                } else {
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<List<Groups>> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void updateFcmToken(Authorization authorization) {
        AuthApi api = ApiClient.getAuthorizedClient().create(AuthApi.class);

        Call<Void> classList = api.updateFcmToken(authorization);
        classList.enqueue(new Callback<Void>() {
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
