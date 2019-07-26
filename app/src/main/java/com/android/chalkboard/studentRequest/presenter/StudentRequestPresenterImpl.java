package com.android.chalkboard.studentRequest.presenter;

import com.android.chalkboard.network.NetworkListener;
import com.android.chalkboard.studentRequest.model.StudentRequestResponse;
import com.android.chalkboard.studentRequest.service.StudentRequestServiceClass;

import java.util.ArrayList;

public class StudentRequestPresenterImpl implements StudentRequestContract.StudentRequestPresenter {

    private StudentRequestContract.View mView;
    private StudentRequestServiceClass serviceClass;

    public StudentRequestPresenterImpl(StudentRequestContract.View mView, StudentRequestServiceClass serviceClass){
        this.mView = mView;
        this.serviceClass = serviceClass;
    }

    public static void createPresenter(StudentRequestContract.View view){
        StudentRequestPresenterImpl studentRequestPresenter = new StudentRequestPresenterImpl(view, new StudentRequestServiceClass());
        view.bindPresenter(studentRequestPresenter);
    }

    @Override
    public void getStudentRequest(final String className, int classId){
        mView.showSpinner();
        serviceClass.getStudentRequest(classId, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                ArrayList<StudentRequestResponse> responses = (ArrayList<StudentRequestResponse>)success;
                for(int i =0; i < responses.size(); i++)
                    responses.get(i).setClassName(className);
                mView.onSuccess(responses);
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.showError(errorMessage);
            }
        });
    }

    @Override
    public void acceptStudentRequest(int classId, int requestId){
        mView.showSpinner();
        serviceClass.acceptStudentRequest(classId, requestId, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.onResponse(success);
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.showError(errorMessage);
            }
        });
    }

    @Override
    public void rejectStudentRequest(int classId, int requestId){
        mView.showSpinner();
        serviceClass.acceptStudentRequest(classId, requestId, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.onResponse(success);
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.showError(errorMessage);
            }
        });
    }
}
