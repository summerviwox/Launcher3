package com.android.summer.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DayViewCanvas5 {

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
    double borderRectLength;
    double rectLength_24_60;
    double halfVerhicleStraightLineLength;
    double halfHorizonStraightLineLength;
    int[][] areaDirect = new int[][]{{0,1},{-1,0},{0,-1},{1,0}};
    double roundRectAreaLength;

    public DayViewCanvas5(DayView dayView) {
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
        borderRectLength =   (2*(borderRectWidth+borderRectHeight)-8*borderRadious+2*Math.PI*borderRadious);
        rectLength_24_60 = borderRectLength /(24*60);
        halfVerhicleStraightLineLength = borderRectHeight/2-borderRadious;
        halfHorizonStraightLineLength = borderRectWidth/2-borderRadious;
        roundRectAreaLength = borderStrokeLength_2+borderRadious;

        int count=0;
        int LineCount = 0;
        int lineToRoundTurningCount = 0;
        int roundToLineTurningCount = 0;
        boolean linteToRoundTurning = false;
        boolean roundToLineTurning = false;
        double lastLineToRoundMiniLength = 0;
        double firstLineToRoundMiniLength = 0;
        double lastRoundToLineMiniLength = 0;
        double firstRoundToLineMiniLength = 0;
        double LineAndRoundLength =   (halfVerhicleStraightLineLength+Math.PI*borderRadious/2);
        double distance = 0;
        ArrayList<Point> point0005 = new ArrayList<>();
        ArrayList<Point> point0611 = new ArrayList<>();
        ArrayList<Point> point1217 = new ArrayList<>();
        ArrayList<Point> point1823 = new ArrayList<>();
        while (true) {
             distance = count*rectLength_24_60;
             if(distance>=borderRectLength/4){
                 break;
             }
             if(distance<halfVerhicleStraightLineLength){
                 point0005.add(new Point(width-borderStrokeLength_2,(float) (height/2+distance)));
                 linteToRoundTurning = false;
                 LineCount++;
             }else if(distance<(LineAndRoundLength)){
                 //拐弯第一个点
                 if(!linteToRoundTurning){
                     lastLineToRoundMiniLength = halfVerhicleStraightLineLength - rectLength_24_60*(count-1);
                     firstLineToRoundMiniLength = rectLength_24_60 - lastLineToRoundMiniLength;
                 }
                 Point point = new Point((float) ((width-borderStrokeLength_2-borderRadious)+borderRadious*Math.cos((firstLineToRoundMiniLength+lineToRoundTurningCount*rectLength_24_60)/borderRadious)),
                         (float) ((height-borderStrokeLength_2-borderRadious)+(borderRadious*Math.sin((firstLineToRoundMiniLength+lineToRoundTurningCount*rectLength_24_60)/borderRadious))));
                 point0005.add(point);
                 linteToRoundTurning = true;
                 lineToRoundTurningCount++;
             }else{
                 if(!roundToLineTurning){
                     lastRoundToLineMiniLength = LineAndRoundLength - rectLength_24_60*(count-1);
                     firstRoundToLineMiniLength =  rectLength_24_60 -lastRoundToLineMiniLength;
                 }
                 Point point = new Point((float) (width-borderStrokeLength_2-borderRadious-firstRoundToLineMiniLength-roundToLineTurningCount*rectLength_24_60),
                         height-borderStrokeLength_2);
                 point0005.add(point);
                 roundToLineTurning = true;
                 roundToLineTurningCount++;
             }
            count++;
         }
        for(int i=point0005.size()-1;i>=0;i--){
            Point point = point0005.get(i);
            point0611.add(new Point(-(point.x-borderRectWidth-2*borderStrokeLength_2),point.y));
        }


        for(int i=point0611.size()-1;i>=0;i--){
            Point point = point0611.get(i);
            point1217.add(new Point(point.x,-(point.y-borderRectHeight-2*borderStrokeLength_2)));
        }
        for(int i=point1217.size()-1;i>=0;i--){
            Point point = point1217.get(i);
            point1823.add(new Point(-(point.x-borderRectWidth-2*borderStrokeLength_2),point.y));
        }

        pointList.addAll(point0005);
        pointList.addAll(point0611);
        pointList.addAll(point1217);
        pointList.addAll(point1823);

    }

    public void draw(Canvas canvas){


        //PreDraw(canvas);
        drawBorder(canvas);
        //drawPoint(canvas);
        drawLine(canvas);
        //drawFinger(canvas);
    }

    private void PreDraw(Canvas canvas){
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);
        canvas.drawLine(borderStrokeLength_2,height/2,width-borderStrokeLength_2,height/2,paint);
        canvas.drawLine(width/2,borderStrokeLength_2,width/2,height-borderStrokeLength_2,paint);
    }

    private void drawBorder(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderStrokeLength);
        paint.setColor(borderColor);
        Path path = new Path();
        for(int i=0;i<pointList.size();i++){
            Point point = pointList.get(i);
            if(i==0){
                path.moveTo(point.x,point.y);
            }
            path.lineTo(point.x,point.y);
        }
        canvas.drawPath(path,paint);
    }

    private void drawPoint(Canvas canvas){
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        for(int i=0;i<pointList.size();i++){
            Point point = pointList.get(i);
            canvas.drawCircle(point.x, point.y, 1, paint);
        }
    }

    private void drawLine(Canvas canvas){
        Path path = new Path();
        paint.setStrokeWidth(10f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        int startPoint = getTimePoint(Calendar.getInstance());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,23);
        int endPoint = getTimePoint(calendar);
        if(endPoint<startPoint){
            endPoint +=pointList.size();
        }
        path.moveTo(pointList.get(startPoint).x,pointList.get(startPoint).y);
        //path.lineTo(pointList.get(endPoint%pointList.size()).x,pointList.get(endPoint%pointList.size()).y);
        for(int i=startPoint;i<endPoint;i++){
          //  path.lineTo(pointList.get(i%pointList.size()).x,pointList.get(i%pointList.size()).y);
        }
       // canvas.drawCircle(100,100,40,paint);
        canvas.drawPath(path,paint);
    }


    private void drawFinger(Canvas canvas){
        canvas.drawLine(width/2,height/2,width/2,0,paint);
    }

    private int getTimePoint(Calendar calendar){
        return calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE);
    }

}
