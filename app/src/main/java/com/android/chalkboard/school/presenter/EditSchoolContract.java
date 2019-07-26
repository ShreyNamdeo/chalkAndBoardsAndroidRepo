package com.android.chalkboard.school.presenter;

import com.android.chalkboard.school.model.AddSchoolRequest;
import com.android.chalkboard.school.model.AddSchoolResponse;
import com.android.chalkboard.school.model.EditSchoolRequest;

public class EditSchoolContract {
    public interface EditSchoolView{
        void bindPresenter(EditSchoolContract.EditSchoolPresenter addSchoolPresenter);
        void showSpinner();
        void closeSchoolForm();
        void hideSpinner();
        void showError();

    }
    public interface EditSchoolPresenter{
        void editSchool(EditSchoolRequest editSchoolRequest);
        //void sendImageByte(byte[] image, String writeUrl);

    }
}
