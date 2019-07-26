package com.android.chalkboard.dashboard.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.chalkboard.R;

public class logout_Dialog extends Dialog {

    TextView yes,no,logout_text;
    Context context;
    Typeface typeface;

    public logout_Dialog(@NonNull Context context) {
        super(context);
        this.context = context;
        typeface = Typeface.createFromAsset(context.getAssets(),"muli_black.ttf");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.logout_confirmation);

        yes = (TextView) findViewById(R.id.yes_logout);
        no = (TextView) findViewById(R.id.no_logout);
        logout_text = (TextView) findViewById(R.id.logout_text);

        yes.setTypeface(typeface);
        no.setTypeface(typeface);
        logout_text.setTypeface(typeface);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavDashBoardActivity)context).logout();
            }
        });


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout_Dialog.this.dismiss();
            }
        });
    }
}
