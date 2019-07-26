package com.android.chalkboard.register.presenter;

import com.android.chalkboard.register.model.User;

public class RegistrationContract {

    public interface RegistrationPresenter {
        void registerUser(User userData);
        void getJWTToken(String mobilenumber, String role);
    }

    public interface View {
        void bindPresenter(RegistrationPresenter registrationPresenter);
        void showSpinner();
        void hideSpinner();
        void showOTPView();
        void showError(String errorMessage);
        void saveToken(String token);
    }
}
