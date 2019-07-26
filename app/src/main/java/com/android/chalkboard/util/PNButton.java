package com.android.chalkboard.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.android.chalkboard.R;

public class PNButton extends android.support.v7.widget.AppCompatButton {

    private boolean isPositive;


    public PNButton(Context context) {
        super(context);
        initView(context, null);

    }

    public PNButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);

    }

    public PNButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.PNButton, 0, 0);
        try {
            isPositive = a.getBoolean(R.styleable.PNButton_isPositive, true);
        } finally {
            a.recycle();
        }
        if (isPositive) {
            // set positive button bg
            this.setBackground(ContextCompat.getDrawable(context, R.drawable.positive_button_bg));
            setTextColor(ContextCompat.getColor(context, R.color.appWhite));


        } else {
            // set negative button bg
            this.setBackground(ContextCompat.getDrawable(context, R.drawable.negative_button_bg));
            setTextColor(ContextCompat.getColor(context, R.color.appGreenColor));


        }
    }
}
