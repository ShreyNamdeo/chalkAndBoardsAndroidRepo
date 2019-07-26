package com.android.chalkboard.studentStory.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.studentStory.assignments.assignDetails;
import com.android.chalkboard.util.FontChangeCrawler;
import com.squareup.picasso.Picasso;

public class testDetails extends Fragment {

    View view;
    testModel data;
    TextView testTitle,testDesc,test_total,test_attempts,testDate;
    RecyclerView imageRecycler;

    public testDetails() {
    }

    @SuppressLint("ValidFragment")
    public testDetails(testModel data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_test_details,container,false);

        ((NavDashBoardActivity)getActivity()).showFragmentToolbar(data.getLevel());

        init();

        testTitle.setText(data.getTitle());
        testDesc.setText(data.getContent());
        test_total.setText(String.valueOf(data.getTotalMarks()));
        test_attempts.setText(String.valueOf(data.getAttemptsAllowed()));
        testDate.setText(data.getDate());

        imageRecycler.setAdapter(new imagesAdapter());
        imageRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));

        return view;
    }

    private void init() {

        testTitle = (TextView) view.findViewById(R.id.testTitle);
        testDesc = (TextView) view.findViewById(R.id.testDesc);
        test_total = (TextView) view.findViewById(R.id.test_totalMarks);
        test_attempts = (TextView) view.findViewById(R.id.test_attempts);
        testDate = (TextView) view.findViewById(R.id.testDate);
        imageRecycler = (RecyclerView)  view.findViewById(R.id.image_recycler);
    }

    private class imagesAdapter extends RecyclerView.Adapter<imagesAdapter.ViewHolder> {

        @NonNull
        @Override
        public imagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View listItem = layoutInflater.inflate(R.layout.assignments_image, viewGroup,false);
            imagesAdapter.ViewHolder viewHolder = new imagesAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull imagesAdapter.ViewHolder viewHolder, int i) {
            Picasso.get().load(data.getImages().get(i).getReadUrl()).placeholder(R.drawable.assignment).into(viewHolder.assignment_image);
        }

        @Override
        public int getItemCount() {
            return data.getImages().size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView assignment_image;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                assignment_image = itemView.findViewById(R.id.assignment_image);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "muli_black.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.main_view));
    }
}
