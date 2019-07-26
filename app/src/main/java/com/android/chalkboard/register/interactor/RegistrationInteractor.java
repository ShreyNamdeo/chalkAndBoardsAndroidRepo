package com.android.chalkboard.register.interactor;

import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.register.model.User;
import com.android.chalkboard.register.service.RegisterServiceClass;

public interface RegistrationInteractor  {
    void createRegisterCall(User user, NetworkListener listener);

    void getJWTToken(String mobilenumber, String role, NetworkListener listener);
}
