package com.android.chalkboard.school.interactor;

import com.android.chalkboard.network.NetworkListener;

import okhttp3.RequestBody;

public interface AddSchoolInteractor {
    void createAddSchoolCall(Object request, NetworkListener listener);

    void uploadImage(String url, RequestBody image, NetworkListener listener);

}
