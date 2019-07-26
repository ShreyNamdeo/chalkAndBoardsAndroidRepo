package com.android.chalkboard.teacherrequest.view;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.chalkboard.R;

public class TeachersRequestActivity extends AppCompatActivity {


    private static final String TAG = TeachersRequestFragment.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_request);
        /*FragmentManager fm = getSupportFragmentManager();
        InstituteListFragment instituteListFragment = new InstituteListFragment();
        instituteListFragment.show(fm, "fragment_edit_name");*/


        TeachersRequestFragment teachersRequestFragment = new TeachersRequestFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.rl_teachers_request_container, teachersRequestFragment, TAG);
        ft.commit();
    }
}
