package com.android.chalkboard.news.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.news.modal.NewsResponse;
import com.android.chalkboard.news.presenter.NewsListContract;
import com.android.chalkboard.util.CommonUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;


public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private List<NewsResponse> mNewsList;
    private NewsResponse mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private NewsListContract.Presenter mPresenter;
    private int currentPos;
    private Context mContext;
    private LinearLayout mParentView;
    private boolean itemDeleted;
    private static final String TAG = NewsListAdapter.class.getSimpleName();

    public NewsListAdapter(List<NewsResponse> response, NewsListContract.Presenter presenter, Context context, LinearLayout parent) {
        mNewsList = response;
        mPresenter = presenter;
        mContext = context;
        mParentView = parent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_news_list, parent, false);
        Snackbar.make(mParentView, "Click to view and swipe to delete news",
                Snackbar.LENGTH_LONG).show();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtContent.setText(mNewsList.get(position).getContent());
        holder.txtTitle.setText(mNewsList.get(position).getTitle());
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(mContext);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        RequestOptions options = new RequestOptions()
                .error(R.drawable.school_list_icon)
                .fitCenter()
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext).load(mNewsList.get(position).getImage().getReadUrl())
                .apply(options).into(holder.imgNewsImage);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsDetailDialog dialog = new NewsDetailDialog();
                Bundle bundle = new Bundle();
                bundle.putSerializable("news", mNewsList.get(position));
                dialog.setArguments(bundle);
                FragmentTransaction ft = ((NavDashBoardActivity)mContext).getSupportFragmentManager().beginTransaction();
                dialog.show(ft, TAG);
            }
        });
//        holder.imgNewsImage.setImageDrawable(mNewsList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public  TextView txtTitle, txtContent;
        private ImageView imgNewsImage;
        private ConstraintLayout parent;

        public ViewHolder(View view) {
            super(view);
            parent = view.findViewById(R.id.news_list_parent);
            txtTitle = view.findViewById(R.id.text_title);
            txtContent = view.findViewById(R.id.text_content);
            imgNewsImage = view.findViewById(R.id.image_news);
        }

    }

    public void deleteItem(int position) {
        currentPos =  position;
        mRecentlyDeletedItem = mNewsList.get(position);
        mRecentlyDeletedItemPosition = position;
        mNewsList.remove(position);
        notifyItemRemoved(position);
        showSnackbar();
    }

    private void showSnackbar() {
        Snackbar snackbar = Snackbar.make(mParentView, "Click to delete this news",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemDeleted = true;
                notifyDataSetChanged();
                mPresenter.deleteNews(CommonUtils.getSelectedInstitute(mContext), mRecentlyDeletedItem.getId());
            }
        });
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if(!itemDeleted) {
                    undoDelete();
                }
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        mNewsList.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }
}
