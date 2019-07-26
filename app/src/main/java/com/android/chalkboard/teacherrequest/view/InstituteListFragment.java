package com.android.chalkboard.teacherrequest.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.model.Institution;
import com.android.chalkboard.school.model.ClassesList;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.teacherrequest.view.adapter.InstituteListAdapter;
import com.android.chalkboard.util.CommonUtils;
import com.android.chalkboard.util.SharedPrefUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InstituteListFragment extends DialogFragment {
    private RecyclerView recyclerView;
    private InstituteListAdapter instituteListAdapter;
    private ArrayList<SchoolListResponse> institutionResponse;
    private Context mContext;
    private ArrayList<ClassesList> classesLists;
    private TextView textError;
    private Window window;
    public InstituteListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_institute_detail, container, false);
        recyclerView = view.findViewById(R.id.institute_recycler_view);
        textError = view.findViewById(R.id.text_error);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getBoolean("isFromNews")) {
            classesLists = SharedPrefUtils.getClasses(mContext);
            if(classesLists==null || classesLists.size()==0){
                textError.setText("There are no classes available");
                getActivity().getSupportFragmentManager().popBackStack();
            }else {
                instituteListAdapter = new InstituteListAdapter(mContext, classesLists, new OnItemClickListener() {
                    @Override
                    public void onItemClick() {
                        dismiss();
                    }
                }, true);
                recyclerView.setAdapter(instituteListAdapter);
            }
        } else {
            institutionResponse = SharedPrefUtils.retrieveInstituteList(mContext);
            if(institutionResponse==null|| institutionResponse.size()==0){
                textError.setText("Please add an school first");
            }else {
                instituteListAdapter = new InstituteListAdapter(mContext, institutionResponse, new OnItemClickListener() {
                    @Override
                    public void onItemClick() {
                        dismiss();
                    }
                });
                recyclerView.setAdapter(instituteListAdapter);
            }
        }

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);*/
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public interface OnItemClickListener {
        void onItemClick();
    }
}
