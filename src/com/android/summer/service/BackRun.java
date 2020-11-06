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
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.launcher3.R;
import com.android.summer.appwidget.TimeWidget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BackRun {

    Handler handler = new Handler();

    private static BackRun backRun;

    private Runnable runnable;

    private int i=0;

    int time = 60*1000;

    private BackRun(){

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
                    handler.postDelayed(runnable, 60-Calendar.getInstance().get(Calendar.SECOND));
                }
            } ;
        }
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 60-Calendar.getInstance().get(Calendar.SECOND));
    }

    private void run(Context context){
        //Toast.makeText(context,Calendar.getInstance().get(Calendar.HOUR)+":"+Calendar.getInstance().get(Calendar.MINUTE),Toast.LENGTH_LONG).show();
        int mins = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)<6?Calendar.getInstance().get(Calendar.HOUR_OF_DAY)*60+Calendar.getInstance().get(Calendar.MINUTE)+24*60:Calendar.getInstance().get(Calendar.HOUR_OF_DAY)*60+Calendar.getInstance().get(Calendar.MINUTE);
        int startmins = 6*60;//圆弧6点起点
        boolean dayOrNight = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)>=6&&Calendar.getInstance().get(Calendar.HOUR_OF_DAY)<18;
        //Toast.makeText(context,""+i,Toast.LENGTH_LONG).show();
        ComponentName componentName = new ComponentName(context,TimeWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews rv=new RemoteViews(context.getPackageName(), R.layout.layout_time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        rv.setTextViewText(R.id.text,(simpleDateFormat.format(new Date()))+"");
        int l = 1000;
        int linewidth =4;//一般线条宽度
        int padding = 0;
        int length = 50;//刻度长度
        int cx = l/2;//圆心x
        int cy = l/2;//圆心y
        int outradius = l/2;//大圆半径
        int distance = 20;//半径间隔
        int innerradius = l/2 - distance;//内圆半径
        int centerradius = innerradius - distance;//中心圆半径
        Bitmap bitmap =Bitmap.createBitmap(l+padding,l+padding,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setColor(Color.parseColor("#1E90FF"));
        paint.setStyle(Paint.Style.STROKE);
        rv.setImageViewBitmap(R.id.image,bitmap);
        int dayColor = Color.parseColor("#39B9E0");
        int nightColor = Color.parseColor("#F1B962");
        int dayOrNightColor = dayOrNight?dayColor:nightColor;
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
            canvas.drawArc(sw/2+distance,sw/2+distance,l-sw/2-distance,l-sw/2-distance,90,(mins-startmins)/2,false,paint);
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
        appWidgetManager.updateAppWidget(new ComponentName(context,TimeWidget.class),rv);

    }
}
