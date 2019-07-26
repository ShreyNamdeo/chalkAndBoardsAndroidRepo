package com.android.chalkboard.teacherrequest.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.teacherrequest.modal.Experience;
import com.android.chalkboard.teacherrequest.modal.ExperienceModal;
import com.android.chalkboard.teacherrequest.modal.TeachersRequestDetails;
import com.android.chalkboard.teacherrequest.view.adapter.TeachersDetailsAdapter;

import java.util.ArrayList;


public class TeacherDetailsFragment extends Fragment {
    private TextView tvName, tvSubject, tvAddress, tvCity;
    private RecyclerView teacherExperience;
    private TeachersDetailsAdapter teachersDetailsAdapter;
    private ArrayList<ExperienceModal> expList;
    private ImageView teachersImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_teacher_details, container, false);
        tvName = view.findViewById(R.id.tv_name_value);
        tvSubject = view.findViewById(R.id.tv_subject_value);
       /* tvAddress = view.findViewById(R.id.tv_address_value);
        tvCity = view.findViewById(R.id.tv_city_value);*/
        teachersImage = view.findViewById(R.id.iv_teachers_image);
        teacherExperience = view.findViewById(R.id.rv_teacher_experience);
        expList = new ArrayList<>();
        loadData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        teacherExperience.setLayoutManager(linearLayoutManager);
        teachersDetailsAdapter = new TeachersDetailsAdapter(getActivity(), expList);
        teacherExperience.setAdapter(teachersDetailsAdapter);
        return view;
    }

    private void loadData() {

        tvName.setText("Prabhat Singh");
        /*tvCity.setText("Pune");
        tvAddress.setText("Vishal Nagar");*/
        tvSubject.setText("Maths");
        teachersImage.setImageResource(R.drawable.school_list_icon);


        expList.add(new ExperienceModal("20/10/12","20/12/18", "St Joseph's", "Maths", "Senior Teacher"));
        expList.add(new ExperienceModal("20/10/12","20/12/18", "St Joseph's", "Maths", "Senior Teacher"));
        expList.add(new ExperienceModal("20/10/12","20/12/18", "St Joseph's", "Maths", "Senior Teacher"));
        expList.add(new ExperienceModal("20/10/12","20/12/18", "St Joseph's", "Maths", "Senior Teacher"));
        expList.add(new ExperienceModal("20/10/12","20/12/18", "St Joseph's", "Maths", "Senior Teacher"));
        expList.add(new ExperienceModal("20/10/12","20/12/18", "St Joseph's", "Maths", "Senior Teacher"));

        }


}
