package com.aanglearning.parentapp.login;

import com.aanglearning.parentapp.model.Credentials;

/**
 * Created by Vinay on 16-02-2017.
 */

public interface LoginView {

    void showProgress();

    void hideProgress();

    void setError();

    void showAPIError(String message);

    void pwdRecovered();

    void noUser();

    void saveUser(Credentials credentials);

    void navigateToDashboard();
}
