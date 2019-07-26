package com.android.chalkboard.studentStory.topics;

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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.studentStory.CustomAdapter;
import com.android.chalkboard.studentStory.CustomModel;
import com.android.chalkboard.studentStory.classes.classModel;
import com.android.chalkboard.util.CustomDialog;
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

public class topics extends Fragment {

    private View view;
    private CustomDialog customDialog;
    private JSONArray jsonArray;
    private RequestQueue requestQueue;
    private ArrayList<topicsModel> data = new ArrayList<>();
    ListView topicsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_topics,container,false);

        ((NavDashBoardActivity)getActivity()).showFragmentToolbar("Topics");

        customDialog = new CustomDialog(getActivity());
        customDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        customDialog.setCanceledOnTouchOutside(false);

        topicsList = (ListView) view.findViewById(R.id.topicssList);

        topicsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((NavDashBoardActivity)getActivity()).replace_Fragment(new topicsList(data.get(i)),1);
            }
        });
        if(data.size()==0) {
            GetResult("https://schoolapp-2018.herokuapp.com/student/3/topics");
        }
        else {
            topicsList.setAdapter(new topicsAdapter());
        }


        return view;
    }

    private class topicsAdapter extends BaseAdapter  {

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

            if(view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.topics_item,viewGroup,false);
            }

            TextView subject = (TextView) view.findViewById(R.id.itemTitle);

            subject.setText(data.get(i).getSubject());

            return view;
        }
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
        try {
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object= jsonArray.getJSONObject(i);
                String subject = object.getString("subject");

                JSONArray topics = object.getJSONArray("topics");
                ArrayList<String> topicsArray = new ArrayList<>();
                for(int j=0;j<topics.length();j++) {
                    topicsArray.add(topics.getString(j));
                }

                topicsModel topicsModel = new topicsModel(subject,topicsArray);

                data.add(topicsModel);
            }
            topicsList.setAdapter(new topicsAdapter());

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
