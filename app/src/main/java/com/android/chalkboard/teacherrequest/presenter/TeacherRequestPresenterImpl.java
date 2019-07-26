package com.android.chalkboard.teacherrequest.presenter;

import com.android.chalkboard.teacherrequest.interactor.TeacherRequestInteractor;
import com.android.chalkboard.teacherrequest.service.TeachersRequestServiceClass;
import com.android.chalkboard.network.NetworkListener;

public class TeacherRequestPresenterImpl implements TeachersRequestContract.Presenter {

    private TeachersRequestContract.View mView;
    private TeacherRequestInteractor mInteractor;

    private TeacherRequestPresenterImpl(TeachersRequestContract.View view, TeacherRequestInteractor teacherRequestInteractor){
        mView = view;
        mInteractor = teacherRequestInteractor;
    }

    public static void createPresenter(TeachersRequestContract.View view){
        TeacherRequestPresenterImpl teacherRequestPresenter = new TeacherRequestPresenterImpl(view, new TeachersRequestServiceClass());
        view.bindPresenter(teacherRequestPresenter);
    }


    @Override
    public void getTeacherRequest(int id) {
        mInteractor.getAllTeacherRequest(id, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    @Override
    public void acceptTeacher(int instituteId, int reqId) {
        mInteractor.acceptTeacherRequest(instituteId, reqId, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    @Override
    public void rejectTeacher(int instituteId, int reqId) {
        mInteractor.rejectTeacherRequest(instituteId, reqId, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });

    }

    @Override
    public void deleteTeacherRequest(int instituteId) {
        mInteractor.deleteTeacherRequest(instituteId, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    }

    @Override
    public void getTeacherDetails(String id) {
        mInteractor.getTeacherDetails(id, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        }) ;
    }
}
