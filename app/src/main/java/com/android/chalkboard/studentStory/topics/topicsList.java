package com.android.chalkboard.studentStory.topics;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.chalkboard.R;

public class topicsList extends Fragment {

    private View view;
    ListView topicsList;
    topicsModel data;

    public topicsList() {
    }

    @SuppressLint("ValidFragment")
    public topicsList(topicsModel data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_topics,container,false);

        topicsList = (ListView) view.findViewById(R.id.topicssList);

        topicsList.setAdapter(new topicsAdapter());
        return view;
    }

    private class topicsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.getTopics().size();
        }

        @Override
        public Object getItem(int i) {
            return data.getTopics().get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if(view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.topics_item,viewGroup,false);
            }

            TextView subject = (TextView) view.findViewById(R.id.itemTitle);

            subject.setText(data.getTopics().get(i));

            return view;
        }
    }
}
