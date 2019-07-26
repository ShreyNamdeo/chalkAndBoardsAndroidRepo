package com.android.chalkboard.studentStory.reports;

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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.studentStory.reports.Models.examModel;
import com.android.chalkboard.studentStory.reports.Models.resultModel;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.android.volley.VolleyLog.v;

public class reportsFragment extends Fragment {

    private View view;
    private RequestQueue requestQueue;
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    ArrayList<resultModel> resultModels = new ArrayList<>();

    TextView profile_roll,profile_name,profile_standard,profile_section,profile_house,profile_fn,profile_mn,profile_gn;
    TextView profile_admission,profile_dob,attPercentage,marks_total,marks_obtained,marks_percentage,grade;
    ProgressBar attendanceProgress;

    Button result_sheet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.reports_fragment,container,false);
        customDialog = new CustomDialog(getActivity());
        customDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        customDialog.setCanceledOnTouchOutside(false);

        ((NavDashBoardActivity)getActivity()).showFragmentToolbar("Reports");

        findIds();

//        ArrayList<examModel> marks = new ArrayList<>();
//        marks.add(new examModel(1,100,75,75.2,"Assignment","level-1"));
//        marks.add(new examModel(2,100,85,85.2,"Assignment-1","level-2"));
//        marks.add(new examModel(3,100,65,65.2,"Assignment-2","level-3"));
//        marks.add(new examModel(4,100,70,70.2,"Assignment-3","level-4"));
//        marks.add(new examModel(4,100,69,70.2,"Assignment-4","level-4"));
//        marks.add(new examModel(4,100,74,70.2,"Assignment-5","level-4"));

//        resultModels.add(new resultModel("English",marks));

        String id  = SharedPrefUtils.getFromSharedPref(getContext(),"id");

        if(jsonObject == null) {
            GetResult("https://schoolapp-2018.herokuapp.com/report/student/" + id + "/institution/" + SharedPrefUtils.getFromSharedPref(getContext(), SharedPrefUtils.School_ID));
        }
        else {
            ReadJson(jsonObject);
        }
        result_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavDashBoardActivity)getActivity()).replace_Fragment(new resultSheet(resultModels,resultModels.size()),1);
            }
        });

        return view;
    }



    public void GetResult(final String mURL) {
//        progressBar.setVisibility(View.VISIBLE);
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
        resultModels.clear();
        try {
            int id = jsonObject.getInt("id");
            String name = jsonObject.getString("name");
//            String rollNumber = jsonObject.getString("rollNumber");
//            String admissionNumber = jsonObject.getString("admissionNumber");
            String dob = jsonObject.getString("dateOfBirth");
            String standard = jsonObject.getString("standard");
//            String session = jsonObject.getString("session");
//            String className = jsonObject.getString("className");
            String section = jsonObject.getString("section");
            String house = jsonObject.getString("house");
            String fatherName = jsonObject.getString("fatherName");
            String motherName = jsonObject.getString("motherName");
            String guardianName = jsonObject.getString("guardianName");
//            String address = jsonObject.getString("address");
//            int height = jsonObject.getInt("height");
//            int weight = jsonObject.getInt("weight");
            int aP = jsonObject.getInt("attendancePercentage");
            int aggregateTotal = jsonObject.getInt("aggregateTotal");
            int aggregateObtained = jsonObject.getInt("aggregateObtained");
            double aggregatePercentage = jsonObject.getDouble("aggregatePercentage");
//            String grade = jsonObject.getString("grade");

//            profile_roll.setText(rollNumber);
//            profile_admission.setText(admissionNumber);
            profile_name.setText(name);
            profile_dob.setText(dob);
            profile_standard.setText(standard);
            profile_section.setText(section);
            profile_house.setText(house);
            profile_fn.setText(fatherName);
            profile_mn.setText(motherName);
            profile_gn.setText(guardianName);
            attPercentage.setText(String.valueOf(aP));
            marks_total.setText(String.valueOf(aggregateTotal));
            marks_obtained.setText(String.valueOf(aggregateObtained));
            marks_percentage.setText(String.valueOf(aggregatePercentage));

            attendanceProgress.setProgress(aP);


            JSONArray results = jsonObject.getJSONArray("results");


            for (int i=0;i<results.length();i++){

                JSONObject object= results.getJSONObject(i);
                String subject = object.getString("subject");

                ArrayList<examModel> examModels = new ArrayList<>();
                JSONArray marks = object.getJSONArray("marks");

                for(int j=0;j<marks.length();j++) {

                    JSONObject marksObject = marks.getJSONObject(j);

                    int examId = marksObject.getInt("id");
                    int classId = marksObject.getInt("classId");
                    String examName = marksObject.getString("title");
                    int total = marksObject.getInt("totalMarks");
                    int obtained = marksObject.getInt("obtainedMarks");
                    String date = marksObject.getString("date");
                    double percentage = (obtained/total)*100;
                    String level = marksObject.getString("level");
                    String result = marksObject.getString("result");

                    examModel examModel = new examModel(examId,total,obtained,percentage,examName,level,classId,result,date);
                    examModels.add(examModel);
                }

                resultModel resultModel = new resultModel(subject,examModels);
                resultModels.add(resultModel);
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

    private void findIds() {

        //profile_roll = (TextView) view.findViewById(R.id.profile_roll);
        profile_name = (TextView) view.findViewById(R.id.profile_name);
        profile_admission = (TextView) view.findViewById(R.id.profile_admission);
        profile_gn = (TextView) view.findViewById(R.id.profile_guardian);
        profile_dob = (TextView) view.findViewById(R.id.profile_dob);
        profile_house = (TextView) view.findViewById(R.id.profile_house);
        profile_mn = (TextView) view.findViewById(R.id.profile_mother);
        profile_section = (TextView) view.findViewById(R.id.profile_section);
        profile_standard = (TextView) view.findViewById(R.id.profile_standard);
        profile_fn = (TextView) view.findViewById(R.id.profile_father);
        attPercentage = (TextView) view.findViewById(R.id.att_percentage);
        marks_total = (TextView) view.findViewById(R.id.total);
        marks_obtained = (TextView) view.findViewById(R.id.obtained);
        marks_percentage = (TextView) view.findViewById(R.id.percentage);
        attendanceProgress = (ProgressBar) view.findViewById(R.id.itemProgress);
        //grade = (TextView) view.findViewById(R.id.grades);
        result_sheet = (Button) view.findViewById(R.id.result_sheet);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "muli_black.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.main_view));
    }
}
