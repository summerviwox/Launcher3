package com.android.summer.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.android.launcher3.CellLayout;
import com.android.launcher3.DeviceProfile;
import com.android.launcher3.Insettable;
import com.android.launcher3.Launcher;
import com.android.launcher3.graphics.RotationMode;
import com.android.launcher3.util.Thunk;
import com.android.summer.appwidget.Event;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
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
public class DayView extends View implements View.OnClickListener,Insettable {



    ArrayList<Event> events = new ArrayList<>();

    DayViewCanvas2 dayViewCanvas;

    public void registTimeTick(Context context){
        IntentFilter filter= new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        context.registerReceiver(receiver,filter);
    }

    public DayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        registTimeTick(context);
        setOnClickListener(this);
      //  setClickable(false);
        getDataFromServer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().unregisterReceiver(receiver);
    }

    void getDataFromServer(){
        invalidate();

     /*   events = new Gson().fromJson(getContext().getSharedPreferences("launcer3",Context.MODE_PRIVATE).getString("alarms","[]"),new TypeToken<List<Event>>(){}.getType());
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
        });*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(dayViewCanvas==null){
            dayViewCanvas = new DayViewCanvas2(DayView.this);
        }
        dayViewCanvas.draw(canvas);
    }

    @Override
    public void onClick(View v) {
        getDataFromServer();
    }

    @Override
    public void setInsets(Rect insets) {

    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                ToastUtils.showLong("123"+intent.getDataString());
                DayView.this.invalidate();
            }
        }
    };
}
