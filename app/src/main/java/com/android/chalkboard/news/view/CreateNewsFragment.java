package com.android.chalkboard.news.view;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.news.modal.NewsRequest;
import com.android.chalkboard.news.modal.NewsResponse;
import com.android.chalkboard.news.presenter.CreateNewsContract;
import com.android.chalkboard.news.presenter.CreateNewsPresenterImpl;
import com.android.chalkboard.util.CommonUtils;
import com.android.chalkboard.util.ImageFilePath;
import com.android.chalkboard.util.PermissionUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNewsFragment extends Fragment implements View.OnClickListener, CreateNewsContract.View, ActivityCompat.OnRequestPermissionsResultCallback {

    private EditText etTitle, etDescription, etTopic;
    private TextView textAdd, textNewsTitle, textNewsDesc;
    private ImageView selectedImage, newsImage;
    private Button btnPost;
    private CreateNewsContract.Presenter presenter;
    private Context mContext;
    private NewsResponse mResponse;
    private ViewSwitcher viewSwitcher;
    private ProgressBar progressBar;
    private ProgressDialog dialog;
    private RequestBody requestFile;
    private LinearLayout mainLayout;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_APP_SETTINGS = 100;
    private Snackbar snackbar ;
    private static String institutionType;

    public CreateNewsFragment() {
        // Required empty public constructor
    }

    public static CreateNewsFragment getInstance(String type){
        institutionType = type;
        return new CreateNewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_news, container, false);
        CreateNewsPresenterImpl.createPresenter(this);
        setupUI(view);
        ((NavDashBoardActivity) getActivity()).getAppBarLayout().setExpanded(false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void setupUI(View view) {
        dialog = new ProgressDialog(mContext);
        CommonUtils.createDialog(dialog);
        mainLayout = view.findViewById(R.id.main_layout);
        etTitle = view.findViewById(R.id.news_title);
        textNewsTitle = view.findViewById(R.id.text_title);
        textNewsDesc = view.findViewById(R.id.text_news_description);
        etDescription = view.findViewById(R.id.news_description);
        textAdd = view.findViewById(R.id.add_image);
        viewSwitcher = view.findViewById(R.id.view_switcher);
        newsImage = view.findViewById(R.id.news_image);
        textAdd.setOnClickListener(this);
        etTopic = view.findViewById(R.id.news_topic);
        selectedImage = view.findViewById(R.id.img_add);
        btnPost = view.findViewById(R.id.btn_post);
        btnPost.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.add_image:
                if (!PermissionUtil.checkPermission(mContext)) {
                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                }else{
                    if (selectedImage.getDrawable() == null) {
                        navToGallery(CommonUtils.IMAGE_REQ_CODE);
                    } else {
                        selectedImage.setImageDrawable(null);
                        navToGallery(CommonUtils.IMAGE_REQ_CODE);
                    }
                }
                break;
            case R.id.btn_post:
                if (areFieldValidated()) {
                    int instituteId = CommonUtils.getSelectedInstitute(mContext);
                    int classId = CommonUtils.getSelectedClass(mContext);
                    if(institutionType.equalsIgnoreCase("institution")) {
                        presenter.createSchoolNews(prepareRequestData(), instituteId);
                    }else{
                        presenter.createClassNews(prepareRequestData(), classId);
                    }
                }
                break;

        }
    }

    private boolean areFieldValidated() {
        boolean isValidate = false;
        if (!TextUtils.isEmpty(etTitle.getText().toString()) && !TextUtils.isEmpty(etTopic.getText().toString()) &&
                !TextUtils.isEmpty(etTitle.getText().toString())) {
            isValidate = true;
        }
        return isValidate;
    }

    private NewsRequest prepareRequestData() {
        NewsRequest newsRequest = new NewsRequest();
        newsRequest.setContent(etDescription.getText().toString());
        newsRequest.setTitle(etTitle.getText().toString());
        newsRequest.setTopic(etTopic.getText().toString());
        newsRequest.setLevel(institutionType);
        newsRequest.setDate(String.valueOf(System.currentTimeMillis()));
        newsRequest.setNumberOfImages("1");
        return newsRequest;
    }

    private void navToGallery(int reqCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), reqCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                if (requestCode == CommonUtils.IMAGE_REQ_CODE) {
                    selectedImage.setVisibility(View.VISIBLE);
                    selectedImage.setImageBitmap(bitmap);

                    String realPath = ImageFilePath.getPath(mContext, data.getData());
                    File finalFile = new File(realPath);
                    requestFile = RequestBody.create(MediaType.parse("image"), finalFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == REQUEST_APP_SETTINGS) {
            if (PermissionUtil.checkPermission(mContext)) {
                {
                    if (selectedImage.getDrawable() == null) {
                        navToGallery(CommonUtils.IMAGE_REQ_CODE);
                    } else {
                        selectedImage.setImageDrawable(null);
                        navToGallery(CommonUtils.IMAGE_REQ_CODE);
                    }
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    String permission = permissions[0];
                    boolean storagePermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean showRationale = shouldShowRequestPermissionRationale( permission );
                    if (storagePermissionAccepted) {
                        if (selectedImage.getDrawable() == null) {
                            navToGallery(CommonUtils.IMAGE_REQ_CODE);
                        } else {
                            selectedImage.setImageDrawable(null);
                            navToGallery(CommonUtils.IMAGE_REQ_CODE);
                        }
                    }else {
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
                            }else if(!showRationale){
                                snackbar = Snackbar.make(mainLayout, R.string.permission_storage_rationale,
                                        Snackbar.LENGTH_INDEFINITE);
                                        snackbar.setAction("OK", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                goToSettings();
                                            }
                                        })
                                        .show();
                                        return;
                            }else {
                                PermissionUtil.requestPermission((NavDashBoardActivity)mContext);
                            }
                        }

                    }
                }


                break;
        }
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + mContext.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }



    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private byte[] getImageString(ImageView image) {
        Bitmap bitmap = getBitmap(image);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();

    }

    public Bitmap getBitmap(ImageView view) {
        try {
            Bitmap bitmap;
            bitmap = Bitmap.createBitmap(view.getDrawable().getIntrinsicWidth(), view.getDrawable().getIntrinsicHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            view.getDrawable().setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            view.getDrawable().draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    @Override
    public void bindPresenter(CreateNewsPresenterImpl createNewsPresenter) {
        this.presenter = createNewsPresenter;
    }

    @Override
    public void sendImage(NewsResponse response) {
        mResponse = response;
        if(requestFile!=null&&response!=null) {
            presenter.sendImageByte(requestFile, response.getImage().getWriteUrl());
        }else{
            postNews();
        }
    }

    @Override
    public void showProgressBar() {
        dialog.show();
    }

    @Override
    public void hideProgressBar() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void postNews() {
        ((NavDashBoardActivity) getActivity()).getAppBarLayout().setExpanded(true);
        viewSwitcher.showNext();
        setupPostedNewsViews();
    }

    @Override
    public void showNetworkError() {
        Snackbar snackbar = Snackbar.make(getView(), "Please try after some time", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void setupPostedNewsViews() {
        textNewsTitle.setText(mResponse.getTitle());
        textNewsDesc.setText(mResponse.getContent());
        if(requestFile!=null){
            newsImage.setVisibility(View.VISIBLE);
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(mContext);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(circularProgressDrawable)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(mContext).load(mResponse.getImage().getReadUrl())
                    .apply(options).into(newsImage);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(snackbar!=null){
            snackbar.dismiss();
        }
    }
}
