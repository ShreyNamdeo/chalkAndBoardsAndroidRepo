package com.android.chalkboard.school.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.model.Institution;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.school.model.SchoolAdminItem;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.school.presenter.SchoolAdminContract;
import com.android.chalkboard.school.presenter.SchoolAdminPresenterImpl;
import com.android.chalkboard.school.view.adapter.SchoolAdminListAdapter;
import com.android.chalkboard.util.OnSchoolListItemClickListener;
import com.android.chalkboard.util.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;

import static com.android.chalkboard.util.Constant.ID;

public class SchoolAdminFragment extends Fragment implements SchoolAdminContract.SchoolAdminView,OnSchoolListItemClickListener, View.OnClickListener, SearchView.OnQueryTextListener, View.OnFocusChangeListener {
    private SchoolAdminListAdapter schoolAdminListAdapter;
    private RecyclerView schoolAdminListRecyclerView;
    private ArrayList<SchoolListResponse> schoolListResponses;
    private SchoolAdminContract.SchoolAdminPresenter schoolAdminPresenter;
    private TextView emptyMessageTextView;
    private ProgressBar progressBar;
    private String role = "";
    private FloatingActionButton sFab;
    private PopupWindow popup = null;
    private View view;

    public SchoolAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_school_admin, container, false);
        SchoolAdminPresenterImpl.createPresenter(this);
        emptyMessageTextView = view.findViewById(R.id.empty_school_list_message_tv);
        schoolAdminListRecyclerView = view.findViewById(R.id.school_admin_recycler_view);
        sFab = view.findViewById(R.id.searchSchoolFab);
        sFab.setOnClickListener(this);
        progressBar = view.findViewById(R.id.progress);
        loadSchoolLists();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        schoolAdminListRecyclerView.setLayoutManager(linearLayoutManager);
        ((NavDashBoardActivity)getActivity()).getAppBarLayout().setExpanded(true);
        ((NavDashBoardActivity)getActivity()).getBannerImage().setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.school_banner));
        popup = new PopupWindow(setupPopWindow(), ViewGroup.LayoutParams.MATCH_PARENT, 100, true);
        /*popup.setContentView(setupPopWindow());
        popup.setHeight(100);
        popup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);*/
        return view;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus){
        if(hasFocus){
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null)
                imm.showSoftInput(view, 0);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s){
        schoolAdminListAdapter.getFilter().filter(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s){
        schoolAdminListAdapter.getFilter().filter(s);
        return false;
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.searchSchoolFab:
                System.out.println("In fab click");
                if(!popup.isShowing()) {
                    popup.showAtLocation(view, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 60);
                } else {
                    popup.dismiss();
                    schoolAdminListAdapter.getFilter().filter("");
                }
                //p.setAnimationStyle(android.R.anim.fade_in);
                break;
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        if(popup.isShowing())
            popup.dismiss();
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
        //searchView.setIconifiedByDefault(false);
        searchView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        //searchView.setOnQueryTextFocusChangeListener(this);
        //searchView.setOnSearchClickListener(this);
        l.setBackgroundResource(R.drawable.drawable_registration_background_white);
        l.addView(searchView);

        return l;
    }

    private void loadSchoolLists(){
        role = SharedPrefUtils.getFromSharedPref(getActivity(), SharedPrefUtils.ROLE);
        if(role.equalsIgnoreCase("admin"))
            schoolAdminPresenter.loadSchoolList();
        else if(role.equalsIgnoreCase("teacher")) {
            schoolAdminPresenter.getRequestedInstituteList(SharedPrefUtils.getFromSharedPref(getActivity(), ID));
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(role.equalsIgnoreCase("admin"))
            schoolAdminPresenter.loadSchoolList();
        else if(role.equalsIgnoreCase("teacher")) {
            schoolAdminPresenter.getRequestedInstituteList(SharedPrefUtils.getFromSharedPref(getActivity(), ID));
        }
    }

    @Override
    public void bindPresenter(SchoolAdminContract.SchoolAdminPresenter schoolAdminPresenter) {
        this.schoolAdminPresenter = schoolAdminPresenter;

    }

    @Override
    public void showSpinner() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadSchoolList(ArrayList<SchoolListResponse> schoolListResponse) {
        this.schoolListResponses = schoolListResponse;
        if(schoolListResponses == null || schoolListResponses.size() == 0){
            emptyMessageTextView.setVisibility(View.VISIBLE);
            schoolAdminListRecyclerView.setVisibility(View.GONE);
            SharedPrefUtils.storeInstitituteObject(getActivity(), schoolListResponses);

        }else{
            emptyMessageTextView.setVisibility(View.GONE);
            schoolAdminListRecyclerView.setVisibility(View.VISIBLE);
            schoolAdminListAdapter = new SchoolAdminListAdapter(getActivity(),schoolListResponses,this);
            schoolAdminListRecyclerView.setAdapter(schoolAdminListAdapter);
            schoolAdminListAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public void hideSpinner() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void teacherInstituteDeleted(Boolean status){
        if(status)
            schoolAdminPresenter.getRequestedInstituteList(SharedPrefUtils.getFromSharedPref(getActivity(), ID));
        Toast.makeText(getContext(), "School Item Deleted Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void instituteDeleted() {
        if(role.equalsIgnoreCase("admin"))
            schoolAdminPresenter.loadSchoolList();
        Toast.makeText(getContext(), "School Item Deleted Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateSchoolListCache(Object object) {

    }

    @Override
    public void onSchoolItemClick(SchoolListResponse schoolListResponse) {
        if(role.equalsIgnoreCase("admin")) {
            Intent i = new Intent(getActivity(), SchoolEditViewActivity.class);
            i.putExtra("SchoolListResponseObj", schoolListResponse);
            startActivity(i);
            schoolAdminListAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "School Item Clicked" + schoolListResponse.getId(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSchoolItemEditClick(SchoolListResponse schoolListResponse) {
            Intent i = new Intent(getActivity(), SchoolEditViewActivity.class);
            i.putExtra("isEditView", true);
            i.putExtra("SchoolListResponseObj", schoolListResponse);
            startActivity(i);
            schoolAdminListAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "School Item Edit Clicked" + schoolListResponse.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSchoolItemDeleteClick(SchoolListResponse schoolListResponse) {
        if(role.equalsIgnoreCase("admin"))
            schoolAdminPresenter.deleteInstitute(schoolListResponse.getId().toString());
        else {
            System.out.println("Id:" + schoolListResponse.getId().toString());
            schoolAdminPresenter.deleteTeacherRequestedInstitute(schoolListResponse.getId().toString());
        }
    }

    @Override
    public void onSchoolItemOptionMenuClick(SchoolListResponse schoolListResponse) {
        Toast.makeText(getContext(), "School Item option Menu Clicked"+schoolListResponse.getId(), Toast.LENGTH_SHORT).show();

    }


   @Override
    public void showError(String errorMessage){
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
   }


}
