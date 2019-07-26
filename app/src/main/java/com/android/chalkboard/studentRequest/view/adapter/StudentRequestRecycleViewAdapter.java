package com.android.chalkboard.studentRequest.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.studentRequest.model.StudentRequestResponse;
import com.android.chalkboard.util.OnStudentRequestItemClickListener;
import com.android.chalkboard.util.PNButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class StudentRequestRecycleViewAdapter extends RecyclerView.Adapter<StudentRequestRecycleViewAdapter.StudentRequestViewHolder> implements Filterable{
    private ArrayList<StudentRequestResponse> responses, responseAll;
    private OnStudentRequestItemClickListener itemClickListener;
    private Context context;

    public StudentRequestRecycleViewAdapter(Context context, ArrayList<StudentRequestResponse> responses, OnStudentRequestItemClickListener itemClickListener){
        this.context = context;
        this.responses = responses;
        this.responseAll = responses;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount(){
        return responses.size();
    }

    @NonNull
    @Override
    public StudentRequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(context).inflate(R.layout.student_request_items, viewGroup, false);
        //view.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        return new StudentRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentRequestViewHolder viewHolder, final int i){
        viewHolder.sName.setText(responses.get(i).getName());
        viewHolder.cName.setText(responses.get(i).getClassName());
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.school_list_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(circularProgressDrawable);
        if(responses.get(i).getImages() != null)
            if(!responses.get(i).getImages().getReadUrl().isEmpty())
                Glide.with(context).load(responses.get(i).getImages().getReadUrl())
                .apply(options).into(viewHolder.profilePic);

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onStudentRequestItemClick(responses.get(i));
            }
        });

        viewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onStudentRequestItemAcceptClick(responses.get(i));
            }
        });

        viewHolder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onStudentRequestItemRejectClick(responses.get(i));
            }
        });
    }

    @Override
    public Filter getFilter(){
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence charSequence){
                ArrayList<StudentRequestResponse> response = new ArrayList<>();
                String query = charSequence.toString();
                if(query.isEmpty())
                    response = responseAll;
                else{
                    for(StudentRequestResponse r: responseAll)
                        if(r.getName().contains(query))
                            response.add(r);
                }

                FilterResults fr = new FilterResults();
                fr.count = response.size();
                fr.values = response;

                return fr;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults){
                responses = (ArrayList<StudentRequestResponse>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class StudentRequestViewHolder extends RecyclerView.ViewHolder{
        private ImageView profilePic;
        private TextView sName, cName;
        private PNButton accept, reject;
        private RelativeLayout container;

        public StudentRequestViewHolder(@NonNull View view){
            super(view);
            container = view.findViewById(R.id.student_request_container);
            profilePic = view.findViewById(R.id.studentPic);
            sName = view.findViewById(R.id.studentName);
            cName = view.findViewById(R.id.schoolName);
            accept = view.findViewById(R.id.accept);
            reject = view.findViewById(R.id.reject);
        }

    }
}
