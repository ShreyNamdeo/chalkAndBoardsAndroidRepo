package com.android.chalkboard.util;

import android.view.View;
import com.android.chalkboard.classes.model.ClassResponse;

public interface OnClassItemCLickListener {
    void onClassItemClick(ClassResponse classResponse);
    void onClassItemEditClick(ClassResponse classResponse);
    void onClassItemDeleteClick(ClassResponse classResponse);
    void onClassItemOptionMenuClick(View imageView, ClassResponse classResponse);
}
