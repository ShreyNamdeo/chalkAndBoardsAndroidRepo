package com.android.chalkboard.studentStory.news;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class newsDetails extends Fragment {

    private View view;
    private NewsModel data;
    ImageView newsImage;
    TextView newsTitle,newsDesc,newsDate;

    public newsDetails() {
    }

    @SuppressLint("ValidFragment")
    public newsDetails(NewsModel data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_news_details,container,false);

        newsImage = (ImageView) view.findViewById(R.id.news_image);
        newsTitle = (TextView) view.findViewById(R.id.news_title);
        newsDesc = (TextView) view.findViewById(R.id.news_content);
        newsDate = (TextView) view.findViewById(R.id.news_date);

        Picasso.get().load(data.getImage().getReadUrl()).placeholder(R.drawable.assignment).into(newsImage);
        newsTitle.setText(data.getTitle());
        newsDesc.setText(data.getContent());

//        SimpleDateFormat format = new SimpleDateFormat("dd/MMMM/yy");
        newsDate.setText(data.getDate());


        return view;
    }


}
