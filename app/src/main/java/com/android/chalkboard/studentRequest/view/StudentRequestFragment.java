package com.android.chalkboard.studentRequest.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.classes.presenter.ClassContract;
import com.android.chalkboard.classes.presenter.ClassPresenterImpl;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.studentRequest.model.StudentRequestResponse;
import com.android.chalkboard.studentRequest.presenter.StudentRequestContract;
import com.android.chalkboard.studentRequest.presenter.StudentRequestPresenterImpl;
import com.android.chalkboard.studentRequest.view.adapter.StudentRequestRecycleViewAdapter;
import com.android.chalkboard.util.OnStudentRequestItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class StudentRequestFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener, ClassContract.View, StudentRequestContract.View, OnStudentRequestItemClickListener{
    private RecyclerView studentRequestListRecyclerView;
    private TextView emptyMessageTextView;
    private ProgressBar progressBar;
    private FloatingActionButton sFab;
    private PopupWindow popup = null;
    private ClassPresenterImpl presenter;
    private StudentRequestPresenterImpl studentRequestPresenter;
    private ArrayList<ClassResponse> classResponses;
    private ArrayList<StudentRequestResponse> requestResponses;
    private StudentRequestRecycleViewAdapter adapter;
    private StudentRequestResponse response;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.student_request_fragment, parent, false);
        ClassPresenterImpl.createPresenter(this);
        StudentRequestPresenterImpl.createPresenter(this);
        studentRequestListRecyclerView = view.findViewById(R.id.student_request_recycler_view);
        progressBar = view.findViewById(R.id.progress);
        sFab = view.findViewById(R.id.searchStudentRequestFab);
        sFab.setOnClickListener(this);
        emptyMessageTextView = view.findViewById(R.id.empty_student_request_list_message_tv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ((NavDashBoardActivity)getActivity()).getAppBarLayout().setExpanded(true);
        ((NavDashBoardActivity)getActivity()).getBannerImage().setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.header_vector_student_request));
        studentRequestListRecyclerView.setLayoutManager(layoutManager);
        popup = new PopupWindow(setupPopWindow(), ViewGroup.LayoutParams.MATCH_PARENT, 100, true);
        presenter.getAllClasses();

        return view;
    }

    public LinearLayout setupPopWindow(){
        LinearLayout l = new LinearLayout(getActivity());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setLayoutParams(param);
        l.setPadding(10, 5, 10, 5);
        l.setBackgroundColor(getResources().getColor(R.color.appWhite));
        SearchView searchView = new SearchView(getActivity());
        searchView.setLayoutParams(param);
        searchView.setId(R.id.searchView);
        searchView.setQueryHint("Search added school");
        searchView.setFocusable(true);
        searchView.setClickable(true);
        searchView.setOnQueryTextListener(this);
        searchView.setBackground(null);
        searchView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        l.setBackgroundResource(R.drawable.drawable_registration_background_white);
        l.addView(searchView);

        return l;
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.searchStudentRequestFab:
                System.out.println("In fab click");
                if(!popup.isShowing()) {
                    popup.showAtLocation(view, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 60);
                } else {
                    popup.dismiss();
                    //schoolAdminListAdapter.getFilter().filter("");
                }
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s){
        adapter.getFilter().filter(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s){
        adapter.getFilter().filter(s);
        return false;
    }

    @Override
    public void bindPresenter(ClassPresenterImpl presenter){
        this.presenter = presenter;
    }

    @Override
    public void showError(String errorMessage){
        if(errorMessage != null)
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(Object success){
        classResponses = (ArrayList<ClassResponse>)success;
        if(classResponses == null || classResponses.size() == 0) {
            emptyMessageTextView.setVisibility(View.VISIBLE);
            emptyMessageTextView.setText("No class added. Please add a class first.");
            sFab.hide();
        }
        else{
            emptyMessageTextView.setVisibility(View.GONE);

            for(int i = 0; i < classResponses.size(); i++) {
                System.out.println(classResponses.get(i).getId());
                studentRequestPresenter.getStudentRequest(classResponses.get(i).getName(), classResponses.get(i).getId());
            }
        }
    }

    @Override
    public void onSuccess(ArrayList<StudentRequestResponse> response){
        if(!(response == null || response.size() == 0)) {
            showError("SRSize: " + response.size() + ":" + response.get(0).getClassName());
            if (requestResponses == null)
                requestResponses = new ArrayList<>();
            for(int i = 0; i< response.size(); i++)
                if(response.get(i).getStatus().equalsIgnoreCase("requested"))
                    requestResponses.add(response.get(i));
        }
        if(requestResponses.size() == 0){
            emptyMessageTextView.setVisibility(View.VISIBLE);
            emptyMessageTextView.setText("No student request to accept.");
            sFab.hide();
        } else {
            sFab.show();
            adapter = new StudentRequestRecycleViewAdapter(getActivity(), requestResponses, this);
            studentRequestListRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void bindPresenter(StudentRequestPresenterImpl studentRequestPresenter){
        this.studentRequestPresenter = studentRequestPresenter;
    }

    @Override
    public void showSpinner(){
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSpinner(){
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStudentRequestItemAcceptClick(StudentRequestResponse response){
        int classId = -1;
        for(ClassResponse c: classResponses)
            if(c.getName().equalsIgnoreCase(response.getClassName())) {
                classId = c.getId();
                break;
            }

        if(classId != -1) {
            studentRequestPresenter.acceptStudentRequest(classId, response.getRequestId());
            this.response = response;
        }
    }

    @Override
    public void onStudentRequestItemClick(StudentRequestResponse response){
        FragmentManager fm = getActivity().getFragmentManager();
        ShowStudentProfileFragment sf = ShowStudentProfileFragment.newInstance(response);
        sf.show(fm, "showStudentProfile");
    }

    @Override
    public void onStudentRequestItemRejectClick(StudentRequestResponse response){
        int classId = -1;
        for(ClassResponse c: classResponses)
            if(c.getName().equalsIgnoreCase(response.getClassName())){
                classId = c.getId();
                break;
            }
        if(classId != -1) {
            this.response = response;
            studentRequestPresenter.acceptStudentRequest(classId, response.getRequestId());
        }
    }

    @Override
    public void onResponse(Object message){
        if(response != null) {
            requestResponses.remove(response);
            adapter.notifyDataSetChanged();
            response = null;
        }
        showError((String)message);
    }
}
