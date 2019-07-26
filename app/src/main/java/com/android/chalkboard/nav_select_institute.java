package com.android.chalkboard;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.studentStory.CustomAdapter;
import com.android.chalkboard.studentStory.CustomModel;
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
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class nav_select_institute extends Fragment {

    private View view;
    SwipeMenuListView schoolsList;
    ImageView searchSchool;
    private JSONArray jsonArray;
    ArrayList<CustomModel> data = new ArrayList<>();
    private RequestQueue requestQueue;
    CustomModel customModel;
    private ProgressBar progressBar;
    private CustomDialog customDialog;
    private String schoolName, address, principalName;
    private int school_id;
    private LinearLayout searchView;
    private EditText searchEdit;
    private CustomAdapter customAdapter;
    int count = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.select_school, container, false);

        ((NavDashBoardActivity) getActivity()).showFragmentToolbar("Schools");

        schoolsList = (SwipeMenuListView) view.findViewById(R.id.schoolList);
        searchSchool = (ImageView) view.findViewById(R.id.searchSchool);
        customDialog = new CustomDialog(getActivity());
        customDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        customDialog.setCanceledOnTouchOutside(false);
        searchEdit = (EditText) view.findViewById(R.id.searchEdit);
        schoolsList.setTextFilterEnabled(true);
        searchView = (LinearLayout) view.findViewById(R.id.searchView);

        schoolsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        searchSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if (count % 2 == 1) {
                    searchView.setVisibility(View.VISIBLE);
                    searchEdit.requestFocus();
                    searchSchool.setImageResource(R.drawable.ic_cut);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(searchEdit, InputMethodManager.SHOW_IMPLICIT);

                } else {
                    searchSchool.setImageResource(R.drawable.search_icon);
                    searchView.setVisibility(View.GONE);
                }
            }
        });

        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
//                SwipeMenuItem openItem = new SwipeMenuItem(
//                        getActivity());
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                        0xCE)));
//                // set item width
//                openItem.setWidth(170);
//                // set item title
//                openItem.setTitle("Open");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem selectedItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                selectedItem.setBackground(new ColorDrawable(Color.rgb(59,
                        218, 104)));
                // set item width
                selectedItem.setWidth(190);
                // set a icon
                selectedItem.setIcon(R.drawable.checked);
                // add to menu
                menu.addMenuItem(selectedItem);
            }
        };

        schoolsList.setMenuCreator(swipeMenuCreator);

        schoolsList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        SharedPrefUtils.storeInSharedPref(getActivity(),
                                                            SharedPrefUtils.School_ID,
                                                            String.valueOf(((CustomModel)customAdapter.getItem(position)).getId()));

                        ((NavDashBoardActivity)getActivity()).onBackPressed();

                        break;
                    case 1:
                        // delete
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });


        GetResult("https://schoolapp-2018.herokuapp.com/institution/Pune");

        schoolsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                schoolsList.setItemChecked(i, true);
                LinearLayout swipe_left = (LinearLayout) view.findViewById(R.id.swipe_left);
                swipe_left.setVisibility(View.VISIBLE);

                AnimationSet animationSet = new AnimationSet(false);
                animationSet.addAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.null_tofade));
                animationSet.addAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.swipe_left_translate));
                swipe_left.startAnimation(animationSet);

//                ImageView check_image = (ImageView) view.findViewById(R.id.check_image);
//                check_image.setVisibility(View.VISIBLE);

//                Bundle bundle = new Bundle();
//                bundle.putString("schoolName",schoolName);
//                bundle.putString("principalName",principalName);
//                bundle.putString("address",address);
//                schoolDetails schoolDetails = new schoolDetails();
//                schoolDetails.setArguments(bundle);
//                ((NavDashBoardActivity)getActivity()).replace_Fragment(schoolDetails,1);
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
                        params.put("Authorization", "Basic ZXlKaGRYUm9iM0pwZW1GMGFXOXVJam9pT0RJd09EQTBNekEzTm40aFFDTjBaV0ZqYUdWeWZpRkFJeUlzSW1Gc1p5STZJa2hUTWpVMkluMC5lMzAuUHNnTHM2ZW1RcDdMM25ocUtpWHdTd3pnODBJVEcxa1lFTUgwejNINU5Fczog");
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
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                school_id = object.getInt("id");
                schoolName = object.getString("name");
                String spec = object.getString("specialization");
                String city = object.getString("city");
                address = object.getString("address");
                principalName = object.getString("principalName");
                customModel = new CustomModel(school_id,schoolName, spec + ", " + city);
                data.add(customModel);
            }
            customAdapter = new CustomAdapter(data, getActivity(), R.drawable.img_school, R.layout.school_select_item, 1);
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
