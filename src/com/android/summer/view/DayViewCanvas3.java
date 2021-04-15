package com.android.summer.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class DayViewCanvas3 {

    Paint paint = new Paint();

    DayView dayView;

    float width,height;

    float borderWidth = 50;

    RectF borderRect;

    float borderRadious = borderWidth*2;

    int borderColor = Color.parseColor("#cccccc");

    public DayViewCanvas3(DayView dayView) {
        this.dayView = dayView;
        paint.setAntiAlias(true);
        width = dayView.getWidth();
        height = dayView.getHeight();
        borderRect = new RectF(borderWidth/2,borderWidth/2,width-borderWidth/2,height-borderWidth/2);
    }

    public void draw(Canvas canvas){
        //drawBorder(canvas);
        //drawFinger(canvas);
    }

    private void drawBorder(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setColor(borderColor);
        canvas.drawRoundRect(borderRect,borderRadious,borderRadious,paint);
    }

    private void drawFinger(Canvas canvas){
        canvas.drawLine(width/2,height/2,width/2,0,paint);
    }


}
