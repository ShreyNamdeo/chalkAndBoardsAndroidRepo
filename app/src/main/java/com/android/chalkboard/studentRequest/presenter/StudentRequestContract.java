package com.android.chalkboard.studentRequest.presenter;

import com.android.chalkboard.studentRequest.model.StudentRequestResponse;

import java.util.ArrayList;

public interface StudentRequestContract {

    public interface View{
        void showError(String errorMessage);
        void showSpinner();
        void hideSpinner();
        void onSuccess(ArrayList<StudentRequestResponse> response);
        void bindPresenter(StudentRequestPresenterImpl studentRequestPresenter);
        void onResponse(Object message);
    }

    public interface StudentRequestPresenter {
        void getStudentRequest(String className, int classId);
        void acceptStudentRequest(int classId, int requestId);
        void rejectStudentRequest(int classId, int requestId);
    }
}
