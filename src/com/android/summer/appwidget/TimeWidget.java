package com.android.summer.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.launcher3.Launcher;
import com.android.launcher3.R;
import com.android.launcher3.settings.SettingsActivity;

//桌面时间小部件
public class TimeWidget extends AppWidgetProvider {

    public static final String myaction= "com.android.launcher3.TimeWidget";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.e("TimeWidget","onReceive"+intent.getAction()+""+intent.toString());
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//        RemoteViews rv=new RemoteViews(context.getPackageName(), R.layout.layout_time);
//        rv.setTextViewText(R.id.text,(i++)+"");
//        appWidgetManager.updateAppWidget(new ComponentName(context,TimeWidget.class),rv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e("TimeWidget","onUpdate");
        for(int i=0;i<appWidgetIds.length;i++){//appWidgetIds里面装的是拖拽出来的所有widget
            //不能直接设置监听，所以需要通过远程视图进行设置,appwidget_layout为显示在桌面的视图
            RemoteViews rv=new RemoteViews(context.getPackageName(), R.layout.layout_time);
            Intent intent = new Intent(context,SettingsActivity.class);
            PendingIntent pendingIntent=PendingIntent.getBroadcast(context, (int) (System.currentTimeMillis()/1000),intent, PendingIntent.FLAG_UPDATE_CURRENT);//转到MainActivity
            rv.setOnClickPendingIntent(R.id.root, pendingIntent);//为btn_widget设置单击事件为intent
            rv.setTextViewText(R.id.text,(System.currentTimeMillis())+"");
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);//更新数据到这个小部件
        }//为每个拖拽出来的小部件设置单击事件
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.e("TimeWidget","onEnabled");
    }
}
