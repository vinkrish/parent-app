package com.aanglearning.parentapp.login;

import com.aanglearning.parentapp.api.APIError;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.AuthApi;
import com.aanglearning.parentapp.api.ErrorUtils;
import com.aanglearning.parentapp.model.CommonResponse;
import com.aanglearning.parentapp.model.Credentials;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 16-02-2017.
 */

class LoginInteractorImpl implements LoginInteractor {
    @Override
    public void login(Credentials credentials, final OnLoginFinishedListener listener) {

        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);

        Call<Credentials> login = authApi.login(credentials);
        login.enqueue(new Callback<Credentials>() {
            @Override
            public void onResponse(Call<Credentials> call, Response<Credentials> response) {
                if(response.isSuccessful()) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onAPIError("Mobile number and password don't match");
                }
            }

            @Override
            public void onFailure(Call<Credentials> call, Throwable t) {
                listener.onError();
            }
        });
    }

    @Override
    public void recoverPwd(String authToken, String newPassword, final OnLoginFinishedListener listener) {
        AuthApi authApi = ApiClient.getAuthorizedClient(authToken).create(AuthApi.class);

        Call<CommonResponse> sendNewPwd = authApi.newPassword(newPassword);
        sendNewPwd.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.isSuccessful()) {
                    if(response.body().isSuccess()){
                        listener.onPwdRecovered();
                    } else {
                        listener.onNoUser();
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    listener.onAPIError(error.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                listener.onError();
            }
        });
    }
}
