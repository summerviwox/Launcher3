package com.android.summer.view;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.android.launcher3.R;
import com.android.launcher3.logging.LoggerUtils;
import com.android.summer.appwidget.Event;
import com.android.summer.appwidget.TimeWidget;
import com.android.summer.service.BackRun;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.summer.logic.Alarm;
import com.summer.logic.BaseRes;
import com.summer.logic.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*一个自定义的view 用于覆盖在主屏幕底部画一些东西*/
public class OriDayView extends View implements View.OnClickListener {

    Paint paint = new Paint();

    ArrayList<Event> events = new ArrayList<>();

    String defaultColor = "#FFFF00";

    public OriDayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        setOnClickListener(this);
    }

    void getDataFromServer(){
        events = new Gson().fromJson(getContext().getSharedPreferences("launcer3",Context.MODE_PRIVATE).getString("alarms","[]"),new TypeToken<List<Event>>(){}.getType());
        Log.e("111",getContext().getSharedPreferences("launcer3",Context.MODE_PRIVATE).getString("alarms","[]"));
        HttpUtil.getInstance().selectAll("3f68f2ec608ba5068816dce9ae6cb4a6").enqueue(new Callback<BaseRes<List<Alarm>>>() {

            @Override
            public void onResponse(Call<BaseRes<List<Alarm>>> call, Response<BaseRes<List<Alarm>>> response) {
                Log.e("onResponse",new Gson().toJson(response.body()));
                if(response.isSuccessful()||response.body()!=null){
                    List<Alarm> alarms = response.body().getData();
                    events.clear();
                    for(int i=0;i<alarms.size();i++){
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date(alarms.get(i).getStarttime()));
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTime(new Date(alarms.get(i).getEndtime()));
                        events.add(new Event(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),calendar2.get(Calendar.HOUR_OF_DAY),calendar2.get(Calendar.MINUTE),alarms.get(i).getText(),alarms.get(i).getColor()));
                    }
                    getContext().getSharedPreferences("launcer3",Context.MODE_PRIVATE).edit().putString("alarms",new Gson().toJson(events)).commit();
                    invalidate();
                }
            }

            @Override
            public void onFailure(Call<BaseRes<List<Alarm>>> call, Throwable t) {
                Log.e("onFailure",t.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        run(canvas);
    }

    @Override
    public void onClick(View v) {
        getDataFromServer();
    }


    private void run(Canvas canvas){
        Log.e("run1","run1");
        //Toast.makeText(context,Calendar.getInstance().get(Calendar.HOUR)+":"+Calendar.getInstance().get(Calendar.MINUTE),Toast.LENGTH_LONG).show();
        int mins = getMins(Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE));
        int startmins =dayOrNight(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))? 6*60:18*60;//圆弧6点起点
        int arucstartmins =3*60;//圆弧3点起点
        //Toast.makeText(context,""+i,Toast.LENGTH_LONG).show();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        int l = getWidth();
        int width = (int) (1.0*l);
        int linewidth =4;//一般线条宽度
        int padding = 30;
        int length = 50;//刻度长度
        int cx = l/2;//圆心x
        int cy = l/2;//圆心y
        int outradius = l/2;//大圆半径
        int distance = 80;//圆弧宽度
        int midradius = l/2 - distance;//内圆半径
        int innerradius = midradius - distance;//中心圆半径
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setColor(Color.parseColor("#1E90FF"));
        paint.setStyle(Paint.Style.STROKE);
        int dayColor = Color.parseColor("#39B9E0");
        int nightColor = Color.parseColor("#F1B962");
        int dayBgColor = Color.parseColor("#ffffff");
        int nightBgColor = Color.parseColor("#666666");
        int dotColor =  Color.parseColor("#FF0000");
        int dayOrNightColor = dayOrNight(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))?dayColor:nightColor;

        // canvas.drawColor(Color.parseColor("#FFB6C1"));

        canvas.save();//------------------------------------
        canvas.translate((float) (width-l)/2,(getHeight()-getWidth())/2);
        paint.setTextSize(40);
        //时间进度
        int sw = distance;
        paint.setStrokeWidth(distance);
//        if(dayOrNight(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))){
//            paint.setColor(dayColor);
//            canvas.drawArc(sw/2,sw/2,l-sw/2,l-sw/2,90,timeToDegree(mins-startmins),false,paint);
//        }else{
//            paint.setColor(dayColor);
//            canvas.drawArc(sw/2,sw/2,l-sw/2,l-sw/2,0,360,false,paint);
//            paint.setColor(nightColor);
//            canvas.drawArc(sw/2+distance,sw/2+distance,l-sw/2-distance,l-sw/2-distance,90,timeToDegree(mins-startmins),false,paint);
//        }
        //白天背景
        paint.setColor(dayBgColor);
        canvas.drawCircle(cx,cy,outradius-distance/2,paint);

        //夜晚背景
        paint.setColor(nightBgColor);
        canvas.drawCircle(cx,cy,midradius-distance/2,paint);

        //中间填充
        int headBitmapGap = innerradius-10*linewidth;
        // canvas.drawBitmap(headBitmp,null,new Rect(cx-headBitmapGap,cy-headBitmapGap,cx+headBitmapGap,cy+headBitmapGap),paint);

        //外表盘圆框
        paint.setStrokeWidth(linewidth*2);
        paint.setColor(dayColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx,cy, outradius-linewidth/2,paint);

        //中表盘描边
        paint.setStrokeWidth(linewidth*2);
        paint.setColor(nightColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx,cy, midradius- linewidth/2,paint);

        //中心圆圆框
        paint.setStrokeWidth(linewidth*2);
        paint.setColor(Color.parseColor("#8B4513"));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx,cy, innerradius,paint);

        paint.setColor(dayOrNightColor);
        paint.setStyle(Paint.Style.FILL);
        //中心点
        canvas.drawCircle(cx,cy, 10,paint);
        paint.setStyle(Paint.Style.STROKE);

        //盘刻度
        paint.setColor(Color.parseColor("#FF0000"));
        for(int i=0;i<60;i++){
            canvas.save();
            canvas.rotate(i*(360/60),cx,cy);
            if((i%5==0)){
                paint.setStrokeWidth(linewidth*2);
                canvas.drawLine(l/2,distance*2,l/2,length/2+distance*2,paint);
            }else{
//                paint.setStrokeWidth(linewidth);
//                canvas.drawLine(l/2,distance*2,l/2,length/2+distance*2,paint);
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
                canvas.drawText(""+(i/5),cx,length+distance*2,paint);
            }
            canvas.restore();
        }
        paint.setStyle(Paint.Style.STROKE);

        //小时刻度
        canvas.save();
        paint.setStrokeWidth(linewidth*4);
        paint.setColor(dayOrNightColor);
        canvas.rotate(mins/2,cx,cy);
        //canvas.drawLine(cx,cy,l/2,l/4 ,paint);
        //paint.setStrokeWidth(linewidth);
        canvas.drawLine(cx,cy,l/2,dayOrNight(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))?distance*2+linewidth*2:distance*2+linewidth*2,paint);
        canvas.restore();



        //HH:mm 中间显示的时间
//        paint.setColor(Color.parseColor("#FF0000"));
//        paint.setStyle(Paint.Style.FILL);
//        paint.setStrokeWidth(linewidth/2);
//        paint.setTextSize(40);
//        canvas.drawText(new SimpleDateFormat("HH:mm").format(new Date()),cx,cy,paint);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setTextSize(40);


        //分钟刻度
//        canvas.save();
//        paint.setStrokeWidth(linewidth*2);
//        paint.setColor(dayOrNightColor);
//        canvas.rotate(mins*6,cx,cy);
//        canvas.drawLine(cx,cy,l/2,dayOrNight(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))?distance*2+linewidth*2:distance*2+linewidth*2,paint);
//        canvas.restore();




        //画时间区域圆弧
        canvas.save();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(distance-linewidth*2);
        sw = sw+linewidth;
        for(int i=0;i<events.size();i++){

            if(!events.get(i).noEnd()){
                paint.setColor(Color.parseColor(TextUtils.isEmpty(events.get(i).color)?defaultColor:events.get(i).color));
                if(dayOrNight(events.get(i).hours)){
                    if(events.get(i).overDayNight()){
                        canvas.drawArc(sw/2,sw/2,l-sw/2,l-sw/2,
                                timeToDegree(getRealMins(events.get(i).hours,events.get(i).minute)-arucstartmins),
                                timeToDegree(getRealMins(18,0)-getRealMins(events.get(i).hours,events.get(i).minute)),
                                false,paint);

                        canvas.drawLine(cx-linewidth,l-distance/2+linewidth,cx-linewidth,l-3*distance/2-linewidth,paint);

                        canvas.drawArc(sw/2+distance,sw/2+distance,l-sw/2-distance,l-sw/2-distance,
                                timeToDegree(getRealMins(18,0)-arucstartmins),
                                timeToDegree(getRealMins(events.get(i).endHours,events.get(i).endMinute)-getRealMins(18,0)),
                                false,paint);
                    }else{
                        canvas.drawArc(sw/2,sw/2,l-sw/2,l-sw/2,
                                timeToDegree(getRealMins(events.get(i).hours,events.get(i).minute)-arucstartmins),
                                timeToDegree(getRealMins(events.get(i).endHours,events.get(i).endMinute)-getRealMins(events.get(i).hours,events.get(i).minute)),
                                false,paint);
                    }
                }else{
                    if(events.get(i).overDayNight()){
                        canvas.drawArc(sw/2+distance,sw/2+distance,l-sw/2-distance,l-sw/2-distance,
                                timeToDegree(getRealMins(events.get(i).hours,events.get(i).minute)-arucstartmins),
                                timeToDegree(getRealMins(6,0)-getRealMins(events.get(i).hours,events.get(i).minute)),
                                false,paint);
                        canvas.drawLine(cx+linewidth,l-distance/2+linewidth,cx+linewidth,l-3*distance/2-linewidth,paint);
                        canvas.drawArc(sw/2,sw/2,l-sw/2,l-sw/2,
                                timeToDegree(getRealMins(6,0)-arucstartmins),
                                timeToDegree(getRealMins(events.get(i).endHours,events.get(i).endMinute)-getRealMins(6,0)),
                                false,paint);
                    }else{
                        canvas.drawArc(sw/2+distance,sw/2+distance,l-sw/2-distance,l-sw/2-distance,
                                timeToDegree(getRealMins(events.get(i).hours,events.get(i).minute)-arucstartmins),
                                timeToDegree(getRealMins(events.get(i).endHours,events.get(i).endMinute)-getRealMins(events.get(i).hours,events.get(i).minute)),
                                false,paint);
                    }
                }
            }
        }
        canvas.restore();

        //画点
        canvas.save();
        paint.setColor(dotColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(50);
        for(int i=0;i<events.size();i++){
            int[] xy = getXY(events.get(i),l/2,dayOrNight(events.get(i).hours)?outradius-distance/2:midradius-distance/2);
            canvas.drawCircle(xy[0],xy[1],linewidth*2,paint);
        }
        canvas.restore();


        //当前时间
        canvas.save();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(distance);
        if(dayOrNight(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))){
            canvas.drawArc(distance/2,distance/2,l-distance/2,l-distance/2,
                    timeToDegree(getMins(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)-3,Calendar.getInstance().get(Calendar.MINUTE))),
                    timeToDegree(2),false,paint);
        }else{
            canvas.drawArc(distance*3/2,distance*3/2,l-distance*3/2,l-distance*3/2,
                    timeToDegree(getMins(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)-3,Calendar.getInstance().get(Calendar.MINUTE))),
                    timeToDegree(2),false,paint);
        }
        canvas.restore();



        canvas.restore();//------------------------------------

        //
        canvas.save();
        paint.setStyle(Paint.Style.FILL);
        canvas.translate(0,padding);
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);
        for(int i=0;i<events.size();i++){
            paint.setColor(Color.parseColor(TextUtils.isEmpty(events.get(i).color)?defaultColor:events.get(i).color));
            int[] xy = getXY(events.get(i),l/2,dayOrNight(events.get(i).hours)?outradius-distance/2:midradius-distance/2);
            canvas.drawText(events.get(i).text,leftOrRight(events.get(i))?50: (float) ( (width-l)/2 + 100+l),xy[1],paint);
            canvas.drawLine((float) (xy[0]+ (width-l)/2),xy[1], leftOrRight(events.get(i))?50: (float) ( (width-l)/2 + 100+l),xy[1],paint);
        }
        canvas.restore();

    }

    public boolean dayOrNight(int hour){
        return (hour>=6&&hour<18);
    }

    public int[] getXY(Event event,int r,int radius){
        int degree = (event.hours<3?((event.hours+12)*60+event.minute-3*60)/2:(event.hours*60+event.minute-3*60)/2);
        int x = (int) (radius*(Math.cos(Math.toRadians(degree)))+r);
        int y = (int) (radius*(Math.sin(Math.toRadians(degree)))+r);
        //Log.e("getXY",degree+":"+x+":"+y);
        return new int[]{x,y};
    }

    public int getMins(int hours,int minute){
        return  hours<6?hours*60+minute+24*60:hours*60+minute;
    }

    public int getRealMins(int hours,int minute){
        return hours*60+minute;
    }

    public int timeToDegree(int minutes){
        return minutes/2;
    }

    public int minToDegree(int minute){
        return minute*6;
    }

    public boolean leftOrRight(Event event){
        if((event.hours>=0&&event.hours<6)||(event.hours>=12&&event.hours<18)){
            return false;
        }
        return true;
    }
}
