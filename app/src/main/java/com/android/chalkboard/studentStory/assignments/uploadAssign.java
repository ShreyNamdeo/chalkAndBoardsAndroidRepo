package com.android.chalkboard.studentStory.assignments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.register.model.Image;
import com.android.chalkboard.studentStory.amazon_s3;
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
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.chalkboard.dashboard.view.DashboardFragment.TAG;

public class uploadAssign extends Fragment {

    private View view;
    private Button button, submitAssign;
    public static final int GET_FROM_GALLERY_MULTIPLE = 3;
    ArrayList<Uri> images = new ArrayList<>();
    private RecyclerView assign_images;
    imagesAdapter imagesAdapter = new imagesAdapter();
    LinearLayoutManager linearLayoutManager;
    ImageView scroll_image;
    TextView scroll_text,title,desc;
    private CustomDialog customDialog;
    private RequestQueue requestQueue;
    private JSONObject jsonObject;
    private assignmentModel data;

    public uploadAssign() {
    }

    @SuppressLint("ValidFragment")
    public uploadAssign(assignmentModel data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.upload_assign_student, container, false);

        button = (Button) view.findViewById(R.id.uploadButton);
        assign_images = (RecyclerView) view.findViewById(R.id.assignment_images);
        scroll_image = (ImageView) view.findViewById(R.id.scroll_image);
        scroll_text = (TextView) view.findViewById(R.id.scroll_text);
        title = (TextView) view. findViewById(R.id.assTitleEdit);
        desc = (TextView) view.findViewById(R.id.assDescEdit);
        customDialog = new CustomDialog(getActivity());
        customDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        customDialog.setCanceledOnTouchOutside(false);
        submitAssign = (Button) view.findViewById(R.id.submitAssign);

        title.setText(data.getTopic());
        desc.setText(data.getContent());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Images"), GET_FROM_GALLERY_MULTIPLE);


                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                }

            }
        });

        assign_images.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Animation fade_appear = AnimationUtils.loadAnimation(getContext(), R.anim.fade_tonull);
                scroll_image.setAnimation(fade_appear);
                scroll_text.setAnimation(fade_appear);
                scroll_image.setVisibility(View.GONE);
                scroll_text.setVisibility(View.GONE);
            }
        });


        if (images.size() == 0) {
            assign_images.setVisibility(View.GONE);
        } else {
            assign_images.setVisibility(View.VISIBLE);
        }
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        assign_images.setLayoutManager(linearLayoutManager);
        assign_images.setAdapter(imagesAdapter);

        submitAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("topic", data.getTopic());
                    jsonObject.put("content", data.getContent());
                    jsonObject.put("startDate", data.getStartDate());
                    jsonObject.put("endDate", data.getEndDate());
                    jsonObject.put("submissionDate", data.getEndDate());
                    jsonObject.put("noOfImages", images.size());

                    String json = jsonObject.toString();

                    GetResult("https://schoolapp-2018.herokuapp.com/assignment/" + data.getId() + "/class/" + data.getClassId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return view;
    }

    private class imagesAdapter extends RecyclerView.Adapter<imagesAdapter.ViewHolder> {

        @NonNull
        @Override
        public imagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View listItem = layoutInflater.inflate(R.layout.assignments_image, viewGroup, false);
            imagesAdapter.ViewHolder viewHolder = new imagesAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull imagesAdapter.ViewHolder viewHolder, int i) {


            viewHolder.assignment_image.setImageURI(images.get(i));

        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView assignment_image;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                assignment_image = itemView.findViewById(R.id.assignment_image);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY_MULTIPLE && resultCode == Activity.RESULT_OK) {

            ClipData clipData = data.getClipData();

            if (clipData != null) {

                for (int i = 0; i < clipData.getItemCount(); i++) {

                    ClipData.Item item = clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    images.add(uri);
                }
            } else if (data.getData() != null) {
                Uri uri = data.getData();
                images.add(uri);
            }
        }

        imagesAdapter.notifyDataSetChanged();
        assign_images.smoothScrollToPosition(0);
        linearLayoutManager.scrollToPositionWithOffset(0, 0);
        if (images.size() == 0) {
            assign_images.setVisibility(View.GONE);
            scroll_image.setVisibility(View.GONE);
            scroll_text.setVisibility(View.GONE);
        } else if (images.size() >= 2) {
            Animation fade_appear = AnimationUtils.loadAnimation(getContext(), R.anim.null_tofade);
            scroll_image.setAnimation(fade_appear);
            scroll_text.setAnimation(fade_appear);
            scroll_image.setVisibility(View.VISIBLE);
            scroll_text.setVisibility(View.VISIBLE);
        } else {
            scroll_image.setVisibility(View.GONE);
            scroll_text.setVisibility(View.GONE);
            assign_images.setVisibility(View.VISIBLE);

        }

    }

    public void GetResult(final String mURL) {
        Log.e("URLLLL",mURL);
        customDialog.show();
        final String requestBody = jsonObject.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, mURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                try {
                    ReadJson(new JSONObject(response.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customDialog.dismiss();
                Toast.makeText(getContext(), "Unable to submit!", Toast.LENGTH_SHORT).show();
                Log.e("VOLLEY", error.toString());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
//                params.put("Content-Type", "application/json");
                params.put("Authorization","Basic "+ SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.JWTToken));
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                String responseString = "";
//                customDialog.dismiss();
//                if (response != null) {
//                    responseString = String.valueOf(response.statusCode);
//                    // can get more details such as response.headers
//                }
//                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//            }
        };

        addToRequestQueue(stringRequest);

    }



    public void ReadJson(JSONObject jsonObject) {

        try {
            JSONArray images = jsonObject.getJSONArray("images");

            for(int i=0;i<images.length();i++) {

                JSONObject image = images.getJSONObject(i);
//                String readUrl = image.getString("readUrl");
//                String writeUrl = image.getString("writeUrl");
//                Log.e("readUrl",image.getString("readUrl"));
                uploadImage(image.getString("writeUrl"),i);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    int mStatusCode = 0;

    public void uploadImage(String writeUrl,int index) {

//        Log.e("WriteUrl",writeUrl);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), images.get(index));
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //sending image to server
        StringRequest request = new StringRequest(Request.Method.PUT, writeUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                ;
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", imageString);
                return parameters;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                mStatusCode = response.statusCode;

                if (mStatusCode == 200) {
                    success("Uploaded Successful");
                } else {
                    success("Some error occurred!");
                }
                customDialog.dismiss();
                ((NavDashBoardActivity)getActivity()).onBackPressed();
                ((NavDashBoardActivity)getActivity()).onBackPressed();
                return super.parseNetworkResponse(response);
            }
        };



        addToRequestQueue(request);


    }

    public void success(String msgs) {

        final String msg = msgs;

        ((NavDashBoardActivity)getActivity()).runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
            }
        });

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
