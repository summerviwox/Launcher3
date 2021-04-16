package com.android.summer.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.android.summer.appwidget.Event;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.function.Predicate;

public class DayViewCanvas5 {

    Paint paint = new Paint();
    DayView dayView;
    float width,height;
    ArrayList<Point> pointList;
    ArrayList<Point> innerPointList;
    float borderlength = 50;
    float planTextSize = 35;
    float planTextStrokWidth = 6;
    int hourTextColor = Color.parseColor("#000000");
    float hourTextSize = 25;
    int currentHourTextColor = Color.parseColor("#ff0000");
    float currentHourTextSize = 40;
    float hourTextStrokeWidth = 16;
    int borderColor = Color.parseColor("#bbbbbb");

    public DayViewCanvas5(DayView dayView) {
        this.dayView = dayView;
        init();
    }

    public void init(){
        paint.setAntiAlias(true);
        width = dayView.getWidth();
        height = dayView.getHeight();
        pointList = initPoint2(borderlength);
        innerPointList = initPoint2(borderlength*2);
    }

    private ArrayList<Point> initPoint(float borderStrokeLength){
        float borderStrokeLength_2;
        RectF borderRect;
        float borderRadious;
        int borderColor;
        borderRadious =100;
        float borderRectWidth;
        float borderRectHeight;
        float borderRectWidth_4;
        float borderRectHeight_8;
        double borderRectLength;
        double rectLength_24_60,rectLength_24_60_v,rectLength_24_60_h;
        double halfVerhicleStraightLineLength;
        double halfHorizonStraightLineLength;
        double roundRectAreaLength;

        borderStrokeLength_2 = borderStrokeLength/2;
        borderRect = new RectF(borderStrokeLength_2, borderStrokeLength_2,width- borderStrokeLength_2,height- borderStrokeLength_2);
        borderRectWidth = width- borderStrokeLength;
        borderRectWidth_4 = borderRectWidth/4;
        borderRectHeight = height - borderStrokeLength;
        borderRectHeight_8 = borderRectHeight/8;
        borderRectLength =   (2*(borderRectWidth+borderRectHeight)-8*borderRadious+2*Math.PI*borderRadious);
        halfVerhicleStraightLineLength = borderRectHeight/2-borderRadious;
        halfHorizonStraightLineLength = borderRectWidth/2-borderRadious;
        rectLength_24_60 = borderRectLength /(24*60);
        rectLength_24_60_v = (halfVerhicleStraightLineLength+Math.PI*borderRadious/4) /(4*60);
        rectLength_24_60_h =  (halfHorizonStraightLineLength+Math.PI*borderRadious/4) /(2*60);
        roundRectAreaLength = borderStrokeLength_2+borderRadious;
        borderColor = Color.parseColor("#cccccc");


        ArrayList<Point> pointList = new ArrayList<>();
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
        Point roundCenterPoint = new Point(width-borderStrokeLength_2-borderRadious,height-borderStrokeLength_2-borderRadious);
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
                Point point = new Point((float) (roundCenterPoint.x+borderRadious*Math.cos((firstLineToRoundMiniLength+lineToRoundTurningCount*rectLength_24_60)/borderRadious)),
                        (float) (roundCenterPoint.y+(borderRadious*Math.sin((firstLineToRoundMiniLength+lineToRoundTurningCount*rectLength_24_60)/borderRadious))));
                point0005.add(point);
                linteToRoundTurning = true;
                lineToRoundTurningCount++;
            }else{
                if(!roundToLineTurning){
                    lastRoundToLineMiniLength = LineAndRoundLength - rectLength_24_60*(count-1);
                    firstRoundToLineMiniLength =  rectLength_24_60 -lastRoundToLineMiniLength;
                }
                Point point = new Point((float) (roundCenterPoint.x-firstRoundToLineMiniLength-roundToLineTurningCount*rectLength_24_60),
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
        return pointList;
    }

    private ArrayList<Point> initPoint2(float borderStrokeLength){
        float borderStrokeLength_2;
        RectF borderRect;
        float borderRadious;
        int borderColor;
        borderRadious =80;
        float borderRectWidth;
        float borderRectHeight;
        float borderRectWidth_4;
        float borderRectHeight_8;
        double borderRectLength;
        double rectLength_24_60,rectLength_24_60_v,rectLength_24_60_h;
        double halfVerhicleStraightLineLength;
        double halfHorizonStraightLineLength;
        double halfVerhicleLineAndRoundLength;
        double halfHorizonRoundAndLineLength;
        double roundRectAreaLength;

        borderStrokeLength_2 = borderStrokeLength/2;
        borderRect = new RectF(borderStrokeLength_2, borderStrokeLength_2,width- borderStrokeLength_2,height- borderStrokeLength_2);
        borderRectWidth = width- borderStrokeLength;
        borderRectWidth_4 = borderRectWidth/4;
        borderRectHeight = height - borderStrokeLength;
        borderRectHeight_8 = borderRectHeight/8;
        borderRectLength =   (2*(borderRectWidth+borderRectHeight)-8*borderRadious+2*Math.PI*borderRadious);
        halfVerhicleStraightLineLength = borderRectHeight/2-borderRadious;
        halfHorizonStraightLineLength = borderRectWidth/2-borderRadious;
        rectLength_24_60 = borderRectLength /(24*60);
        halfVerhicleLineAndRoundLength = halfVerhicleStraightLineLength+Math.PI*borderRadious/4;
        halfHorizonRoundAndLineLength = halfHorizonStraightLineLength+Math.PI*borderRadious/4;
        rectLength_24_60_v = halfVerhicleLineAndRoundLength /(4*60);
        rectLength_24_60_h = halfHorizonRoundAndLineLength/(2*60);
        roundRectAreaLength = borderStrokeLength_2+borderRadious;
        borderColor = Color.parseColor("#cccccc");


        ArrayList<Point> pointList = new ArrayList<>();
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
        Point roundCenterPoint = new Point(width-borderStrokeLength_2-borderRadious,height-borderStrokeLength_2-borderRadious);
        double piR4 = Math.PI*borderRadious/4;
        ArrayList<Point> point0005 = new ArrayList<>();
        ArrayList<Point> point0611 = new ArrayList<>();
        ArrayList<Point> point1217 = new ArrayList<>();
        ArrayList<Point> point1823 = new ArrayList<>();
        while (true) {
            if(count>=6*60){
                break;
            }
            if(count<60*4){
                rectLength_24_60 = rectLength_24_60_v;
                distance=count*rectLength_24_60;
                if(distance<halfVerhicleStraightLineLength){
                    point0005.add(new Point(width-borderStrokeLength_2,(float) (height/2+distance)));
                    linteToRoundTurning = false;
                    LineCount++;
                }else{
                    //拐弯第一个点
                    if(!linteToRoundTurning){
                        lastLineToRoundMiniLength = halfVerhicleStraightLineLength - rectLength_24_60*(count-1);
                        firstLineToRoundMiniLength = rectLength_24_60 - lastLineToRoundMiniLength;
                    }
                    Point point = new Point((float) (roundCenterPoint.x+borderRadious*Math.cos((firstLineToRoundMiniLength+lineToRoundTurningCount*rectLength_24_60)/borderRadious)),
                            (float) (roundCenterPoint.y+(borderRadious*Math.sin((firstLineToRoundMiniLength+lineToRoundTurningCount*rectLength_24_60)/borderRadious))));
                    point0005.add(point);
                    linteToRoundTurning = true;
                    lineToRoundTurningCount++;
                }
            }else{
                rectLength_24_60 = rectLength_24_60_h;
                int horizonCount = count - 60*4;
                //distance=halfVerhicleLineAndRoundLength + horizonCount*rectLength_24_60;
                if(horizonCount*rectLength_24_60<piR4){
                    Point point = new Point((float) (roundCenterPoint.x+borderRadious*Math.cos((horizonCount*rectLength_24_60+piR4)/borderRadious)),
                            (float) (roundCenterPoint.y+(borderRadious*Math.sin((horizonCount*rectLength_24_60+piR4)/borderRadious))));
                    point0005.add(point);
                    roundToLineTurning = false;
                }else{
                    if(!roundToLineTurning){
                        lastRoundToLineMiniLength = piR4- (horizonCount-1)*rectLength_24_60;
                        firstRoundToLineMiniLength =  rectLength_24_60 -lastRoundToLineMiniLength;
                    }
                    Point point = new Point((float) (roundCenterPoint.x-firstRoundToLineMiniLength-roundToLineTurningCount*rectLength_24_60),
                            height-borderStrokeLength_2);
                    point0005.add(point);
                    roundToLineTurning = true;
                    roundToLineTurningCount++;
                }
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

        ArrayList<Point> points = new ArrayList<>();
        for(int i=18*60;i<24*60;i++){
            points.add(pointList.get(i));
        }
        pointList.removeAll(points);
        pointList.addAll(0,points);
        return pointList;
    }

    public void draw(Canvas canvas){
        drawBorder(canvas);
        drawMinutePoint(canvas);
        drawCurrentMinute(canvas);
        drawPlan(canvas);
        drawCurrentHour(canvas);
        drawHourPoint(canvas);
        drawHourText(canvas);

//        for(int i=0;i<100;i++){
//            canvas.drawCircle(pointList.get(i).x,pointList.get(i).y,10,paint);
//        }
      //  paint.setStrokeWidth(1);
     //   canvas.drawLine(0,height/2,width,height/2,paint);
      //  canvas.drawLine(40,0,40,height,paint);
    }

    private void drawBorder(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderlength);
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

    private void drawHourPoint(Canvas canvas){
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        for(int i=0;i<pointList.size();i++){
            if(i%60==0){
                if((i/60)%2==1){
                    Point point = pointList.get(i);
                    canvas.drawCircle(point.x, point.y, i%120==0?10:5, paint);
                }
            }
        }
    }

    private void drawMinutePoint(Canvas canvas){
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(2);
        float shift = (paint.getFontMetrics().top+paint.getFontMetrics().bottom)/2;
        for(int i=0;i<pointList.size();i++){
            if(i%24==0){
                Point point = pointList.get(i);

                if(i%120==0){
//                    paint.setStyle(Paint.Style.FILL);
//                    paint.setColor(Color.BLACK);
//                    canvas.drawCircle(point.x, point.y,10, paint);
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.WHITE);
                    canvas.drawCircle(point.x, point.y,8, paint);
                   // canvas.drawText((i/120)+"",point.x,point.y-shift,paint);
                }else{
                  //  canvas.drawCircle(point.x, point.y,5, paint);
                }
            }
        }
    }


    private void drawHourText(Canvas canvas){
        paint.setStrokeWidth(hourTextStrokeWidth);
        paint.setFakeBoldText(true);
        paint.setColor(hourTextColor);
        paint.setTextSize(hourTextSize);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        float shift = (paint.getFontMetrics().top+paint.getFontMetrics().bottom)/2;
        for(int i=0;i<pointList.size();i++){
            if(i%120==0){
//                canvas.drawText(getTimeString(i/60)+"-"+getTimeString(i/24),pointList.get(i).x,pointList.get(i).y-shift,paint);
                paint.setColor(hourTextColor);
                paint.setTextSize(hourTextSize);
                if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)==(i/60)){
                    paint.setColor(currentHourTextColor);
                    paint.setTextSize(currentHourTextSize);
                }
                canvas.drawText(getTimeString(i/60),pointList.get(i).x,pointList.get(i).y-shift,paint);
            }
        }
    }


    private void drawCurrentMinute(Canvas canvas){
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        Calendar calendar = Calendar.getInstance();
        int count = ((calendar.get(Calendar.MINUTE))*24);
        if(count<0){
            count += innerPointList.size();
        }
        Point minutePoint = innerPointList.get(count);
        paint.setColor(Color.WHITE);
        canvas.drawLine(width/2,height/2,minutePoint.x,minutePoint.y,paint);
        //canvas.drawCircle(minutePoint.x,minutePoint.y,10,paint);
        float shift = (paint.getFontMetrics().top+paint.getFontMetrics().bottom)/2;
        canvas.drawText(getTimeString(calendar.get(Calendar.MINUTE)),minutePoint.x,minutePoint.y-shift,paint);

    }


    private void drawCurrentHour(Canvas canvas){
        paint.setColor(Color.RED);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        int startIndex = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)*60;
        int endIndex = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)*60+Calendar.getInstance().get(Calendar.MINUTE);
        Point hourStartPoint = pointList.get(startIndex);
        Point hourEndPoint = pointList.get(endIndex);
        Path path = new Path();
        path.moveTo(hourStartPoint.x,hourStartPoint.y);
        for(int i=startIndex;i<endIndex;i++){
            Point point = pointList.get(i%pointList.size());
            path.lineTo(point.x,point.y);
        }
        canvas.drawPath(path,paint);

        canvas.drawLine(width/2,height/2,hourEndPoint.x,hourEndPoint.y,paint);
        //canvas.drawCircle(hourEndPoint.x,hourEndPoint.y,borderlength*80/100,paint);
        if(startIndex%120!=0){
            paint.setStrokeWidth(hourTextStrokeWidth);
            paint.setFakeBoldText(true);
            paint.setColor(currentHourTextColor);
            paint.setTextSize(currentHourTextSize);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.CENTER);
            float shift = (paint.getFontMetrics().top+paint.getFontMetrics().bottom)/2;
            canvas.drawText(getTimeString(startIndex/60),hourStartPoint.x,hourStartPoint.y-shift,paint);
        }
    }

    private void drawPlan(Canvas canvas){
        paint.setColor(Color.RED);
        paint.setStrokeWidth(planTextStrokWidth);
        paint.setTextSize(planTextSize);

        for(int i=0;i<dayView.getEvents().size();i++){
            paint.setStyle(Paint.Style.STROKE);
            Event event  = dayView.getEvents().get(i);
            Path path = new Path();
            int startIndex =  event.hours*60+event.minute;
            int endIndex = event.endHours*60+event.endMinute;
            path.moveTo(pointList.get(startIndex).x,pointList.get(startIndex).y);
            if(endIndex<startIndex){
                endIndex+=pointList.size();
            }
            for(int j=startIndex;j<endIndex;j++){
                path.lineTo(pointList.get(j%pointList.size()).x,pointList.get(j%pointList.size()).y);
            }
            paint.setColor(Color.parseColor(event.color));
            canvas.drawPath(path,paint);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawTextOnPath(event.text,path,0,-(paint.getFontMetrics().bottom+paint.getFontMetrics().top)/2,paint);
        }
    }

    private int getTimePoint(Calendar calendar){
        return calendar.get(Calendar.HOUR_OF_DAY)*60+calendar.get(Calendar.MINUTE);
    }

    private String getTimeString(int hour){
        if(hour<10){
            return "0"+hour;
        }
        return hour+"";
    }

}
