package com.android.summer.appwidget;

import java.io.Serializable;

public class Event implements Serializable {

    public int hours;

    public int minute;

    public int endHours;

    public int endMinute;

    public String text;

    public String color;

    public Event(int hours, int minute, int endHours, int endMinute, String text,String color) {
        this.hours = hours;
        this.minute = minute;
        this.endHours = endHours;
        this.endMinute = endMinute;
        this.text = text;
        this.color = color;
    }

    public boolean noEnd(){
        return (this.hours==this.endHours)&&(this.minute==this.endMinute);
    }

    public boolean overDayNight(){
        return dayOrNight(hours)==!dayOrNight(endHours);
    }

    public boolean dayOrNight(int hour){
        return (hour>=6&&hour<18);
    }
}
