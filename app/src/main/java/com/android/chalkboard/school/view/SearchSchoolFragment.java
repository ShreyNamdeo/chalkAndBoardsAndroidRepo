package com.android.chalkboard.school.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.presenter.DashboardContract;
import com.android.chalkboard.dashboard.presenter.DashboardPresenterImpl;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.school.view.adapter.SchoolAddListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchSchoolFragment extends DialogFragment implements DashboardContract.View{
    SearchView search;
    ListView listview;
    private DashboardContract.DashboardPresenter presenter;
    private List<SchoolListResponse> schoolListResponses;
    public static final String TAG = SearchSchoolFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        //setCancelable(true);
        //getDialog().setCanceledOnTouchOutside(false);
        View view = layoutInflater.inflate(R.layout.search_school, null);
        search = view.findViewById(R.id.schoolSearchView);
        listview = view.findViewById(R.id.schoolSearchListView);

        DashboardPresenterImpl.createPresenter(this);

        //final SchoolAddListViewAdapter adapter = new SchoolAddListViewAdapter(getActivity(), R.layout.school_items, schoolListResponses);
        //listview.setAdapter(adapter);

        search.setQueryHint("Search school by name, city");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                    String sn = "", sc = "";
                    if (s.split(", ").length > 1) {
                        sn = s.split(", ")[0];
                        sc = s.split(", ")[1];
                    } else if (s.split(",").length > 1) {
                        sn = s.split(",")[0];
                        sc = s.split(",")[1];
                    } else {
                        System.out.println("Please enter value in school name, city format");
                        throw new RuntimeException("Please enter value in school name, city format");
                    }
                    presenter.getInstitutions(sc, sn);
                } catch(Exception e){
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();

        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void bindPresenter(DashboardPresenterImpl presenter){
        this.presenter = presenter;
    }

    @Override
    public void storeInstituteData(Object success) {
        List<SchoolListResponse> list = (List<SchoolListResponse>)success;
        //System.out.println("Id:"+ ((List<SchoolListResponse>) success).get(0).getId());
        schoolListResponses = new ArrayList<>();
        schoolListResponses.addAll(list);
        final SchoolAddListViewAdapter adapter = new SchoolAddListViewAdapter(getActivity(), R.layout.school_items, schoolListResponses, this);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void showDialogFragment(SchoolListResponse response){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ViewSchoolFragment v = new ViewSchoolFragment();
        v.setResponse(response);
        ft.replace(R.id.school_fragment_content_holder, v).addToBackStack(TAG);
        ft.commit();

    }
    @Override
    public void storeClassesofInstitute(Object success) {

    }

    @Override
    public void showError(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume(){
        super.onResume();
        String s = search.getQuery().toString();
        if(!s.equals("")) {
            try {
                String sn = "", sc = "";
                if (s.split(", ").length > 1) {
                    sn = s.split(", ")[0];
                    sc = s.split(", ")[1];
                } else if (s.split(",").length > 1) {
                    sn = s.split(",")[0];
                    sc = s.split(",")[1];
                } else {
                    System.out.println("Please enter value in school name, city format");
                    throw new RuntimeException("Please enter value in school name, city format");
                }
                presenter.getInstitutions(sc, sn);
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setRequestedInstitute(Object success){

    }
}
