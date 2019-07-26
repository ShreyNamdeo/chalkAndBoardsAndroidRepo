package com.android.chalkboard.news.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.CircularProgressDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.news.modal.NewsResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class NewsDetailDialog extends DialogFragment {

    private NewsResponse response;
    private TextView textTopic, textNewsTitle, textNewsDesc;
    private ImageView newsImage;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_news_details, container, false);
        Bundle bundle = getArguments();
        if(bundle!=null){
            response = (NewsResponse) bundle.getSerializable("news");
        }
        setupNewsData(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void setupNewsData(View view) {
        textNewsTitle = view.findViewById(R.id.text_title);
        textNewsDesc = view.findViewById(R.id.text_news_description);
        textTopic = view.findViewById(R.id.text_topic);
        textNewsTitle.setText(response.getTitle());
        textNewsDesc.setText(response.getContent());
        textTopic.setText(response.getTopic());
        newsImage = view.findViewById(R.id.news_image);
//        if(requestFile!=null){
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(mContext);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(mContext).load(response.getImage().getReadUrl())
                .apply(options).into(newsImage);
//        }
    }
}


