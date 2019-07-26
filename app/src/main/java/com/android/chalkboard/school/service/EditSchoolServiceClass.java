package com.android.chalkboard.school.service;

import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.network.RetrofitClient;
import com.android.chalkboard.school.interactor.EditSchoolInteractor;
import com.android.chalkboard.school.model.EditSchoolRequest;
import com.android.chalkboard.school.model.EditSchoolResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditSchoolServiceClass implements EditSchoolInteractor, Callback {
    private NetworkListener mListener;
    private EditSchoolServiceInterface editSchoolServiceInterface;
    @Override
    public void createEditSchoolCall(final Object request, NetworkListener listener) {
        mListener = listener;
        editSchoolServiceInterface = new  RetrofitClient().getClient().create(EditSchoolServiceInterface.class);
        Call<EditSchoolResponse>  editSchoolResponseCall = editSchoolServiceInterface.editInstitute((EditSchoolRequest)request);
        editSchoolResponseCall.enqueue(new Callback<EditSchoolResponse>() {
            @Override
            public void onResponse(Call<EditSchoolResponse> call, Response<EditSchoolResponse> response) {
                mListener.onSuccess(response);
            }

            @Override
            public void onFailure(Call<EditSchoolResponse> call, Throwable t) {

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
