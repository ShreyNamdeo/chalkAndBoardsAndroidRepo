package com.android.chalkboard.studentStory.reports;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.studentStory.reports.Models.resultModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

public class resultSheet extends Fragment {

   private View view;
   private ListView resultList;
   ArrayList<resultModel> data;

    public resultSheet() {
    }

    @SuppressLint("ValidFragment")
    public resultSheet(ArrayList<resultModel> data,int size) {
        this.data = data;
//        if(this.data.size() > size) {
//            this.data
//        }
        int size2 = data.size();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_result_sheet,container,false);

        ((NavDashBoardActivity)getActivity()).showFragmentToolbar("Result Sheet");

        resultList = (ListView) view.findViewById(R.id.resultList);



        resultList.setAdapter(new resultAdapter());

        return view;
    }

    private class resultAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if(view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.report_result_item,viewGroup,false);
            }

            TextView resultSub = (TextView) view.findViewById(R.id.resultName);
            BarChart resultChart = (BarChart) view.findViewById(R.id.resultChart);
//            TextView result = (TextView) view.findViewById(R.id.result);
//            TextView date = (TextView) view.findViewById(R.id.date);
            LinearLayout comments = (LinearLayout) view.findViewById(R.id.comments);

            ArrayList<BarEntry> entries = new ArrayList<>();
            final ArrayList<String> labels = new ArrayList<>();

            resultSub.setText(data.get(i).getSubject());
//            result.setText(data.get(i).get);
            for(int j=0;j<data.get(i).getMarks().size();j++) {
                entries.add(new BarEntry(j,(float) data.get(i).getMarks().get(j).getObtained()));
                labels.add(data.get(i).getMarks().get(j).getExamName());
            }

            BarDataSet barDataSet = new BarDataSet(entries,"Marks Obtained");
            barDataSet.setColor(getResources().getColor(R.color.themeGreen));
            BarData barData = new BarData(barDataSet);

            resultChart.setData(barData);
            resultChart.getDescription().setEnabled(false);

            final XAxis xAxis = resultChart.getXAxis();
            xAxis.setTextSize(0.5f);

            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return labels.get((int) value);
                }
            });

            return view;
        }
    }
}
