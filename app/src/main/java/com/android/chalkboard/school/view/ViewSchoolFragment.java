package com.android.chalkboard.school.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.presenter.DashboardContract;
import com.android.chalkboard.dashboard.presenter.DashboardPresenterImpl;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.util.ImageFilePath;

import java.io.InputStream;
import java.net.URL;

public class ViewSchoolFragment extends DialogFragment implements View.OnClickListener, DashboardContract.View{

    private SchoolListResponse response;
    private DashboardContract.DashboardPresenter presenter;

    public void setResponse(SchoolListResponse response){
        this.response = response;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        DashboardPresenterImpl.createPresenter(this);
        View v = inflater.inflate(R.layout.teaccher_view_school_fragment, null);
        TextView sName = v.findViewById(R.id.schoolNameText);
        TextView pName = v.findViewById(R.id.schoolPrincipleNameText);
        TextView sAddr = v.findViewById(R.id.schoolAddressText);
        TextView sCity = v.findViewById(R.id.schoolCityText);
        TextView sAff = v.findViewById(R.id.schoolAffiliationText);
        TextView sReg = v.findViewById(R.id.schoolRegistrationText);
        TextView sSpec = v.findViewById(R.id.schoolSpecializationText);

        sName.setText(response.getName());
        pName.setText(response.getPrincipalName());
        sAddr.setText(response.getAddress());
        sCity.setText(response.getCity());
        sAff.setText(response.getAffiliation());
        sSpec.setText(response.getSpecialization());
        sReg.setText(response.getRegistrationNo());

        Button join = v.findViewById(R.id.join);
        Button cancel = v.findViewById(R.id.cancel);
        join.setOnClickListener(this);
        cancel.setOnClickListener(this);

        ImageView image = v.findViewById(R.id.schoolIcon);
        String iUrl = response.getImages().get(0).getReadUrl();
        //System.out.println(iUrl);
        new DownloadTask(image).execute(iUrl);

        return v;
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.join:
                presenter.requestInstitutionJoin(String.valueOf(response.getId()));
                break;
            case R.id.cancel:
                getFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void bindPresenter(DashboardPresenterImpl presenter){
        this.presenter = presenter;
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
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    public class DownloadTask extends AsyncTask<String, Void, Bitmap> {
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
                Bitmap b = ImageFilePath.resizeImage(getActivity(), result, image.getWidth(), image.getHeight());
                image.setImageBitmap(b);
            }
            image.invalidate();
        }
    }

    @Override
    public void setRequestedInstitute(Object success){

    }

}
