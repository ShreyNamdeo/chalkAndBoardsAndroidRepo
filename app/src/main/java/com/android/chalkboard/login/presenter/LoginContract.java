package com.android.chalkboard.login.presenter;

public class LoginContract {

    public interface LoginView{
        void bindPresenter(LoginPresenter presenter);
        void showSpinner();
        void hideSpinner();
        void navigateToDashBoard(Object success);
        void showError(String msg);
        void createValidateOTPCall();
    }

    public interface LoginPresenter{
        void loginUser(Object request, boolean challengeOtp);

    }
}
