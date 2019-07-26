package com.android.chalkboard.studentRequest.view;

import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v4.widget.CircularProgressDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.studentRequest.model.StudentRequestResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;

import static android.view.View.GONE;

public class ShowStudentProfileFragment extends DialogFragment{
    private StudentRequestResponse response;

    public static ShowStudentProfileFragment newInstance(StudentRequestResponse response){
        Bundle b = new Bundle();
        b.putSerializable("studentResponse", response);
        ShowStudentProfileFragment sf = new ShowStudentProfileFragment();
        sf.setArguments(b);

        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        if(getArguments() != null){
            response = (StudentRequestResponse)getArguments().getSerializable("studentResponse");
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.show_student_profile, parent, false);
        ImageView pic = v.findViewById(R.id.profilePic);
        TextView requestId = v.findViewById(R.id.requestId);
        TextView studentId = v.findViewById(R.id.studentId);
        TextView studentName = v.findViewById(R.id.studentName);
        TextView studentEmail = v.findViewById(R.id.studentEmail);
        TextView studentMobile = v.findViewById(R.id.studentMobile);
        TextView requestedOn = v.findViewById(R.id.requestedOn);

        System.out.println(response.getRequestId());
        requestId.setTextColor(getResources().getColor(R.color.colorAccent));
        requestId.setText(String.valueOf(response.getRequestId()));
        studentId.setText(String.valueOf(response.getId()));
        studentName.setText(response.getName());
        studentEmail.setText(response.getEmail());
        studentMobile.setText(response.getMobileNumber());
        requestedOn.setText(response.getCreated());

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getActivity());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        RequestOptions options = new RequestOptions().fitCenter().error(R.drawable.school_list_icon).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(circularProgressDrawable);
        if(response.getImages() != null && !response.getImages().getReadUrl().isEmpty()) {
            Glide.with(getActivity()).load(response.getImages().getReadUrl()).apply(options).into(pic);
            v.findViewById(R.id.emptyMessage).setVisibility(GONE);
        }

        Button cancel = v.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        
        return v;
    }
}
