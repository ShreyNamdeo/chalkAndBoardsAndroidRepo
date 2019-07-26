package com.android.chalkboard.classnews.view;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.classes.presenter.ClassContract;
import com.android.chalkboard.classes.presenter.ClassPresenterImpl;
import com.android.chalkboard.classnews.model.CreateClassNewsResponse;
import com.android.chalkboard.classnews.presenter.ClassNewsContract;
import com.android.chalkboard.classnews.presenter.ClassNewsPresenterImpl;
import com.android.chalkboard.classnews.view.adapter.ClassNewsExpandableAdapter;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.util.OnClassNewsItemClickListener;
import com.android.chalkboard.util.SharedPrefUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassNewsFragment extends Fragment implements ClassNewsContract.View, ClassContract.View, OnClassNewsItemClickListener {
    ExpandableListView listView;
    ArrayList<ClassResponse> classResponses;
    TextView emptyTextView;
    ProgressBar progressBar;
    ClassPresenterImpl classPresenter;
    ClassNewsPresenterImpl newsPresenter;
    HashMap<ClassResponse, ArrayList<CreateClassNewsResponse>> newsItem;
    int classCount = 0;
    ClassNewsExpandableAdapter adapter;
    ClassResponse classResponse;

    @Override
    public void bindPresenter(ClassPresenterImpl classPresenter){
        this.classPresenter = classPresenter;
    }

    @Override
    public void bindPresenter(ClassNewsPresenterImpl newsPresenter){
        this.newsPresenter = newsPresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.class_news_fragment, parent, false);
        listView = v.findViewById(R.id.class_news_expandable_list_view);
        emptyTextView = v.findViewById(R.id.empty_class_news_list_message_tv);
        progressBar = v.findViewById(R.id.progress);
        ClassPresenterImpl.createPresenter(this);
        ClassNewsPresenterImpl.createPresenter(this);
        classPresenter.getAllClasses();
        ((NavDashBoardActivity)getActivity()).getAppBarLayout().setExpanded(true);
        ((NavDashBoardActivity)getActivity()).getBannerImage().setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.news));
        ((NavDashBoardActivity)getActivity()).getBannerImage().setImageDrawable(getResources().getDrawable(R.drawable.news));
        ((NavDashBoardActivity)getActivity()).setCollapsableTitle("Class News");
        newsItem = new HashMap<>();

        return v;
    }

    @Override
    public void showError(String errorMessage){
        if(errorMessage != null)
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        hideSpinner();
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
    public void onSuccess(Object response){
        if(response == null && ((ArrayList<ClassResponse>)response).size() == 0 ){
            listView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText("No class added. Please add a class first");

        } else {
            emptyTextView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            classResponses = (ArrayList<ClassResponse>)response;
            SharedPrefUtils.storeClassObject(getActivity(), classResponses);
            for (ClassResponse c: classResponses) {
                newsPresenter.getClassNews(c.getId(), c);
            }
        }
    }

    @Override
    public void onSuccess(Object response, String method, ClassResponse classResponse){
        if(!(response == null && ((ArrayList<CreateClassNewsResponse>)response).size() == 0)){
            ArrayList<CreateClassNewsResponse> r = (ArrayList<CreateClassNewsResponse>)response;
            newsItem.put(classResponse, r);
            for(CreateClassNewsResponse res : r)
                res.setClassId(classResponse.getId());
            adapter = new ClassNewsExpandableAdapter(getActivity(), this, classResponses, newsItem);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else if(classResponses.size() == classCount) {
            if(newsItem.size() == 0) {
                emptyTextView.setVisibility(View.VISIBLE);
                emptyTextView.setText("No class news");
                listView.setVisibility(View.GONE);
            }
        }
        classCount++;
    }

    @Override
    public void onItemClick(CreateClassNewsResponse response){
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        ClassNewsEditFragment c = ClassNewsEditFragment.newInstance(response, false, null);
        c.show(fragmentManager, "ClassNewsViewFragment");
    }

    public void getClasses(){
        classPresenter.getAllClasses();
    }

    @Override
    public void onItemEditClick(CreateClassNewsResponse response, final ClassResponse classResponse){
        this.classResponse = classResponse;
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        ClassNewsEditFragment c = ClassNewsEditFragment.newInstance(response, true, classResponse);
        c.setItemListener(this);
        c.show(fragmentManager, "ClassNewsEditFragment");
    }

    @Override
    public void onItemDeleteClick(int classId, int newsId){
        newsPresenter.deleteClassNews(classId, newsId);
    }

    @Override
    public void onDeleteSuccess(String response){
        showError("Class news has been deleted.");
        newsItem.values().contains(classResponses);
        adapter.removeItem();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ((NavDashBoardActivity)getActivity()).setCollapsableTitle("Home");
    }


    @Override
    public void onEditFragmentClose(ClassResponse classResponse){
        System.out.println("Class Name:" + classResponse.getName());
        newsItem.remove(classResponse);
        adapter.notifyDataSetChanged();
        newsPresenter.getClassNews(classResponse.getId(), classResponse);
    }
}
