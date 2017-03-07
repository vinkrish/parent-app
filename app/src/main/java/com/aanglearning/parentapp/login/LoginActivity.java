package com.aanglearning.parentapp.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.aanglearning.parentapp.R;
import com.aanglearning.parentapp.dao.ChildInfoDao;
import com.aanglearning.parentapp.dashboard.DashboardActivity;
import com.aanglearning.parentapp.model.Credentials;
import com.aanglearning.parentapp.util.AppGlobal;
import com.aanglearning.parentapp.util.EditTextWatcher;
import com.aanglearning.parentapp.util.SharedPreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginView {
    @BindView(R.id.mobile_et) EditText mobile;
    @BindView(R.id.password_et) EditText password;
    @BindView(R.id.mobile) TextInputLayout mobileLayout;
    @BindView(R.id.password) TextInputLayout passwordLayout;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        AppGlobal.setSqlDbHelper(getApplicationContext());

        mobile.addTextChangedListener(new EditTextWatcher(mobileLayout));
        password.addTextChangedListener(new EditTextWatcher(passwordLayout));
        presenter = new LoginPresenterImpl(this);
    }

    private void showSnackbar(String message) {
        Snackbar.make(coordinatorLayout, message, 3000).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setError() {
        showSnackbar(getString(R.string.request_error));
    }

    @Override
    public void showAPIError(String message) {
        showSnackbar(message);
    }

    @Override
    public void pwdRecovered() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Notification");
        alertDialog.setMessage("New password has been sent to your mobile number");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void noUser() {
        showSnackbar(getString(R.string.no_user));
    }

    @Override
    public void saveUser(Credentials credentials) {
        ChildInfoDao.clear();
        ChildInfoDao.insert(credentials.getInfo());
        SharedPreferenceUtil.saveUser(this, credentials);
    }

    @Override
    public void navigateToDashboard() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

    public void login(View view) {
        if(validate()){
            Credentials c = new Credentials();
            c.setMobileNo(mobile.getText().toString());
            c.setPassword(password.getText().toString());
            presenter.validateCredentials(c);
        }
    }

    public void forgotPassword(View view){
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if(mobile.getText().toString().length() != 10) {
            mobileLayout.setError(getString(R.string.valid_mobile));
        } else {
            presenter.pwdRecovery(mobile.getText().toString());
        }
    }

    public boolean validate(){
        if(mobile.getText().toString().isEmpty()){
            mobileLayout.setError(getString(R.string.password_error));
            return false;
        } else if (mobile.getText().toString().length() != 10) {
            mobileLayout.setError(getString(R.string.valid_mobile));
            return false;
        }else if (password.getText().toString().isEmpty() ||
                password.getText().toString().length() < 6) {
            passwordLayout.setError(getString(R.string.valid_password));
            return false;
        }
        return true;
    }
}
