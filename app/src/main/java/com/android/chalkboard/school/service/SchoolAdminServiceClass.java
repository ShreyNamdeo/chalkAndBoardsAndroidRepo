package com.android.chalkboard.school.service;

import com.android.chalkboard.dashboard.model.SimpleResponseMessage;
import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.network.RetrofitClient;
import com.android.chalkboard.school.interactor.SchoolAdminInteractor;
import com.android.chalkboard.school.model.SchoolListResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class SchoolAdminServiceClass implements SchoolAdminInteractor,Callback {

    private NetworkListener mListener;
    private ShoolAdminServiceInterface adminServiceInterface;


    @Override
    public void getSchoolListCall(NetworkListener listener) {
        mListener = listener;
        adminServiceInterface = new RetrofitClient().getClient().create(ShoolAdminServiceInterface.class);
        Call<ArrayList<SchoolListResponse>> schoolListResponseCall = adminServiceInterface.getSchoolList();
        schoolListResponseCall.enqueue(this);
    }

    @Override
    public void getRequestedSchoolListCall(String teacherId, NetworkListener listener) {
        mListener = listener;
        adminServiceInterface = new RetrofitClient().getClient().create(ShoolAdminServiceInterface.class);
        Call<ArrayList<SchoolListResponse>> schoolListResponseCall = adminServiceInterface.getRequestedInstitute(teacherId);
        schoolListResponseCall.enqueue(this);
    }

    @Override
    public void deleteTeacherRequestedInstitute(String instituteId, NetworkListener listener){
        mListener = listener;
        adminServiceInterface = new RetrofitClient().getClient().create(ShoolAdminServiceInterface.class);
        Call<SimpleResponseMessage> deleteTeacherInstituteRequested = adminServiceInterface.deleteTeacherRequestedInstitute(instituteId);
        deleteTeacherInstituteRequested.enqueue(this);
    }

    @Override
    public void deleteSchoolCall(String institutionId, NetworkListener networkListener) {
        mListener = networkListener;
        adminServiceInterface = new RetrofitClient().getClient().create(ShoolAdminServiceInterface.class);
        Call<Void> deleteInstituteCall = adminServiceInterface.deleteInstitute(institutionId);
        deleteInstituteCall.enqueue(this);
    }


    @Override
    public void onResponse(Call call, retrofit2.Response response) {
        mListener.onSuccess(response);
    }

    @Override
    public void onFailure(Call call, Throwable t) {

    }
}
