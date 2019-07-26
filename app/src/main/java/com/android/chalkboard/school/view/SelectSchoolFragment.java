package com.android.chalkboard.school.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.presenter.DashboardContract;
import com.android.chalkboard.dashboard.presenter.DashboardPresenterImpl;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.school.view.adapter.SchoolAddListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectSchoolFragment extends DialogFragment implements View.OnClickListener, DashboardContract.View {
    Button join, cancel;
    private List<SchoolListResponse> list;
    private DashboardContract.DashboardPresenter presenter;
    ListView lv;
    String selectedSchoolId = "";

    public void setList(List<SchoolListResponse> list) {
        this.list = new ArrayList<>();
        this.list.addAll(list);
        System.out.println("Id3:" + this.list.get(0).getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        setCancelable(false);
        int height = (int)getResources().getDimension(R.dimen.select_school_fragement_height);
        int width = (int)getResources().getDimension(R.dimen.select_school_fragement_width);
        getDialog().getWindow().setLayout(width, height);
        View view = inflater.inflate(R.layout.search_and_school_fragment, null);

        lv = view.findViewById(R.id.schoolListView);
        //final SchoolAddListViewAdapter adapter = new SchoolAddListViewAdapter(getActivity(), R.layout.school_listview_item, list);
        //lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), String.valueOf(i), Toast.LENGTH_LONG).show();
                //selectedSchoolId = String.valueOf(adapter.getItem(i).getId());
            }
        });

        join = view.findViewById(R.id.join);
        cancel = view.findViewById(R.id.cancel);

        join.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.join:
                if(!selectedSchoolId.equals(""))
                    presenter.requestInstitutionJoin(selectedSchoolId);
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void storeInstituteData(Object success) {
        showError("Institution join request successfully.");
        dismiss();
    }

    @Override
    public void bindPresenter(DashboardPresenterImpl presenter){
        this.presenter = presenter;
    }

    @Override
    public void showError(String errorMessage){
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG);
    }

    @Override
    public void storeClassesofInstitute(Object success) {

    }

    @Override
    public void setRequestedInstitute(Object success){

    }
}
