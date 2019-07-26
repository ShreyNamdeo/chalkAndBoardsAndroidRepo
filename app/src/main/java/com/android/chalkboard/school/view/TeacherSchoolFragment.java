package com.android.chalkboard.school.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.android.chalkboard.R;

public class TeacherSchoolFragment extends DialogFragment {

    private static final String TAG = SearchSchoolFragment.class.getName();
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View view = layoutInflater.inflate(R.layout.teacher_school_fragment, null);
        SearchSchoolFragment ssf = new SearchSchoolFragment();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.school_fragment_content_holder, ssf, TAG);
        ft.commit();

        return view;
    }

   @Override
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();

        if (dialog != null) {
            //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            //dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setLayout(width, height);
        }
    }
}
