package com.android.chalkboard.studentStory.profile;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.register.model.Image;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.android.volley.VolleyLog.v;

public class profileFragment extends Fragment {

    private View view;
    private RequestQueue requestQueue;
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    private ImageView edit_profile;

    TextView profile_id,profile_name,profile_standard,profile_section,profile_house,profile_fn,profile_mn,profile_gn;
    TextView profile_height,profile_weight,profile_waist,profile_tl,profile_shoulder,profile_chest;
    LinearLayout education_history;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile,container,false);
        customDialog = new CustomDialog(getActivity());
        customDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        customDialog.setCanceledOnTouchOutside(false);

        //Log.e("JWT", SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.JWTToken));

        ((NavDashBoardActivity)getActivity()).showFragmentToolbar("Profile");
        findIds();

        if(SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.STANDARD)!=null) {
            profile_standard.setText(SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.STANDARD));
        }

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_profile edit_profile = new edit_profile();

                Bundle bundle = new Bundle();
                bundle.putString("profile_id",profile_id.getText().toString());
                bundle.putString("profile_name",profile_name.getText().toString());
                bundle.putString("profile_standard",profile_standard.getText().toString());
                bundle.putString("profile_section",profile_section.getText().toString());
                bundle.putString("profile_house",profile_house.getText().toString());
                bundle.putString("profile_fn",profile_fn.getText().toString());
                bundle.putString("profile_mn",profile_mn.getText().toString());
                bundle.putString("profile_gn",profile_gn.getText().toString());
                bundle.putString("profile_height",profile_height.getText().toString());
                bundle.putString("profile_weight",profile_weight.getText().toString());
                bundle.putString("profile_waist",profile_waist.getText().toString());
                bundle.putString("profile_tl",profile_tl.getText().toString());
                bundle.putString("profile_shoulder",profile_shoulder.getText().toString());
                bundle.putString("profile_chest",profile_chest.getText().toString());

                edit_profile.setArguments(bundle);

                ((NavDashBoardActivity)getActivity()).replace_Fragment(edit_profile,1);
            }
        });

        GetResult("https://schoolapp-2018.herokuapp.com/student/profile?=");

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
                        params.put("Authorization","Basic "+SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.JWTToken));
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

            try {
                int id = jsonObject.getInt("studentId");
                profile_id.setText(String.valueOf(id));
            }catch (JSONException e) {
                profile_id.setText("---");
            }
            try {
                String name = jsonObject.getString("studentName");
                profile_name.setText(name);
            }catch (JSONException e) {
                profile_name.setText("---");
            }
            try {
                String standard = jsonObject.getString("standard");
                profile_standard.setText(standard);
            }catch (JSONException e) {
                profile_standard.setText("---");
            }
            try {
                String section = jsonObject.getString("section");
                profile_section.setText(section);
            }catch (JSONException e) {
                profile_section.setText("---");
            }
            try {
            String house = jsonObject.getString("house");
                profile_house.setText(house);
            }catch (JSONException e) {
                profile_house.setText("---");
            }
            try {
                String fatherName = jsonObject.getString("fatherName");
                profile_fn.setText(fatherName);
            }catch (JSONException e) {
                profile_fn.setText("---");
            }
            try {
                String motherName = jsonObject.getString("motherName");
                profile_mn.setText(motherName);
            }catch (JSONException e) {
                profile_mn.setText("---");
            }
            try {
                String guardianName = jsonObject.getString("guardianName");
                profile_gn.setText(guardianName);
            }catch (JSONException e) {
                profile_gn.setText("---");
            }
            try {
                double height = jsonObject.getDouble("height");
                profile_height.setText(String.valueOf(height)+" cm");
            }catch (JSONException e) {
                profile_height.setText("---");
            }
            try {
                double weight = jsonObject.getDouble("weight");
                profile_weight.setText(String.valueOf(weight)+" Kgs");
            }catch (JSONException e) {
                profile_weight.setText("---");
            }
            try {
                int waist = jsonObject.getInt("waist");
                profile_waist.setText(String.valueOf(waist)+"\'\'");
            }catch (JSONException e) {
                profile_waist.setText("---");
            }
            try {
                double trouserLength = jsonObject.getDouble("trouserLength");
                profile_tl.setText(String.valueOf(trouserLength)+"\'\'");
            }catch (JSONException e) {
                profile_tl.setText("---");
            }
            try {
                double shoulder = jsonObject.getDouble("shoulder");
                profile_shoulder.setText(String.valueOf(shoulder)+"\'\'");
            }catch (JSONException e) {
                profile_shoulder.setText("---");
            }
            try {
                double chest = jsonObject.getDouble("chest");
                profile_chest.setText(String.valueOf(chest)+"\'\'");
            }catch (JSONException e) {
                profile_chest.setText("---");
            }
            //String city = jsonObject.getString("city");
        try {
            JSONArray educationHistory = jsonObject.getJSONArray("educationHistory");
            LayoutInflater inflater = LayoutInflater.from(getContext());
            education_history.removeAllViews();

            for (int i = 0; i < educationHistory.length(); i++) {

                View view1 = inflater.inflate(R.layout.education_history_item, null);

                JSONObject object = educationHistory.getJSONObject(i);

                int eduId = object.getInt("id");
                String eduName = object.getString("name");
                int insitutionId = object.getInt("institutionId");
                String institutionName = object.getString("institutionName");
                String subject = object.getString("subject");
                String eduStandard = object.getString("standard");
                Long startDate = object.getLong("startDate");
                Long endDate = object.getLong("endDate");

                TextView className = (TextView) view1.findViewById(R.id.className);
                TextView institution_Name = (TextView) view1.findViewById(R.id.institute_name);
                TextView standard_View = (TextView) view1.findViewById(R.id.standard_View);
                TextView start_date = (TextView) view1.findViewById(R.id.start_date);
                TextView end_date = (TextView) view1.findViewById(R.id.end_date);


                className.setText(eduName);
                institution_Name.setText(institutionName);
                standard_View.setText("Standard : " + eduStandard);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                start_date.setText("Start Date : " + simpleDateFormat.format(new Date(startDate)));
                end_date.setText("End Date : " + simpleDateFormat.format(new Date(endDate)));


                education_history.addView(view1);

            }
        }catch (JSONException e) {
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
        edit_profile = (ImageView) view.findViewById(R.id.edit_profile);
        profile_id = (TextView) view.findViewById(R.id.profile_id);
        profile_name = (TextView) view.findViewById(R.id.profile_name);
        profile_chest = (TextView) view.findViewById(R.id.profile_chest);
        profile_gn = (TextView) view.findViewById(R.id.profile_guardian);
        profile_height = (TextView) view.findViewById(R.id.profile_height);
        profile_house = (TextView) view.findViewById(R.id.profile_house);
        profile_mn = (TextView) view.findViewById(R.id.profile_mother);
        profile_section = (TextView) view.findViewById(R.id.profile_section);
        profile_shoulder = (TextView) view.findViewById(R.id.profile_shoulder);
        profile_standard = (TextView) view.findViewById(R.id.profile_standard);
        profile_fn = (TextView) view.findViewById(R.id.profile_father);
        profile_waist = (TextView) view.findViewById(R.id.profile_waist);
        profile_weight = (TextView) view.findViewById(R.id.profile_weight);
        profile_tl = (TextView) view.findViewById(R.id.profile_tl);
        education_history = (LinearLayout) view.findViewById(R.id.education_history);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "muli_black.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.main_view));
    }
}
