package com.aanglearning.parentapp.login;

import com.aanglearning.parentapp.model.Credentials;

/**
 * Created by Vinay on 16-02-2017.
 */

public interface LoginInteractor {
    interface OnLoginFinishedListener{

        void onSuccess(Credentials credentials);

        void onPwdRecovered();

        void onNoUser();

        void onError();

        void onAPIError(String message);
    }

    void login(Credentials credentials, OnLoginFinishedListener listener);

    void recoverPwd(String newPassword, OnLoginFinishedListener listener);
}
