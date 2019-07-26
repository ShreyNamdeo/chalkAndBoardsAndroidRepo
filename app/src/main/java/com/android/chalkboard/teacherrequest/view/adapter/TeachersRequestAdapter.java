package com.android.chalkboard.teacherrequest.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.teacherrequest.modal.TeachersRequestDetails;
import com.android.chalkboard.util.OnRecyclerItemClickListener;
import com.android.chalkboard.util.PNButton;

import java.util.ArrayList;

public class TeachersRequestAdapter extends RecyclerView.Adapter<TeachersRequestAdapter.TeacherRequestViewHolder> {

    private Context context;
    private ArrayList<TeachersRequestDetails> teachersRequestDetailsArrayList;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public TeachersRequestAdapter(Context context, ArrayList<TeachersRequestDetails> listTeachersRequestDetails, OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.context = context;
        this.teachersRequestDetailsArrayList = listTeachersRequestDetails;
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    @NonNull
    @Override
    public TeachersRequestAdapter.TeacherRequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.school_admin_items,viewGroup,false);
        return new TeachersRequestAdapter.TeacherRequestViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull TeacherRequestViewHolder teacherRequestViewHolder, int position) {

       TeachersRequestDetails teachersRequestDetails = teachersRequestDetailsArrayList.get(position);
       teacherRequestViewHolder.teachersName.setText(teachersRequestDetails.getTeachersName());
       teacherRequestViewHolder.subject.setText(teachersRequestDetails.getSubjectName());
       teacherRequestViewHolder.positiveButton.setText(context.getString(R.string.accept_text));
       teacherRequestViewHolder.negativeButton.setText(context.getString(R.string.reject_text));
       teacherRequestViewHolder.teachersImage.setImageResource(teachersRequestDetails.getTeachersImage());

    }

    @Override
    public int getItemCount() {
        return teachersRequestDetailsArrayList.size();
    }

    public class TeacherRequestViewHolder extends RecyclerView.ViewHolder {

        private ImageView teachersImage;
        private TextView teachersName, subject;
        private PNButton positiveButton, negativeButton;

        public TeacherRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            teachersImage = itemView.findViewById(R.id.school_admin_item_image);
            teachersName = itemView.findViewById(R.id.school_name);
            subject = itemView.findViewById(R.id.school_address);
            positiveButton = itemView.findViewById(R.id.btn_edit);
            negativeButton = itemView.findViewById(R.id.btn_delete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerItemClickListener.onClick("id");
                }
            });
        }
    }
}
