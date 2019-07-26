package com.android.chalkboard.studentStory.attendance;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.studentStory.CustomAdapter;
import com.android.chalkboard.util.CustomDialog;
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
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.android.volley.VolleyLog.v;

public class elaboratedAttendance extends Fragment {

    private View view;
    private JSONObject jsonObject;
    private CustomDialog customDialog;
    private RequestQueue requestQueue;
    ArrayList<timelineModel> data = new ArrayList<>();
    TextView presentPercentage;
    ProgressBar progressBar;
    ListView elaboratedList;
    Calendar calendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_elaborated_attendance,container,false);

        customDialog = new CustomDialog(getActivity());
        customDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        customDialog.setCanceledOnTouchOutside(false);

        presentPercentage = (TextView) view.findViewById(R.id.present_percentage);
        progressBar = (ProgressBar) view.findViewById(R.id.presentProgress);
        progressBar.setProgressDrawable(getContext().getResources().getDrawable(R.drawable.progress_drawable));

        elaboratedList = (ListView) view.findViewById(R.id.elaboratedList);

        int id = getArguments().getInt("Id");

        GetResult("https://schoolapp-2018.herokuapp.com/student/class/"+id+"/attendance");

        elaboratedList.setAdapter(new elaboratedAdapter());


        return view;
    }

    private class elaboratedAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = LayoutInflater.from(getContext()).inflate(R.layout.elaborated_list_item,viewGroup,false);

            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.null_tofade);
            animation.setDuration(500);
            view.setAnimation(animation);

            timelineModel timelineModel = data.get(i);

            TextView day = (TextView) view.findViewById(R.id.day);
            TextView date = (TextView) view.findViewById(R.id.date);
            ImageView attendance_image = (ImageView) view.findViewById(R.id.attendance_image);

            SimpleDateFormat format = new SimpleDateFormat("EEEE");
            date.setText(timelineModel.getDate());

            try {
                day.setText(format.parse(timelineModel.getDate()).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(timelineModel.getAttendance())
                attendance_image.setImageResource(R.drawable.present);
            else
                attendance_image.setImageResource(R.drawable.absent);

            return view;
        }
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
        data.clear();
        try {

            int id = jsonObject.getInt("id");
            String name = jsonObject.getString("name");
            ((NavDashBoardActivity)getActivity()).showFragmentToolbar(name);
            int present = jsonObject.getInt("present");

            presentPercentage.setText(present*100+"%");
            progressBar.setProgress(present*100);

            JSONArray timeline = jsonObject.getJSONArray("timeline");

            for(int i=0;i<timeline.length();i++) {
                JSONObject jsonObject1 = timeline.getJSONObject(i);
                String date = jsonObject1.getString("date");
                boolean present_bool = jsonObject1.getBoolean("present");
                timelineModel timelineModel = new timelineModel(date,present_bool);
                data.add(timelineModel);
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
}