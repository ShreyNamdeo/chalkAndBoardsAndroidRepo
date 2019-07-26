package com.android.chalkboard.register.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import com.android.chalkboard.R;

public class RegisterFragment2 extends Fragment implements View.OnFocusChangeListener{

    private String title;
    private EditText nameText, emailText, phoneText;

    public static RegisterFragment2 NewInstance(String title){
        RegisterFragment2 r = new RegisterFragment2();
        Bundle b = new Bundle();
        b.putString("title", title);
        r.setArguments(b);
        return r;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_register2, container, false);
        nameText = view.findViewById(R.id.nameText);
        nameText.setOnFocusChangeListener(this);
        emailText = view.findViewById(R.id.emailText);
        emailText.setOnFocusChangeListener(this);
        phoneText = view.findViewById(R.id.phoneText);
        phoneText.setOnFocusChangeListener(this);

        return view;
    }

    @Override
    public void onFocusChange(View view, boolean status){
        switch(view.getId()){
            case R.id.nameText:
                RegistrationActivity.user.setName(nameText.getText().toString());
                nameText.setBackgroundResource(R.drawable.roundcorner_rectangle_border);
                emailText.setBackgroundResource(R.drawable.roundcorner_rectangle_border_greu);
                phoneText.setBackgroundResource(R.drawable.roundcorner_rectangle_border_greu);
                break;
            case R.id.emailText:
                nameText.setBackgroundResource(R.drawable.roundcorner_rectangle_border_greu);
                emailText.setBackgroundResource(R.drawable.roundcorner_rectangle_border);
                phoneText.setBackgroundResource(R.drawable.roundcorner_rectangle_border_greu);
                RegistrationActivity.user.setEmail(emailText.getText().toString());
                break;
            case R.id.phoneText:
                nameText.setBackgroundResource(R.drawable.roundcorner_rectangle_border_greu);
                emailText.setBackgroundResource(R.drawable.roundcorner_rectangle_border_greu);
                phoneText.setBackgroundResource(R.drawable.roundcorner_rectangle_border);
                RegistrationActivity.user.setMobileNumber(phoneText.getText().toString());
                break;
        }
    }

    public EditText getNameEditText(){
        return nameText;
    }

    public EditText getEmailEditText(){
        return emailText;
    }

    public EditText getPhoneEditText(){
        return phoneText;
    }

}
