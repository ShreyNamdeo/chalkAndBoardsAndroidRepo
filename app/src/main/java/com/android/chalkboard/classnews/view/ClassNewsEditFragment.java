package com.android.chalkboard.classnews.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.app.DialogFragment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.classes.model.ClassResponse;
import com.android.chalkboard.classes.presenter.ClassContract;
import com.android.chalkboard.classes.presenter.ClassPresenterImpl;
import com.android.chalkboard.classnews.model.CreateClassNewsRequest;
import com.android.chalkboard.classnews.model.CreateClassNewsResponse;
import com.android.chalkboard.classnews.model.EditClassNewsRequest;
import com.android.chalkboard.classnews.presenter.ClassNewsContract;
import com.android.chalkboard.classnews.presenter.ClassNewsPresenterImpl;
import com.android.chalkboard.school.view.AddSchoolActivity;
import com.android.chalkboard.util.ImageFilePath;
import com.android.chalkboard.util.OnClassNewsItemClickListener;
import com.android.chalkboard.util.PNButton;
import com.android.chalkboard.util.PermissionUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ClassNewsEditFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, ClassNewsContract.View, ClassContract.View {

    Activity activity;
    EditText title, requestId,  date, topic;
    EditText content;
    CreateClassNewsResponse classNewsResponse;
    boolean editStatus;
    DatePickerDialog datePickerDialog;
    String dateStr;
    Button cancelNews, editNews;
    ClassNewsPresenterImpl presenter;
    ClassPresenterImpl cPresenter;
    ClassResponse classResponse;
    ProgressBar circularProgressDrawable;
    ArrayList<ClassResponse> classResponseArrayList;
    Spinner classSpinner;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_APP_SETTINGS = 100;
    private static final int PICK_IMAGE = 1;
    private Snackbar snackbar;
    ImageView pic;
    TextView emptyMessage;
    private RequestBody requestFile;
    private Bitmap bitmap;
    private OnClassNewsItemClickListener listener;

    public static ClassNewsEditFragment newInstance(CreateClassNewsResponse response, boolean edit, ClassResponse classResponse){
        Bundle b = new Bundle();
        b.putSerializable("response",response);
        b.putSerializable("classResponse", classResponse);
        b.putBoolean("edit", edit);

        ClassNewsEditFragment c = new ClassNewsEditFragment();
        c.setArguments(b);

        return c;
    }

    public void setItemListener(OnClassNewsItemClickListener listener){
        System.out.println("in setitemlistener");
        this.listener = listener;
    }

    @Override
    public void bindPresenter(ClassNewsPresenterImpl presenter){
        this.presenter = presenter;
    }

    @Override
    public void bindPresenter(ClassPresenterImpl presenter){
        this.cPresenter = presenter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        if(getArguments() != null){
            classNewsResponse = (CreateClassNewsResponse)getArguments().getSerializable("response");
            editStatus = getArguments().getBoolean("edit");
            classResponse = (ClassResponse)getArguments().getSerializable("classResponse");
            listener = getArguments().getParcelable("listener");
        }

        if(getDialog() != null){
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ClassNewsPresenterImpl.createPresenter(this);
        ClassPresenterImpl.createPresenter(this);
        View v = inflater.inflate(R.layout.class_news_detail_fragment, container, false);
        pic = v.findViewById(R.id.classNewsPic);
        emptyMessage = v.findViewById(R.id.emptyMessage);
        circularProgressDrawable = v.findViewById(R.id.progress);
        cPresenter.getAllClasses();
        RelativeLayout requestIdView = v.findViewById(R.id.newsIdView);
        RelativeLayout classNameView = v.findViewById(R.id.classNameView);

        title = v.findViewById(R.id.title);
        content = v.findViewById(R.id.content);
        date = v.findViewById(R.id.date);
        topic = v.findViewById(R.id.topic);

        cancelNews = v.findViewById(R.id.cancelNews);
        editNews = v.findViewById(R.id.edit);

        if(classNewsResponse != null) {
            requestId = v.findViewById(R.id.reqId);
            requestIdView.setVisibility(View.VISIBLE);
            classNameView.setVisibility(View.GONE);
            requestId.setText(String.valueOf(classNewsResponse.getId()));
            title.setText(String.valueOf(classNewsResponse.getTitle()));
            content.setText(String.valueOf(classNewsResponse.getContent()));
            date.setText(String.valueOf(classNewsResponse.getDate()));
            topic.setText(String.valueOf(classNewsResponse.getTopic()));
            String[] strArray = classNewsResponse.getDate().split("-");
            datePickerDialog = new DatePickerDialog(activity, this, Integer.parseInt(strArray[0]), Integer.parseInt(strArray[1]), Integer.parseInt(strArray[2]));
            editNews.setText("Save");
            if(classNewsResponse.getImage() != null){
                CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(activity);
                circularProgressDrawable.setStrokeWidth(5f);
                circularProgressDrawable.setCenterRadius(30f);
                circularProgressDrawable.start();
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .error(R.drawable.newspaper_icon02)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(circularProgressDrawable);
                Glide.with(activity).load(classNewsResponse.getImage().getReadUrl())
                        .apply(options).into(pic);
                emptyMessage.setVisibility(View.GONE);
            } else
            if(editStatus == false)
                emptyMessage.setText("No image available.");

        } else {
            requestIdView.setVisibility(View.GONE);
            classNameView.setVisibility(View.VISIBLE);
            classSpinner = classNameView.findViewById(R.id.classDropDown);
            classSpinner.setEnabled(true);
            Date d = new Date();
            datePickerDialog = new DatePickerDialog(activity, this, d.getYear(), d.getMonth(), d.getDay());
            editNews.setText("Add");
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        title.setEnabled(editStatus);
        content.setEnabled(editStatus);
        date.setEnabled(editStatus);
        topic.setEnabled(editStatus);


        if(!editStatus)
            editNews.setVisibility(View.GONE);

        emptyMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageFromGallery();
            }
        });
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editStatus)
                    chooseImageFromGallery();
            }
        });

        editNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(classNewsResponse == null){
                    int selected = (int)classSpinner.getSelectedItemId();
                    CreateClassNewsRequest request = new CreateClassNewsRequest(title.getText().toString(), content.getText().toString(), date.getText().toString(), topic.getText().toString());
                    presenter.createClassNews(classResponseArrayList.get(selected).getId(), request, classResponseArrayList.get(selected));
                } else {
                    EditClassNewsRequest request = new EditClassNewsRequest(classNewsResponse.getId(), title.getText().toString(), content.getText().toString(), date.getText().toString(), topic.getText().toString());
                    presenter.editClassNews(classResponse.getId(), request);
                }

            }
        });

        cancelNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        return v;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){
        dateStr = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
        date.setText(dateStr);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onSuccess(Object success){
        if(success != null) {
            if (success instanceof CreateClassNewsResponse) {
                Toast.makeText(activity, "Class news has been updated.", Toast.LENGTH_LONG).show();

                if(((CreateClassNewsResponse)success).getImage() == null) {
                    System.out.println("In if");
                    presenter.sendImageByte(requestFile, classNewsResponse.getImage().getWriteUrl());
                }
                else {
                    System.out.println("In else");
                    presenter.sendImageByte(requestFile, ((CreateClassNewsResponse) success).getImage().getWriteUrl());
                }
                listener.onEditFragmentClose(classResponse);
                dismiss();
            }
            else if(success instanceof ArrayList){
                if(classResponse == null && editStatus) {
                    classResponseArrayList = (ArrayList<ClassResponse>) success;
                    String[] classListValues = new String[classResponseArrayList.size()];
                    for (int i = 0; i < classResponseArrayList.size(); i++)
                        classListValues[i] = classResponseArrayList.get(i).getName() + ", " + classResponseArrayList.get(i).getInstitutionName();

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, classListValues);
                        classSpinner.setAdapter(adapter);
                }
            }
            else
                Toast.makeText(activity, "Class news has been deleted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showError(String message){
        if(message != null)
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSpinner(){
        circularProgressDrawable.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSpinner(){
        circularProgressDrawable.setVisibility(View.GONE);
    }

    @Override
    public void onDeleteSuccess(String message){

    }

    @Override
    public void onSuccess(Object success, String message, ClassResponse classResponse){
        if(success != null) {
            Toast.makeText(activity, "Class news has been created.", Toast.LENGTH_LONG).show();
            presenter.sendImageByte(requestFile, ((CreateClassNewsResponse) success).getImage().getWriteUrl());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean storagePermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    String permission = permissions[0];
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                    if (storagePermissionAccepted)
                        chooseImageFromGallery();
                    else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (showRationale) {
                                showMessageOKCancel("You need to give permission to access storage in order to work this feature.",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            } else if (!showRationale) {
                                snackbar = Snackbar.make(editNews, R.string.permission_storage_rationale,
                                        Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        goToSettings();
                                    }
                                })
                                        .show();
                                return;
                            } else {
                                PermissionUtil.requestPermission(activity);
                            }
                        }

                    }
                }


                break;
        }
    }

    private void chooseImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select class news Logo"), PICK_IMAGE);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            Uri uri = null;
            Toast.makeText(activity, "Image selection Successful", Toast.LENGTH_SHORT).show();
            if (data != null)
                uri = data.getData();
            try {
                if (null != activity.getContentResolver() && null != uri) {
                    emptyMessage.setVisibility(View.GONE);
                    pic.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(uri).into(pic);

                    String realPath = ImageFilePath.getPath(activity, data.getData());
                    File finalFile = new File(realPath);
                    requestFile = RequestBody.create(MediaType.parse("image"), finalFile);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == REQUEST_APP_SETTINGS) {
            if (PermissionUtil.checkPermission(activity)) {
                chooseImageFromGallery();
            }
        }


        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        if(listener != null)
            listener.onEditFragmentClose(classResponse);
        else
            System.out.println("listener is null");
    }
}
