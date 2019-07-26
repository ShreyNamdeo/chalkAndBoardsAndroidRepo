package com.android.chalkboard.register.view;

import android.support.v4.view.ViewPager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

    public MyViewPager(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent){
        return true;
    }

}
