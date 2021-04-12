package com.android.summer;

import android.os.Handler;

public class HandleUtil {

    private static Handler handler = new Handler();

    public static void recycle(int time,HandBack handBack){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handBack.handle();
                handler.postDelayed(this,time);
            }
        };
        handler.postDelayed(runnable,time);
    }

    public static interface HandBack{
        public void handle();
    }

}
