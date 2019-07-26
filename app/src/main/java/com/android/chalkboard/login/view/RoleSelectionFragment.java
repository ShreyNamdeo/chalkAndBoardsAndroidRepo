package com.android.chalkboard.login.view;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.chalkboard.R;
import com.android.chalkboard.register.view.RegistrationActivity;
import com.android.chalkboard.util.Constant;
import com.android.chalkboard.util.SharedPrefUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoleSelectionFragment extends DialogFragment implements View.OnClickListener {

    private ImageView ivTeacher, ivStudent, ivAdmin, ivParent;
    private String roleSelected;
    private RadioButton radioButton;
    loginFragment_1 loginFragment_1;


    public RoleSelectionFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public RoleSelectionFragment(com.android.chalkboard.login.view.loginFragment_1 loginFragment_1) {
        this.loginFragment_1 = loginFragment_1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_role_selection, container, false);
        ivAdmin = view.findViewById(R.id.iv_role_admin);
        ivStudent = view.findViewById(R.id.iv_role_student);
        ivParent = view.findViewById(R.id.iv_role_parent);
        ivTeacher = view.findViewById(R.id.iv_role_teacher);
        ivAdmin.setOnClickListener(this);
        ivStudent.setOnClickListener(this);
        ivParent.setOnClickListener(this);
        ivTeacher.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_role_admin :
                SharedPrefUtils.storeInSharedPref(getActivity(),SharedPrefUtils.ROLE, getActivity().getString(R.string.role_admin));
                roleSelected = getActivity().getString(R.string.role_admin);

                break;
            case R.id.iv_role_parent :
                SharedPrefUtils.storeInSharedPref(getActivity(),SharedPrefUtils.ROLE, getActivity().getString(R.string.role_parent));
                roleSelected = getActivity().getString(R.string.role_parent);

                break;
            case R.id.iv_role_student :
                SharedPrefUtils.storeInSharedPref(getActivity(),SharedPrefUtils.ROLE, getActivity().getString(R.string.role_student));
                roleSelected = getActivity().getString(R.string.role_student);

                break;
            case R.id.iv_role_teacher :
                SharedPrefUtils.storeInSharedPref(getActivity(),SharedPrefUtils.ROLE, getActivity().getString(R.string.role_teacher));
                roleSelected = getActivity().getString(R.string.role_teacher);

                break;

        }
        loginFragment_1.setRoleText(roleSelected);
        dismiss();
    }
}
