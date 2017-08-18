package com.aanglearning.parentapp.login;

import com.aanglearning.parentapp.App;
import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.api.APIError;
import com.aanglearning.parentapp.api.ApiClient;
import com.aanglearning.parentapp.api.AuthApi;
import com.aanglearning.parentapp.api.ErrorUtils;
import com.aanglearning.parentapp.model.CommonResponse;
import com.aanglearning.parentapp.model.Credentials;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vinay on 16-02-2017.
 */

class LoginInteractorImpl implements LoginInteractor {
    @Override
    public void login(final Credentials credentials, final OnLoginFinishedListener listener) {

        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);

        Call<Credentials> login = authApi.login(credentials);
        login.enqueue(new Callback<Credentials>() {
            @Override
            public void onResponse(Call<Credentials> call, Response<Credentials> response) {
                if(response.isSuccessful()) {
                    SharedPreferenceUtil.saveAuthorizedUser(App.getInstance(), credentials.getMobileNo());
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("mobile number and password don't match");
                }
            }

            @Override
            public void onFailure(Call<Credentials> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }

    @Override
    public void recoverPwd(String username, final OnLoginFinishedListener listener) {
        AuthApi authApi = ApiClient.getClient().create(AuthApi.class);

        Call<Void> sendNewPwd = authApi.newPassword(username);
        sendNewPwd.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    listener.onPwdRecovered();
                } else {
                    //APIError error = ErrorUtils.parseError(response);
                    //listener.onAPIError(error.getMessage());
                    listener.onError(App.getInstance().getString(R.string.request_error));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onError(App.getInstance().getString(R.string.network_error));
            }
        });
    }
}
