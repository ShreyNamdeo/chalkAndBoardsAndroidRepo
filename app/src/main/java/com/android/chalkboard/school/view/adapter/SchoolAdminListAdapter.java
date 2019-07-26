package com.android.chalkboard.school.view.adapter;

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
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.school.model.SchoolAdminItem;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.util.OnSchoolListItemClickListener;
import com.android.chalkboard.util.SharedPrefUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class SchoolAdminListAdapter extends RecyclerView.Adapter<SchoolAdminListAdapter.AdminListViewHolder> implements Filterable {
    private Context context;
    private ArrayList<SchoolListResponse> schoolAdminItems;
    private ArrayList<SchoolListResponse> schoolAdminItemsAll;
    private OnSchoolListItemClickListener itemClickListener;
    private String role;

    public SchoolAdminListAdapter(Context context, ArrayList<SchoolListResponse> schoolAdminItems,OnSchoolListItemClickListener itemClickListener) {
        this.context = context;
        this.schoolAdminItems = schoolAdminItems;
        this.schoolAdminItemsAll = schoolAdminItems;
        this.itemClickListener = itemClickListener;
        role = SharedPrefUtils.getFromSharedPref(this.context, SharedPrefUtils.ROLE);
    }

    @NonNull
    @Override
    public AdminListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.school_admin_items,viewGroup,false);
        return new AdminListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminListViewHolder adminListViewHolder, final int i) {
        //adminListViewHolder.schoolAdminIv.setImageResource(schoolAdminItems.get(i).getSchoolImage());
        adminListViewHolder.schoolNameTv.setText(schoolAdminItems.get(i).getName());
        adminListViewHolder.schoolAddressTv.setText(schoolAdminItems.get(i).getCity());
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.school_list_icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(circularProgressDrawable);
        Glide.with(context).load(schoolAdminItems.get(i).getImages().get(0).getReadUrl())
                .apply(options).into(adminListViewHolder.schoolAdminIv);
        adminListViewHolder.schoolItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onSchoolItemClick(schoolAdminItems.get(i));
            }
        });

        if(role.equalsIgnoreCase("teacher")){
            adminListViewHolder.editButton.setVisibility(View.INVISIBLE);
            adminListViewHolder.optionIv.setVisibility(View.INVISIBLE);
        } else {
            adminListViewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onSchoolItemEditClick(schoolAdminItems.get(i));
                }
            });
            adminListViewHolder.optionIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onSchoolItemOptionMenuClick(schoolAdminItems.get(i));
                }
            });
        }

        adminListViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onSchoolItemDeleteClick(schoolAdminItems.get(i));
            }
        });

    }



    @Override
    public int getItemCount() {
        return schoolAdminItems.size();
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString().toLowerCase().trim();
                ArrayList<SchoolListResponse> r = new ArrayList<>();

                if(query.equals(""))
                    r = schoolAdminItemsAll;
                else{
                    for(SchoolListResponse response: schoolAdminItemsAll){
                        if(response.getName().toLowerCase().contains(query))
                            r.add(response);
                    }
                }

                FilterResults fr = new FilterResults();
                fr.count = r.size();
                fr.values = r;

                return fr;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                schoolAdminItems = (ArrayList<SchoolListResponse>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class AdminListViewHolder extends RecyclerView.ViewHolder{
        private ImageView schoolAdminIv,optionIv;
        private TextView schoolNameTv,schoolAddressTv;
        private LinearLayout schoolItemContainer;
        private Button editButton,deleteButton;
        public AdminListViewHolder(@NonNull View itemView) {
            super(itemView);
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
