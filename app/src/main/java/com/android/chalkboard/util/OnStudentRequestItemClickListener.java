package com.android.chalkboard.util;

import com.android.chalkboard.studentRequest.model.StudentRequestResponse;

public interface OnStudentRequestItemClickListener {
    void onStudentRequestItemClick(StudentRequestResponse response);
    void onStudentRequestItemAcceptClick(StudentRequestResponse response);
    void onStudentRequestItemRejectClick(StudentRequestResponse response);
}
