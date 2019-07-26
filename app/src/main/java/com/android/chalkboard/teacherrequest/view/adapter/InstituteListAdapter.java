package com.android.chalkboard.teacherrequest.view.adapter;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.model.Institution;
import com.android.chalkboard.dashboard.presenter.DashboardContract;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.school.model.ClassesList;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.teacherrequest.view.InstituteListFragment;
import com.android.chalkboard.teacherrequest.view.TeachersRequestFragment;
import com.android.chalkboard.util.CommonUtils;
import com.android.chalkboard.util.SharedPrefUtils;

import java.util.ArrayList;

public class InstituteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<SchoolListResponse> institutionList;
    private ArrayList<ClassesList> classesList;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_CLASSES = 2;
    private static final String TAG = TeachersRequestFragment.class.getSimpleName();
    private InstituteListFragment.OnItemClickListener listener;
    private boolean isFromNews;

    public InstituteListAdapter(Context context, ArrayList<SchoolListResponse> response, InstituteListFragment.OnItemClickListener onItemClickListener) {
        mContext = context;
        institutionList = response;
        listener = onItemClickListener;
    }

    public InstituteListAdapter(Context context, ArrayList<ClassesList> response, InstituteListFragment.OnItemClickListener onItemClickListener, boolean isFromNews) {
        mContext = context;
        classesList = response;
        listener = onItemClickListener;
        this.isFromNews = isFromNews;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == TYPE_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.header_view, viewGroup, false);
            return new VHHeader(view);
        } else if (i == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.school_items, viewGroup, false);
            return new VHItem(view);
        } else if (i == TYPE_CLASSES) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.school_items, viewGroup, false);
            return new VHClasses(view);
        }
        throw new RuntimeException("there is no type that matches the type " + i);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder instanceof VHItem) {
            ((VHItem) viewHolder).schoolNameTv.setText(institutionList.get(i - 1).getName());
            ((VHItem) viewHolder).schoolAddressTv.setText(institutionList.get(i - 1).getAddress());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferenceManager.getDefaultSharedPreferences(mContext).edit().putInt("SELECT_INSTITUTE", institutionList.get(i - 1).getId()).apply();
                    NavDashBoardActivity activity = new NavDashBoardActivity();
                    activity.getInstituteClasses(institutionList.get(i - 1).getId());
                    listener.onItemClick();
                }
            });
        } else if (viewHolder instanceof VHClasses) {
            ((VHClasses) viewHolder).schoolNameTv.setText(classesList.get(i - 1).getInstitutionName());
            ((VHClasses) viewHolder).schoolAddressTv.setText(classesList.get(i - 1).getName());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferenceManager.getDefaultSharedPreferences(mContext).edit().putInt("SELECTED_CLASS", classesList.get(i - 1).getId()).apply();
                    listener.onItemClick();
                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        if (isFromNews) {
            return TYPE_CLASSES;
        } else {
            return TYPE_ITEM;
        }


    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        if (isFromNews) {
            return classesList.size() + 1;
        } else {
            return institutionList.size() + 1;
        }
    }


    class VHItem extends RecyclerView.ViewHolder {
        private TextView schoolNameTv, schoolAddressTv;

        public VHItem(View itemView) {
            super(itemView);
            schoolNameTv = itemView.findViewById(R.id.school_name);
            schoolAddressTv = itemView.findViewById(R.id.school_address);
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {

        private TextView title;

        public VHHeader(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.header_text);
        }
    }

    class VHClasses extends RecyclerView.ViewHolder {
        private TextView schoolNameTv, schoolAddressTv;

        public VHClasses(View itemView) {
            super(itemView);
            schoolNameTv = itemView.findViewById(R.id.school_name);
            schoolAddressTv = itemView.findViewById(R.id.school_address);
        }
    }
}

