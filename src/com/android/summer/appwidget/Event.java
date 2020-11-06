package com.android.summer.appwidget;

import java.io.Serializable;

public class Event implements Serializable {

    public int hours;

    public int minute;

    public String text;

    public Event(int hours, int minute, String text) {
        this.hours = hours;
        this.minute = minute;
        this.text = text;
    }
}
