package com.android.chalkboard.school.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.presenter.DashboardContract;
import com.android.chalkboard.dashboard.presenter.DashboardPresenterImpl;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.school.view.SearchSchoolFragment;
import com.android.chalkboard.util.ImageFilePath;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.android.chalkboard.util.ImageFilePath.resizeImage;

public class SchoolAddListViewAdapter extends ArrayAdapter<SchoolListResponse> implements View.OnClickListener, DashboardContract.View {
    Context context;
    int layoutResId;
    List<SchoolListResponse> list = null;
    ImageView mImage;
    PopupWindow popupWindow;
    int selected = -1;
    private DashboardContract.DashboardPresenter presenter;
    SearchSchoolFragment dialogFragment;

    public SchoolAddListViewAdapter(Context context, int layoutResId, List<SchoolListResponse> list, SearchSchoolFragment dialogFragment){
        super(context, layoutResId, list);
        this.context = context;
        this.layoutResId = layoutResId;
        this.list = new ArrayList<>();
        this.list.addAll(list);
        this.dialogFragment = dialogFragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(layoutResId, parent, false);
            }
            SchoolListResponse item = list.get(position);
            //TextView sName = convertView.findViewById(R.id.schoolName);
            //TextView pName = convertView.findViewById(R.id.principleName);
            //TextView sAddr = convertView.findViewById(R.id.school_address);
            //ImageView sImage = convertView.findViewById(R.id.schoolImage);

            TextView sName = convertView.findViewById(R.id.school_name);
            TextView sAddr = convertView.findViewById(R.id.school_address);
            ImageView sImage = convertView.findViewById(R.id.school_admin_item_image);
            mImage = convertView.findViewById(R.id.school_item_options);

            LinearLayout v = setupPopup();
            popupWindow = new PopupWindow(convertView, 200, 200);
            popupWindow.setContentView(v);
            //popupWindow.setHeight(200);
            //popupWindow.setWidth(200);
            popupWindow.setFocusable(true);

            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected = position;
                    int location[] = new int[2];
                    view.getLocationOnScreen(location);
                    Point p = new Point();
                    p.x = location[0];
                    p.y =  location[1];
                    //popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, p.x - 150, p.y + 10);
                    //popupWindow.showAsDropDown(view);
                    popupWindow.showAsDropDown(view);
                    popupWindow.update(p.x - 150, p.y + 10, 200, 200);
                }
            });

            sName.setText(item.getName());
            //pName.setText(item.getPrincipalName());
            sAddr.setText(item.getAddress());
            String iUrl = item.getImages().get(0).getReadUrl();
            //System.out.println(iUrl);
           new DownloadTask(sImage).execute(iUrl);
        } catch(Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return convertView;
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.join:
                if(selected != -1) {
                    SchoolListResponse sr = list.get(selected);
                    DashboardPresenterImpl.createPresenter(this);
                    presenter.requestInstitutionJoin(String.valueOf(sr.getId()));
                }
            break;
            case R.id.view:
                if(selected != -1) {
                    popupWindow.dismiss();
                    dialogFragment.showDialogFragment(list.get(selected));
                }
                break;
        }
    }

    @Override
    public void storeInstituteData(Object success) {
        System.out.println("Store data");
        Boolean status = (Boolean)success;
        if(status == true)
            showError("Institute join has been requested successfully");
    }

    @Override
    public void storeClassesofInstitute(Object success) {

    }

    @Override
    public void showError(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    private LinearLayout setupPopup(){
        LinearLayout l = new LinearLayout(context);
        l.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        Button join = new Button(context);
        join.setLayoutParams(params);
        join.setTextColor(context.getResources().getColor(R.color.black));
        join.setText("Join");
        join.setBackgroundColor(context.getResources().getColor(R.color.appWhite));
        join.setId(R.id.join);
        join.setOnClickListener(this);
        l.addView(join);
        View v = new View(context);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5));
        v.setBackgroundColor(Color.parseColor("#B3B3B3"));
        l.addView(v);
        Button view = new Button(context);
        view.setLayoutParams(params);
        view.setText("View");
        view.setBackgroundColor(context.getResources().getColor(R.color.appWhite));
        view.setTextColor(context.getResources().getColor(R.color.black));
        view.setId(R.id.view);
        view.setOnClickListener(this);
        l.addView(view);

        return l;
    }

    @Override
    public void bindPresenter(DashboardPresenterImpl presenter){
        this.presenter = presenter;
    }

    public class DownloadTask extends AsyncTask<String, Void, Bitmap>{
        ImageView image;

        public DownloadTask(ImageView image){
            this.image = image;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap icon = null;
            try{
                InputStream in = new URL(url).openConnection().getInputStream();
                icon = BitmapFactory.decodeStream(in);
            } catch(Exception e){
                //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(e.getMessage());
            }
            return icon;
        }

        @Override
        protected void onPostExecute(Bitmap result){
            if(result != null) {
                Bitmap b = ImageFilePath.resizeImage(context, result, image.getWidth(), image.getHeight());
                image.setImageBitmap(b);
            }
            image.invalidate();
        }
    }

    @Override
    public SchoolListResponse getItem(int position){
        return list.get(position);
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public void setRequestedInstitute(Object success){

    }
}
