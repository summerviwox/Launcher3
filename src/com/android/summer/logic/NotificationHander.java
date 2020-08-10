package com.android.summer.logic;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import com.android.launcher3.logging.LoggerUtils;
import com.summer.logic.HttpUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationHander {


    public static final String SHUT_DOWN_WINDOWS = "关闭电脑";

    public static void onNotificationPosted(Context context,final StatusBarNotification sbn) {
        if (sbn == null) {
            // There is a bug in platform where we can get a null notification; just ignore it.
            return;
        }
        Bundle bundle = sbn.getNotification().extras;
        switch (sbn.getPackageName()){
            case "com.android.deskclock":
                //闹铃服务 添加特定闹铃召唤相关逻辑
                switch (bundle.getString(Notification.EXTRA_TEXT)){
                    case SHUT_DOWN_WINDOWS:
                        Toast.makeText(context,"即将关闭电脑",Toast.LENGTH_LONG).show();
                        HttpUtil.getInstance().shutDownWindows().enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {

                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });
                        break;
                }
                break;
            case "com.tencent.mm":
                //微信呆逼信息
                //Bundle[{android.title=呆逼, android.reduced.images=true, android.subText=null, android.showChronometer=false, android.text=一也, android.progress=0, android.progressMax=0, android.appInfo=ApplicationInfo{c29dc63 com.tencent.mm}, android.showWhen=true, android.largeIcon=Icon(typ=BITMAP size=110x110), android.infoText=null, android.progressIndeterminate=false, android.remoteInputHistory=null}]
                String title = bundle.getString(Notification.EXTRA_TITLE);
                String text = bundle.getString(Notification.EXTRA_TEXT);
                if(title.equals("呆逼")){
                    Toast.makeText(context,text,Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
