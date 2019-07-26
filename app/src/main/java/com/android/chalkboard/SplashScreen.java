package com.android.chalkboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.chalkboard.dashboard.view.NavDashBoardActivity;
import com.android.chalkboard.login.view.LoginActivity;
import com.android.chalkboard.util.SharedPrefUtils;

import static com.android.chalkboard.util.Constant.LOGINSTATUS;

public class SplashScreen extends AppCompatActivity {

    RelativeLayout logo;
    TextView tag_line;
    private Handler handler;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_splash_screen);
        logo = (RelativeLayout) findViewById(R.id.logo);
        tag_line = (TextView) findViewById(R.id.tag_line);
        typeface = Typeface.createFromAsset(this.getAssets(),"muli_black.ttf");

        tag_line.setTypeface(typeface);

        handler =new Handler();

        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.welcome_fade);
        logo.setAnimation(animation1);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_2);
        tag_line.setAnimation(animation);

        Handlers();

    }

    private void Handlers() {
        handler.postDelayed(new Runnable() {
            public void run() {

                if (SharedPrefUtils.getFromSharedPref(getApplicationContext(), LOGINSTATUS).equalsIgnoreCase("true")) {
                    Intent intent = new Intent(SplashScreen.this, NavDashBoardActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 2000);
    }


}
