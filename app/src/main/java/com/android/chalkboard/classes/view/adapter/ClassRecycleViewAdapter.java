package com.android.chalkboard.classes.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.classes.model.Class;
import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.util.OnClassItemCLickListener;
import com.android.chalkboard.util.SharedPrefUtils;

import java.util.ArrayList;
import java.util.List;

public class ClassRecycleViewAdapter extends RecyclerView.Adapter<ClassRecycleViewAdapter.ClassViewHolder> implements Filterable{
    private ArrayList<ClassResponse> classList, classListAll;
    private Context context;
    PopupWindow p;
    OnClassItemCLickListener onClassItemCLickListener;

    public ClassRecycleViewAdapter(Context context, OnClassItemCLickListener onClassItemCLickListener, ArrayList<ClassResponse> classList){
        this.context = context;
        this.classList = classList;
        this.classListAll = classList;
        this.onClassItemCLickListener = onClassItemCLickListener;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(context).inflate(R.layout.class_list_items, viewGroup, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ClassViewHolder classViewHolder, final int i){
        classViewHolder.schoolNameTv.setText(classList.get(i).getName());
        ArrayList<SchoolListResponse> sl = SharedPrefUtils.retrieveRequestedInstituteList(context);
        String sName = "";
        for(SchoolListResponse r: sl)
            if(r.getId() == classList.get(i).getInstitutionId())
                sName = r.getName();
        classViewHolder.schoolAddressTv.setText(sName);
        classViewHolder.optionIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onClassItemCLickListener.onClassItemOptionMenuClick(view, classList.get(i));

            }
        });

        classViewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClassItemCLickListener.onClassItemEditClick(classList.get(i));
            }
        });

        classViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClassItemCLickListener.onClassItemDeleteClick(classList.get(i));
            }
        });
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<ClassResponse> r = new ArrayList<>();

                if(charSequence == null || charSequence.toString().isEmpty())
                    r = classListAll;
                else{
                    String query = charSequence.toString().toLowerCase().trim();
                    for(ClassResponse c: classList){
                        if(c.getName().toLowerCase().contains(query))
                            r.add(c);
                    }
                }

                FilterResults fr = new FilterResults();
                fr.count = r.size();
                fr.values = r;

                return fr;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                classList = (ArrayList<ClassResponse>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    public void clearFilter(){
        classList = classListAll;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount(){
        return classList.size();
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder{
        private ImageView schoolAdminIv,optionIv;
        private TextView schoolNameTv,schoolAddressTv;
        private LinearLayout schoolItemContainer;
        private Button editButton,deleteButton;
        public ClassViewHolder(@NonNull View viewItem){
            super(viewItem);
            schoolAdminIv = itemView.findViewById(R.id.school_admin_item_image);
            schoolNameTv = itemView.findViewById(R.id.school_name);
            schoolAddressTv = itemView.findViewById(R.id.school_address);
            schoolItemContainer = itemView.findViewById(R.id.school_item_container);
            optionIv = itemView.findViewById(R.id.school_item_options);
            editButton = itemView.findViewById(R.id.btn_edit);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }
    }
}
