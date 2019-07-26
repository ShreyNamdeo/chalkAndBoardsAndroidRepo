package com.android.chalkboard.network;

public interface NetworkListener {

    void onSuccess(Object success);
    void onFailure(String errorMessage);
}
