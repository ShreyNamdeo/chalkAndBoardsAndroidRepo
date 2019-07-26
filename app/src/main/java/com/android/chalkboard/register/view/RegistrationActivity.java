package com.android.chalkboard.register.view;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.register.model.User;
import com.android.chalkboard.register.presenter.RegistrationContract;
import com.android.chalkboard.register.presenter.RegistrationPresenterImpl;
import com.android.chalkboard.util.SharedPrefUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import static com.android.chalkboard.util.Constant.LOGINSTATUS;
import static com.android.chalkboard.util.SharedPrefUtils.ROLE;

// TODO: Need to connect with presenter. This is just for starting purpose

public class RegistrationActivity extends AppCompatActivity implements RegistrationContract.View {

    private RegistrationContract.RegistrationPresenter registrationPresenter;
    public static User user;
    private ProgressBar progressBar;
    private String nameText="", emailText="", phoneText="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            RegistrationPresenterImpl.createPresenter(this);

            setContentView(R.layout.layout_activity_registration);

            progressBar = findViewById(R.id.progress);

            final ViewPager mImageViewPager = findViewById(R.id.registrationViewPager);
            TabLayout tabLayout = findViewById(R.id.registrationTabLayout);
            tabLayout.setupWithViewPager(mImageViewPager, true);
            final RegistrationFragmentAdapter r = new RegistrationFragmentAdapter(getSupportFragmentManager());
            mImageViewPager.setAdapter(r);

            final Button b = findViewById(R.id.next);

            mImageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    if(i == 2)
                        b.setBackgroundResource(R.drawable.checkmark);
                    else
                        b.setBackgroundResource(R.drawable.next_arrow);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });

            user = new User();
            final EditText name = r.getRegisterFragment2().getNameEditText();
            final EditText email = r.getRegisterFragment2().getEmailEditText();
            final EditText phone = r.getRegisterFragment2().getPhoneEditText();
            final EditText password = r.getRegisterFragment3().getPassword();
            final EditText cPassword = r.getRegisterFragment3().getCpassword();

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if(mImageViewPager.getCurrentItem() == 0) {
                            if (user.getRole().equals("")) {
                                showError("Select atleast one role to register");
                            } else
                                mImageViewPager.setCurrentItem(mImageViewPager.getCurrentItem() + 1);
                        } else if(mImageViewPager.getCurrentItem() == 1) {
                            if (r.getRegisterFragment2().getNameEditText().getText().toString().trim().equals("")) {
                                showError("Name cannot be empty");
                                r.getRegisterFragment2().getNameEditText().setError("Name cannot be empty");
                            } else if (r.getRegisterFragment2().getNameEditText().getText().toString().trim().equals("")) {
                                showError("Name cannot be empty");
                                r.getRegisterFragment2().getNameEditText().setError("Name cannot be empty");
                            } else if (!isValidEmailAddress(r.getRegisterFragment2().getEmailEditText().getText())) {
                                showError("Email address not valid.");
                                r.getRegisterFragment2().getEmailEditText().setError("Email address not valid.");
                            } else if (!isPhoneValid(r.getRegisterFragment2().getPhoneEditText().getText())) {
                                showError("Either phone number is empty or invalid.");
                                r.getRegisterFragment2().getPhoneEditText().setError("Either phone number is empty or invalid.");
                            } else {
                                nameText = r.getRegisterFragment2().getNameEditText().getText().toString();
                                emailText = r.getRegisterFragment2().getEmailEditText().getText().toString();
                                phoneText = r.getRegisterFragment2().getPhoneEditText().getText().toString();
                                mImageViewPager.setCurrentItem(mImageViewPager.getCurrentItem() + 1);

                            }
                        } else if(mImageViewPager.getCurrentItem() == 2){
                            if (!isValidPassword(r.getRegisterFragment3().getPassword().getText().toString())) {
                                showError(getApplicationContext().getString(R.string.invalid_password));
                                email.setError(getApplicationContext().getString(R.string.invalid_password));
                            } else if (!r.getRegisterFragment3().getPassword().getText().toString().equals(r.getRegisterFragment3().getCpassword().getText().toString())) {
                                showError("Both password does not match");
                                r.getRegisterFragment3().getCpassword().setError("Both password does not match");
                            }
                            else if (r.getRegisterFragment3().getCpassword().getText().toString().trim().equals("")) {
                                showError("Confirm Password cannot be empty");
                                r.getRegisterFragment3().getCpassword().setError("Confirm Password cannot be empty");
                            } else {
                                if (user.getRole().equals("")) {
                                    showError("Select atleast one role to register");
                                    mImageViewPager.setCurrentItem(0);
                                }
                                else if(r.getRegisterFragment2().getNameEditText().getText().toString().equals("")){
                                    showError("Name cannot be empty");
                                    mImageViewPager.setCurrentItem(1);
                                    r.getRegisterFragment2().getNameEditText().setError("Name cannot be empty");
                                }
                                else if (!isValidEmailAddress(r.getRegisterFragment2().getEmailEditText().getText().toString())) {
                                    showError("Email address not valid.");
                                    mImageViewPager.setCurrentItem(1);
                                    r.getRegisterFragment2().getEmailEditText().setError("Email address not valid.");
                                } else if (!isPhoneValid(r.getRegisterFragment2().getPhoneEditText().getText().toString())) {
                                    showError("Either phone number is empty or invalid.");
                                    mImageViewPager.setCurrentItem(1);
                                    r.getRegisterFragment2().getPhoneEditText().setError("Either phone number is empty or invalid.");
                                } else {
                                    user.setName(r.getRegisterFragment2().getNameEditText().getText().toString());
                                    user.setEmail(r.getRegisterFragment2().getEmailEditText().getText().toString());
                                    user.setMobileNumber(r.getRegisterFragment2().getPhoneEditText().getText().toString());
                                    user.setPassword(r.getRegisterFragment3().getPassword().getText().toString());
                                    user.setDateOfBirth("1009-01-01");
                                    Snackbar.make(progressBar, user.getRole(), Snackbar.LENGTH_LONG).show();
                                    registrationPresenter.registerUser(user);
                                    //showOTPView();
                                }
                            }
                        }
                    } catch(Exception e){
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
    }

    public boolean isPhoneValid(CharSequence phone){
        boolean empty = !phone.toString().trim().equals("");
        boolean check91 = (phone.toString().startsWith("+91") && phone.toString().length() == 13);
        boolean check0 = (phone.toString().startsWith("0") && phone.toString().length() == 11);
        boolean checklength = phone.toString().length() == 10;

        return empty && (check91 || check0 || checklength);
    }


    public boolean isValidEmailAddress(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches() && target.toString().trim().length() >= 8);
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    @Override
    public void bindPresenter(RegistrationContract.RegistrationPresenter registrationPresenter) {
        this.registrationPresenter = registrationPresenter;
    }

    @Override
    public void showSpinner() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideSpinner() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void showOTPView() {
        SharedPrefUtils.storeInSharedPref(getApplicationContext(), LOGINSTATUS, "true");
        SharedPrefUtils.storeInSharedPref(getApplicationContext(), ROLE, user.getRole());
        registrationPresenter.getJWTToken(user.getMobileNumber(), user.getRole());
    }

    public void navigateToDashboard(){
        Snackbar.make(progressBar, "Login Success", Snackbar.LENGTH_LONG).show();
        Intent intent = new Intent(RegistrationActivity.this, NavDashBoardActivity.class);
        startActivity(intent);
    }

    @Override
    public void showError(String errorMessage) {
        //Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
        Snackbar.make(progressBar, errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void saveToken(String token) {
        SharedPrefUtils.storeInSharedPref(this, SharedPrefUtils.JWTToken,token);


        Snackbar.make(progressBar, token, Snackbar.LENGTH_LONG).show();
                navigateToDashboard();
        // JwtTokenSingleton.getInstance().setJwtToken(jwtToken);
    }
}
