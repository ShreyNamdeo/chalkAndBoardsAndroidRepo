package com.android.chalkboard.studentStory.news;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.chalkboard.dashboard.view.DashboardFragment.TAG;

public class classNewsFragment extends Fragment {

    private View view;
    ListView newsList;
    private CustomDialog customDialog;
    private RequestQueue requestQueue;
    private JSONArray jsonArray;
    private ArrayList<NewsModel> data = new ArrayList<>();
    TextView class_level,institute_level;
    Typeface typeface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.class_news,container,false);

        ((NavDashBoardActivity)getActivity()).showFragmentToolbar("News");
        typeface = Typeface.createFromAsset(getContext().getAssets(),"muli_black.ttf");

        newsList = (ListView) view.findViewById(R.id.news_list);
        class_level = (TextView) view.findViewById(R.id.class_level);
        institute_level = (TextView) view.findViewById(R.id.institute_level);

        customDialog = new CustomDialog(getActivity());
        customDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        customDialog.setCanceledOnTouchOutside(false);

        onClassClick();

//        GetResult("https://schoolapp-2018.herokuapp.com/news/institution/"+SharedPrefUtils.getFromSharedPref(getActivity(),SharedPrefUtils.School_ID)+"/student");

        class_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onClassClick();
            }
        });

        institute_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                institute_level.setBackgroundTintList(getResources().getColorStateList(R.color.themeGreen));
                class_level.setBackgroundTintList(getResources().getColorStateList(R.color.appWhite));
                institute_level.setTextColor(getResources().getColor(R.color.appWhite));
                class_level.setTextColor(getResources().getColor(R.color.themeGreen));
                if(data.size()==0) {
                    GetResult("https://schoolapp-2018.herokuapp.com/admin/institution/"+SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.School_ID)+"/news",2);
                }
                else {
                    newsList.setAdapter(new newsAdapter());
                }


            }
        });


        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((NavDashBoardActivity)getActivity()).replace_Fragment(new newsDetails(data.get(i)),1);
            }
        });

        return view;
    }

    public void onClassClick() {
        class_level.setBackgroundTintList(getResources().getColorStateList(R.color.themeGreen));
        institute_level.setBackgroundTintList(getResources().getColorStateList(R.color.appWhite));
        class_level.setTextColor(getResources().getColor(R.color.appWhite));
        institute_level.setTextColor(getResources().getColor(R.color.themeGreen));
        if(data.size()==0) {
            GetResult("https://schoolapp-2018.herokuapp.com/teacher/class/2/news",1);
        }
        else {
            newsList.setAdapter(new newsAdapter());
        }

    }

    private class newsAdapter extends BaseAdapter {


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

            if(view==null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.class_news_item_student,viewGroup,false);
            }
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.null_tofade);
            animation.setDuration(500);
            view.setAnimation(animation);

            ImageView newsImage = (ImageView) view.findViewById(R.id.newsImage);
            TextView newsTitle = (TextView) view.findViewById(R.id.newsTitle);
            TextView newsDate = (TextView) view.findViewById(R.id.newsDate);

            newsTitle.setTypeface(typeface);
            newsDate.setTypeface(typeface);

            newsTitle.setText(data.get(i).getTitle());
//            SimpleDateFormat format = new SimpleDateFormat("dd/MMMM/yy");
//            newsDate.setText(format.format(data.get(i).getStartDate()));
            newsDate.setText(data.get(i).getDate());
            Log.e("Date",data.get(i).getDate());
            Picasso.get().load(data.get(i).getImage().getReadUrl()).placeholder(R.drawable.assignment).into(newsImage);

            return view;
        }
    }

    public void GetResult(final String mURL, final int i) {
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
                                        Log.e("Response",response.toString());
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
                                                GetResult(mURL,i);
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        if(i==1) {
                            params.put("Content-Type", "application/json");
                            params.put("Authorization", "Basic ZXlKaGRYUm9iM0pwZW1GMGFXOXVJam9pT0RJd09EQTBNekEzTm40aFFDTjBaV0ZqYUdWeWZpRkFJeUlzSW1Gc1p5STZJa2hUTWpVMkluMC5lMzAuUHNnTHM2ZW1RcDdMM25ocUtpWHdTd3pnODBJVEcxa1lFTUgwejNINU5Fczog");
                        }
                        else {
                            params.put("Content-Type", "application/json");
                            params.put("Authorization","Basic "+ SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.JWTToken));
                        }
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
                int id = object.getInt("id");
                String title = object.getString("title");
                String content = object.getString("content");
                String date = object.getString("date");
                String topic = object.getString("topic");
                String level = object.getString("level");
                JSONObject object1 = object.getJSONObject("image");
                String s3_key = object1.getString("s3Key");
                String readUrl = object1.getString("readUrl");
                String writeUrl = object1.getString("writeUrl");
                NewsModel newsModel = new NewsModel(id,title,content,topic,level,date,new amazon_s3(s3_key,readUrl,writeUrl));
                data.add(newsModel);
            }
            newsList.setAdapter(new newsAdapter());

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
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.news_main));
    }
}
