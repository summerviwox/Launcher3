package com.android.summer.appwidget;

import android.app.backup.BackupAgent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.summer.service.BackRun;

public class EventReceive extends BroadcastReceiver {


    long time = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        //避免反复点击
        if(System.currentTimeMillis()-time<10*1000){
            return;
        }
        BackRun.getBackRun().refresh(context);
    }
}
