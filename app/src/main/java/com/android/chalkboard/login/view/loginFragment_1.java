package com.android.chalkboard.login.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.login.modal.LoginResponse;
import com.android.chalkboard.login.modal.UserRequest;
import com.android.chalkboard.login.modal.UserRequestOTP;
import com.android.chalkboard.login.modal.ValidateOTP;
import com.android.chalkboard.login.presenter.LoginContract;
import com.android.chalkboard.login.presenter.LoginPresenterImpl;
import com.android.chalkboard.register.view.RegistrationActivity;
import com.android.chalkboard.util.CommonUtils;
import com.android.chalkboard.util.FontChangeCrawler;
import com.android.chalkboard.util.SharedPrefUtils;

import static com.android.chalkboard.util.Constant.ID;
import static com.android.chalkboard.util.Constant.LOGINSTATUS;

public class loginFragment_1 extends Fragment implements View.OnClickListener, LoginContract.LoginView, View.OnFocusChangeListener, TextWatcher {

    private View view;
    private Button loginButton;
    private TextInputEditText emailIdMobileNumberEditText;
    private ProgressBar progressBar;
    private TextInputLayout emailPhoneNo;
    private TextView select_role;
    private RelativeLayout rootView;
    private String role = "";
    private LoginContract.LoginPresenter mPresenter;
    ViewPager viewPager;
    LinearLayout admin_role,parent_role,teacher_role,student_role;
    private TextView signUpTextView;
    InputMethodManager inputManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_login_fragment_1,container,false);

        if (SharedPrefUtils.getFromSharedPref(getActivity().getApplicationContext(), LOGINSTATUS).equalsIgnoreCase("true")) {
            navigateToDashBoard(null);
        }
        LoginPresenterImpl.createPresenter(this);
        loginButton = (Button) view.findViewById(R.id.btn_login);
        signUpTextView = view.findViewById(R.id.tv_signup);
        loginButton.setOnClickListener(this);
        emailIdMobileNumberEditText = (TextInputEditText) view.findViewById(R.id.et_email_phone_number);
        emailPhoneNo =(TextInputLayout) view.findViewById(R.id.til_email_phone_number);
        rootView = view.findViewById(R.id.parent_login_container);
        select_role = (TextView) view.findViewById(R.id.select_role);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        emailIdMobileNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                }
                handleError(b);
            }
        });
        viewPager = getActivity().findViewById(R.id.loginViewPager);
        admin_role = (LinearLayout) view.findViewById(R.id.admin_role);
        parent_role = (LinearLayout) view.findViewById(R.id.parent_role);
        teacher_role = (LinearLayout) view.findViewById(R.id.teacher_role);
        student_role = (LinearLayout) view.findViewById(R.id.student_role);
        student_role.setFocusable(true);
        student_role.setFocusableInTouchMode(true);
        admin_role.setFocusable(true);
        admin_role.setFocusableInTouchMode(true);
        teacher_role.setFocusable(true);
        teacher_role.setFocusableInTouchMode(true);
        parent_role.setFocusable(true);
        parent_role.setFocusableInTouchMode(true);

        inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        student_role.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setRoleText("Student");
                select_role.setText("Role Selected");
                select_role.setTextColor(getResources().getColor(R.color.flatBlack));
            }
        });

        teacher_role.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setRoleText("Teacher");
                select_role.setText("Role Selected");
                select_role.setTextColor(getResources().getColor(R.color.flatBlack));
            }
        });

        parent_role.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setRoleText("Parent");
                select_role.setText("Role Selected");
                select_role.setTextColor(getResources().getColor(R.color.flatBlack));
            }
        });

        admin_role.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setRoleText("Admin");
                select_role.setText("Role Selected");
                select_role.setTextColor(getResources().getColor(R.color.flatBlack));
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void bindPresenter(LoginContract.LoginPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showSpinner() {
        ((LoginActivity)getActivity()).showSpinner();
    }

    @Override
    public void hideSpinner() {
        ((LoginActivity)getActivity()).hideSpinner();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_login:
                if (role.equals("")) {
                    select_role.setTextColor(getResources().getColor(R.color.colorRed));
                    select_role.setText("Please select a role");
                    return;
                }
                if (loginButton.getText().toString().equalsIgnoreCase(getString(R.string.get_otp))) {
                    {
                        handleGetOTPClick();
                       if(CommonUtils.isValidPhoneNumber(emailIdMobileNumberEditText.getText().toString())) {

                           viewPager.setCurrentItem(1);

                       }
                       else{
                           emailPhoneNo.setErrorEnabled(true);
                           emailPhoneNo.setError(getString(R.string.enter_correct_email_number));
                       }
                    }
                }else if(loginButton.getText().toString().equalsIgnoreCase("Enter Password!")) {
                    if(CommonUtils.isValidEmailAddress(emailIdMobileNumberEditText.getText().toString())) {
                        viewPager.setCurrentItem(1);
                    }
                }
                else if (loginButton.getText().toString().equalsIgnoreCase(getString(R.string.get_started_btn))){
                    handleGetStartedClick();
                }
                break;
            case R.id.tv_signup:
                //if (!TextUtils.isEmpty(selectRoleEditText.getText().toString())) {
                Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                startActivity(intent);
                //} else {
                // selectRoleTil.setErrorEnabled(true);
                //selectRoleTil.setError(getString(R.string.select_a_role));
                //}
                break;

            case R.id.et_select_role:
                if (role.equals("")) {
                    FragmentTransaction dialogTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    RoleSelectionFragment dialogFragment = new RoleSelectionFragment(this);
                    dialogFragment.show(dialogTransaction, "dialog");
                }
                break;

        }
    }

    @Override
    public void navigateToDashBoard(Object success) {
        //Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Login Success", Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
        SharedPrefUtils.storeInSharedPref(getActivity().getApplicationContext(), LOGINSTATUS, "true");
        if(success != null) {
            SharedPrefUtils.storeInSharedPref(getActivity().getApplicationContext(), ID, String.valueOf(((LoginResponse) success).getId()));
            SharedPrefUtils.storeInSharedPref(getActivity().getApplicationContext(), SharedPrefUtils.NAME_KEY, String.valueOf(((LoginResponse) success).getName()));
            saveToken(((LoginResponse) success).getJwtToken());
        }
        Intent intent = new Intent(getActivity(), NavDashBoardActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showError(String errorMessage) {
        Toast toast = Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void createValidateOTPCall() {
        loginButton.setText(getString(R.string.verify_otp));
        Toast toast = Toast.makeText(getContext(), R.string.otp_sent, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    public void saveToken(String jwtToken) {
        SharedPrefUtils.storeInSharedPref(getContext(), SharedPrefUtils.JWTToken,jwtToken);
        // JwtTokenSingleton.getInstance().setJwtToken(jwtToken);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        switch (id) {
            case R.id.et_select_role:
                if (hasFocus && role.equals("")) {
//                    FragmentTransaction dialogTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    RoleSelectionFragment dialogFragment = new RoleSelectionFragment(this);
//                    dialogFragment.show(dialogTransaction, "dialog");
                } else if (!hasFocus && !role.equals("")) {
                    select_role.setText("Role Selected");
                    select_role.setTextColor(getResources().getColor(R.color.flatBlack));
                }
                break;
        }
    }

    public void handleError(boolean hasFocus){
        if(!hasFocus){
            if(!CommonUtils.isValidPhoneNumber(emailIdMobileNumberEditText.getText().toString()) &&
                    !CommonUtils.isValidEmailAddress(emailIdMobileNumberEditText.getText().toString())){
                emailPhoneNo.setErrorEnabled(true);
                emailPhoneNo.setError(getString(R.string.enter_correct_email_number));
            }else if(CommonUtils.isValidPhoneNumber(emailIdMobileNumberEditText.getText().toString())){
                emailPhoneNo.setErrorEnabled(false);
                loginButton.setText(R.string.get_otp);
            }else if(CommonUtils.isValidEmailAddress(emailIdMobileNumberEditText.getText().toString())){
                emailPhoneNo.setErrorEnabled(false);
                loginButton.setText("Enter Password!");
            }
            else{
                emailPhoneNo.setErrorEnabled(false);
                loginButton.setText(R.string.get_started_btn);
            }

        } else if (hasFocus && TextUtils.isEmpty(emailIdMobileNumberEditText.getText().toString())) {
            loginButton.setText(R.string.get_started_btn);
        }

    }

    public void setRoleText(String role) {
        SharedPrefUtils.storeInSharedPref(getActivity(),SharedPrefUtils.ROLE, role);
        this.role = role;
    }

    public void handleSubmitOTPClick(String otp) {
        if(CommonUtils.isValidPhoneNumber(emailIdMobileNumberEditText.getText().toString())) {
            ValidateOTP validateOTP = new ValidateOTP();
            validateOTP.setMobileNo(emailIdMobileNumberEditText.getText().toString());
            validateOTP.setRole(role.toLowerCase());
            validateOTP.setOtp(otp);
            mPresenter.loginUser(validateOTP, false);
        }else{
            emailPhoneNo.setErrorEnabled(true);
            emailPhoneNo.setError(getString(R.string.enter_correct_email_number));
        }
    }

    public void handleGetStartedClick() {
        if(CommonUtils.isValidEmailAddress(emailIdMobileNumberEditText.getText().toString())){
            UserRequest userRequest = new UserRequest();
            userRequest.setUserName(emailIdMobileNumberEditText.getText().toString());
            userRequest.setRole(role.toLowerCase());
            mPresenter.loginUser(userRequest, false);
        }else{
            emailPhoneNo.setErrorEnabled(true);
            emailPhoneNo.setError(getString(R.string.enter_correct_email_number));
        }
    }

    public void handleGetStartedClick(String password) {
        if(CommonUtils.isValidEmailAddress(emailIdMobileNumberEditText.getText().toString())){
            UserRequest userRequest = new UserRequest();
            userRequest.setUserName(emailIdMobileNumberEditText.getText().toString());
            userRequest.setPassword(password);
            userRequest.setRole(role.toLowerCase());
            mPresenter.loginUser(userRequest, false);
        }else{
            emailPhoneNo.setErrorEnabled(true);
            emailPhoneNo.setError(getString(R.string.enter_correct_email_number));
        }
    }

    public boolean isValidEmail() {
        return CommonUtils.isValidEmailAddress(emailIdMobileNumberEditText.getText().toString());
    }

    public void handleGetOTPClick() {
        if(CommonUtils.isValidPhoneNumber(emailIdMobileNumberEditText.getText().toString())) {
            UserRequestOTP requestOTP = new UserRequestOTP();
            requestOTP.setMobileNo(emailIdMobileNumberEditText.getText().toString());
            requestOTP.setRole(role.toLowerCase());
            mPresenter.loginUser(requestOTP, true);
        }else{
            emailPhoneNo.setErrorEnabled(true);
            emailPhoneNo.setError(getString(R.string.enter_correct_email_number));
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "muli_black.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.main_view));
    }
}
