package com.android.chalkboard.classagenda.view.adapter;

import android.content.Context;
import android.support.v4.widget.CircularProgressDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.classagenda.model.AgendaResponse;
import com.android.chalkboard.classagenda.model.CreateAgendaRequest;
import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.classnews.model.CreateClassNewsResponse;
import com.android.chalkboard.util.OnClassAgendaItemListener;
import com.android.chalkboard.util.OnClassNewsItemClickListener;
import com.android.chalkboard.util.PNButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassAgendaExpandableAdapter extends BaseExpandableListAdapter {
    private ArrayList<ClassResponse> classResponses;
    private HashMap<ClassResponse, AgendaResponse> agendaResponses;
    private Context context;
    private PopupWindow popupWindow;
    private OnClassAgendaItemListener onClassAgendaItemListener;
    public static int deleteClassId = -1, deleteNewsId = -1;
    private Button b;

    public ClassAgendaExpandableAdapter(Context context, OnClassAgendaItemListener onClassAgendaItemListener, ArrayList<ClassResponse> classResponses, HashMap<ClassResponse, AgendaResponse> agendaResponses){
        this.context = context;
        this.onClassAgendaItemListener = onClassAgendaItemListener;
        this.classResponses = classResponses;
        this.agendaResponses = agendaResponses;
    }

    @Override
    public Object getChild(int position, int expandableListPosition){
        return this.agendaResponses.get(this.classResponses.get(position));
    }

    @Override
    public long getChildId(int position, int expandableListPosition){
        return expandableListPosition;
    }

    @Override
    public View getChildView(final int position, final int expandableListPosition, boolean isLastChild, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.class_agenda_item, null);
        }
        TextView topic = convertView.findViewById(R.id.class_agenda_topic);
        TextView agenda = convertView.findViewById(R.id.class_agenda);
        topic.setText(agendaResponses.get(classResponses.get(position)).getTopic());
        agenda.setText(agendaResponses.get(classResponses.get(position)).getAgenda());

        PNButton edit = convertView.findViewById(R.id.btn_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAgendaRequest request = new CreateAgendaRequest(agendaResponses.get(classResponses.get(position)).getTopic(),
                        agendaResponses.get(classResponses.get(position)).getAgenda());
                onClassAgendaItemListener.updateAgenda(request, classResponses.get(position));
            }
        });

        PNButton viewBtn = convertView.findViewById(R.id.btn_view);
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return convertView;

    }


    @Override
    public int getChildrenCount(int position){
        return agendaResponses.get(classResponses.get(position)).getTopic() == null && agendaResponses.get(classResponses.get(position)).getAgenda() == null ? 0 : 1;
    }

    @Override
    public Object getGroup(int position){
        return classResponses.get(position);
    }

    @Override
    public int getGroupCount(){
        return classResponses.size();
    }

    @Override
    public long getGroupId(int position){
        return position;
    }

    @Override
    public View getGroupView(final int position, boolean eisExpanded, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.class_agenda_expandablelist_parent_view, parent, false);
        }
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
        convertView.setLayoutParams(p);
        TextView t = convertView.findViewById(R.id.className);
        t.setText(classResponses.get(position).getName());

        return convertView;
    }

    @Override
    public boolean hasStableIds(){
        return false;
    }

    @Override
    public boolean isChildSelectable(int position, int expandedPosition){
        return true;
    }

}
