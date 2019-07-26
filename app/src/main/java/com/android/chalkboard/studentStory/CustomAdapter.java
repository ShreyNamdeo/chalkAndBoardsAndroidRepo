package com.android.chalkboard.studentStory;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.register.model.Image;
import com.android.chalkboard.studentStory.attendance.attendanceModel;
import com.android.chalkboard.studentStory.attendance.elaboratedAttendance;
import com.android.chalkboard.studentStory.attendance.recordsModel;
import com.android.chalkboard.studentStory.attendance.timelineModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomAdapter extends BaseAdapter{

    List<CustomModel> data;
    List<attendanceModel> data2;
    List<CustomModel> dataFiltered;
    Context context;
    int image,layout,dashboardItem;
    Typeface typeface;

    public CustomAdapter(ArrayList<CustomModel> data, Context context,int image,int layout,int i) {
        this.data = data;
        this.context = context;
        this.image = image;
        this.layout = layout;
        this.dashboardItem = i;
        dataFiltered = new ArrayList<CustomModel>();
        dataFiltered.addAll(data);
        typeface = Typeface.createFromAsset(context.getAssets(),"muli_black.ttf");
        Log.e("DATA FILTER SIZE",String.valueOf(dataFiltered.size()));
    }

    public CustomAdapter(ArrayList<attendanceModel> data, int image, int layout, int i, Context context) {
        this.data2 = data;
        this.context = context;
        this.image = image;
        this.layout = layout;
        this.dashboardItem = i;
        dataFiltered = new ArrayList<CustomModel>();
        typeface = Typeface.createFromAsset(context.getAssets(),"muli_black.ttf");
        Log.e("DATA FILTER SIZE",String.valueOf(dataFiltered.size()));
    }

    @Override
    public int getCount() {
        if(dashboardItem==3)
            return data2.size();
        else
            return data.size();
    }

    @Override
    public Object getItem(int i) {
        if(dashboardItem==3)
            return data2.get(i);
        else
            return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) {
            view = LayoutInflater.from(context).inflate(layout,viewGroup,false);
        }

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.null_tofade);
        animation.setDuration(500);
        view.setAnimation(animation);

        ImageView imageView;
        TextView itemTitle,itemDesc;

        switch (dashboardItem) {

            case 1:
            case 2:
            case 5:
                CustomModel customModel = data.get(i);
                imageView = (ImageView) view.findViewById(R.id.listImage);
                itemTitle = (TextView) view.findViewById(R.id.itemTitle);
                itemDesc = (TextView) view.findViewById(R.id.itemDesc);

                itemTitle.setTypeface(typeface);
                itemDesc.setTypeface(typeface);

                imageView.setImageResource(image);
                itemTitle.setText(customModel.getName());
                itemDesc.setText(customModel.getDesc());

                break;

            case 3:
                attendanceModel attendanceModel = data2.get(i);
                TextView attendanceDate = (TextView) view.findViewById(R.id.attendance_date);
                attendanceDate.setText("Date : "+attendanceModel.getDate());
                attendanceDate.setTypeface(typeface);
                LinearLayout records = (LinearLayout) view.findViewById(R.id.records);
                LayoutInflater inflater = LayoutInflater.from(context);
                records.removeAllViews();
                for(int j=0;j<attendanceModel.getRecordsModels().size();j++) {

                    final recordsModel recordsModel = attendanceModel.getRecordsModels().get(j);
                    View view1 = inflater.inflate(R.layout.attendance_list_item_student,null);
                    imageView = (ImageView) view1.findViewById(R.id.itemImage);
                    itemTitle = (TextView) view1.findViewById(R.id.itemTitle);
                    ImageView attendance_image = view1.findViewById(R.id.attendance_image);

                    itemTitle.setTypeface(typeface);
                    Log.e("ITEM TITLE",recordsModel.getName());
                    imageView.setImageResource(image);
                    itemTitle.setText(recordsModel.getName());

                    if(recordsModel.getPresent())
                        attendance_image.setImageResource(R.drawable.present);
                    else
                        attendance_image.setImageResource(R.drawable.absent);

                    view1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("Id",recordsModel.getClassId());
                            elaboratedAttendance elaboratedAttendance = new elaboratedAttendance();
                            elaboratedAttendance.setArguments(bundle);
                            ((NavDashBoardActivity)context).replace_Fragment(elaboratedAttendance,1);
                        }
                    });
                    records.addView(view1);
                }



            break;


        }



        return view;
    }

    public void filter(String s) {
        s = s.toLowerCase(Locale.getDefault());
        data.clear();
        if (s.isEmpty()) {
            data.addAll(dataFiltered);
        } else {
            for (CustomModel row : dataFiltered) {
                if (row.getName().toLowerCase().contains(s) || row.getDesc().toLowerCase().contains(s)) {
                    data.add(row);
                }
            }
        Log.e("SEARCH",String.valueOf(data.size()));
        }
        notifyDataSetChanged();
    }
}
