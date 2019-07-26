package com.android.chalkboard.dashboard.view;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
//import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.classagenda.view.CLassAgendaFragment;
import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.classes.view.AddClassFragment;
import com.android.chalkboard.classes.view.TeacherClassFragment;
import com.android.chalkboard.classnews.view.ClassNewsEditFragment;
import com.android.chalkboard.classnews.view.ClassNewsFragment;
import com.android.chalkboard.classtimings.view.ClassTimingsFragment;
import com.android.chalkboard.dashboard.model.DashboardItem;
import com.android.chalkboard.dashboard.model.Item;
import com.android.chalkboard.dashboard.model.ItemModel;
import com.android.chalkboard.dashboard.view.adapter.DashBoardListAdapter;
import com.android.chalkboard.news.presenter.CreateNewsContract;
import com.android.chalkboard.news.view.AdminNewsBaseFragment;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.school.presenter.SchoolAdminContract;
import com.android.chalkboard.school.presenter.SchoolAdminPresenterImpl;
import com.android.chalkboard.school.view.AddSchoolActivity;
import com.android.chalkboard.school.view.SchoolAdminFragment;
import com.android.chalkboard.school.view.SearchSchoolFragment;
import com.android.chalkboard.school.view.TeacherSchoolFragment;
import com.android.chalkboard.studentRequest.view.StudentRequestFragment;
import com.android.chalkboard.studentStory.QR_Code;
import com.android.chalkboard.studentStory.attendance.attendanceFragment;
import com.android.chalkboard.studentStory.classes.classFragment;
import com.android.chalkboard.studentStory.news.classNewsFragment;
import com.android.chalkboard.studentStory.reports.reportsFragment;
import com.android.chalkboard.studentStory.schools.schoolFragment;
import com.android.chalkboard.studentStory.topics.topics;
import com.android.chalkboard.teacherrequest.view.TeachersRequestActivity;
import com.android.chalkboard.util.CommonUtils;
import com.android.chalkboard.util.OnRecyclerItemClickListener;
import com.android.chalkboard.util.SharedPrefUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.android.chalkboard.util.Constant.ID;
import static com.android.chalkboard.util.SharedPrefUtils.ROLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements OnRecyclerItemClickListener,View.OnClickListener, SchoolAdminContract.SchoolAdminView {

    private List<DashboardItem> dashBoardItems;
    private ProgressBar progressBar;
    private RecyclerView dashBoardRecyclerView;
    private TextView title1, title2, button;
    private List<ItemModel> itemModelList = new ArrayList<>();
    private DashBoardListAdapter dashBoardListAdapter;
    private String role;
    public static final String TAG = SchoolAdminFragment.class.getSimpleName();
    private FloatingActionButton addSchoolFab ;
    private Context mContext;
    private String module = "";
    private SchoolAdminContract.SchoolAdminPresenter schoolAdminPresenter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        ((NavDashBoardActivity)getActivity()).changeToolbar(0);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        role = SharedPrefUtils.getFromSharedPref(mContext, SharedPrefUtils.ROLE);
        SchoolAdminPresenterImpl.createPresenter(this);
        schoolAdminPresenter.getRequestedInstituteList(SharedPrefUtils.getFromSharedPref(getActivity(), ID));
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        progressBar = view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        title1 = view.findViewById(R.id.tv_title1);
        title2 = view.findViewById(R.id.tv_title2);
        button = view.findViewById(R.id.tv_button);
        addSchoolFab = getActivity().findViewById(R.id.fab);
        //addSchoolFab.show();
        addSchoolFab.setOnClickListener(this);
        role = SharedPrefUtils.getFromSharedPref(getActivity(), ROLE);
        dashBoardRecyclerView = view.findViewById(R.id.rv_dashboard_items);
        fetchDashboardItems();
        parseRoleDataAndSetViews(role);
        dashBoardListAdapter = new DashBoardListAdapter(getActivity(), itemModelList, this);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        dashBoardRecyclerView.setLayoutManager(mGridLayoutManager);
        dashBoardRecyclerView.setAdapter(dashBoardListAdapter);
        dashBoardRecyclerView.setNestedScrollingEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavDashBoardActivity)getActivity()).replace_Fragment(new QR_Code(),1);
            }
        });
        return view;
    }

    private void fetchDashboardItems() {
        try {
            String myJson = CommonUtils.inputStreamToString(getActivity().getResources().getAssets().open("dashboard_items.json"));
            Type listType = new TypeToken<List<DashboardItem>>() {
            }.getType();
            dashBoardItems = new Gson().fromJson(myJson, listType);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void parseRoleDataAndSetViews(String role) {
        itemModelList = new ArrayList<>();
        for (DashboardItem dashboardItem : dashBoardItems) {
            if (dashboardItem.getRole().equalsIgnoreCase(role)) {
                title1.setText(dashboardItem.getTile1());
                title2.setText(dashboardItem.getTitle2());
                button.setText(dashboardItem.getButtonText());
                for (Item item : dashboardItem.getItems()) {
                    ItemModel itemModel = new ItemModel();
                    itemModel.setName(item.getName());
                    itemModel.setLocalImage(item.getLocalImage());
                    itemModelList.add(itemModel);


                }
            }
        }


    }

    private void loadFragment(String itemName) {

        if(role.equalsIgnoreCase("parent") || role.equalsIgnoreCase("student")) {
            switch(role.toLowerCase()) {

                case "student" :
                case "parent" :
                    if(itemName.equals("Schools")) {
                        ((NavDashBoardActivity)getActivity()).replace_Fragment(new schoolFragment(),1);
                    }
                    else if(itemName.equals("Classes")) {
                        classFragment classFragment = new classFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("choice",0);
                        classFragment.setArguments(bundle);
                        ((NavDashBoardActivity)getActivity()).replace_Fragment(classFragment,1);
                    }
                    else if(itemName.equals("Attendance")) {
                        ((NavDashBoardActivity)getActivity()).replace_Fragment(new attendanceFragment(),1);
                    }
                    else if(itemName.equals("Assignments")) {
                        classFragment classFragment = new classFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("choice",2);
                        classFragment.setArguments(bundle);
                        ((NavDashBoardActivity)getActivity()).replace_Fragment(classFragment,1);
                    }
                    else if(itemName.equals("Class News")) {
                        ((NavDashBoardActivity)getActivity()).replace_Fragment(new classNewsFragment(),1);
                    }
                    else if(itemName.equals("Class Agenda")) {
                        classFragment classFragment = new classFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("choice",1);
                        classFragment.setArguments(bundle);
                        ((NavDashBoardActivity)getActivity()).replace_Fragment(classFragment,1);
                    }
                    else if(itemName.equals("Reports")) {
                        ((NavDashBoardActivity)getActivity()).replace_Fragment(new reportsFragment(),1);
                    }
                    else if(itemName.equals("Tests")) {
                        classFragment classFragment = new classFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("choice",3);
                        classFragment.setArguments(bundle);
                        ((NavDashBoardActivity)getActivity()).replace_Fragment(classFragment,1);
                    }
                    else if(itemName.equals("Topics")) {
                        ((NavDashBoardActivity)getActivity()).replace_Fragment(new topics(),1);
                    }
                break;
            }
        }
        else {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            if (itemName.equalsIgnoreCase(getString(R.string.school_text))) {
                ft.replace(R.id.fragment_content_holder, new SchoolAdminFragment()).addToBackStack(TAG);
                ft.commit();

            } else if (itemName.equalsIgnoreCase(getString(R.string.txt_teachers_request))){
                Intent intent = new Intent(getActivity(), TeachersRequestActivity.class);
                startActivity(intent);

            } else if (itemName.equalsIgnoreCase(getString(R.string.news_text))) {
                ft.replace(R.id.fragment_content_holder, new AdminNewsBaseFragment()).addToBackStack(TAG);
                ft.commit();
            } else if(itemName.equalsIgnoreCase(getString(R.string.classes))){
                ft.replace(R.id.fragment_content_holder, new TeacherClassFragment()).addToBackStack(TAG);
                ft.commit();
            } else if(itemName.equalsIgnoreCase(getString(R.string.student_request))) {
                addSchoolFab.hide();
                ft.replace(R.id.fragment_content_holder, new StudentRequestFragment()).addToBackStack(TAG);
                ft.commit();
            } else if(itemName.equalsIgnoreCase("class news")){
                ClassNewsFragment classNewsFragment = new ClassNewsFragment();
                ft.replace(R.id.fragment_content_holder, classNewsFragment).addToBackStack(TAG);
                ft.commit();
            } else if(itemName.equalsIgnoreCase("class agenda")){
                ft.replace(R.id.fragment_content_holder, new CLassAgendaFragment()).addToBackStack(TAG);
                ft.commit();
            } else if(itemName.equalsIgnoreCase("class timings")){
                ft.replace(R.id.fragment_content_holder, new ClassTimingsFragment()).addToBackStack(TAG);
                ft.commit();
            }
            module = itemName;
        }


    }


    @Override
    public void onClick(String itemName) {
        loadFragment(itemName);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.fab) {
            String role = SharedPrefUtils.getFromSharedPref(getActivity(), SharedPrefUtils.ROLE);
            if(role.equalsIgnoreCase("admin")) {
                Intent intent = new Intent(mContext, AddSchoolActivity.class);
                startActivity(intent);
            } else if(role.equalsIgnoreCase("teacher")){
                final FragmentManager fm = getActivity().getFragmentManager();
                if(module.equalsIgnoreCase("school")) {
                    final TeacherSchoolFragment ssf = new TeacherSchoolFragment();
                    ssf.show(fm, "TeacherSchoolFragment");
                } else if(module.equalsIgnoreCase("class")){
                    final AddClassFragment classFragment = new AddClassFragment();
                    classFragment.show(fm, "AddClassFragment");
                } else if(module.equalsIgnoreCase("class news")){
                    ClassNewsEditFragment c = ClassNewsEditFragment.newInstance(null, true, null);
                    c.show(fm, "ClassNewsEditFragment");
                }
            }

        }
    }

    @Override
    public void loadSchoolList(ArrayList< SchoolListResponse > schoolListResponse) {
        SharedPrefUtils.storeRequestedInstituteObject(getActivity(), schoolListResponse);
    }

    @Override
    public void updateSchoolListCache(Object object) {

    }

    @Override
    public void bindPresenter(SchoolAdminContract.SchoolAdminPresenter schoolAdminPresenter) {
        this.schoolAdminPresenter = schoolAdminPresenter;
    }

    @Override
    public void showSpinner() {

    }

    @Override
    public void hideSpinner() {

    }

    @Override
    public void teacherInstituteDeleted(Boolean status){
    }

    @Override
    public void instituteDeleted() {
    }

    public void showError(String errorMessage){
//        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

}
