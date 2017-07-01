package com.aanglearning.parentapp.login;

import com.aanglearning.parentapp.model.Credentials;

/**
 * Created by Vinay on 16-02-2017.
 */

interface LoginInteractor {
    interface OnLoginFinishedListener{

        void onSuccess(Credentials credentials);

        void onPwdRecovered();

        void onNoUser();

        void onError(String message);
    }

    void login(Credentials credentials, OnLoginFinishedListener listener);

    void recoverPwd(String username, OnLoginFinishedListener listener);
}
