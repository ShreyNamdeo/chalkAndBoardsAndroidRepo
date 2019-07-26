package com.android.chalkboard.teacherrequest.service;

import com.android.chalkboard.teacherrequest.interactor.TeacherRequestInteractor;
import com.android.chalkboard.teacherrequest.modal.TeacherRequest;
import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeachersRequestServiceClass implements TeacherRequestInteractor {

    @Override
    public void getAllTeacherRequest(int institutionId, final NetworkListener listener) {
        TeachersRequestServiceInterface serviceInterface = new RetrofitClient().getClient().create(TeachersRequestServiceInterface.class);
        serviceInterface.getTeacherRequest(institutionId).enqueue(new retrofit2.Callback<List<TeacherRequest>>() {
            @Override
            public void onResponse(Call<List<TeacherRequest>> call, Response<List<TeacherRequest>> response) {

            }

            @Override
            public void onFailure(Call<List<TeacherRequest>> call, Throwable t) {

            }
        });

    }

    @Override
    public void rejectTeacherRequest(int instituteId, int requestId, NetworkListener listener) {
        TeachersRequestServiceInterface serviceInterface = new RetrofitClient().getClient().create(TeachersRequestServiceInterface.class);
        serviceInterface.rejectTeacherRequest(instituteId,requestId).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    @Override
    public void acceptTeacherRequest(int instituteId, int requestId, NetworkListener listener) {
        TeachersRequestServiceInterface serviceInterface = new RetrofitClient().getClient().create(TeachersRequestServiceInterface.class);
        serviceInterface.acceptTeacherRequest(instituteId, requestId).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    @Override
    public void deleteTeacherRequest(int instituteId, NetworkListener listener) {
        TeachersRequestServiceInterface serviceInterface = new RetrofitClient().getClient().create(TeachersRequestServiceInterface.class);
        serviceInterface.deleteTeacherRequest(instituteId).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    }

    @Override
    public void getTeacherDetails(String id, NetworkListener listener) {

    }

}
