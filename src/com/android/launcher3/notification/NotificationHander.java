package com.android.launcher3.notification;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;

public class NotificationHander {

    public static void onNotificationPosted(Context context,final StatusBarNotification sbn) {
        if (sbn == null) {
            // There is a bug in platform where we can get a null notification; just ignore it.
            return;
        }
        if(sbn.getPackageName().equals("com.tencent.mm")){
            //Bundle[{android.title=呆逼, android.reduced.images=true, android.subText=null, android.showChronometer=false, android.text=一也, android.progress=0, android.progressMax=0, android.appInfo=ApplicationInfo{c29dc63 com.tencent.mm}, android.showWhen=true, android.largeIcon=Icon(typ=BITMAP size=110x110), android.infoText=null, android.progressIndeterminate=false, android.remoteInputHistory=null}]
            Bundle bundle = sbn.getNotification().extras;
            String title = bundle.getString(Notification.EXTRA_TITLE);
            String text = bundle.getString(Notification.EXTRA_TEXT);
            if(title.equals("呆逼")){
                Toast.makeText(context,text,Toast.LENGTH_LONG).show();
            }
        }
    }
}
