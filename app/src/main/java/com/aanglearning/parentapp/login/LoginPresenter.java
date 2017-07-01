package com.aanglearning.parentapp.login;

import com.aanglearning.parentapp.model.Credentials;

/**
 * Created by Vinay on 16-02-2017.
 */

interface LoginPresenter {
    void validateCredentials(Credentials credentials);

    void pwdRecovery(String username);

    void onDestroy();
}
