package com.android.chalkboard.news.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.news.modal.NewsItems;
import com.android.chalkboard.util.OnRecyclerItemClickListener;

import java.util.ArrayList;

public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.ViewHolder> {

    private ArrayList<NewsItems> newsItems;
    private Context context;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public NewsItemAdapter(Context context, ArrayList<NewsItems> newsItems, OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.newsItems = newsItems;
        this.context = context;
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(context).inflate(R.layout.dash_card_items, viewGroup, false);
        return new NewsItemAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.itemNameTv.setText(newsItems.get(i).getItemName());
        viewHolder.itemImage.setBackgroundResource(newsItems.get(i).getImage());
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemNameTv;
        private RelativeLayout parentRelativeLayout;
        private ImageView itemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTv = itemView.findViewById(R.id.tv_item_name);
            itemImage = itemView.findViewById(R.id.item_image);
            parentRelativeLayout = itemView.findViewById(R.id.rl_dashboard_item_container);
            parentRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerItemClickListener.onClick(itemNameTv.getText().toString());
                }
            });
        }
    }
}
