package com.android.chalkboard.classagenda.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.classagenda.model.AgendaResponse;
import com.android.chalkboard.classagenda.model.CreateAgendaRequest;
import com.android.chalkboard.classagenda.presenter.ClassAgendaContract;
import com.android.chalkboard.classagenda.presenter.ClassAgendaPresenterImpl;
import com.android.chalkboard.classagenda.view.adapter.ClassAgendaExpandableAdapter;
import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.classes.presenter.ClassContract;
import com.android.chalkboard.classes.presenter.ClassPresenterImpl;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.util.OnClassAgendaItemListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CLassAgendaFragment extends Fragment implements ClassContract.View, ClassAgendaContract.View, OnClassAgendaItemListener {

    ClassAgendaPresenterImpl impl;
    ClassPresenterImpl cImpl;
    ProgressBar progressBar;
    ExpandableListView expandableListView;
    TextView emptytextView;
    HashMap<ClassResponse, AgendaResponse> hashMap;
    ArrayList<ClassResponse> classList;
    ClassAgendaExpandableAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.class_agenda_fragment, parent, false);
        emptytextView = v.findViewById(R.id.empty_class_agenda_list_message_tv);
        expandableListView = v.findViewById(R.id.class_agenda_expandable_list_view);
        progressBar = v.findViewById(R.id.progress);
        ClassPresenterImpl.createPresenter(this);
        ClassAgendaPresenterImpl.CreatePresenter(this);
        cImpl.getAllClasses();

        ((NavDashBoardActivity)getActivity()).getAppBarLayout().setExpanded(true);
        ((NavDashBoardActivity)getActivity()).getBannerImage().setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.news));
        ((NavDashBoardActivity)getActivity()).getBannerImage().setImageDrawable(getResources().getDrawable(R.drawable.news));
        ((NavDashBoardActivity)getActivity()).setCollapsableTitle("Class Agenda");
        hashMap = new HashMap<>();

        return v;
    }

    @Override
    public void bindPresenter(ClassPresenterImpl cImpl){
        this.cImpl = cImpl;
    }

    @Override
    public void bindPresenter(ClassAgendaPresenterImpl impl){
        this.impl = impl;
    }

    @Override
    public void showError(String errorMessage){
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateSuccess(Object success){

    }

    @Override
    public void onGetSuccess(Object success, ClassResponse response){
        if(success != null){
            emptytextView.setVisibility(View.GONE);
            expandableListView.setVisibility(View.VISIBLE);
            hashMap.put(response, (AgendaResponse)success);
            adapter = new ClassAgendaExpandableAdapter(getActivity(), this, classList, hashMap);
            expandableListView.setAdapter(adapter);
        } else if(hashMap.size() == 0){
            emptytextView.setVisibility(View.VISIBLE);
            expandableListView.setVisibility(View.GONE);
            emptytextView.setText("No agenda found");
        }
    }

    @Override
    public void onUpdateSuccess(Object success){

    }

    @Override
    public void hideSpinner(){
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSpinner(){
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(Object success){
        if(success != null && ((ArrayList)success).size() > 0 ){
            classList = (ArrayList)success;
            for(ClassResponse r: classList)
                impl.getAgenda(r.getId(), r);
        } else{
            emptytextView.setVisibility(View.VISIBLE);
            expandableListView.setVisibility(View.GONE);
            emptytextView.setText("No class added. Add a class first.");
        }
    }

    @Override
    public void createAgenda(CreateAgendaRequest request, ClassResponse response){

    }

    @Override
    public void updateAgenda(CreateAgendaRequest request, ClassResponse response){

    }
}
