package com.android.chalkboard.register.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.chalkboard.R;
import static com.android.chalkboard.register.view.RegistrationActivity.user;

public class RegisterFragment1 extends Fragment implements View.OnClickListener{

    private String title;
    LinearLayout adminLayout,teacherLayout,parentLayout,studentLayout;

    public static RegisterFragment1 NewInstance(String title){
        RegisterFragment1 r = new RegisterFragment1();
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
        View view = inflater.inflate(R.layout.fragment_register1, container, false);

        final ImageView img1 = view.findViewById(R.id.adminIcon);
        final ImageView img2 = view.findViewById(R.id.teacherIcon);
        final ImageView img3 = view.findViewById(R.id.parentIcon);
        final ImageView img4 = view.findViewById(R.id.studentIcon);

        adminLayout = view.findViewById(R.id.adminLayout);
        teacherLayout = view.findViewById(R.id.teacherLayout);
        parentLayout = view.findViewById(R.id.parentLayout);
        studentLayout = view.findViewById(R.id.studentLayout);


        adminLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFragment1.this.onClick(img1);
            }
        });

        teacherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFragment1.this.onClick(img2);
            }
        });

        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFragment1.this.onClick(img3);
            }
        });

        studentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFragment1.this.onClick(img4);
            }
        });

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);

        switch(user.getRole()){
            case "admin":
                img1.performClick();
                break;
            case "teacher":
                img2.performClick();
                break;
            case "parent":
                img3.performClick();
                break;
            case "student":
                img4.performClick();
                break;
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        try {
            LinearLayout parent = (LinearLayout) view.getParent().getParent();
            int childsCount = parent.getChildCount();

            for (int i = 0; i < childsCount; i++) {
                LinearLayout p = (LinearLayout) parent.getChildAt(i);
                if (p.getChildAt(0).getId() == view.getId() && !p.getChildAt(0).isActivated()) {
                    ((ImageView) p.getChildAt(0)).setImageResource(R.drawable.user_white_icon);
                    p.setBackgroundResource(R.drawable.drawable_reistration_background_green);
                    ((TextView) p.getChildAt(1)).setTextColor(getResources().getColor(R.color.appWhite));
                    view.setActivated(true);
                    switch (view.getId()) {
                        case R.id.adminIcon:
                            user.setRole("admin");
                            break;
                        case R.id.teacherIcon:
                            user.setRole("teacher");
                            break;
                        case R.id.parentIcon:
                            user.setRole("parent");
                            break;
                        case R.id.studentIcon:
                            user.setRole("student");
                            break;
                    }
                } else {
                    ((ImageView) p.getChildAt(0)).setImageResource(R.drawable.user_black_icon);
                    p.setBackgroundResource(R.drawable.drawable_registration_background_white);
                    ((TextView) p.getChildAt(1)).setTextColor(getResources().getColor(R.color.light_gray));
                    view.setActivated(false);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
