package com.android.chalkboard.studentRequest.service;

import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.network.RetrofitClient;
import com.android.chalkboard.studentRequest.interactor.StudentRequestInteractor;
import com.android.chalkboard.studentRequest.model.StudentRequestResponse;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentRequestServiceClass implements StudentRequestInteractor {

    @Override
    public void getStudentRequest(int classId, final NetworkListener listener){
        StudentRequestServiceInterface serviceInterface = new RetrofitClient().getClient().create(StudentRequestServiceInterface.class);
        serviceInterface.getStudentRequest(classId).enqueue(new Callback<ArrayList<StudentRequestResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<StudentRequestResponse>> call, Response<ArrayList<StudentRequestResponse>> response) {
                try {
                    if (response.code() == 200)
                        listener.onSuccess(response.body());
                    else {
                        JSONObject object = new JSONObject(response.errorBody().string());
                        listener.onFailure(object.getString("message"));
                    }
                } catch(Exception e){
                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StudentRequestResponse>> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void acceptStudentRequest(int classId, int requestId,final NetworkListener listener){
        StudentRequestServiceInterface serviceInterface = new RetrofitClient().getClient().create(StudentRequestServiceInterface.class);
        serviceInterface.acceptStudentRequest(classId, requestId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                try {
                    if (response.code() == 200)
                        listener.onSuccess("Request has been accepted.");
                    else {
                        JSONObject object = new JSONObject(response.errorBody().string());
                        listener.onFailure(object.getString("message"));
                    }
                } catch(Exception e){
                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void rejectStudentRequest(int classId, int requestId,final NetworkListener listener){
        StudentRequestServiceInterface serviceInterface = new RetrofitClient().getClient().create(StudentRequestServiceInterface.class);
        serviceInterface.acceptStudentRequest(classId, requestId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                try {
                    if (response.code() == 200)
                        listener.onSuccess("Request has been rejected.");
                    else {
                        JSONObject object = new JSONObject(response.errorBody().string());
                        listener.onFailure(object.getString("message"));
                    }
                } catch(Exception e){
                    listener.onFailure(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
