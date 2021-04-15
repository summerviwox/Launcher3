package com.android.summer.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class DayViewCanvas4 {

    Paint paint = new Paint();

    DayView dayView;

    float width,height;

    float borderStrokeLength = 50;

    float borderStrokeLength_2 = borderStrokeLength/2;

    RectF borderRect;

    float borderRadious;

    int borderColor = Color.parseColor("#cccccc");

    float verhicleHourLenght,horizonHourLength;

    HashMap<Integer,Point> pointMap = new HashMap<>();

    ArrayList<Point> pointList = new ArrayList<>();

    float borderRectWidth;
    float borderRectHeight;
    float borderRectWidth_4;
    float borderRectHeight_8;
    float rectLength;
    float rectLength_24_60;
    float halfVerhicleStraightLineLength;
    float halfHorizonStraightLineLength;
    int[][] areaDirect = new int[][]{{0,1},{-1,0},{0,-1},{1,0}};
    float roundRectAreaLength;

    public DayViewCanvas4(DayView dayView) {
        this.dayView = dayView;
        paint.setAntiAlias(true);
        width = dayView.getWidth();
        height = dayView.getHeight();
        borderRect = new RectF(borderStrokeLength_2, borderStrokeLength_2,width- borderStrokeLength_2,height- borderStrokeLength_2);
        borderRadious =100;
        init();
    }

    private void init(){
        borderRectWidth = width- borderStrokeLength;
        borderRectWidth_4 = borderRectWidth/4;
        borderRectHeight = height - borderStrokeLength;
        borderRectHeight_8 = borderRectHeight/8;
        rectLength = (float) (2*(borderRectWidth+borderRectHeight)-8*borderRadious+2*Math.PI*borderRadious);
        rectLength_24_60 = rectLength/(24*60);
        halfVerhicleStraightLineLength = height/2-2*borderRadious;
        halfHorizonStraightLineLength = width/2-2*borderRadious;
        roundRectAreaLength = borderStrokeLength_2+borderRadious;



        for(int i=4*60;i<8*60;i++){
            pointList.add(new Point(width-borderStrokeLength_2,borderStrokeLength_2+i*borderRectHeight/(8*60)));
        }


        for(int i=4*60-1;i>=0;i--){
            pointList.add(new Point(borderStrokeLength_2+i*borderRectWidth/(4*60),height-borderStrokeLength_2));
        }

        for(int i=8*60-1;i>=0;i--){
            pointList.add(new Point(borderStrokeLength_2,borderStrokeLength_2+i*borderRectHeight/(8*60)));
        }

        for(int i=0;i<4*60;i++){
            pointList.add(new Point(borderStrokeLength_2+i*borderRectWidth/(4*60),borderStrokeLength_2));
        }

        for(int i=0;i<4*60;i++){
            pointList.add(new Point(width-borderStrokeLength_2,borderStrokeLength_2+i*borderRectHeight/(8*60)));
        }

        for(int i=0;i<pointList.size();i++){
            Point point = pointList.get(i);
            if((point.x<=roundRectAreaLength||(point.x>=(width-roundRectAreaLength)))
            &&((point.y<=roundRectAreaLength)||(point.y>=(height-roundRectAreaLength)))){

            }
        }
    }

    public void draw(Canvas canvas){
        drawBorder(canvas);
        drawPoint(canvas);
        //drawFinger(canvas);
    }

    private void drawBorder(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderStrokeLength);
        paint.setColor(borderColor);
        canvas.drawRoundRect(borderRect,borderRadious,borderRadious,paint);
    }

    private void drawPoint(Canvas canvas){
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        for(int i=0;i<pointList.size();i++){
            Point point = pointList.get(i);
            if((point.x<=roundRectAreaLength||(point.x>=(width-roundRectAreaLength)))
                    &&((point.y<=roundRectAreaLength)||(point.y>=(height-roundRectAreaLength)))){
                canvas.drawCircle(point.x,
                        point.y,
                        1,
                        paint);
            }

        }
    }


    private void drawFinger(Canvas canvas){
        canvas.drawLine(width/2,height/2,width/2,0,paint);
    }

}
