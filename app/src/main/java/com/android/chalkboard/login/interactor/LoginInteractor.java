package com.android.chalkboard.login.interactor;

import com.android.chalkboard.network.NetworkListener;

public interface LoginInteractor {

   void createLoginCall(Object request, NetworkListener listener);

   /*void createLoginWithOTP(UserRequestOTP requestOTP, NetworkListener listener);

   void createLoginValidateOTP(ValidateOTP validateOTP, NetworkListener listener);

   void createLoginWithPassword(UserRequest request, NetworkListener listener);*/
}
