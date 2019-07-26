package com.android.chalkboard.teacherrequest.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.model.Institution;
import com.android.chalkboard.teacherrequest.modal.TeacherDetail;
import com.android.chalkboard.teacherrequest.modal.TeacherRequest;
import com.android.chalkboard.teacherrequest.modal.TeachersRequestDetails;
import com.android.chalkboard.teacherrequest.presenter.TeacherRequestPresenterImpl;
import com.android.chalkboard.teacherrequest.presenter.TeachersRequestContract;
import com.android.chalkboard.teacherrequest.view.adapter.TeachersRequestAdapter;
import com.android.chalkboard.util.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class TeachersRequestFragment extends Fragment implements TeachersRequestContract.View, OnRecyclerItemClickListener {

    private ArrayList<TeachersRequestDetails> teachersRequestDetailsArrayList;
    private RecyclerView teachersRequestRecyclerView;
    private TeachersRequestAdapter teachersRequestAdapter;
    private TeachersRequestContract.Presenter presenter;
    private static final String TAG = TeacherDetailsFragment.class.getSimpleName();
    private int instituteId;


    public TeachersRequestFragment() {
        // Required empty public constructor
    }

    public static TeachersRequestFragment newInstance() {
        TeachersRequestFragment fragment = new TeachersRequestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TeacherRequestPresenterImpl.createPresenter(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
        teachersRequestRecyclerView = view.findViewById(R.id.rv_teachers_request);
        teachersRequestDetailsArrayList = new ArrayList<>();
        loadData();
        Bundle bundle = getArguments();
        if(bundle!=null){
            instituteId = bundle.getInt("instituteId");
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        teachersRequestRecyclerView.setLayoutManager(linearLayoutManager);
        teachersRequestAdapter = new TeachersRequestAdapter(getActivity(), teachersRequestDetailsArrayList,this);
        teachersRequestRecyclerView.setAdapter(teachersRequestAdapter);
        TeacherRequestPresenterImpl.createPresenter(this);
        presenter.getTeacherRequest(instituteId);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void bindPresenter(TeacherRequestPresenterImpl teacherRequestPresenter) {
          this.presenter = teacherRequestPresenter;
    }

    @Override
    public void showTeachersRequest(List<TeacherRequest> teachersRequestList) {
        // TODO For Teacher Request : inflate this list in recycler view instead of hard coded data
    }

    @Override
    public void displayTeachersDetails(TeacherDetail teacherDetail) {
        // TODO : Navigate to teacher detail fragment
    }


    private void loadData() {


        teachersRequestDetailsArrayList.add(new TeachersRequestDetails("Mr. Chandler","Maths Teacher",
                R.drawable.school_list_icon));
        teachersRequestDetailsArrayList.add(new TeachersRequestDetails("Mr. Stark","Maths Teacher",
                R.drawable.school_list_icon));
        teachersRequestDetailsArrayList.add(new TeachersRequestDetails("Mr. Prabhat","Maths Teacher",
                R.drawable.school_list_icon));
        teachersRequestDetailsArrayList.add(new TeachersRequestDetails("Mr. Rakesh","Maths Teacher",
                R.drawable.school_list_icon));
        teachersRequestDetailsArrayList.add(new TeachersRequestDetails("Mr. Saurabh","Maths Teacher",
                R.drawable.school_list_icon));
        teachersRequestDetailsArrayList.add(new TeachersRequestDetails("Mr. Steven","Maths Teacher",
                R.drawable.school_list_icon));

    }

    @Override
    public void onClick(String id) {
        TeacherDetailsFragment teacherDetailsFragment = new TeacherDetailsFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.rl_teachers_request_container, teacherDetailsFragment, TAG).addToBackStack(TAG);
        ft.commit();

    }

    @Override
    public void showError(String errorMessage){
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }
}
