package com.android.chalkboard.studentStory.profile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.util.CustomDialog;
import com.android.chalkboard.util.FontChangeCrawler;
import com.android.chalkboard.util.SharedPrefUtils;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class edit_profile extends Fragment {

    private View view;
    EditText profile_id, profile_name, profile_standard, profile_section, profile_house, profile_fn, profile_mn, profile_gn;
    EditText profile_height, profile_weight, profile_waist, profile_tl, profile_shoulder, profile_chest;
    Button save_edit;
    private CustomDialog customDialog;
    private RequestQueue requestQueue;
    private JSONObject jsonObject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        customDialog = new CustomDialog(getActivity());
        customDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        customDialog.setCanceledOnTouchOutside(false);

        ((NavDashBoardActivity) getActivity()).showFragmentToolbar("Edit Profile");

//        Log.e("AUTHORIZATION",SharedPrefUtils.getFromSharedPref(getContext(),SharedPrefUtils.JWTToken));

        findIds();

        profile_id.setText(getArguments().getString("profile_id"));
        profile_name.setText(getArguments().getString("profile_name"));
        profile_standard.setText(getArguments().getString("profile_standard"));
        profile_section.setText(getArguments().getString("profile_section"));
        profile_house.setText(getArguments().getString("profile_house"));
        profile_fn.setText(getArguments().getString("profile_fn"));
        profile_mn.setText(getArguments().getString("profile_mn"));
        profile_gn.setText(getArguments().getString("profile_gn"));
        profile_height.setText(getArguments().getString("profile_height"));
        profile_weight.setText(getArguments().getString("profile_weight"));
        profile_waist.setText(getArguments().getString("profile_waist"));
        profile_tl.setText(getArguments().getString("profile_tl"));
        profile_shoulder.setText(getArguments().getString("profile_shoulder"));
        profile_chest.setText(getArguments().getString("profile_chest"));

        save_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("studentId", profile_id.getText().toString());
                    jsonObject.put("studentName", profile_name.getText().toString());
                    if (profile_standard.getText().toString() != "---") {
                        jsonObject.put("standard", profile_standard.getText().toString());
                    } else {
                        jsonObject.put("standard", "1");
                    }
                    jsonObject.put("section", profile_section.getText().toString());
                    jsonObject.put("house", profile_house.getText().toString());
                    jsonObject.put("fatherName", profile_fn.getText().toString());
                    jsonObject.put("motherName", profile_mn.getText().toString());
                    jsonObject.put("guardianName", profile_gn.getText().toString());
                    jsonObject.put("height", (int) Double.parseDouble(profile_height.getText().toString().split(" ")[0]));
                    jsonObject.put("weight", (int) Double.parseDouble(profile_weight.getText().toString().split(" ")[0]));
                    jsonObject.put("waist", (int) Double.parseDouble(profile_waist.getText().toString().split("'")[0]));
                    jsonObject.put("trouserLength", (int) Double.parseDouble(profile_tl.getText().toString().split("'")[0]));
                    jsonObject.put("shoulder", (int) Double.parseDouble(profile_shoulder.getText().toString().split("'")[0]));
                    jsonObject.put("chest", (int) Double.parseDouble(profile_chest.getText().toString().split("'")[0]));

//                    GetResult("https://schoolapp-2018.herokuapp.com/student/profile");
                    UpdateData updateData = new UpdateData("https://schoolapp-2018.herokuapp.com/student/profile");
                    updateData.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void findIds() {
        save_edit = (Button) view.findViewById(R.id.save_edit);
        profile_id = (EditText) view.findViewById(R.id.profile_id);
        profile_name = (EditText) view.findViewById(R.id.profile_name);
        profile_chest = (EditText) view.findViewById(R.id.profile_chest);
        profile_gn = (EditText) view.findViewById(R.id.profile_guardian);
        profile_height = (EditText) view.findViewById(R.id.profile_height);
        profile_house = (EditText) view.findViewById(R.id.profile_house);
        profile_mn = (EditText) view.findViewById(R.id.profile_mother);
        profile_section = (EditText) view.findViewById(R.id.profile_section);
        profile_shoulder = (EditText) view.findViewById(R.id.profile_shoulder);
        profile_standard = (EditText) view.findViewById(R.id.profile_standard);
        profile_fn = (EditText) view.findViewById(R.id.profile_father);
        profile_waist = (EditText) view.findViewById(R.id.profile_waist);
        profile_weight = (EditText) view.findViewById(R.id.profile_weight);
        profile_tl = (EditText) view.findViewById(R.id.profile_tl);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "muli_black.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.main_view));
    }

    public class UpdateData extends AsyncTask<String, Void, String> {

        String mUrl;

        public UpdateData(String mUrl) {
            this.mUrl = mUrl;
        }

        @Override
        protected void onPostExecute(String result) {

            customDialog.dismiss();
            Log.e("UPDATE RESULT", result);
            if (result!=null) {
                Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to update!", Toast.LENGTH_SHORT).show();
            }
            ((NavDashBoardActivity) getActivity()).onBackPressed();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialog.show();
        }

        //String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(mUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Basic " + SharedPrefUtils.getFromSharedPref(getContext(), SharedPrefUtils.JWTToken));
                OutputStream os = connection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = jsonObject.toString();
                Log.e("ASYNC DATA", data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder builder = new StringBuilder();
                while ((data = br.readLine()) != null) {
                    builder.append(data + "\n");
                }
                br.close();
                is.close();
                return builder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
