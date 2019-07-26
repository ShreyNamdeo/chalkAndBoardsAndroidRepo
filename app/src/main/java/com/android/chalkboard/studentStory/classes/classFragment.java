package com.android.chalkboard.studentStory.classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.studentStory.CustomAdapter;
import com.android.chalkboard.studentStory.CustomModel;
import com.android.chalkboard.studentStory.agenda;
import com.android.chalkboard.studentStory.assignments.assignmentFragment;
import com.android.chalkboard.studentStory.news.classNewsFragment;
import com.android.chalkboard.studentStory.test.testFragment;
import com.android.chalkboard.util.CustomDialog;
import com.android.chalkboard.util.FontChangeCrawler;
import com.android.chalkboard.util.SharedPrefUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.chalkboard.dashboard.view.DashboardFragment.TAG;

public class classFragment extends Fragment {

    private View view;
    ListView classList;
    private CustomDialog customDialog;
    private JSONArray jsonArray;
    private RequestQueue requestQueue;
    ArrayList<classModel> data = new ArrayList<>();
    ArrayList<CustomModel> data2 = new ArrayList<>();
    int choice = 0;
    String school_id;
    TextView inst_error;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_class,container,false);
        school_id = SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.School_ID);

        customDialog = new CustomDialog(getActivity());
        customDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        customDialog.setCanceledOnTouchOutside(false);

        choice = getArguments().getInt("choice");
        inst_error = (TextView) view.findViewById(R.id.inst_error);

        switch (choice) {
            case 0 :
                ((NavDashBoardActivity)getActivity()).showFragmentToolbar("Classes");
                break;

            case 1 :
                ((NavDashBoardActivity)getActivity()).showFragmentToolbar("Agenda");
                break;

            case 2:
                ((NavDashBoardActivity)getActivity()).showFragmentToolbar("Assignments");
                break;

            case 3:
                ((NavDashBoardActivity)getActivity()).showFragmentToolbar("Tests");
                break;

        }

        classList = (ListView) view.findViewById(R.id.class_list);

        choice = getArguments().getInt("choice");

        if(data.size()==0) {
            if(school_id == "" || school_id == null)
            {
                classList.setVisibility(View.GONE);
                inst_error.setVisibility(View.VISIBLE);
            }
            else {
                inst_error.setVisibility(View.GONE);
                GetResult("https://schoolapp-2018.herokuapp.com/institution/"+school_id+"/classes");
            }

        }
        else {
            CustomAdapter customAdapter = new CustomAdapter(data2,getActivity(),R.drawable.img_class,R.layout.class_list_item_student,2);
            classList.setAdapter(customAdapter);
        }



        classList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                agenda agenda = new agenda();
                Bundle bundle = new Bundle();
                bundle.putInt("id",data.get(i).getId());
                switch (choice) {


                    case 0:
                        bundle.putString("name",data.get(i).getClassName());
                        bundle.putString("school_name",data.get(i).getInstitutionName());
                        bundle.putString("start_time",data.get(i).getStartTime());
                        bundle.putString("end_time",data.get(i).getEndTime());
                        bundle.putString("standard",data.get(i).getStandard());
                        classDetails classDetails = new classDetails(1);
                        classDetails.setArguments(bundle);
                        ((NavDashBoardActivity)getActivity()).replace_Fragment(classDetails,1);

                        break;

                    case 1:

                        agenda.setArguments(bundle);

                        ((NavDashBoardActivity)getActivity()).replace_Fragment(agenda,1);

                        break;

                    case 2:

                        assignmentFragment assignmentFragment = new assignmentFragment();
                        assignmentFragment.setArguments(bundle);

                        ((NavDashBoardActivity)getActivity()).replace_Fragment(assignmentFragment,1);

                        break;

                    case 3:

                        testFragment testFragment = new testFragment();
                        testFragment.setArguments(bundle);
                        bundle.putString("className",data.get(i).getClassName());
                        ((NavDashBoardActivity)getActivity()).replace_Fragment(testFragment,1);

                        break;

                }

            }
        });
        return view;
    }

    public void GetResult(final String mURL) {
//        progressBar.setVisibility(View.VISIBLE);
        customDialog.show();
        JsonArrayRequest jsonObjectRequest = new
                JsonArrayRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());
//                                progressBar.s etVisibility(View.GONE);
                                customDialog.dismiss();
                                if (response.toString() != null) {
                                    try {
                                        jsonArray = new JSONArray(response.toString());
                                        ReadJson(jsonArray);
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

    public void ReadJson(JSONArray jsonArray) {
        data.clear();
        data2.clear();
        try {
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object= jsonArray.getJSONObject(i);
                int id = object.getInt("id");
                String className = object.getString("name");
                int instId = object.getInt("institutionId");
                String instName = object.getString("institutionName");
                String standard = object.getString("standard");
                String startTime = object.getString("startTime");
                String endTime = object.getString("endTime");
                long startDate = object.getLong("startDate");
                long endDate = object.getLong("endDate");
                String subject = object.getString("subject");

                classModel classModel = new classModel(id,instId,startDate,endDate,instName,standard,startTime,endTime,subject,className);
                CustomModel customModel = new CustomModel(id,className,startTime+" to "+endTime);

                data.add(classModel);
                data2.add(customModel);
            }
            CustomAdapter customAdapter = new CustomAdapter(data2,getActivity(),R.drawable.img_class,R.layout.class_list_item_student,2);
            classList.setAdapter(customAdapter);
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
