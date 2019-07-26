package com.android.chalkboard.school.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.chalkboard.R;
import com.android.chalkboard.school.model.AddSchoolResponse;
import com.android.chalkboard.school.model.EditSchoolRequest;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.school.model.StandardFeesModel;
import com.android.chalkboard.school.presenter.AddSchoolPresenterImpl;
import com.android.chalkboard.school.presenter.EditSchoolContract;
import com.android.chalkboard.school.presenter.EditSchoolPresenterImpl;

import java.util.ArrayList;
import java.util.List;

public class SchoolEditViewActivity extends AppCompatActivity implements EditSchoolContract.EditSchoolView {
    private SchoolListResponse schoolListResponse;
    private TextView schoolNameTextViewView,principleNameTextView,addressTextView,cityTextView,specializationTextView,
                    affiliationTextView,typeTextView,regNoTextView;
    private LinearLayout feesInfoViewLinearLayout,feesInfoEditViewLinearLayout;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapseToolbarLayout;
    private AppBarLayout appBarLayout;
    private TextInputEditText schoolNameEditText,principleNameEditText,addressEditText,cityEditText,specializationEditText,
            affiliationEditText,typeEditText,regNoEditText;
    private  boolean isEditMode;
    private TextInputLayout schoolNameTextInputEditText,principleNameTextInputEditText,addressTextInputEditText,
            cityTextInputEditText,specializationTextInputEditText,affiliationTextInputEditText,typeTextInputEditText,
            regNoTextInputEditText;
    private Button submitButton;
    private String instituteId;
    private EditSchoolContract.EditSchoolPresenter editSchoolPresenter;
    private EditSchoolRequest editSchoolRequest;
    private ArrayList<StandardFeesModel> fee;
    private ArrayList<TextInputEditText> textInputEditTexts;
    private ArrayList<TextView> textViews;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_edit_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapseToolbarLayout = findViewById(R.id.toolbar_layout);
        appBarLayout = findViewById(R.id.app_bar);
        EditSchoolPresenterImpl.createPrensenter(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progress);
        setSupportActionBar(toolbar);
        textInputEditTexts = new ArrayList<>();
        textViews = new ArrayList<>();
        fee = new ArrayList<>();
        schoolListResponse = (SchoolListResponse) getIntent().getSerializableExtra("SchoolListResponseObj");
        setUpToolbar();
        instituteId = schoolListResponse.getId().toString();
        isEditMode = getIntent().getBooleanExtra("isEditView",false);
        initViews();
        if(!isEditMode)
            showSchoolInformation();
        else
            editSchoolInformation();

    }

    private void editSchoolInformation() {
        setVisibilityForViews();
        schoolNameEditText.setText(schoolListResponse.getName());
        principleNameEditText.setText(schoolListResponse.getPrincipalName());
        addressEditText.setText(schoolListResponse.getAddress());
        cityEditText.setText(schoolListResponse.getCity());
        specializationEditText.setText(schoolListResponse.getSpecialization());
        affiliationEditText.setText(schoolListResponse.getAffiliation());
        typeEditText.setText(schoolListResponse.getType());
        regNoEditText.setText(schoolListResponse.getRegistrationNo());
        for (int i = 0; i<schoolListResponse.getFee().size();i++ )
        {
            addStandardFeesEditView(schoolListResponse.getFee().get(i));
        }
    }

    private void addStandardFeesEditView(StandardFeesModel standardFeesModel) {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.fees_information_edit_view,null);
        TextView standard = view.findViewById(R.id.school_fees2_standard_label_tv);
        TextInputEditText fees = view.findViewById(R.id.et_school_fees_value);
        textInputEditTexts.add(fees);
        textViews.add(standard);
        standard.setText(standardFeesModel.getStandard());
        fees.setText(standardFeesModel.getFees()+"");
        feesInfoEditViewLinearLayout.addView(view,feesInfoEditViewLinearLayout.getChildCount());
    }

    private void showSchoolInformation() {
        setVisibilityForViews();
        schoolNameTextViewView.setText(schoolListResponse.getName());
        principleNameTextView.setText(schoolListResponse.getPrincipalName());
        addressTextView.setText(schoolListResponse.getAddress());
        cityTextView.setText(schoolListResponse.getCity());
        specializationTextView.setText(schoolListResponse.getSpecialization());
        affiliationTextView.setText(schoolListResponse.getAffiliation());
        typeTextView.setText(schoolListResponse.getType());
        regNoTextView.setText(schoolListResponse.getRegistrationNo());

        for (int i = 0; i<schoolListResponse.getFee().size();i++ )
        {
            addStandardFeesView(schoolListResponse.getFee().get(i));
        }


    }

    private void setVisibilityForViews() {
        if(!isEditMode) {
            schoolNameTextInputEditText.setVisibility(View.GONE);
            principleNameTextInputEditText.setVisibility(View.GONE);
            addressTextInputEditText.setVisibility(View.GONE);
            cityTextInputEditText.setVisibility(View.GONE);
            specializationTextInputEditText.setVisibility(View.GONE);
            affiliationTextInputEditText.setVisibility(View.GONE);
            typeTextInputEditText.setVisibility(View.GONE);
            regNoTextInputEditText.setVisibility(View.GONE);

            schoolNameTextViewView.setVisibility(View.VISIBLE);
            principleNameTextView.setVisibility(View.VISIBLE);
            addressTextView.setVisibility(View.VISIBLE);
            cityTextView.setVisibility(View.VISIBLE);
            specializationTextView.setVisibility(View.VISIBLE);
            affiliationTextView.setVisibility(View.VISIBLE);
            typeTextView.setVisibility(View.VISIBLE);
            regNoTextView.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.GONE);
        }else{
            schoolNameTextInputEditText.setVisibility(View.VISIBLE);
            principleNameTextInputEditText.setVisibility(View.VISIBLE);
            addressTextInputEditText.setVisibility(View.VISIBLE);
            cityTextInputEditText.setVisibility(View.VISIBLE);
            specializationTextInputEditText.setVisibility(View.VISIBLE);
            affiliationTextInputEditText.setVisibility(View.VISIBLE);
            typeTextInputEditText.setVisibility(View.VISIBLE);
            regNoTextInputEditText.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.VISIBLE);

            schoolNameTextViewView.setVisibility(View.GONE);
            principleNameTextView.setVisibility(View.GONE);
            addressTextView.setVisibility(View.GONE);
            cityTextView.setVisibility(View.GONE);
            specializationTextView.setVisibility(View.GONE);
            affiliationTextView.setVisibility(View.GONE);
            typeTextView.setVisibility(View.GONE);
            regNoTextView.setVisibility(View.GONE);
        }
    }

    private void addStandardFeesView(StandardFeesModel standardFeesModel) {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.fees_information_view,null);
        TextView standard = view.findViewById(R.id.school_fees1_standard_label_tv);
        TextView fees = view.findViewById(R.id.school_fees_value_tv);
        standard.setText(standardFeesModel.getStandard());
        fees.setText(standardFeesModel.getFees()+"");
        feesInfoViewLinearLayout.addView(view,feesInfoViewLinearLayout.getChildCount());

    }



    private void initViews() {
        schoolNameTextViewView = findViewById(R.id.school_name_value_tv);
        principleNameTextView = findViewById(R.id.school_principle_name_value_tv);
        addressTextView = findViewById(R.id.school_address_value_tv);
        cityTextView = findViewById(R.id.school_city_value_tv);
        specializationTextView = findViewById(R.id.school_specialization_value_tv);
        affiliationTextView = findViewById(R.id.school_affiliation_value_tv);
        typeTextView = findViewById(R.id.school_type_value_tv);
        regNoTextView = findViewById(R.id.school_reg_no_value_tv);

        schoolNameEditText = findViewById(R.id.et_school_name);
        principleNameEditText = findViewById(R.id.et_principle_name);
        addressEditText = findViewById(R.id.et_school_address);
        cityEditText = findViewById(R.id.et_school_city);
        specializationEditText = findViewById(R.id.et_school_specialization );
        affiliationEditText = findViewById(R.id.et_affiliation_name);
        typeEditText = findViewById(R.id.et_type);
        regNoEditText = findViewById(R.id.et_reg_no);

        schoolNameTextInputEditText = findViewById(R.id.til_school_name);
        principleNameTextInputEditText = findViewById(R.id.til_principle_name);
        addressTextInputEditText = findViewById(R.id.til_school_address);
        cityTextInputEditText = findViewById(R.id.til_school_city);
        specializationTextInputEditText = findViewById(R.id.til_school_specialization );
        affiliationTextInputEditText = findViewById(R.id.til_affiliation_name);
        typeTextInputEditText = findViewById(R.id.til_type);
        regNoTextInputEditText = findViewById(R.id.til_reg_no);

        feesInfoViewLinearLayout = findViewById(R.id.fees_text_view_ll);
        feesInfoEditViewLinearLayout = findViewById(R.id.fees_edit_view_ll);

        submitButton = findViewById(R.id.pnb_submit_fee);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                schoolNameEditText = findViewById(R.id.et_school_name);
                principleNameEditText = findViewById(R.id.et_principle_name);
                addressEditText = findViewById(R.id.et_school_address);
                cityEditText = findViewById(R.id.et_school_city);
                specializationEditText = findViewById(R.id.et_school_specialization );
                affiliationEditText = findViewById(R.id.et_affiliation_name);
                typeEditText = findViewById(R.id.et_type);
                regNoEditText = findViewById(R.id.et_reg_no);
                for (int i = 0; i<textInputEditTexts.size(); i++){
                    StandardFeesModel standardFeesModel = new StandardFeesModel(textViews.get(i).getText().toString(),Integer.parseInt(textInputEditTexts.get(i).getText().toString()),
                            (schoolListResponse.getFee().get(i).getId()),schoolListResponse.getFee().get(i).getInstitutionId());

                    fee.add(standardFeesModel);
                }
                editSchoolRequest = new EditSchoolRequest(schoolListResponse.getId(), schoolNameEditText.getText().toString(),principleNameEditText.getText().toString(),
                        addressEditText.getText().toString(),cityEditText.getText().toString(),specializationEditText.getText().toString(),affiliationEditText.getText().toString(),
                        typeEditText.getText().toString(),regNoEditText.getText().toString(),fee,schoolListResponse.getImages().size(),schoolListResponse.getImages());
                editSchoolPresenter.editSchool(editSchoolRequest);

            }
        });

    }

    @Override
    public void bindPresenter(EditSchoolContract.EditSchoolPresenter editSchoolPresenter) {
        this.editSchoolPresenter = editSchoolPresenter;
    }

    protected void setUpToolbar() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapseToolbarLayout.setTitle("School Details");
                    isShow = true;
                } else if(isShow) {
                    collapseToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
        toolbar = findViewById(R.id.toolbar_collapsable);
        collapseToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.appGreenColor));
    }


    @Override
    public void showSpinner() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void closeSchoolForm() {
        this.finish();

    }

    @Override
    public void hideSpinner() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {

    }


}
