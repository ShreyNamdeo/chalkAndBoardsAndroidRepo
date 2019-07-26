package com.android.chalkboard.classnews.view.adapter;

import android.content.Context;
import android.support.v4.widget.CircularProgressDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.classnews.model.CreateClassNewsResponse;
import com.android.chalkboard.util.OnClassItemCLickListener;
import com.android.chalkboard.util.OnClassNewsItemClickListener;
import com.android.chalkboard.util.PNButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassNewsExpandableAdapter extends BaseExpandableListAdapter {
    private ArrayList<ClassResponse> classResponses;
    private HashMap<ClassResponse, ArrayList<CreateClassNewsResponse>> newsResponses;
    private Context context;
    private PopupWindow popupWindow;
    private OnClassNewsItemClickListener onClassNewsItemClickListener;
    public static int deleteClassId = -1, deleteNewsId = -1;
    private Button b;

    public ClassNewsExpandableAdapter(Context context, OnClassNewsItemClickListener onClassNewsItemClickListener, ArrayList<ClassResponse> classResponses, HashMap<ClassResponse, ArrayList<CreateClassNewsResponse>> newsResponses){
        this.context = context;
        this.onClassNewsItemClickListener = onClassNewsItemClickListener;
        this.classResponses = classResponses;
        this.newsResponses = newsResponses;
    }

    @Override
    public Object getChild(int position, int expandableListPosition){
        return this.newsResponses.get(this.classResponses.get(position)).get(expandableListPosition);
    }

    @Override
    public long getChildId(int position, int expandableListPosition){
        return expandableListPosition;
    }

    @Override
    public View getChildView(final int position, final int expandableListPosition, boolean isLastChild, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.class_news_item, null);
        }
        TextView title = convertView.findViewById(R.id.class_news_title);
        TextView content = convertView.findViewById(R.id.class_news_content);

        title.setText(newsResponses.get(classResponses.get(position)).get(expandableListPosition).getTitle());
        content.setText(newsResponses.get(classResponses.get(position)).get(expandableListPosition).getContent());
        ImageView classPic = convertView.findViewById(R.id.class_news_item_image);
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .error(R.drawable.newspaper_icon02)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(circularProgressDrawable);
        if(newsResponses.get(classResponses.get(position)).get(expandableListPosition).getImage() != null)
            if(!newsResponses.get(classResponses.get(position)).get(expandableListPosition).getImage().getReadUrl().isEmpty())
                Glide.with(context).load(newsResponses.get(classResponses.get(position)).get(expandableListPosition).getImage().getReadUrl())
                        .apply(options).into(classPic);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClassNewsItemClickListener.onItemClick(newsResponses.get(classResponses.get(position)).get(expandableListPosition));
            }
        });

        PNButton edit = convertView.findViewById(R.id.btn_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClassNewsItemClickListener.onItemEditClick(newsResponses.get(classResponses.get(position)).get(expandableListPosition), classResponses.get(position));
            }
        });

        PNButton delete = convertView.findViewById(R.id.btn_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteClassId = position;
                deleteNewsId = expandableListPosition;
                onClassNewsItemClickListener.onItemDeleteClick(classResponses.get(position).getId(), newsResponses.get(classResponses.get(position)).get(expandableListPosition).getId());
            }
        });
        return convertView;

    }

    public void removeItem(){
        if(deleteClassId != -1 && deleteNewsId != -1){
            newsResponses.get(classResponses.get(deleteClassId)).remove(deleteNewsId);
            notifyDataSetChanged();
            deleteNewsId = -1;
            deleteClassId = -1;
        }
    }

    @Override
    public int getChildrenCount(int position){
        return newsResponses.get(classResponses.get(position)).size();
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
            convertView = inflater.inflate(R.layout.class_news_expandable_parent_view, parent, false);
        }
        ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
        convertView.setLayoutParams(p);
        TextView t = convertView.findViewById(R.id.className);
        t.setText(classResponses.get(position).getName() + ", " + classResponses.get(position).getInstitutionName());
        b = convertView.findViewById(R.id.expand);
        b.setVisibility(View.GONE);

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
