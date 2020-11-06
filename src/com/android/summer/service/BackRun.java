package com.android.summer.service;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.telephony.CellInfoWcdma;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.android.launcher3.R;
import com.android.summer.appwidget.Event;
import com.android.summer.appwidget.TimeWidget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BackRun {

    Handler handler = new Handler();

    private static BackRun backRun;

    private static Runnable runnable;

    private int i=0;

    int time = 60*1000;

    ArrayList<Event> events = new ArrayList<>();

    private BackRun(){
        runnable= null;
        events.add(new Event(1,0,"睡觉"));
        events.add(new Event(8,30,"起床"));
        events.add(new Event(18,10,"下班"));
    }

    public static BackRun getBackRun(){
        if(backRun==null){
            backRun = new BackRun();
        }
        return backRun;
    }

    public void start(Context context){
        BackRun.this.run(context);
        if(runnable==null){
            runnable = new Runnable() {
                @Override
                public void run() {
                    BackRun.this.run(context);
                    handler.postDelayed(runnable, (60-Calendar.getInstance().get(Calendar.SECOND))*1000);
                }
            } ;
        }
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, (60-Calendar.getInstance().get(Calendar.SECOND))*1000);
    }

    private void run(Context context){
        Log.e("run","run");
        //Toast.makeText(context,Calendar.getInstance().get(Calendar.HOUR)+":"+Calendar.getInstance().get(Calendar.MINUTE),Toast.LENGTH_LONG).show();
        int mins = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)<6?Calendar.getInstance().get(Calendar.HOUR_OF_DAY)*60+Calendar.getInstance().get(Calendar.MINUTE)+12*60:Calendar.getInstance().get(Calendar.HOUR_OF_DAY)*60+Calendar.getInstance().get(Calendar.MINUTE);
        int startmins = 6*60;//圆弧6点起点
        boolean dayOrNight = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>=6&&Calendar.getInstance().get(Calendar.HOUR_OF_DAY)<18;
        //Toast.makeText(context,""+i,Toast.LENGTH_LONG).show();
        ComponentName componentName = new ComponentName(context,TimeWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews rv=new RemoteViews(context.getPackageName(), R.layout.layout_time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        rv.setTextViewText(R.id.text,(simpleDateFormat.format(new Date()))+"");
        int l = 1000;
        int width = (int) (1.4*l);
        int linewidth =4;//一般线条宽度
        int padding = 0;
        int length = 50;//刻度长度
        int cx = l/2;//圆心x
        int cy = l/2;//圆心y
        int outradius = l/2;//大圆半径
        int distance = 20;//圆弧宽度
        int innerradius = l/2 - distance;//内圆半径
        int centerradius = innerradius - distance;//中心圆半径
        Bitmap bitmap =Bitmap.createBitmap((int) (width+padding),l+padding,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setColor(Color.parseColor("#1E90FF"));
        paint.setStyle(Paint.Style.STROKE);
        rv.setImageViewBitmap(R.id.image,bitmap);
        int dayColor = Color.parseColor("#39B9E0");
        int nightColor = Color.parseColor("#F1B962");
        int dotColor =  Color.parseColor("#FF0000");
        int dayOrNightColor = dayOrNight?dayColor:nightColor;

       // canvas.drawColor(Color.parseColor("#FFB6C1"));

        canvas.save();
        canvas.translate((float) (width-l)/2,0);

        //世间进度
        int sw = distance;
        paint.setStrokeWidth(distance);
        if(dayOrNight){
            paint.setColor(dayColor);
            canvas.drawArc(sw/2,sw/2,l-sw/2,l-sw/2,90,(mins-startmins)/2,false,paint);
        }else{
            paint.setColor(dayColor);
            canvas.drawArc(sw/2,sw/2,l-sw/2,l-sw/2,0,360,false,paint);
            paint.setColor(nightColor);
            canvas.drawArc(sw/2+distance,sw/2+distance,l-sw/2-distance,l-sw/2-distance,90,(mins-startmins)/2+90,false,paint);
        }

        //外表盘圆框
        paint.setStrokeWidth(linewidth);
        paint.setColor(dayColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx,cy, outradius-linewidth/2,paint);

        //中表盘描边
        paint.setStrokeWidth(linewidth);
        paint.setColor(nightColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx,cy, innerradius- linewidth/2,paint);

        //中心圆圆框
        paint.setStrokeWidth(linewidth);
        paint.setColor(Color.parseColor("#8B4513"));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx,cy, centerradius,paint);

        paint.setColor(dayOrNightColor);
        paint.setStyle(Paint.Style.FILL);
        //中心点
        canvas.drawCircle(cx,cy, 10,paint);
        paint.setStyle(Paint.Style.STROKE);

        //盘刻度
        paint.setTextSize(40);
        paint.setColor(Color.parseColor("#FF0000"));
        for(int i=0;i<60;i++){
            canvas.save();
            canvas.rotate(i*(360/60),cx,cy);
            if((i%5==0)){
                paint.setStrokeWidth(linewidth*2);
                canvas.drawLine(l/2,distance*2,l/2,length/2+distance*2,paint);
            }else{
                paint.setStrokeWidth(linewidth);
                canvas.drawLine(l/2,distance*2,l/2,length/2+distance*2,paint);
            }
            canvas.restore();
        }


        //小时数字
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#FF0000"));
        paint.setTextAlign(Paint.Align.CENTER);
        for(int i=0;i<60;i++){
            canvas.save();
            canvas.rotate(i*(360/60),cx,cy);
            //canvas.drawLine(l/2,distance,l/2,(i%5==0)?length+distance:length/2+distance,paint);
            if(i%5==0){
                canvas.drawText(""+(i/5),l/2,length*2+distance,paint);
            }
            canvas.restore();
        }
        paint.setStyle(Paint.Style.STROKE);

        //小时刻度
        paint.setStrokeWidth(linewidth*4);
        paint.setColor(dayOrNightColor);
        canvas.save();
        canvas.rotate(mins/2,cx,cy);
        canvas.drawLine(cx,cy,l/2,l/4 ,paint);
        canvas.restore();

        //分钟刻度
        paint.setStrokeWidth(linewidth*2);
        canvas.save();
        paint.setColor(dayOrNightColor);
        canvas.rotate(mins*6,cx,cy);
        canvas.drawLine(cx,cy,l/2,dayOrNight?distance*2+linewidth*2:distance*2+linewidth*2,paint);
        canvas.restore();


        paint.setColor(dotColor);
        paint.setStyle(Paint.Style.FILL);
        //Log.e("getXY",l+":"+linewidth);
        paint.setTextSize(50);
        for(int i=0;i<events.size();i++){
            int[] xy = getXY(events.get(i),l/2,dayOrNight(events.get(i))?outradius-distance/2:innerradius-distance/2);
            canvas.drawCircle(xy[0],xy[1],linewidth*2,paint);
//            canvas.save();
//            canvas.rotate((getMins(events.get(i))-startmins)/2,l/2,l/2);
//            canvas.drawCircle(l/2,dayOrNight(events.get(i))?l-distance/2:l-distance*3/2,linewidth,paint);

            //canvas.drawText(events.get(i).text,l+50,(i+1)*100,paint);
           // canvas.drawLine(xy[0],xy[1],leftOrRight(events.get(i))?50:l+50,(i+1)*100,paint);
            //canvas.drawText(events.get(i).text,l/2,dayOrNight(events.get(i))?l:l-distance*2/2,paint);
//            canvas.restore();
        }
        canvas.restore();
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);
        for(int i=0;i<events.size();i++){
            int[] xy = getXY(events.get(i),l/2,dayOrNight(events.get(i))?outradius-distance/2:innerradius-distance/2);
            canvas.drawText(events.get(i).text,leftOrRight(events.get(i))?50: (float) ( (width-l)/2 + 100+l),xy[1],paint);
            canvas.drawLine((float) (xy[0]+ (width-l)/2),xy[1], leftOrRight(events.get(i))?50: (float) ( (width-l)/2 + 100+l),xy[1],paint);
        }


        appWidgetManager.updateAppWidget(new ComponentName(context,TimeWidget.class),rv);

    }


    public boolean dayOrNight(Event event){
        return (event.hours>=6&&event.hours<18);
    }

    public int[] getXY(Event event,int r,int radius){
            int degree = (event.hours<3?((event.hours+12)*60+event.minute-3*60)/2:(event.hours*60+event.minute-3*60)/2);
            int x = (int) (radius*(Math.cos(Math.toRadians(degree)))+r);
            int y = (int) (radius*(Math.sin(Math.toRadians(degree)))+r);
        //Log.e("getXY",degree+":"+x+":"+y);
            return new int[]{x,y};
    }

    public int getMins(Event event){
        return event.hours<6?event.hours*60+event.minute+12*60:event.hours*60+event.minute;
    }

    public boolean leftOrRight(Event event){
        if((event.hours>=0&&event.hours<6)||(event.hours>=12&&event.hours<18)){
            return false;
        }
        return true;
    }
}
