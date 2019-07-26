package com.android.chalkboard.school.interactor;

import com.android.chalkboard.network.NetworkListener;

public interface SchoolAdminInteractor {
    void getSchoolListCall(NetworkListener listener);
    void deleteSchoolCall(String institutionId, NetworkListener networkListener);
    void getRequestedSchoolListCall(String teacherId, NetworkListener listener);
    void deleteTeacherRequestedInstitute(String instituteId, NetworkListener listener);
}
