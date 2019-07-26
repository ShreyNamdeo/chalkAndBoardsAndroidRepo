package com.android.chalkboard.teacherrequest.interactor;

import com.android.chalkboard.network.NetworkListener;

public interface TeacherRequestInteractor {
    void getAllTeacherRequest(int strInstituteId, NetworkListener listener);

    void rejectTeacherRequest(int instituteId, int requestId, NetworkListener listener);

    void acceptTeacherRequest(int instituteId, int requestId, NetworkListener listener);

    void deleteTeacherRequest(int instituteId, NetworkListener listener);

    void getTeacherDetails(String id, NetworkListener listener);
}
