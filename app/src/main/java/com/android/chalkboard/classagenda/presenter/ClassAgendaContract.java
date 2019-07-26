package com.android.chalkboard.classagenda.presenter;

import com.android.chalkboard.classagenda.model.CreateAgendaRequest;
import com.android.chalkboard.classes.model.ClassResponse;

public interface ClassAgendaContract {

    public interface View{

        void showError(String errorMessage);

        void onCreateSuccess(Object response);

        void onUpdateSuccess(Object response);

        void onGetSuccess(Object response, ClassResponse classResponse);

        void bindPresenter(ClassAgendaPresenterImpl impl);

        void showSpinner();

        void hideSpinner();

    }

    public interface Presenter{
        void createAgenda(int classId, CreateAgendaRequest request);

        void getAgenda(int classId, ClassResponse response);

        void updateAgenda(int classId, CreateAgendaRequest request);
    }
}
