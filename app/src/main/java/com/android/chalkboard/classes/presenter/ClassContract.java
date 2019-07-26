package com.android.chalkboard.classes.presenter;

import com.android.chalkboard.classes.model.Class;

public interface ClassContract {

    public interface View {

        public void showError(String errorMessage);

        public void showSpinner();

        public void hideSpinner();

        public void bindPresenter(ClassPresenterImpl presenter);

        public void onSuccess(Object message);
    }

    public interface ClassPresenter{

        void createNewClass(Class body);

        void getAllClasses();

        void deleteClass(int classId);

        void updateClass(Class body);
    }
}
