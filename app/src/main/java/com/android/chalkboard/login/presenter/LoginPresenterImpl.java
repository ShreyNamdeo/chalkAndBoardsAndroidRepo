package com.android.chalkboard.login.presenter;

import com.android.chalkboard.login.interactor.LoginInteractor;
import com.android.chalkboard.login.modal.LoginResponse;
import com.android.chalkboard.login.service.LoginServiceClass;
import com.android.chalkboard.login.view.LoginActivity;
import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.util.SharedPrefUtils;

import org.json.JSONObject;

import retrofit2.Response;

public class LoginPresenterImpl implements LoginContract.LoginPresenter {

    private LoginInteractor loginInteractor;
    private LoginContract.LoginView mLoginView;

    public static void createPresenter(LoginContract.LoginView loginView){
        LoginPresenterImpl loginPresenter = new LoginPresenterImpl(loginView, new LoginServiceClass());
        loginView.bindPresenter(loginPresenter);
    }

    public LoginPresenterImpl(LoginContract.LoginView loginView, LoginServiceClass loginServiceClass){
        mLoginView = loginView;
        loginInteractor = loginServiceClass;
    }

    @Override
    public void loginUser(Object request, final boolean challengeOtp) {
        mLoginView.showSpinner();
        loginInteractor.createLoginCall(request, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                int code = ((Response)success).code();
                mLoginView.hideSpinner();
                if (code == 200) {
                    if (!challengeOtp) {
                        mLoginView.navigateToDashBoard(((Response) success).body());
                    } else {
                            mLoginView.createValidateOTPCall();
                    }
                } else {
                    try {
                        String response = ((Response) success).errorBody().string();
                        JSONObject obj = new JSONObject(response);
                        onFailure(obj.getString("message"));
                    } catch(Exception e){
                        mLoginView.showError(e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                mLoginView.hideSpinner();
                mLoginView.showError(errorMessage);
            }
        });
    }
}
