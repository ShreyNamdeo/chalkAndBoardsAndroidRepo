package com.android.chalkboard.school.presenter;

import com.android.chalkboard.school.model.AddSchoolRequest;
import com.android.chalkboard.school.model.AddSchoolResponse;
import com.android.chalkboard.school.model.SchoolListResponse;

import okhttp3.RequestBody;

public class AddSchoolContract {

    public interface AddSchoolView{
        void bindPresenter(AddSchoolPresenter addSchoolPresenter);
        void showSpinner();
        void closeSchoolForm();
        void hideSpinner();
        void showError();
        void sendImage(SchoolListResponse response);
        void updateSchoolListCache(Object object);

    }
    public interface AddSchoolPresenter{
        void addSchool(AddSchoolRequest addSchoolRequest);
        void sendImageByte(RequestBody body, String writeUrl);

    }
}
