package com.android.chalkboard.news.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.news.modal.NewsResponse;
import com.android.chalkboard.news.presenter.NewsListContract;
import com.android.chalkboard.news.presenter.NewsListPresenterImpl;
import com.android.chalkboard.news.util.SwipeToDeleteCallback;
import com.android.chalkboard.teacherrequest.view.InstituteListFragment;
import com.android.chalkboard.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;


public class NewsListFragment extends Fragment implements NewsListContract.View, View.OnClickListener {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private String itemType = null;
    private NewsListContract.Presenter presenter;
    private Context mContext;
    private List<NewsResponse> newsList;
    private TextView textNoNews;
    private RecyclerView recyclerView;
    private ProgressDialog dialog;
    private List<NewsResponse> schoolNews;
    private List<NewsResponse> classNews;
    private static String mType;
    private FloatingActionButton addSchoolFab ;
    private LinearLayout mParentView;
    private List<NewsResponse> tempList;

    private static final String TAG = NewsListFragment.class.getSimpleName();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsListFragment() {
    }

    public static NewsListFragment getInstance(String school) {
        mType = school;
        return new NewsListFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newslist_list, container, false);
        NewsListPresenterImpl.createPresenter(this);
        if (mType.equalsIgnoreCase("class") ){
            InstituteListFragment fragment = new InstituteListFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean("isFromNews", true);
            fragment.setArguments(bundle);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragment.show(transaction, TAG);
        }
        ((NavDashBoardActivity)getActivity()).getAppBarLayout().setExpanded(true);
        setupUI(view);
        // Set the adapter

        return view;
    }



    private void setupUI(View view) {
        int instituteId = CommonUtils.getSelectedInstitute(mContext);
        dialog = new ProgressDialog(mContext);
        mParentView = view.findViewById(R.id.news_parent);
        addSchoolFab = getActivity().findViewById(R.id.fab);
        addSchoolFab.setOnClickListener(this);
        schoolNews = new ArrayList<>();
        classNews = new ArrayList<>();
        CommonUtils.createDialog(dialog);
        presenter.getNewsList(instituteId, itemType);
        textNoNews = view.findViewById(R.id.text_create_news);
        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showProgressBar() {
        dialog.show();
    }

    @Override
    public void hideProgressBar() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    public void bindPresenter(NewsListPresenterImpl newsListPresenter) {
        presenter = newsListPresenter;
    }

    @Override
    public void populateListData(List<NewsResponse> response) {
        newsList = response;
        tempList = response;
        createSchoolAndClassNewsList(response);
        if(mType.equalsIgnoreCase("institution") && schoolNews.size()>0){
            NewsListAdapter mAdapter = new NewsListAdapter(schoolNews, presenter,mContext,mParentView);
            recyclerView.setAdapter(mAdapter);
            ItemTouchHelper itemTouchHelper = new
                    ItemTouchHelper(new SwipeToDeleteCallback(mAdapter, mContext));
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }else if(mType.equalsIgnoreCase("class") && classNews.size()>0){
            NewsListAdapter mAdapter = new NewsListAdapter(classNews, presenter,mContext,mParentView);
            recyclerView.setAdapter(mAdapter);
            ItemTouchHelper itemTouchHelper = new
                    ItemTouchHelper(new SwipeToDeleteCallback(mAdapter, mContext));
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }else{
            textNoNews.setVisibility(View.VISIBLE);
        }

    }

    private void createSchoolAndClassNewsList(List<NewsResponse> response) {
        for (int i = 0; i < response.size(); i++) {
            if (newsList.get(i).getLevel().equalsIgnoreCase("INSTITUTION")) {
                int pos = 0;
                schoolNews.add(pos, newsList.get(i));
                pos++;
            } else {
                int pos = 0;
                classNews.add(pos, newsList.get(i));
                pos++;
            }
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        if(v.getId()==R.id.fab){
            ft.replace(R.id.fragment_content_holder,CreateNewsFragment.getInstance(mType)).addToBackStack(TAG);
            ft.commit();
        }
    }

    @Nullable
    @Override
    public Context getContext() {
        return mContext;
    }


}
