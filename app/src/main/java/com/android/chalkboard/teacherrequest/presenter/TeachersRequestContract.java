package com.android.chalkboard.teacherrequest.presenter;

import com.android.chalkboard.teacherrequest.modal.TeacherDetail;
import com.android.chalkboard.teacherrequest.modal.TeacherRequest;

import java.util.List;

public class TeachersRequestContract {

    public interface View {

        void bindPresenter(TeacherRequestPresenterImpl dashboardPresenter);
        void showTeachersRequest(List<TeacherRequest> teachersRequestList);
        void displayTeachersDetails(TeacherDetail teacherDetail);
        void showError(String errorMessage);
    }


    public interface Presenter {

        void getTeacherRequest(int instituteId);

        void acceptTeacher(int instituteId, int reqId);

        void rejectTeacher(int instituteId, int reqId);

        void deleteTeacherRequest(int instituteId);

        void getTeacherDetails(String id);
    }

}
