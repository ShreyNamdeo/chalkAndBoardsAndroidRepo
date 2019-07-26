package com.android.chalkboard.news.view;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.news.modal.NewsItems;
import com.android.chalkboard.school.model.SchoolAdminItem;
import com.android.chalkboard.school.view.SchoolAdminFragment;
import com.android.chalkboard.school.view.adapter.SchoolAdminListAdapter;
import com.android.chalkboard.util.OnRecyclerItemClickListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminNewsBaseFragment extends Fragment implements OnRecyclerItemClickListener {

    private NewsItemAdapter newsItemAdapter;
    private RecyclerView schoolAdminListRecyclerView;
    private ArrayList<NewsItems> newsItems;
    private FloatingActionButton addSchoolFab ;
    private static final String TAG = AdminNewsBaseFragment.class.getSimpleName();


    public AdminNewsBaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_news_base, container, false);
        schoolAdminListRecyclerView = view.findViewById(R.id.rv_news_items);
        addSchoolFab = getActivity().findViewById(R.id.fab);
        addSchoolFab.setOnClickListener(null);
        addSchoolFab.setBackgroundColor(Color.WHITE);
        loadData();
        newsItemAdapter = new NewsItemAdapter(getActivity(),newsItems, this);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        schoolAdminListRecyclerView.setLayoutManager(mGridLayoutManager);
        schoolAdminListRecyclerView.setAdapter(newsItemAdapter);
        schoolAdminListRecyclerView.setNestedScrollingEnabled(false);
        ((NavDashBoardActivity)getActivity()).getBannerImage().setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.news));
        ((NavDashBoardActivity)getActivity()).getAppBarLayout().setExpanded(false);

        return view;
    }

    private void loadData() {
        newsItems = new ArrayList<>();
        newsItems.add(new NewsItems(getString(R.string.school_news),R.drawable.ic_school_news));
        newsItems.add(new NewsItems(getString(R.string.class_news), R.drawable.ic_class_news));
    }

    @Override
    public void onClick(String itemName) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        if(itemName.equalsIgnoreCase(getString(R.string.school_news))){
            ft.replace(R.id.fragment_content_holder, NewsListFragment.getInstance("INSTITUTION")).addToBackStack(TAG);
            ft.commit();

        }else if(itemName.equalsIgnoreCase(getString(R.string.class_news))){
            ft.replace(R.id.fragment_content_holder, NewsListFragment.getInstance("CLASS")).addToBackStack(TAG);
            ft.commit();
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
        addSchoolFab.setOnClickListener(null);
    }
}
