package com.android.chalkboard.classes.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.app.Fragment;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.classes.model.Class;
import com.android.chalkboard.classes.presenter.ClassContract;
import com.android.chalkboard.classes.presenter.ClassPresenterImpl;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.school.presenter.SchoolAdminContract;
import com.android.chalkboard.school.presenter.SchoolAdminPresenterImpl;
import com.android.chalkboard.school.view.TeacherSchoolFragment;
import com.android.chalkboard.util.SharedPrefUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.android.chalkboard.util.Constant.ID;

public class AddClassFragment extends DialogFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, Spinner.OnItemSelectedListener, AdapterView.OnItemClickListener, ClassContract.View{

    private TextInputEditText cName, subject, sTime, ETime, sDate, eDate;
    private Button cancel, create;
    private String classNameText, schoolNameText, schoolStandardText, classSubjectText, classStartTimeText, classEndTimeText,
        classStartDateText= "", classEndDateText = "", date = "", time = "", curDate;
    private DatePickerDialog dPicker;
    private TimePickerDialog timePicker;
    private PopupWindow popup = null;
    private AutoCompleteTextView schoolView;
    private ArrayList<SchoolListResponse> schoolListResponses;
    private Spinner standardList;
    private int schoolId;
    private ClassPresenterImpl presenter;
    private ProgressBar progressBar;
    private  String[] standardListString = {"1", "2"};
    private boolean edit = false;
    private Class c;
    private boolean active;
    public static AddClassFragment newInstance(Class c, boolean active){
        Bundle b = new Bundle();
        b.putSerializable("classResponse", c);
        b.putBoolean("active", active);
        AddClassFragment ac = new AddClassFragment();
        ac.setArguments(b);

        return ac;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        if(getArguments() != null){
            c = (Class)getArguments().getSerializable("classResponse");
            active = getArguments().getBoolean("active");
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        ClassPresenterImpl.createPresenter(this);
        schoolListResponses = SharedPrefUtils.retrieveRequestedInstituteList(getActivity());
        View view = inflater.inflate(R.layout.add_class_fragment, container, false);
        progressBar = view.findViewById(R.id.progress);
        cName = view.findViewById(R.id.classNameText);
        schoolView = view.findViewById(R.id.schoolNameAutoText);
        String[] names = new String[schoolListResponses.size()];
        for(int i = 0; i< schoolListResponses.size(); i++)
            names[i] = schoolListResponses.get(i).getName();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, names);
        schoolView.setThreshold(1);
        schoolView.setAdapter(adapter);
        schoolView.setOnItemClickListener(this);

        standardList = view.findViewById(R.id.standardDropDown);
        subject = view.findViewById(R.id.classSubjectText);
        sTime = view.findViewById(R.id.classStartTimeText);
        ETime = view.findViewById(R.id.classEndTimeText);
        sDate = view.findViewById(R.id.classStartDateText);
        eDate = view.findViewById(R.id.classEndDateText);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, standardListString);
        standardList.setAdapter(listAdapter);
        standardList.setOnItemSelectedListener(this);
        sTime.setOnClickListener(this);
        ETime.setOnClickListener(this);
        sDate.setOnClickListener(this);
        eDate.setOnClickListener(this);

        cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        create = view.findViewById(R.id.create);
        create.setOnClickListener(this);

        if(c != null)
            setValues(c);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        schoolStandardText = (String)adapterView.getItemAtPosition(i);
        Toast.makeText(getActivity(), schoolStandardText, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView){
        standardList.setPrompt("Select Standard");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        schoolNameText = (String)adapterView.getItemAtPosition(i);
    }

    @Override
    public void onClick(View view){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        switch (view.getId()){
            case R.id.classStartTimeText:
                time = "start";
                timePicker = new TimePickerDialog(getActivity(), this, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), false);
                timePicker.show();
                break;
            case R.id.classEndTimeText:
                time = "end";
                timePicker = new TimePickerDialog(getActivity(), this, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), false);
                timePicker.show();
                break;
            case R.id.classStartDateText:
                date = "start";
                dPicker = new DatePickerDialog(getActivity(), this, year, month, day);
                dPicker.show();
                break;
            case R.id.classEndDateText:
                date = "end";
                dPicker = new DatePickerDialog(getActivity(), this, year, month, day);
                dPicker.show();
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.create:
                    curDate = String.valueOf(new Date().getTime());
                    classSubjectText = subject.getText().toString();
                    schoolNameText = schoolView.getText().toString();
                    for(SchoolListResponse r: schoolListResponses)
                        if(r.getName().equalsIgnoreCase(schoolNameText))
                            schoolId = r.getId();
                    classNameText = cName.getText().toString();
                Class body = new Class(classNameText, schoolId, schoolStandardText, curDate, classSubjectText, classStartTimeText, classEndTimeText, classStartDateText, classEndDateText);
                System.out.println(classNameText + "," + schoolId + "," + schoolStandardText + "," + curDate + "," + classSubjectText + "," + classStartTimeText + "," + classEndTimeText + "," + classStartDateText + "," + classEndDateText);
                if(!edit)
                    presenter.createNewClass(body);
                else
                    presenter.updateClass(body);
                break;

        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute){
        String timeVal = "";
        if(hour > 12)
            timeVal = (hour - 12) + ":" + minute + " PM";
        else
            timeVal = hour + ":" + minute + " AM";

        if(time.equals("start")) {
            classStartTimeText = timeVal;
            sTime.setText(classStartTimeText);
        }
        else if(time.equals("end")) {
            classEndTimeText = timeVal;
            ETime.setText(classEndTimeText);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Dialog d = getDialog();
        if(d != null){
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height =  LinearLayout.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){
        try {
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            Date d = s.parse(year + "-" + (month + 1) + "-" + day);
            if(date.equals("start")) {
                classStartDateText = String.valueOf(d.getTime());
                sDate.setText(s.format(d));
            }
            else if(date.equals("end")) {
                classEndDateText = String.valueOf(d.getTime());
                eDate.setText(s.format(d));
            }
        } catch(Exception e){
            showError(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void showError(String errorMessage){
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(Object message){
        Toast.makeText(getActivity(), (String)message, Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void bindPresenter(ClassPresenterImpl presenter){
        this.presenter = presenter;
    }

    @Override
    public void showSpinner(){
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSpinner(){
        progressBar.setVisibility(View.GONE);
    }

    public void setValues(Class body){
        String className = body.getcName(), schoolName = "", standard = body.getStandard(), subjectName = body.getSubject();
        String startTime = body.getStartTime(), endTime = body.getEndTime(), startDate = body.getStartDate(), endDate = body.getEndDate();
        cName.setText(className);
        for(SchoolListResponse r: schoolListResponses)
            if(r.getId() == body.getInstitutionId())
                schoolName = r.getName();
        schoolView.setText(schoolName);
        int index = 0;
        for(int i=0; i < standardListString.length; i++)
            if(standardListString[i].equalsIgnoreCase(String.valueOf(standard))) {
                index = i;
                break;
            }
        standardList.setSelection(index);
        subject.setText(subjectName);
        sTime.setText(startTime);
        ETime.setText(endTime);
        sDate.setText(startDate);
        eDate.setText(endDate);
        create.setText("Update");
        edit = true;


        cName.setEnabled(active);
        schoolView.setEnabled(active);
        standardList.setEnabled(active);
        subject.setEnabled(active);
        sTime.setEnabled(active);
        ETime.setEnabled(active);
        sDate.setEnabled(active);
        eDate.setEnabled(active);

        if(active == false) {
            create.setVisibility(View.GONE);
        }
    }
}


