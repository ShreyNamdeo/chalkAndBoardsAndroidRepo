package com.android.chalkboard.util;

import android.os.Parcelable;

import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.classnews.model.CreateClassNewsResponse;

public interface OnClassNewsItemClickListener{
    void onItemClick(CreateClassNewsResponse response);
    void onItemDeleteClick(int classId, int newsId);
    void onItemEditClick(CreateClassNewsResponse response, ClassResponse classResponse);
    void onEditFragmentClose(ClassResponse classResponse);
}
