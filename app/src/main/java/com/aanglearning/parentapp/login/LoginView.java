package com.aanglearning.parentapp.login;

import com.aanglearning.parentapp.model.Credentials;

/**
 * Created by Vinay on 16-02-2017.
 */

interface LoginView {

    void showProgress();

    void hideProgress();

    void setError(String message);

    void pwdRecovered();

    void noUser();

    void saveUser(Credentials credentials);

    void navigateToDashboard();
}
