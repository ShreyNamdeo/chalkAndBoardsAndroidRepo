package com.android.chalkboard.school.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.model.Institution;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.school.model.AddSchoolRequest;
import com.android.chalkboard.school.model.AddSchoolResponse;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.school.model.StandardFeesModel;
import com.android.chalkboard.school.presenter.AddSchoolContract;
import com.android.chalkboard.school.presenter.AddSchoolPresenterImpl;
import com.android.chalkboard.util.CommonUtils;
import com.android.chalkboard.util.ImageFilePath;
import com.android.chalkboard.util.PermissionUtil;
import com.android.chalkboard.util.SharedPrefUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class AddSchoolActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener,
        AddSchoolContract.AddSchoolView {

    private static final int PICK_IMAGE = 1;
    private TextInputEditText nameEditText, principleEditText, addressEditText, locationEdittext, specializationEditText, affiliationEditText, regNoEditText, feesEditText, logoEditText;
    private TextInputLayout nameTextInputLayout, principleTextInputLayout, addressTextInputLayout, locationTextInputLayout,
            affiliationTextInputLayout, regNoTextInputLayout, feesTextInputLayout, logoTextInputLayout;
    private Button addSchoolButton;
    private Spinner typeSpinner;
    private ArrayList<StandardFeesModel> standardFees, autonomousFee;
    private String name, principleName, address, location, specializaton, affiliation, schoolRegNo, type;
    private int logoCount;
    private String jwtToken;
    private String spinnerItemList[] = new String[]{"Type", "Standard", "Autonomous"};
    private AddSchoolContract.AddSchoolPresenter addSchoolPresenter;
    private ImageView schoolLogo;
    private ProgressBar progressBar;
    private RequestBody requestFile;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_APP_SETTINGS = 100;
    private Snackbar snackbar;
    private LinearLayout addSchoolParent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_add_school);
        AddSchoolPresenterImpl.createPrensenter(this);
        addSchoolParent = findViewById(R.id.add_school_parent);
        addSchoolButton = findViewById(R.id.pnb_add_school);
        progressBar = findViewById(R.id.progress);
        addSchoolButton.setOnClickListener(this);
        nameEditText = findViewById(R.id.et_name);
        principleEditText = findViewById(R.id.et_principle_name);
        addressEditText = findViewById(R.id.et_address);
        locationEdittext = findViewById(R.id.et_location);
        specializationEditText = findViewById(R.id.et_specialization);
        affiliationEditText = findViewById(R.id.et_affiliation);
        regNoEditText = findViewById(R.id.et_registration_no);
        logoTextInputLayout = findViewById(R.id.til_picture_logo);
        logoTextInputLayout.setOnClickListener(this);
        logoEditText = findViewById(R.id.et_picture_logo);
        feesEditText = findViewById(R.id.et_fees);
        feesEditText.setOnClickListener(this);
        logoEditText.setOnClickListener(this);
        logoEditText.setOnFocusChangeListener(this);
        feesEditText.setOnFocusChangeListener(this);
        typeSpinner = findViewById(R.id.sp_type);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerItemList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0)
                    return false;
                else
                    return true;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return super.getDropDownView(position, convertView, parent);
            }
        };
        typeSpinner.setAdapter(spinnerAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    Toast.makeText(AddSchoolActivity.this, "" + spinnerItemList[i], Toast.LENGTH_SHORT).show();
                    feesTextInputLayout.setVisibility(View.VISIBLE);
                }
                if (i == 2) {
                    feesTextInputLayout.setVisibility(View.GONE);

                }
                type = spinnerItemList[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        nameTextInputLayout = findViewById(R.id.til_name);
        principleTextInputLayout = findViewById(R.id.til_principle_name);
        addressTextInputLayout = findViewById(R.id.til_address);
        locationTextInputLayout = findViewById(R.id.til_location);
        affiliationTextInputLayout = findViewById(R.id.til_affiliation);
        regNoTextInputLayout = findViewById(R.id.til_registration_no);
        feesTextInputLayout = findViewById(R.id.til_fees);
        logoTextInputLayout = findViewById(R.id.til_picture_logo);
        schoolLogo = findViewById(R.id.img_school_logo);
        jwtToken = SharedPrefUtils.getFromSharedPref(this, SharedPrefUtils.JWTToken);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.pnb_add_school) {
            /*if (nameEditText.getText().toString().equals("")){
                nameTextInputLayout.setError("Name should not be empty");
                nameTextInputLayout.requestFocus();
            } else if(principleEditText.getText().toString().equals("")){
                principleTextInputLayout.setError("Name should not be empty");
                principleTextInputLayout.requestFocus();
            }*/
            name = nameEditText.getText().toString();
            principleName = principleEditText.getText().toString();
            address = addressEditText.getText().toString();
            location = locationEdittext.getText().toString();
            specializaton = specializationEditText.getText().toString();
            affiliation = affiliationEditText.getText().toString();
            schoolRegNo = regNoEditText.getText().toString();

            if (logoEditText.getText().toString().length() != 0)
                logoCount = Integer.parseInt(logoEditText.getText().toString());
            logoCount = 1;
            if (type.equalsIgnoreCase("AUTONOMOUS")) {
                standardFees = new ArrayList<>();
            }
            addSchoolPresenter.addSchool(new AddSchoolRequest(name, principleName, address, location, specializaton, affiliation, schoolRegNo, type, logoCount, standardFees));


        }
        if (view.getId() == R.id.et_fees) {
            if (TextUtils.isEmpty(feesEditText.getText().toString())) {
                showAddFeesDialog();
            }
            //Toast.makeText(this,"Select Fees",Toast.LENGTH_SHORT).show();
        }
        if (view.getId() == R.id.et_picture_logo || view.getId() == R.id.til_picture_logo) {
            if (!PermissionUtil.checkPermission(this)) {
                PermissionUtil.requestPermission(this);
            } else {
                chooseImageFromGallery();
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean storagePermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    String permission = permissions[0];
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
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
                                snackbar = Snackbar.make(addSchoolParent, R.string.permission_storage_rationale,
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
                                PermissionUtil.requestPermission(this);
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void showAddFeesDialog() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AddFeesFragment addFeesFragment = new AddFeesFragment();
        addFeesFragment.show(transaction, "AddFeesFragment");
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        int id = view.getId();
        switch (id) {
            case R.id.et_fees:
                if (hasFocus && TextUtils.isEmpty(feesEditText.getText().toString())) {
                    showAddFeesDialog();
                }
                break;
            case R.id.et_picture_logo:
                if (hasFocus && TextUtils.isEmpty(logoEditText.getText().toString())) {
                    if (!PermissionUtil.checkPermission(this)) {
                        PermissionUtil.requestPermission(this);
                    } else {
                        chooseImageFromGallery();
                    }
                }
        }
    }

    private void chooseImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select School Logo"), PICK_IMAGE);
    }

    public void setStandardFees(ArrayList<StandardFeesModel> standardFees) {
        this.standardFees = standardFees;
        feesEditText.setText(standardFees.get(0).getStandard() + "," + standardFees.get(0).getFees());
    }

    @Override
    public void bindPresenter(AddSchoolContract.AddSchoolPresenter addSchoolPresenter) {
        this.addSchoolPresenter = addSchoolPresenter;
    }

    @Override
    public void showSpinner() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void closeSchoolForm() {
        hideSpinner();
        this.finish();
    }

    @Override
    public void hideSpinner() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {

    }

    @Override
    public void sendImage(SchoolListResponse response) {
        if (requestFile == null) {
            closeSchoolForm();
        } else if (response.getImages() != null) {
            addSchoolPresenter.sendImageByte(requestFile, response.getImages().get(0).getWriteUrl());
        }

    }

    @Override
    public void updateSchoolListCache(Object object) {
        List<SchoolListResponse> institutions = SharedPrefUtils.retrieveInstituteList(this);
        institutions.add((SchoolListResponse) object);
        SharedPrefUtils.storeInstitituteObject(this, institutions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            Uri uri = null;
            Toast.makeText(this, "Image selection Succesfull", Toast.LENGTH_SHORT).show();
            if (data != null)
                uri = data.getData();
            try {
                if (null != this.getContentResolver() && null != uri) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    schoolLogo.setVisibility(View.VISIBLE);
                    schoolLogo.setImageBitmap(bitmap);

                    String realPath = ImageFilePath.getPath(AddSchoolActivity.this, data.getData());
                    File finalFile = new File(realPath);
                    requestFile = RequestBody.create(MediaType.parse("image"), finalFile);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == REQUEST_APP_SETTINGS) {
            if (PermissionUtil.checkPermission(this)) {
                chooseImageFromGallery();
            }
        }


        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP

    }

    private byte[] getImageString(ImageView image) {
        Bitmap bitmap = getBitmap(image);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        return byteArrayOutputStream.toByteArray();

    }

    public Bitmap getBitmap(ImageView view) {
        try {
            if (view.getDrawable() != null) {
                Bitmap bitmap;
                bitmap = Bitmap.createBitmap(view.getDrawable().getIntrinsicWidth(), view.getDrawable().getIntrinsicHeight(), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                view.getDrawable().setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                view.getDrawable().draw(canvas);
                return bitmap;
            }
        } catch (OutOfMemoryError e) {
            return null;
        }
        return null;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID);
        return cursor.getString(idx);
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

}
