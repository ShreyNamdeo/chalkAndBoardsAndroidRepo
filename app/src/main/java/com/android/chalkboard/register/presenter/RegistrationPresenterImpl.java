package com.android.chalkboard.register.presenter;

import android.widget.Toast;

import com.android.chalkboard.register.model.jwt;
import com.android.chalkboard.register.interactor.RegistrationInteractor;
import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.register.model.User;
import com.android.chalkboard.register.service.RegisterServiceClass;

import org.json.JSONObject;

import retrofit2.Response;

public class RegistrationPresenterImpl implements com.android.chalkboard.register.presenter.RegistrationContract.RegistrationPresenter {

    private RegistrationContract.View view;
    private RegistrationInteractor registrationInteractor;

    public static void createPresenter(RegistrationContract.View registrationView) {
        RegistrationPresenterImpl registrationPresenter = new RegistrationPresenterImpl(registrationView, new RegisterServiceClass());
        registrationView.bindPresenter(registrationPresenter);

    }

    public RegistrationPresenterImpl(RegistrationContract.View view, RegistrationInteractor registrationInteractor){
                this.view = view;
                this.registrationInteractor = registrationInteractor;
    }

    @Override
    public void registerUser(User userData) {
        view.showSpinner();
        registrationInteractor.createRegisterCall(userData, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                try {
                    int code = ((Response) success).code();
                    view.hideSpinner();
                    if (code == 201) {
                        view.showOTPView();
                    } else {
                        String response = ((Response) success).errorBody().string();
                        JSONObject obj = new JSONObject(response);
                        onFailure(obj.getString("message"));
                    }
                } catch(Exception e){
                    view.showError(e.getMessage());
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                //Handle service error
                view.hideSpinner();
                view.showError(errorMessage);

            }
        });
    }

    @Override
    public void getJWTToken(String mobilenumber, String role){
        view.showSpinner();
        registrationInteractor.getJWTToken(mobilenumber, role, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                try{
                    int code = ((Response) success).code();
                    view.hideSpinner();
                    if (code == 200) {
                        String jwt = ((jwt)((Response) success).body()).getMessage();
                        view.saveToken(jwt);
                    } else {
                        String response = ((Response) success).errorBody().string();
                        JSONObject obj = new JSONObject(response);
                        onFailure(obj.getString("message"));
                    }
                } catch(Exception e){
                    view.showError(e.getMessage());
                }
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }
}
