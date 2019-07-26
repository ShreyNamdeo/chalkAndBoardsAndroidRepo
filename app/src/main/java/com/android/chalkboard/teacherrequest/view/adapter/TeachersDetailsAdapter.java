package com.android.chalkboard.teacherrequest.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.teacherrequest.modal.ExperienceModal;

import java.util.ArrayList;

public class TeachersDetailsAdapter extends RecyclerView.Adapter<TeachersDetailsAdapter.ExperienceHolder> {

    private Context context;
    private ArrayList<ExperienceModal> expList;

    public TeachersDetailsAdapter(Context context, ArrayList<ExperienceModal> expList) {
        this.context = context;
        this.expList = expList;
    }

    @NonNull
    @Override
    public TeachersDetailsAdapter.ExperienceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.teacher_detail_item,viewGroup,false);
        return new TeachersDetailsAdapter.ExperienceHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ExperienceHolder experienceHolder, int position) {

        ExperienceModal experience = expList.get(position);
        experienceHolder.designation.setText(experience.getDesignation());
        experienceHolder.institute.setText(experience.getInstitute());
        experienceHolder.startDate.setText(experience.getStartDate());
        experienceHolder.endDate.setText(experience.getEndDate());
        experienceHolder.subject.setText(experience.getSubject());

    }

    @Override
    public int getItemCount() {
        return expList.size();
    }

    public class ExperienceHolder extends RecyclerView.ViewHolder {


        private TextView institute, startDate, endDate, subject, designation;

        public ExperienceHolder(@NonNull View itemView) {
            super(itemView);
            institute = itemView.findViewById(R.id.tv_institute_value);
            startDate = itemView.findViewById(R.id.tv_start_date_value);
            endDate = itemView.findViewById(R.id.tv_end_date_value);
            subject = itemView.findViewById(R.id.tv_subject_value);
            designation = itemView.findViewById(R.id.tv_designation_value);

        }
    }
}
