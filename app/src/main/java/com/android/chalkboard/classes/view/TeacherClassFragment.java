package com.android.chalkboard.classes.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.classes.model.Class;
import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.classes.presenter.ClassContract;
import com.android.chalkboard.classes.presenter.ClassPresenterImpl;
import com.android.chalkboard.classes.view.adapter.ClassRecycleViewAdapter;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.util.OnClassItemCLickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TeacherClassFragment extends Fragment implements ClassContract.View, SearchView.OnQueryTextListener, View.OnClickListener, OnClassItemCLickListener{

    FloatingActionButton classFab;
    RecyclerView rBanner;
    TextView rList;
    ProgressBar progressBar;
    ClassPresenterImpl presenter;
    private PopupWindow popup = null, menu = null;
    private ArrayList<ClassResponse> classArrayList;
    private ClassRecycleViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ClassPresenterImpl.createPresenter(this);
        View view = inflater.inflate(R.layout.teacher_classes_fragment, container, false);
        rBanner = view.findViewById(R.id.class_recycler_view);
        rList = view.findViewById(R.id.empty_class_list_message_tv);
        classFab = view.findViewById(R.id.searchClassFab);
        classFab.setOnClickListener(this);
        progressBar = view.findViewById(R.id.progress);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rBanner.setLayoutManager(layoutManager);
        ((NavDashBoardActivity)getActivity()).getAppBarLayout().setExpanded(true);
        ((NavDashBoardActivity)getActivity()).getBannerImage().setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.header_vector_classes));
        popup = new PopupWindow(setupPopWindow(false), ViewGroup.LayoutParams.MATCH_PARENT, 100, true);
        presenter.getAllClasses();

        return view;
    }

    public LinearLayout setupPopWindow(boolean menu){
        LinearLayout l = new LinearLayout(getActivity());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setLayoutParams(param);
        l.setPadding(10, 5, 10, 5);
        l.setBackgroundColor(getResources().getColor(R.color.appWhite));
        if(menu == false) {
            SearchView searchView = new SearchView(getActivity());
            searchView.setLayoutParams(param);
            searchView.setId(R.id.searchView);
            searchView.setQueryHint("Search added school");
            searchView.setFocusable(true);
            searchView.setClickable(true);
            searchView.setOnQueryTextListener(this);
            searchView.setBackground(null);
            searchView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            l.setBackgroundResource(R.drawable.drawable_registration_background_white);
            l.addView(searchView);
        } else {
            TextView t = new TextView(getActivity());
            t.setText("View");
            t.setTextColor(getResources().getColor(R.color.black));
            t.setTextSize(20);
            t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t.setId(R.id.view);
            t.setGravity(Gravity.CENTER);
            l.addView(t);
        }

        return l;
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.searchClassFab:
                System.out.println("In fab click");
                if(!popup.isShowing()) {
                    popup.showAtLocation(view, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 60);
                } else {
                    showError("In else");
                    adapter.clearFilter();
                    popup.dismiss();
                }
                //p.setAnimationStyle(android.R.anim.fade_in);
                break;
        }
    }


    @Override
    public boolean onQueryTextSubmit(String s){
        adapter.getFilter().filter(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s){
        adapter.getFilter().filter(s);
        return false;
    }


    @Override
    public void showError(String message){
       Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
   }

   @Override
    public void onSuccess(Object message){
        if(message instanceof ArrayList) {
            this.classArrayList = (ArrayList<ClassResponse>) message;
            if (classArrayList == null || classArrayList.size() == 0) {
                rList.setVisibility(View.VISIBLE);
                rBanner.setVisibility(View.GONE);
            } else {
                rList.setVisibility(View.GONE);
                rBanner.setVisibility(View.VISIBLE);
                adapter = new ClassRecycleViewAdapter(getActivity(), this, classArrayList);
                rBanner.setAdapter(adapter);
            }
        } else {
            showError((String)message);
            presenter.getAllClasses();
        }
   }

   @Override
    public void showSpinner(){
        progressBar.setVisibility(View.VISIBLE);
   }

   @Override
    public void hideSpinner(){
        progressBar.setVisibility(View.GONE);
   }

   @Override
    public void bindPresenter(ClassPresenterImpl presenter){
        this.presenter = presenter;
   }

   @Override
   public void onResume(){
        super.onResume();
        presenter.getAllClasses();
   }

   @Override
    public void onDestroyView(){
        super.onDestroyView();
        if(popup.isShowing())
            popup.dismiss();
   }

   @Override
    public void onClassItemClick(ClassResponse classResponse){

   }

   @Override
   public void onClassItemEditClick(ClassResponse classResponse){
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
       Date sDate = new Date(classResponse.getStartDate());
       Date eDate = new Date(classResponse.getEndDate());
       Date d = new Date(classResponse.getDate());
        Class c = new Class(classResponse.getName(), classResponse.getInstitutionId(), String.valueOf(classResponse.getStandard()), format.format(d), classResponse.getSubject(), classResponse.getStartTime(), classResponse.getEndTime(), format.format(sDate), format.format(eDate));
        FragmentManager fm = getActivity().getFragmentManager();
       AddClassFragment ac = AddClassFragment.newInstance(c, true);
       ac.show(fm, "");

   }

   @Override
    public void onClassItemDeleteClick(ClassResponse classResponse){
        presenter.deleteClass(classResponse.getId());
   }

   @Override
    public void onClassItemOptionMenuClick(View view, final ClassResponse classResponse){
        View v = setupPopWindow(true);
        TextView t = v.findViewById(R.id.view);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date sDate = new Date(classResponse.getStartDate());
                Date eDate = new Date(classResponse.getEndDate());
                Date d = new Date(classResponse.getDate());
                Class c = new Class(classResponse.getName(), classResponse.getInstitutionId(), String.valueOf(classResponse.getStandard()), format.format(d), classResponse.getSubject(), classResponse.getStartTime(), classResponse.getEndTime(), format.format(sDate), format.format(eDate));
                FragmentManager fm = getActivity().getFragmentManager();
                AddClassFragment ac = AddClassFragment.newInstance(c, false);
                ac.show(fm, "");
            }
        });
        if(menu == null) {
            menu = new PopupWindow(v, ViewGroup.LayoutParams.WRAP_CONTENT, 100, true);
            menu.showAsDropDown(view, -100, 0);
        }
        else if(menu.isShowing())
            menu.dismiss();
        else
            menu.showAsDropDown(view, -100, 0);

   }
}
