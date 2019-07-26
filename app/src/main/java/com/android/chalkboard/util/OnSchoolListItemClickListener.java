package com.android.chalkboard.util;

import com.android.chalkboard.school.model.SchoolListResponse;

public interface OnSchoolListItemClickListener {
    void onSchoolItemClick(SchoolListResponse schoolListResponse);
    void onSchoolItemEditClick(SchoolListResponse schoolListResponse);
    void onSchoolItemDeleteClick(SchoolListResponse schoolListResponse);
    void onSchoolItemOptionMenuClick(SchoolListResponse schoolListResponse);
}
