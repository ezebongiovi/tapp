package com.testableapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import com.testableapp.R;
import com.testableapp.dto.User;
import com.testableapp.manager.AuthenticationManager;
import com.testableapp.presenters.LoginPresenter;
import com.testableapp.views.LoginView;

public class LoginActivity extends AbstractMvpActivity<LoginPresenter> implements LoginView {

    @Override
    protected boolean shouldAuthenticate() {
        return false;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                          @NonNull final LoginPresenter presenter) {

        final EditText mUserField = (EditText) findViewById(R.id.userField);
        final EditText mPasswordField = (EditText) findViewById(R.id.passwordField);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                presenter.login(mUserField.getText().toString(),
                        mPasswordField.getText().toString());
            }
        });

        findViewById(R.id.noAccountButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        if (AuthenticationManager.getInstance().getUser(this) != null) {
            startActivity(new Intent(this, NavigationActivity.class));
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @NonNull
    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onLogin(@NonNull final User user) {
        AuthenticationManager.getInstance().onLogin(this, user);
        startActivity(new Intent(this, NavigationActivity.class));
    }

    @Override
    public void onError() {
        Snackbar.make(findViewById(R.id.rootView), "Ups algo salió mal", Snackbar.LENGTH_LONG).show();
    }
}
