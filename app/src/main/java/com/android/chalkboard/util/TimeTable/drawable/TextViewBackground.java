package com.android.chalkboard.util.TimeTable.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.android.chalkboard.R;

public class TextViewBackground extends Drawable {

    Context context;
    int color;
    Paint paint;
    Path path;
    Rect rect1, rect2;
    int direction;
    int percent;
    int height, width;
    public static int DOWNWARD = 0, UPWARD = 1, NONE = -1;
    int todayColor;

    public TextViewBackground(Context context, int color,  int direction, int percent, int height, int width){
        this.context = context;
        this.color = color;
        this.direction = direction;
        this.percent = percent;
        this.height = height;
        this.width = width;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);

        int bound = (int)(percent * height / 100);
        /*if(direction == UPWARD){
            rect1 = new Rect(0, bound, width, height);
            rect2 = new Rect(0, 0, width, bound);
        }
        else if(direction == DOWNWARD){
            rect1 = new Rect(0, 0, width,bound);
            rect2 = new Rect(0, bound, width, height);
        } else
            rect1 = new Rect(0, 0, width, bound);*/
        rect1 = new Rect(0, 0, width, bound);
        rect2 = new Rect(0, bound, width, height);
    }

    public TextViewBackground setTodayColor(int color){
        this.todayColor = color;
        return this;
    }

    @Override
    public void setAlpha(int alpha){
        paint.setAlpha(alpha);
    }

    @Override
    public void draw(Canvas canvas){
        int colorNill = todayColor != 0 ? todayColor : R.color.appWhite;
        if(direction == UPWARD)
            paint.setColor(context.getResources().getColor(colorNill));
        else
            paint.setColor(context.getResources().getColor(R.color.light_pink));

        canvas.drawRect(rect1, paint);

        if(direction != NONE) {
            if(direction == UPWARD)
                paint.setColor(context.getResources().getColor(R.color.light_pink));
            else
                paint.setColor(context.getResources().getColor(colorNill));
        //paint.setColor(context.getResources().getColor(R.color.light_salmon));

            canvas.drawRect(rect2, paint);
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf){
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity(){
        return PixelFormat.TRANSLUCENT;
    }
}
