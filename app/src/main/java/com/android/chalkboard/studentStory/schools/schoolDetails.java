package com.android.chalkboard.studentStory.schools;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.studentStory.ClassModel;
import com.android.chalkboard.studentStory.CustomAdapter;
import com.android.chalkboard.studentStory.CustomModel;
import com.android.chalkboard.util.CustomDialog;
import com.android.chalkboard.util.FontChangeCrawler;
import com.android.chalkboard.util.SharedPrefUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.chalkboard.studentStory.classes.classDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.chalkboard.dashboard.view.DashboardFragment.TAG;


public class schoolDetails extends Fragment {

    private View view;
    private TextView schoolName,address,principal;
    Button cancel,join;
    private CustomDialog customDialog;
    private RequestQueue requestQueue;
    private JSONArray jsonArray;
    private ArrayList<ClassModel> data = new ArrayList<>();
    private RecyclerView classRecycler;
    LinearLayout buttons;
    int height;
    int instID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_school_details,container,false);

        customDialog = new CustomDialog(getActivity());
        customDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        customDialog.setCanceledOnTouchOutside(false);

        schoolName = (TextView) view.findViewById(R.id.assignTitle);
        address = (TextView) view.findViewById(R.id.assignTitle1);
        principal = (TextView) view.findViewById(R.id.assignTitle2);
        cancel = (Button) view.findViewById(R.id.submitAssign);
        join = (Button) view.findViewById(R.id.replyAssign);
        classRecycler = (RecyclerView) view.findViewById(R.id.classesRecycler);
        buttons = (LinearLayout) view.findViewById(R.id.buttons);

        if(SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.ROLE).equalsIgnoreCase("Student")) {
            buttons.setVisibility(View.GONE);
        }
        classRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));

        instID = getArguments().getInt("id");
        //String jwt = SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.JWTToken);

        schoolName.setText(getArguments().getString("schoolName"));
        address.setText(getArguments().getString("address"));
        principal.setText(getArguments().getString("principalName"));
        cancel.setText("CANCEL");
        join.setText("JOIN");

        //https://schoolapp-2018.herokuapp.com

//        data.add(new ClassModel(1,"English Class","10th","10:00 AM","11:00 AM",1500123123L,1500131231L,"English"));
//        data.add(new ClassModel(2,"Physics Class","11th","10:00 AM","11:00 AM",1500123123L,1500131231L,"Physics"));
//        data.add(new ClassModel(3,"Chemistry Class","12th","10:00 AM","11:00 AM",1500123123L,1500131231L,"Chemistry"));

        GetResult("https://schoolapp-2018.herokuapp.com/institution/"+instID+"/classes");

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GetResult2("https://schoolapp-2018.herokuapp.com/institution/"+instID+"/join");
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavDashBoardActivity)getActivity()).onBackPressed();
            }
        });

        return view;
    }

    public void GetResult(final String mURL) {
//        progressBar.setVisibility(View.VISIBLE);
        customDialog.show();
        Log.e("URL",mURL);
        Log.e("URL",SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.JWTToken));
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
                int class_id = object.getInt("id");
                String name = object.getString("name");
                String standard= object.getString("standard");
                String subject = object.getString("subject");
                String startTime = object.getString("startTime");
                String endTime = object.getString("endTime");
                Long startDate = object.getLong("startDate");
                Long endDate = object.getLong("endDate");

                ClassModel classModel = new ClassModel(class_id,name,standard,startTime,endTime,startDate,endDate,subject);
                data.add(classModel);
            }
            classRecycler.setAdapter(new RecyclerAdapter());
            classRecycler.smoothScrollToPosition(0);

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

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        public int minHeight = 0;
        public CardView class_card;
        int pos;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View listItem= layoutInflater.inflate(R.layout.clas_recycler_item, viewGroup,false);

            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            viewHolder.class_standard.setText("Standard : "+data.get(i).getStandard());
            viewHolder.class_subject.setText(data.get(i).getSubject());
            pos = i;
            viewHolder.item_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    classDetails classDetails = new classDetails(2);
                    Bundle bundle = new Bundle();
                    bundle.putString("name",data.get(pos).getName());
                    bundle.putString("school_name",getArguments().getString("schoolName"));
                    bundle.putString("start_time",data.get(pos).getStartTime());
                    bundle.putString("end_time",data.get(pos).getEndTime());
                    bundle.putString("standard",data.get(pos).getStandard());
                    bundle.putInt("id",data.get(pos).getClassID());
                    bundle.putInt("school_id",instID);
                    classDetails.setArguments(bundle);
                    ((NavDashBoardActivity)getActivity()).replace_Fragment(classDetails,1);
                }
            });
        }


        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {


            public TextView class_subject,class_standard;
            public LinearLayout item_parent;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.class_standard = (TextView) itemView.findViewById(R.id.class_standard);
                this.class_subject = (TextView) itemView.findViewById(R.id.class_subject);
                this.item_parent = (LinearLayout) itemView.findViewById(R.id.item_parent);
                class_card = (CardView) itemView.findViewById(R.id.class_card);


                class_card.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        class_card.getViewTreeObserver().removeOnPreDrawListener(this);
                        minHeight = class_card.getHeight();
                        ViewGroup.LayoutParams layoutParams = class_card.getLayoutParams();
                        layoutParams.height = minHeight;
                        class_card.setLayoutParams(layoutParams);

                        return true;
                    }
                });
            }


        }
    }
}
