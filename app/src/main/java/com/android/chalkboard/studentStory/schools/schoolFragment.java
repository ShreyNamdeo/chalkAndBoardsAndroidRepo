package com.android.chalkboard.studentStory.schools;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.register.model.Image;
import com.android.chalkboard.studentStory.CustomAdapter;
import com.android.chalkboard.studentStory.CustomModel;
import com.android.chalkboard.studentStory.classes.classFragment;
import com.android.chalkboard.util.CustomDialog;
import com.android.chalkboard.util.FontChangeCrawler;
import com.android.chalkboard.util.SharedPrefUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.chalkboard.dashboard.view.DashboardFragment.TAG;

public class schoolFragment extends Fragment {

    private View view;
    ListView schoolsList;
    ImageView searchSchool;
    private JSONArray jsonArray;
    ArrayList<CustomModel> data = new ArrayList<>();
    private RequestQueue requestQueue;
    CustomModel customModel;
    private ProgressBar progressBar;
    private CustomDialog customDialog;
    private String schoolName,address,principalName;
    private int school_id;
    private LinearLayout searchView;
    private EditText searchEdit;
    private CustomAdapter customAdapter;
    int count=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_school,container,false);

        ((NavDashBoardActivity)getActivity()).showFragmentToolbar("Schools");

        schoolsList = (ListView) view.findViewById(R.id.schoolList);
        searchSchool = (ImageView) view.findViewById(R.id.searchSchool);

        customDialog = new CustomDialog(getActivity());
        customDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        customDialog.setCanceledOnTouchOutside(false);

        searchEdit = (EditText) view.findViewById(R.id.searchEdit);
        schoolsList.setTextFilterEnabled(true);
        searchView = (LinearLayout) view.findViewById(R.id.searchView);

        searchSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if(count%2==1) {
                    searchView.setVisibility(View.VISIBLE);
                    searchEdit.requestFocus();
                    searchSchool.setImageResource(R.drawable.ic_cut);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(searchEdit, InputMethodManager.SHOW_IMPLICIT);

                }
                else {
                    searchSchool.setImageResource(R.drawable.search_icon );
                    searchView.setVisibility(View.GONE);
                }
            }
        });


        if(data.size()==0) {
            GetResult("https://schoolapp-2018.herokuapp.com/institution/Pune");
        }
        else {
            schoolsList.setAdapter(customAdapter);
        }


        schoolsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("schoolName",data.get(i).getName());
                bundle.putString("principalName",data.get(i).getPrincipal());
                bundle.putString("address",data.get(i).getAddress());
                bundle.putInt("id",data.get(i).getId());
                schoolDetails schoolDetails = new schoolDetails();
                schoolDetails.setArguments(bundle);
                ((NavDashBoardActivity)getActivity()).replace_Fragment(schoolDetails,1);
            }
        });



        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                customAdapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                customAdapter.filter(editable.toString());
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
        try {
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object= jsonArray.getJSONObject(i);
                school_id = object.getInt("id");
                schoolName = object.getString("name");
                String spec= object.getString("specialization");
                String city = object.getString("city");
                address = object.getString("address");
                principalName = object.getString("principalName");
                customModel = new CustomModel(school_id,schoolName,spec+", "+city,address,principalName);
                data.add(customModel);
            }
            customAdapter = new CustomAdapter(data,getActivity(),R.drawable.img_school,R.layout.class_list_item_student,1);
            schoolsList.setAdapter(customAdapter);
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
