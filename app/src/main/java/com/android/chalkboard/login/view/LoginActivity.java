package com.android.chalkboard.login.view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.SplashScreen;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.login.modal.LoginResponse;
import com.android.chalkboard.login.modal.UserRequest;
import com.android.chalkboard.login.modal.UserRequestOTP;
import com.android.chalkboard.login.modal.ValidateOTP;
import com.android.chalkboard.login.presenter.LoginContract;
import com.android.chalkboard.login.presenter.LoginPresenterImpl;
import com.android.chalkboard.register.view.RegistrationActivity;
import com.android.chalkboard.util.CommonUtils;
import com.android.chalkboard.util.SharedPrefUtils;

import static com.android.chalkboard.util.Constant.ID;
import static com.android.chalkboard.util.Constant.LOGINSTATUS;

// TODO : Need to be connected with presenter and vice versa

public class LoginActivity extends AppCompatActivity {

//    private TextInputEditText emailIdMobileNumberEditText, otpPasswordEditText, selectRoleEditText;
//    private TextInputLayout selectRoleTil, emailPhoneNo;
//    private Button loginButton;
//    private TextView signUpTextView;
//    private LoginContract.LoginPresenter mPresenter;
//    private RelativeLayout rootView;
    private ProgressBar progressBar;
//    private String role;
    ViewPager loginViewPager;
    loginFragment_1 loginFragment_1 = new loginFragment_1();
    loginFragment_2 loginFragment_2 = new loginFragment_2(loginFragment_1);

    ViewPager loginPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LoginPresenterImpl.createPresenter(this);
        setContentView(R.layout.layout_activity_login);
//        loginButton = findViewById(R.id.btn_login);
//        signUpTextView = findViewById(R.id.tv_signup);
        loginPager = findViewById(R.id.loginViewPager);
        loginViewPager = findViewById(R.id.loginViewPager);
//        loginButton.setOnClickListener(this);
//        signUpTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
//                startActivity(intent);
//            }
//        });
//        emailIdMobileNumberEditText = findViewById(R.id.et_email_phone_number);
//        emailPhoneNo = findViewById(R.id.til_email_phone_number);
//        selectRoleEditText = findViewById(R.id.et_select_role);
//        otpPasswordEditText = findViewById(R.id.et_password_otp);
//        rootView = findViewById(R.id.parent_login_container);
//        selectRoleTil = findViewById(R.id.til_select_role);
        progressBar = findViewById(R.id.progress);
//        selectRoleEditText.setOnFocusChangeListener(this);
//        selectRoleEditText.setOnClickListener(this);
//        emailIdMobileNumberEditText.setOnFocusChangeListener(this);
//        otpPasswordEditText.addTextChangedListener(this);

        loginViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
    }

    public void replace_Fragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_view, fragment, null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
        transaction.addToBackStack("Fragment");
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            Fragment fragment = null;

            switch (i) {
                case 0:
                    fragment = loginFragment_1;
                    break;
                case 1:
                    fragment = loginFragment_2;
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//
//        switch (id) {
//            case R.id.btn_login:
//                if (TextUtils.isEmpty(selectRoleEditText.getText().toString())) {
//                    selectRoleTil.setErrorEnabled(true);
//                    selectRoleTil.setError("Please select a role");
//                    return;
//                }
//                if (loginButton.getText().toString().equalsIgnoreCase(getString(R.string.get_otp))) {
//                    {
//                        handleGetOTPClick();
//                    }
//                }else if (loginButton.getText().toString().equalsIgnoreCase(getString(R.string.get_started_btn))){
//                    handleGetStartedClick();
//                }else if(loginButton.getText().toString().equalsIgnoreCase(getString(R.string.verify_otp))){
//                    handleSubmitOTPClick();
//                }
//                break;
//            case R.id.tv_signup:
//                //if (!TextUtils.isEmpty(selectRoleEditText.getText().toString())) {
//                    Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
//                    startActivity(intent);
//                //} else {
//                   // selectRoleTil.setErrorEnabled(true);
//                    //selectRoleTil.setError(getString(R.string.select_a_role));
//                //}
//                break;
//
//            case R.id.et_select_role:
//                if (TextUtils.isEmpty(selectRoleEditText.getText().toString())) {
//                    FragmentTransaction dialogTransaction = getSupportFragmentManager().beginTransaction();
//                    RoleSelectionFragment dialogFragment = new RoleSelectionFragment();
//                    dialogFragment.show(dialogTransaction, "dialog");
//                }
//                break;
//
//        }
//    }
//
//    private void handleSubmitOTPClick() {
//        if(CommonUtils.isValidPhoneNumber(emailIdMobileNumberEditText.getText().toString())) {
//            ValidateOTP validateOTP = new ValidateOTP();
//            validateOTP.setMobileNo(emailIdMobileNumberEditText.getText().toString());
//            validateOTP.setRole(role.toLowerCase());
//            validateOTP.setOtp(otpPasswordEditText.getText().toString());
//            mPresenter.loginUser(validateOTP, false);
//        }else{
//            emailPhoneNo.setErrorEnabled(true);
//            emailPhoneNo.setError(getString(R.string.enter_correct_email_number));
//        }
//    }
//
//    private void handleGetStartedClick() {
//        if(CommonUtils.isValidEmailAddress(emailIdMobileNumberEditText.getText().toString())){
//            UserRequest userRequest = new UserRequest();
//            userRequest.setUserName(emailIdMobileNumberEditText.getText().toString());
//            userRequest.setPassword(otpPasswordEditText.getText().toString());
//            userRequest.setRole(role.toLowerCase());
//            mPresenter.loginUser(userRequest, false);
//        }else{
//            emailPhoneNo.setErrorEnabled(true);
//            emailPhoneNo.setError(getString(R.string.enter_correct_email_number));
//        }
//    }
//
//    public void handleGetOTPClick() {
//        if(CommonUtils.isValidPhoneNumber(emailIdMobileNumberEditText.getText().toString())) {
//            UserRequestOTP requestOTP = new UserRequestOTP();
//            requestOTP.setMobileNo(emailIdMobileNumberEditText.getText().toString());
//            requestOTP.setRole(role.toLowerCase());
//            mPresenter.loginUser(requestOTP, true);
//        }else{
//            emailPhoneNo.setErrorEnabled(true);
//            emailPhoneNo.setError(getString(R.string.enter_correct_email_number));
//        }
//    }
//
//
//    @Override
//    public void bindPresenter(LoginContract.LoginPresenter presenter) {
//        mPresenter = presenter;
//    }
//

    public void showSpinner() {
        progressBar.setVisibility(View.VISIBLE);
    }


    public void hideSpinner() {
        progressBar.setVisibility(View.GONE);

    }
//
//    @Override
//    public void navigateToDashBoard(Object success) {
//        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();
//        SharedPrefUtils.storeInSharedPref(getApplicationContext(), LOGINSTATUS, "true");
//        if(success != null) {
//            SharedPrefUtils.storeInSharedPref(getApplicationContext(), ID, String.valueOf(((LoginResponse) success).getId()));
//            saveToken(((LoginResponse) success).getJwtToken());
//        }
//        Intent intent = new Intent(LoginActivity.this, NavDashBoardActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//    @Override
//    public void showError(String errorMessage) {
//        Snackbar.make(loginButton, errorMessage, Snackbar.LENGTH_INDEFINITE).setAction("OK", null).show();
//    }
//
//    @Override
//    public void createValidateOTPCall() {
//        loginButton.setText(getString(R.string.verify_otp));
//        Snackbar.make(loginButton, R.string.otp_sent, Snackbar.LENGTH_INDEFINITE).setAction("OK", null).show();
//
//    }
//
//    public void saveToken(String jwtToken) {
//       SharedPrefUtils.storeInSharedPref(this, SharedPrefUtils.JWTToken,jwtToken);
//       // JwtTokenSingleton.getInstance().setJwtToken(jwtToken);
//    }
//
//    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//        int id = v.getId();
//        switch (id) {
//            case R.id.et_select_role:
//            if (hasFocus && TextUtils.isEmpty(selectRoleEditText.getText().toString())) {
//                FragmentTransaction dialogTransaction = getSupportFragmentManager().beginTransaction();
//                RoleSelectionFragment dialogFragment = new RoleSelectionFragment();
//                dialogFragment.show(dialogTransaction, "dialog");
//            } else if (!hasFocus && !TextUtils.isEmpty(selectRoleEditText.getText().toString())) {
//                selectRoleTil.setErrorEnabled(false);
//            }
//            break;
//            case R.id.et_email_phone_number:
//                handleError(hasFocus);
//                break;
//        }
//    }
//
//    public void handleError(boolean hasFocus){
//        if(!hasFocus){
//            if(!CommonUtils.isValidPhoneNumber(emailIdMobileNumberEditText.getText().toString()) &&
//                    !CommonUtils.isValidEmailAddress(emailIdMobileNumberEditText.getText().toString())){
//                emailPhoneNo.setErrorEnabled(true);
//                emailPhoneNo.setError(getString(R.string.enter_correct_email_number));
//            }else if(CommonUtils.isValidPhoneNumber(emailIdMobileNumberEditText.getText().toString())){
//                emailPhoneNo.setErrorEnabled(false);
//                loginButton.setText(R.string.get_otp);
//            }else{
//                emailPhoneNo.setErrorEnabled(false);
//                loginButton.setText(R.string.get_started_btn);
//            }
//
//        } else if (hasFocus && TextUtils.isEmpty(emailIdMobileNumberEditText.getText().toString())) {
//            loginButton.setText(R.string.get_started_btn);
//        }
//
//    }
//
//    public void setRoleText(String role) {
//        this.role = role;
//        selectRoleEditText.setText(role);
//    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//
//    }
}

