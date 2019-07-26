package com.android.chalkboard.school.service;

import android.util.Log;

import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.network.RetrofitClient;
import com.android.chalkboard.school.interactor.AddSchoolInteractor;
import com.android.chalkboard.school.model.AddSchoolRequest;
import com.android.chalkboard.school.model.AddSchoolResponse;
import com.android.chalkboard.school.model.SchoolListResponse;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;


public class AddSchoolServiceClass implements AddSchoolInteractor, Callback {
    private NetworkListener mListener;
    private AddSchoolServiceInterface addSchoolServiceInterface;

    @Override
    public void createAddSchoolCall(Object request, NetworkListener listener) {
        mListener = listener;
        addSchoolServiceInterface = new RetrofitClient().getClient().create(AddSchoolServiceInterface.class);
        Call<SchoolListResponse> schoolResponseCall = addSchoolServiceInterface.createSchool((AddSchoolRequest) request);
        schoolResponseCall.enqueue(new Callback<SchoolListResponse>() {
            @Override
            public void onResponse(Call<SchoolListResponse> call, Response<SchoolListResponse> response) {
                mListener.onSuccess(response);
            }

            @Override
            public void onFailure(Call<SchoolListResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void uploadImage(String url, RequestBody bodyPart, NetworkListener listener) {
        mListener = listener;
        addSchoolServiceInterface = new RetrofitClient().getClientWithoutAuthorization().create(AddSchoolServiceInterface.class);
        Call<Void> uploadImageCall = addSchoolServiceInterface.uploadImage(url,bodyPart);
        uploadImageCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mListener.onSuccess(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.v("Image Upload fail", call.toString());
            }
        });
    }


    @Override
    public void onResponse(Call call, Response response) {

    }

    @Override
    public void onFailure(Call call, Throwable t) {

    }
}
