package com.android.chalkboard.dashboard.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.model.Item;
import com.android.chalkboard.dashboard.model.ItemModel;
import com.android.chalkboard.util.OnRecyclerItemClickListener;
import com.android.chalkboard.util.SharedPrefUtils;

import java.util.List;

public class DashBoardListAdapter extends RecyclerView.Adapter<DashBoardListAdapter.DashBoardViewHolder> {

    private List<ItemModel> dashboardItems;
    private Context context;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;
    Typeface typeface;
    String role;

    public DashBoardListAdapter(Context context, List<ItemModel> dashboardItems, OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.dashboardItems = dashboardItems;
        this.context = context;
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
        typeface = Typeface.createFromAsset(context.getAssets(), "muli_light.ttf");
        role = SharedPrefUtils.getFromSharedPref(context, SharedPrefUtils.ROLE);
    }

    @NonNull
    @Override
    public DashBoardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = null;
        if(role.equalsIgnoreCase("parent") || role.equalsIgnoreCase("student") || role.equalsIgnoreCase("teacher")) {
            mView = LayoutInflater.from(context).inflate(R.layout.dash_card_items_2, viewGroup, false);
        }
        else {
            mView = LayoutInflater.from(context).inflate(R.layout.dash_card_items, viewGroup, false);
        }

        return new DashBoardViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DashBoardViewHolder dashBoardViewHolder, int i) {
        String name = dashboardItems.get(i).name;
        dashBoardViewHolder.itemNameTv.setText(name);

        if (SharedPrefUtils.getFromSharedPref(context, SharedPrefUtils.ROLE).equalsIgnoreCase("parent") || SharedPrefUtils.getFromSharedPref(context, SharedPrefUtils.ROLE).equalsIgnoreCase("student") || SharedPrefUtils.getFromSharedPref(context, SharedPrefUtils.ROLE).equalsIgnoreCase("teacher")) {
            int imageId = context.getResources().getIdentifier(dashboardItems.get(i).getLocalImage(), "drawable", context.getPackageName());
            dashBoardViewHolder.itemImage.setImageResource(imageId);
        }
        else {
            if (name.equalsIgnoreCase(context.getString(R.string.string_school))) {
                dashBoardViewHolder.itemImage.setBackground(ContextCompat.getDrawable(context, R.drawable.school));
            } else if (name.equalsIgnoreCase(context.getString(R.string.string_teachers_request))) {
                dashBoardViewHolder.itemImage.setBackground(ContextCompat.getDrawable(context, R.drawable.teacher));

            } else if (name.equalsIgnoreCase(context.getString(R.string.string_news))) {
                dashBoardViewHolder.itemImage.setBackground(ContextCompat.getDrawable(context, R.drawable.school_news));

            }
        }

    }

    @Override
    public int getItemCount() {
        return dashboardItems.size();
    }


    public class DashBoardViewHolder extends RecyclerView.ViewHolder {

        private TextView itemNameTv;
        private RelativeLayout parentRelativeLayout;
        private ImageView itemImage;

        public DashBoardViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTv = itemView.findViewById(R.id.tv_item_name);
            itemNameTv.setTypeface(typeface);
            parentRelativeLayout = itemView.findViewById(R.id.rl_dashboard_item_container);
            itemImage = itemView.findViewById(R.id.item_image);
            parentRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerItemClickListener.onClick(itemNameTv.getText().toString());
                }
            });

        }
    }
}
