package com.android.chalkboard.studentRequest.interactor;

import com.android.chalkboard.network.NetworkListener;

public interface StudentRequestInteractor {

    void getStudentRequest(int classId, NetworkListener listener);
    void acceptStudentRequest(int classId, int requestId, NetworkListener listener);
    void rejectStudentRequest(int classId, int requestId, NetworkListener listener);
}
