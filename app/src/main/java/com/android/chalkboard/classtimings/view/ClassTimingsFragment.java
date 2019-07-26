package com.android.chalkboard.classtimings.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.chalkboard.R;
import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.classes.presenter.ClassContract;
import com.android.chalkboard.classes.presenter.ClassPresenterImpl;
import com.android.chalkboard.classtimings.model.ClassTimeItem;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.util.TimeTable.Interfaces.TimeTableEventListener;
import com.android.chalkboard.util.TimeTable.TimeTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClassTimingsFragment extends Fragment implements View.OnClickListener, ClassContract.View, TimeTableEventListener {

    private TimeTable timetable;
    private ClassPresenterImpl presenter;
    private List<ClassResponse> responses;
    private ProgressBar progress;
    private List<ClassTimeItem> classTimeItems;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        this.inflater = inflater;
        ClassPresenterImpl.createPresenter(this);
        View v = inflater.inflate(R.layout.class_timmings_fragment, parent, false);
        progress = v.findViewById(R.id.progress);
        presenter.getAllClasses();
        timetable = v.findViewById(R.id.timetable);
        timetable.setVisibility(View.INVISIBLE);
        ((NavDashBoardActivity)getActivity()).getBannerImage().setImageDrawable(getResources().getDrawable(R.drawable.header_vector_classes));
        ((NavDashBoardActivity)getActivity()).setCollapsableTitle("Class Timings");
        return v;
        //return new TimeTable(getActivity());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ((NavDashBoardActivity)getActivity()).setCollapsableTitle("Home");
    }


    protected String getEventTitle(Calendar time){
        return String.format("Class at: %02d:%02d %s %d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view){

    }

    @Override
    public void showError(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEventLongPress(ClassTimeItem item){

    }

    @Override
    public void bindPresenter(ClassPresenterImpl presenter){
        this.presenter = presenter;
    }

    @Override
    public void showSpinner(){
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSpinner(){
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(Object response){
        if(response != null && ((ArrayList<ClassResponse>)response).size() != 0) {
            responses = (ArrayList<ClassResponse>) response;
            classTimeItems = new ArrayList<>();
            for(int i = 0; i < responses.size(); i++){
                Date startDate = new Date(responses.get(i).getStartDate());
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(responses.get(i).getStartTime().split(":")[0]));
                startTime.set(Calendar.MINUTE, Integer.parseInt(responses.get(i).getStartTime().split(":")[1].split(" ")[0]));
                startTime.set(Calendar.DAY_OF_MONTH, startDate.getDay());
                startTime.set(Calendar.MONTH, startDate.getMonth());
                startTime.set(Calendar.YEAR, startDate.getYear());
                Date endDate = new Date(responses.get(i).getEndDate());
                Calendar endTime = Calendar.getInstance();
                endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(responses.get(i).getEndTime().split(":")[0]));
                endTime.set(Calendar.MINUTE, Integer.parseInt(responses.get(i).getEndTime().split(":")[1].split(" ")[0]));
                endTime.set(Calendar.DAY_OF_MONTH,endDate.getDay());
                endTime.set(Calendar.MONTH, endDate.getMonth());
                endTime.set(Calendar.YEAR, endDate.getYear());
                ClassTimeItem e = new ClassTimeItem(responses.get(i));
                //e.setColor(getResources().getColor(R.color.appWhite));
                classTimeItems.add(e);
            }

            /*CustomAdapter adapter = new CustomAdapter(getActivity(), inflater, R.layout.class_timings_grid_child_view, classTimeItems);
            timetable.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            timetable.setAdapter(adapter);*/
            timetable.setClassItems(classTimeItems, this);
        }
        timetable.setVisibility(View.VISIBLE);
    }
}
