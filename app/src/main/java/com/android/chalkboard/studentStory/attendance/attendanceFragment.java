package com.android.chalkboard.studentStory.attendance;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.studentStory.CustomAdapter;
import com.android.chalkboard.util.CustomDialog;
import com.android.chalkboard.util.FontChangeCrawler;
import com.android.chalkboard.util.SharedPrefUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class attendanceFragment extends Fragment {

    private View view;
    private ListView attendanceList;
    private RequestQueue requestQueue;
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    private ArrayList<attendanceModel> attendanceModels = new ArrayList<>();
    final Calendar myCalendar = Calendar.getInstance();
    EditText startDate,endDate;
    int click = 0;
    long start = 0,end = 0;
    TextView date_select;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((NavDashBoardActivity)getActivity()).showFragmentToolbar("Attendance");

        view = inflater.inflate(R.layout.activity_attendance_student,container,false);
        customDialog = new CustomDialog(getActivity());
        customDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        customDialog.setCanceledOnTouchOutside(false);

        startDate = (EditText) view.findViewById(R.id.startDate);
        endDate = (EditText) view.findViewById(R.id.endDate);
        date_select = (TextView) view.findViewById(R.id.date_select);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        attendanceList = (ListView) view.findViewById(R.id.attendance_list);

        if(attendanceModels.size()>0) {
            date_select.setVisibility(View.GONE);
            attendanceList.setVisibility(View.VISIBLE);
        }
        else {
            date_select.setVisibility(View.VISIBLE);
            attendanceList.setVisibility(View.GONE);
        }


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                click = 1;
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                click = 2;
            }
        });

//        ArrayList<CustomModel> data = new ArrayList<>();
//
//        data.add(new CustomModel(1,"All Hallows School",85));
//        data.add(new CustomModel(2,"All Hallows School",45));
//        data.add(new CustomModel(3,"All Hallows School",35));
//        data.add(new CustomModel(4,"All Hallows School",75));
//        data.add(new CustomModel(5,"All Hallows School",55));
//        data.add(new CustomModel(6,"All Hallows School",15));
//        data.add(new CustomModel(7,"All Hallows School",95));
//        data.add(new CustomModel(8,"All Hallows School",75));
//        data.add(new CustomModel(9,"All Hallows School",65));

//        ArrayList<attendanceModel> data2 = new ArrayList<>();
//        ArrayList<recordsModel> recordsModels = new ArrayList<>();
//        recordsModels.add(new recordsModel(1,100,"Physics",78.0,25.8));
//        recordsModels.add(new recordsModel(1,100,"Physics",78.0,25.8));
//        recordsModels.add(new recordsModel(1,100,"Physics",78.0,25.8));
//        recordsModels.add(new recordsModel(1,100,"Physics",78.0,25.8));
//        data2.add(new attendanceModel(1500023123L,recordsModels));
//        data2.add(new attendanceModel(1500023123L,recordsModels));
//        data2.add(new attendanceModel(1500023123L,recordsModels));

        attendanceList.setAdapter(new CustomAdapter(attendanceModels,R.drawable.img_class,R.layout.attendance_date_wise,3,getActivity()));

        return view;
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if(click == 1) {
            startDate.setText(sdf.format(myCalendar.getTime()));
        }
        else {
            endDate.setText(sdf.format(myCalendar.getTime()));
            String start = startDate.getText().toString();
            String end = endDate.getText().toString();
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            try {
//                Date dateFetch = simpleDateFormat.parse(startDate.getText().toString());
//                start = dateFetch.getTime();
//                dateFetch = simpleDateFormat.parse(endDate.getText().toString());
//                end = dateFetch.getTime();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

            GetResult("https://schoolapp-2018.herokuapp.com/student/attendance?startDate="+startDate.getText().toString()+"&endDate="+endDate.getText().toString());
        }
    }

    public void GetResult(final String mURL) {
//        progressBar.setVisibility(View.VISIBLE);
        String url = mURL;
        customDialog.show();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
//                                progressBar.s etVisibility(View.GONE);
                                customDialog.dismiss();
                                if (response.toString() != null) {
                                    try {
                                        jsonObject = new JSONObject(response.toString());
                                        ReadJson(jsonObject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                                progressBar.setVisibility(View.GONE);
                                customDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(error.toString())
                                        .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                GetResult(mURL);
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization","Basic "+ SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.JWTToken));
                        return params;
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        return params;
                    }
                };
        addToRequestQueue(jsonObjectRequest);
    }

    public void ReadJson(JSONObject jsonObject) {
        attendanceModels.clear();
        try {

            int id = jsonObject.getInt("id");
            date_select.setVisibility(View.GONE);
            attendanceList.setVisibility(View.VISIBLE);
            JSONArray attendances = jsonObject.getJSONArray("attendances");

            for (int i=0;i<attendances.length();i++){

                JSONObject object= attendances.getJSONObject(i);
                attendanceModel attendanceModel = new attendanceModel();

                String date = object.getString("date");

                ArrayList<recordsModel> recordsModels = new ArrayList<>();
                JSONArray records = object.getJSONArray("records");

                for(int j=0;j<records.length();j++) {
                    JSONObject record = records.getJSONObject(j);
                    recordsModel recordsModel = new recordsModel();
                    int classId = record.getInt("id");
                    String name = record.getString("name");
                    boolean present = record.getBoolean("present");

                    recordsModel.setClassId(classId);
                    recordsModel.setName(name);
                    recordsModel.setPresent(present);
                    recordsModels.add(recordsModel);
                }
                attendanceModel.setDate(date);
                attendanceModel.setRecordsModels(recordsModels);

                attendanceModels.add(attendanceModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addToRequestQueue(Request request) {
        getRequestQueue().add(request);
    }
    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getActivity());
        return requestQueue;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "muli_black.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.main_view));
    }

}
