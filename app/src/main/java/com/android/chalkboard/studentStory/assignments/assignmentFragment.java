package com.android.chalkboard.studentStory.assignments;

import android.content.DialogInterface;
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
import com.android.chalkboard.dashboard.view.DashboardFragment;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.studentStory.CustomAdapter;
import com.android.chalkboard.studentStory.amazon_s3;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class assignmentFragment extends Fragment {

    private View view;
    ListView assignList;
    private RequestQueue requestQueue;
    private JSONArray jsonArray;
    private CustomDialog customDialog;
    ArrayList<assignmentModel> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_assignments_student,container,false);

        ((NavDashBoardActivity)getActivity()).showFragmentToolbar("Assignments");

        assignList = (ListView) view.findViewById(R.id.assignment_list);
        customDialog = new CustomDialog(getActivity());
        customDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        customDialog.setCanceledOnTouchOutside(false);

        int id = getArguments().getInt("id");

        if(data.size()==0) {
            GetResult("https://schoolapp-2018.herokuapp.com/assignment/class/"+id);
        }
        else {
            assignList.setAdapter(new assignmentAdapter());
        }


        assignList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((NavDashBoardActivity)getActivity()).replace_Fragment(new assignDetails(data.get(i)),1);
            }
        });

        return view;
    }

    private class assignmentAdapter extends BaseAdapter {


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
                view = LayoutInflater.from(getContext()).inflate(R.layout.class_list_item_student,viewGroup,false);
            }

            assignmentModel assignmentModel = data.get(i);

            TextView title = (TextView) view.findViewById(R.id.itemTitle);
            TextView itemDesc = (TextView) view.findViewById(R.id.itemDesc);

            title.setText(assignmentModel.getTopic());
//            SimpleDateFormat format = new SimpleDateFormat("dd/MMMM/yy");
//            itemDesc.setText("Date : "+format.format(assignmentModel.getStartDate())+"   End Date : "+format.format(assignmentModel.getEndDate()));

            itemDesc.setText("From "+assignmentModel.getStartDate()+" to "+assignmentModel.getEndDate());

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
                                Log.d(DashboardFragment.TAG, response.toString());
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
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
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

            for(int i=0;i<jsonArray.length();i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                int classId = jsonObject.getInt("classId");
                int instId = jsonObject.getInt("institutionId");
                String topic = jsonObject.getString("topic");
                String content = jsonObject.getString("content");
                String startdate = jsonObject.getString("startDate");
                String enddate = jsonObject.getString("endDate");
                JSONArray images = jsonObject.getJSONArray("images");
                ArrayList<amazon_s3> imagesArray = new ArrayList<>();
                for(int j=0;j<images.length();j++) {

                    JSONObject image = images.getJSONObject(j);

                    String s3Key = image.getString("s3Key");
                    String readUrl = image.getString("readUrl");
                    String writeUrl = image.getString("writeUrl");

                    imagesArray.add(new amazon_s3(s3Key,readUrl,writeUrl));
                }
                data.add(new assignmentModel(id,classId,instId,topic,content,startdate,enddate,imagesArray));

            }

            assignList.setAdapter(new assignmentAdapter());

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
