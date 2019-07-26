package com.android.chalkboard.classes.presenter;

import com.android.chalkboard.classes.model.Class;
import com.android.chalkboard.classes.service.ClassServiceClass;
import com.android.chalkboard.network.NetworkListener;

public class ClassPresenterImpl implements ClassContract.ClassPresenter {

    private ClassContract.View mView;
    private ClassServiceClass serviceClass;

    private ClassPresenterImpl(ClassContract.View mView, ClassServiceClass serviceClass){
        this.serviceClass = serviceClass;
        this.mView = mView;
    }

    public static void createPresenter(ClassContract.View view){
        ClassPresenterImpl classPresenter = new ClassPresenterImpl(view, new ClassServiceClass());
        view.bindPresenter(classPresenter);
    }

    @Override
    public void createNewClass(Class body){
        mView.showSpinner();
        serviceClass.createNewClass(body, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.onSuccess("New class " + ((Class)success).getcName() + " has been created.");
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
    public void getAllClasses(){
        mView.showSpinner();
        serviceClass.getAllClasses(new NetworkListener() {
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
    public void deleteClass(int classId){
        mView.showSpinner();
        serviceClass.deleteClass(classId, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.onSuccess(success);
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.showError(errorMessage);
            }
        });
    }

    @Override
    public void updateClass(Class body){
        mView.showSpinner();
        serviceClass.updateClass(body, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                mView.onSuccess("Class "+ ((Class)success).getcName() + " has been updated.");
                mView.hideSpinner();
            }

            @Override
            public void onFailure(String errorMessage) {
                mView.showError(errorMessage);
                mView.hideSpinner();
            }
        });
    }
}
