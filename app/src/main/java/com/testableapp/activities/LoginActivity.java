package com.testableapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.testableapp.R;
import com.testableapp.dto.Authentication;
import com.testableapp.dto.User;
import com.testableapp.manager.AuthenticationManager;
import com.testableapp.presenters.LoginPresenter;
import com.testableapp.views.LoginView;

public class LoginActivity extends AbstractMvpActivity<LoginPresenter> implements LoginView {

    private CallbackManager mCallbackManager;

    @Override
    protected boolean shouldAuthenticate() {
        return false;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 @NonNull final LoginPresenter presenter) {

        if (AuthenticationManager.getInstance().isAuthenticated(LoginActivity.this)) {
            startActivity(new Intent(this, NavigationActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
            return;
        }

        mCallbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = findViewById(R.id.button_login_facebook);
        loginButton.setReadPermissions(getResources().getStringArray(R.array.facebook_permissions));
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                presenter.login(new Authentication.Builder()
                        .withAccessToken(loginResult.getAccessToken().getToken())
                        .withProviderName(Authentication.PROVIDER_FACEBOOK).build());
            }

            @Override
            public void onCancel() {
                // Nothing to do
            }

            @Override
            public void onError(final FacebookException exception) {
                // TODO: Track error event
                exception.printStackTrace();
            }
        });
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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
    public void showProgressLayout() {
        findViewById(R.id.progressView).setVisibility(View.VISIBLE);
    }

    @Override
    public void showRegularLayout() {
        findViewById(R.id.progressView).setVisibility(View.GONE);
    }

    @Override
    public void onNetworkError() {
        super.onNetworkError();
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onGenericError() {
        super.onGenericError();
        LoginManager.getInstance().logOut();
    }
}
