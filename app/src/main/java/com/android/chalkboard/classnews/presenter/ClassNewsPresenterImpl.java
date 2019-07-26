package com.android.chalkboard.classnews.presenter;

import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.classnews.model.CreateClassNewsRequest;
import com.android.chalkboard.classnews.model.EditClassNewsRequest;
import com.android.chalkboard.classnews.service.ClassNewsServiceClass;
import com.android.chalkboard.dashboard.model.SimpleResponseMessage;
import com.android.chalkboard.network.NetworkListener;

import okhttp3.RequestBody;

public class ClassNewsPresenterImpl implements ClassNewsContract.ClassNewsPresenter {

    private ClassNewsContract.View mView;
    private ClassNewsServiceClass serviceClass;

    public ClassNewsPresenterImpl(ClassNewsContract.View mView, ClassNewsServiceClass serviceClass){
        this.mView = mView;
        this.serviceClass = serviceClass;
    }

    public static void createPresenter(ClassNewsContract.View view){
        ClassNewsPresenterImpl classNewsPresenter = new ClassNewsPresenterImpl(view, new ClassNewsServiceClass());
        view.bindPresenter(classNewsPresenter);
    }

    @Override
    public void createClassNews(int classId, CreateClassNewsRequest request, final ClassResponse classResponse){
        mView.showSpinner();
        serviceClass.createClassNews(classId, request, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.onSuccess(success, "create", classResponse);
                mView.hideSpinner();
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.showError(errorMessage);
                mView.hideSpinner();
            }
        });
    }

    @Override
    public void deleteClassNews(int classId, int newsId){
        mView.showSpinner();
        serviceClass.deleteClassNews(classId, newsId, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.hideSpinner();
                String statusResponse = ((SimpleResponseMessage)success).getMessage();
                mView.onDeleteSuccess(statusResponse);
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.hideSpinner();
                mView.showError(errorMessage);
            }
        });
    }

    @Override
    public void getClassNews(int classId, final ClassResponse classResponse){
        mView.showSpinner();
        serviceClass.getClassNews(classId, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.onSuccess(success, "get", classResponse);
                mView.hideSpinner();
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.showError(errorMessage);
                mView.hideSpinner();
            }
        });
    }

    @Override
    public void editClassNews(int classId, EditClassNewsRequest request){
        mView.showSpinner();
        serviceClass.editClassNews(classId, request, new NetworkListener(){
            @Override
            public void onSuccess(Object success) {
                mView.onSuccess(success);
                mView.hideSpinner();
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.showError(errorMessage);
                mView.hideSpinner();
            }
        });
    }

    @Override
    public void sendImageByte(RequestBody body, String writeUrl) {
        serviceClass.uploadImage(writeUrl, body, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.hideSpinner();
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.hideSpinner();

            }
        });
    }
}
