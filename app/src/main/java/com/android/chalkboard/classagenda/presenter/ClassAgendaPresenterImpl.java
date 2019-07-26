package com.android.chalkboard.classagenda.presenter;

import com.android.chalkboard.classagenda.model.CreateAgendaRequest;
import com.android.chalkboard.classagenda.service.ClassAgendaServiceClass;
import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.network.NetworkListener;

public class ClassAgendaPresenterImpl implements ClassAgendaContract.Presenter {

    ClassAgendaContract.View view;
    ClassAgendaServiceClass serviceClass;

    private ClassAgendaPresenterImpl(ClassAgendaContract.View view, ClassAgendaServiceClass serviceClass){
        this.view = view;
        this.serviceClass = serviceClass;
    }

    public static void CreatePresenter(ClassAgendaContract.View view){
        ClassAgendaPresenterImpl impl = new ClassAgendaPresenterImpl(view, new ClassAgendaServiceClass());
        view.bindPresenter(impl);
    }

    @Override
    public void createAgenda(int classId, CreateAgendaRequest request){
        view.showSpinner();
        serviceClass.createAgenda(classId, request, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                view.hideSpinner();
                view.onCreateSuccess(success);
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideSpinner();
                view.showError(errorMessage);
            }
        });
    }

    @Override
    public void getAgenda(int classId, final ClassResponse classResponse){
        view.showSpinner();
        serviceClass.getAgenda(classId, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                view.hideSpinner();
                view.onGetSuccess(success, classResponse);
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideSpinner();
                view.showError(errorMessage);
            }
        });
    }

    @Override
    public void updateAgenda(int classId, CreateAgendaRequest request){
        view.hideSpinner();
        serviceClass.updateAgenda(classId, request, new NetworkListener() {
            @Override
            public void onSuccess(Object success) {
                view.hideSpinner();
                view.onUpdateSuccess(success);
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideSpinner();
                view.showError(errorMessage);
            }
        });
    }
}
