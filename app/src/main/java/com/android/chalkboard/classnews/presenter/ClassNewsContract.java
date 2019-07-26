package com.android.chalkboard.classnews.presenter;

import com.android.chalkboard.classes.model.Class;
import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.classnews.model.CreateClassNewsRequest;
import com.android.chalkboard.classnews.model.EditClassNewsRequest;

import okhttp3.RequestBody;

public interface ClassNewsContract {

    public interface View{
        void showError(String message);
        void showSpinner();
        void hideSpinner();
        void bindPresenter(ClassNewsPresenterImpl impl);
        void onSuccess(Object response, String method, ClassResponse classResponse);
        void onDeleteSuccess(String response);
        void onSuccess(Object response);
    }

    public interface ClassNewsPresenter{
        void createClassNews(int classId, CreateClassNewsRequest request, ClassResponse classResponse);
        void getClassNews(int classId, ClassResponse classResponse);
        void deleteClassNews(int classId, int newsId);
        void editClassNews(int classId, EditClassNewsRequest request);
        void sendImageByte(RequestBody body, String writeUrl);
    }
}
