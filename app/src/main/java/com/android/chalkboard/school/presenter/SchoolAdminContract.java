package com.android.chalkboard.school.presenter;

import com.android.chalkboard.school.model.SchoolListResponse;

import java.util.ArrayList;

public class SchoolAdminContract {

    public interface SchoolAdminView{
            void bindPresenter(SchoolAdminPresenter schoolAdminPresenter);
            void showSpinner();
            void loadSchoolList(ArrayList<SchoolListResponse> schoolListResponse);
            void hideSpinner();
            void showError(String errorMessage);
            void instituteDeleted();
            void updateSchoolListCache(Object object);
            void teacherInstituteDeleted(Boolean status);

    }

    public interface SchoolAdminPresenter {
        void loadSchoolList();
        void deleteInstitute(String InstituteId);
        void getRequestedInstituteList(String teacherId);
        void deleteTeacherRequestedInstitute(String instituteId);
    }
}
