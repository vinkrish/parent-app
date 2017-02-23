package com.aanglearning.parentapp.login;

import com.aanglearning.parentapp.model.Credentials;

/**
 * Created by Vinay on 16-02-2017.
 */

class LoginPresenterImpl implements LoginPresenter, LoginInteractor.OnLoginFinishedListener  {

    private LoginView loginView;
    private LoginInteractor interactor;

    LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.interactor = new LoginInteractorImpl();
    }

    @Override
    public void validateCredentials(Credentials credentials) {
        if(loginView != null) {
            loginView.showProgress();
            interactor.login(credentials, this);
        }
    }

    @Override
    public void pwdRecovery(String newPassword) {
        if(loginView != null) {
            loginView.showProgress();
            interactor.recoverPwd(newPassword, this);
        }
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void onSuccess(Credentials credentials) {
        if(loginView != null) {
            loginView.saveUser(credentials);
            loginView.hideProgress();
            loginView.navigateToDashboard();
        }
    }

    @Override
    public void onPwdRecovered() {
        if(loginView != null) {
            loginView.hideProgress();
            loginView.pwdRecovered();
        }
    }

    @Override
    public void onNoUser() {
        if(loginView != null) {
            loginView.hideProgress();
            loginView.noUser();
        }
    }

    @Override
    public void onError() {
        if(loginView != null) {
            loginView.hideProgress();
            loginView.setError();
        }
    }

    @Override
    public void onAPIError(String message) {
        if(loginView != null) {
            loginView.hideProgress();
            loginView.showAPIError(message);
        }
    }
}
