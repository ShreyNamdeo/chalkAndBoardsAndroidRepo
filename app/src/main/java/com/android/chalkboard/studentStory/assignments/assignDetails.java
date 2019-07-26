package com.android.chalkboard.studentStory.assignments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.studentStory.classes.classFragment;
import com.android.chalkboard.studentStory.schools.schoolDetails;
import com.android.chalkboard.util.FontChangeCrawler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class assignDetails extends Fragment {

    private View view;
    Button replyButton;
    assignmentModel data;
    RecyclerView imageRecycler;
    TextView assignTitle, assignDesc;
    ImageView scroll_image;
    TextView scroll_text;

    public assignDetails() {
    }

    @SuppressLint("ValidFragment")
    public assignDetails(assignmentModel data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.assignment_details_student, container, false);

        replyButton = (Button) view.findViewById(R.id.replyAssign);
        imageRecycler = (RecyclerView) view.findViewById(R.id.image_recycler);
        assignTitle = (TextView) view.findViewById(R.id.assignTitle);
        assignDesc = (TextView) view.findViewById(R.id.assignDesc);
        scroll_image = (ImageView) view.findViewById(R.id.scroll_image);
        scroll_text = (TextView) view.findViewById(R.id.scroll_text);

        assignTitle.setText(data.getTopic());
        assignDesc.setText(data.getContent());

        imageRecycler.setAdapter(new imagesAdapter());
        imageRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavDashBoardActivity) getActivity()).replace_Fragment(new uploadAssign(data), 1);
            }
        });

        imageRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Animation fade_appear = AnimationUtils.loadAnimation(getContext(), R.anim.fade_tonull);
                scroll_image.setAnimation(fade_appear);
                scroll_text.setAnimation(fade_appear);
                scroll_image.setVisibility(View.GONE);
                scroll_text.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private class imagesAdapter extends RecyclerView.Adapter<imagesAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View listItem = layoutInflater.inflate(R.layout.assignments_image, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
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
