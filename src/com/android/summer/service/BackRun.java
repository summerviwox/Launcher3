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
                    handler.postDelayed(runnable, time);
                }
            } ;
        }
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, time);
    }

    private void run(Context context){
        //Toast.makeText(context,Calendar.getInstance().get(Calendar.HOUR)+":"+Calendar.getInstance().get(Calendar.MINUTE),Toast.LENGTH_LONG).show();
        int mins = Calendar.getInstance().get(Calendar.HOUR)*60+Calendar.getInstance().get(Calendar.MINUTE);

        //Toast.makeText(context,""+i,Toast.LENGTH_LONG).show();
        ComponentName componentName = new ComponentName(context,TimeWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews rv=new RemoteViews(context.getPackageName(), R.layout.layout_time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        rv.setTextViewText(R.id.text,(simpleDateFormat.format(new Date()))+"");
        int l = 1000;
        int length = 50;//刻度长度
        Bitmap bitmap =Bitmap.createBitmap(l,l,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(l/2,l/2, l/2,paint);
        rv.setImageViewBitmap(R.id.image,bitmap);
        canvas.drawCircle(l/2,l/2, 10,paint);

        paint.setTextSize(40);
        //表盘刻度
       for(int i=0;i<60;i++){
           canvas.save();
           canvas.rotate(i*(360/60),l/2,l/2);
           canvas.drawLine(l/2,0,l/2,(i%5==0)?length:length/2,paint);
           if(i%5==0){
               canvas.drawText(""+(i/5),l/2,length*2,paint);
           }
           canvas.restore();
       }


       //小时刻度
        canvas.save();
        canvas.rotate(mins/2,l/2,l/2);
        canvas.drawLine(l/2,l/2,l/2,l/4 ,paint);
        canvas.restore();

        //分钟刻度
        canvas.save();
        paint.setColor(Color.GREEN);
        canvas.rotate(mins*6,l/2,l/2);
        canvas.drawLine(l/2,l/2,l/2,0,paint);
        canvas.restore();
        appWidgetManager.updateAppWidget(new ComponentName(context,TimeWidget.class),rv);

    }
}
