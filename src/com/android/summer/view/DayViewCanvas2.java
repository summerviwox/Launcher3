package com.android.summer.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;

import java.util.Calendar;

public class DayViewCanvas2 {

    Paint paint = new Paint();

    DayView dayView;

    //        0 右箭头 1 下箭头 2 左箭头 3 上箭头
//        数组内 按位置功能 时间运行方向、x、y、是否左右、是否上下、 右下1 左上-1
    int[][] points = new int[][]{
            {1,4,4,-1,0},{1,4,5,-1,0},{1,4,6,-1,0},{1,4,7,-1,0},//0,1,2,3
            {2,4,8,-1,-1},{2,3,8,0,-1},{2,2,8,0,-1},{2,1,8,0,-1},//4,5,6,7
            {3,0,8,1,-1},{3,0,7,1,0},{3,0,6,1,0},{3,0,5,1,0},{3,0,4,1,0},{3,0,3,1,0},{3,0,2,1,0},{3,0,1,1,0},//8,9,10,11,12,13,14,15
            {0,0,0,1,1}, {0,1,0,0,1},{0,2,0,0,1},{0,3,0,0,1},//16,17,18,19
            {1,4,0,-1,1},{1,4,1,-1,0},{1,4,2,-1,0},{1,4,3,-1,0},//20,21,22,23
    };

    float[][] dirct = new float[][]{{1,0},{0,1},{-1,0},{0,-1}};

    float[][] reallyPoints = new float[24][2];
    float timearroundwidthheight = 35;
    float pointwidthheight = timearroundwidthheight;//整点宽高
    float roundRectRadious = 80;//边角矩形半径
    float adjustRoundRectTimePoint = roundRectRadious*30/100;
    int timepointlinewidth = 10;
    int borderColor = Color.parseColor("#4a4a4a");
    int timeTextSize = SizeUtils.dp2px(11);
    float blockwidth,blockheight;
    public DayViewCanvas2(DayView dayView) {
        this.dayView = dayView;
        init();
    }

    void init(){
        paint.setAntiAlias(true);
        blockwidth = (dayView.getWidth()-timearroundwidthheight)/4;
        blockheight = (dayView.getHeight()-timearroundwidthheight)/8;
        for(int i=0;i<points.length;i++){
            reallyPoints[i] = new float[]{timearroundwidthheight/2+points[i][1]*blockwidth,timearroundwidthheight/2+points[i][2]*blockheight};
        }
        dayView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void draw(Canvas canvas){
        canvas.save();
        drawBorder2(canvas);
        drawTimeRect(canvas);
        drawTimeText(canvas);
        drawCurentTime(canvas);
        canvas.restore();

    }

    private void drawBorder2(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(timearroundwidthheight);
        paint.setColor(borderColor);
        float gap = timearroundwidthheight/2;
        canvas.drawRoundRect(gap,gap,dayView.getWidth()-gap,dayView.getHeight()-gap,roundRectRadious,roundRectRadious,paint);
    }
    private void drawTimePoint(Canvas canvas){
        canvas.save();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        for(int i=0;i<reallyPoints.length;i++){
            if(i==4||i==8||i==16||i==20){
                canvas.drawCircle((float) (reallyPoints[i][0]+points[i][3]*roundRectRadious*0.3),
                        (float) (reallyPoints[i][1]+points[i][4]*roundRectRadious*0.3),
                        timearroundwidthheight/2,
                        paint);
            }else{
                canvas.drawCircle(reallyPoints[i][0],reallyPoints[i][1],timearroundwidthheight/2,paint);
            }
        }
        canvas.restore();
    }

    private void drawTimeRect(Canvas canvas){
        canvas.save();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        float[] thispoint;
        for(int i=0;i<reallyPoints.length;i++){
            canvas.save();

            if(i==4||i==8||i==16||i==20){
                thispoint = new float[]{
                        (float) (reallyPoints[i][0]+points[i][3]*roundRectRadious*0.3),
                        (float) (reallyPoints[i][1]+points[i][4]*roundRectRadious*0.3)};
                canvas.rotate(-45,thispoint[0],thispoint[1]);
            }else{
                thispoint = reallyPoints[i];
            }


            if(points[i][0]%2==0){
                canvas.drawRect(
                        thispoint[0]-timearroundwidthheight/4,
                        thispoint[1]-timearroundwidthheight/2,
                        thispoint[0]+timearroundwidthheight/4,
                        thispoint[1]+timearroundwidthheight/2,
                        paint
                );
            }else{
                canvas.drawRect(
                        thispoint[0]-timearroundwidthheight/2,
                        thispoint[1]-timearroundwidthheight/4,
                        thispoint[0]+timearroundwidthheight/2,
                        thispoint[1]+timearroundwidthheight/4,
                        paint
                );
            }
            canvas.restore();
        }
        canvas.restore();
    }
    private void drawTimeText(Canvas canvas){
        canvas.save();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(timeTextSize);
        paint.setColor(Color.WHITE);
        int textshift = (int) ((paint.getFontMetrics().top+paint.getFontMetrics().bottom)/2);
        for(int i=0;i<reallyPoints.length;i++){
            if(i==4||i==8||i==16||i==20){
                canvas.drawText(i+"",
                        (float) (reallyPoints[i][0]+points[i][3]*roundRectRadious*0.3),
                        (float) (reallyPoints[i][1]+points[i][4]*roundRectRadious*0.3-textshift),
                        paint);
            }else{
                canvas.drawText(i+"",
                        (float) (reallyPoints[i][0]),
                        (float) (reallyPoints[i][1]-textshift),
                        paint);
            }
        }
        canvas.restore();
    }
    private void drawCurentTime(Canvas canvas){
       int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
       int minite = Calendar.getInstance().get(Calendar.MINUTE);

       paint.setStrokeWidth(timepointlinewidth/2);
        paint.setColor(Color.RED);

        if(hour==3||hour==4||hour==7||hour==8||hour==15||hour==16||hour==19||hour==20){
            float[] thisround;
            switch (hour){
                case 3:
                    thisround=new float[]{reallyPoints[4][0]-2*roundRectRadious,reallyPoints[4][1]-2*roundRectRadious,reallyPoints[4][0],reallyPoints[4][1],-1,-1,1,0};//小圆角矩形x,y,x偏移,y偏移,是否直线开始，顺时针顺序
                    break;
                case 4:
                    thisround=new float[]{reallyPoints[4][0]-2*roundRectRadious,reallyPoints[4][1]-2*roundRectRadious,reallyPoints[4][0],reallyPoints[4][1],-1,-1,-1,1};
                    break;
                case 7:
                    thisround=new float[]{reallyPoints[8][0],reallyPoints[8][1]-2*roundRectRadious,reallyPoints[8][0]+2*roundRectRadious,reallyPoints[8][1],1,-1,1,2};
                    break;
                case 8:
                    thisround=new float[]{reallyPoints[8][0],reallyPoints[8][1]-2*roundRectRadious,reallyPoints[8][0]+2*roundRectRadious,reallyPoints[8][1],1,-1,-1,3};
                    break;
                case 15:
                    thisround=new float[]{reallyPoints[16][0],reallyPoints[16][1],reallyPoints[16][0]+2*roundRectRadious,reallyPoints[16][1]+2*roundRectRadious,1,-1,1,4};
                    break;
                case 16:
                    thisround=new float[]{reallyPoints[16][0],reallyPoints[16][1],reallyPoints[16][0]+2*roundRectRadious,reallyPoints[16][1]+2*roundRectRadious,1,-1,-1,5};
                    break;
                case 19:
                    thisround=new float[]{reallyPoints[20][0]-2*roundRectRadious,reallyPoints[20][1],reallyPoints[20][0],reallyPoints[20][1]+2*roundRectRadious,-1,1,1,6};
                    break;
                case 20:
                    thisround=new float[]{reallyPoints[20][0]-2*roundRectRadious,reallyPoints[20][1],reallyPoints[20][0],reallyPoints[20][1]+2*roundRectRadious,-1,1,-1,7};
                    break;
                default:
                    thisround=new float[]{reallyPoints[4][0]-2*roundRectRadious,reallyPoints[4][1]-2*roundRectRadious,reallyPoints[4][0],reallyPoints[4][1],-1,-1,1,0};//小圆角矩形x,y,x偏移,y偏移,是否直线开始，顺时针顺序
                    break;
            }

            float[] thislinedirect = (float[]) dirct[points[hour][0]];
            float roundlength = (float) (Math.PI*roundRectRadious/4);//pi*r/4
            float thisblocklength = thislinedirect[0]==0?blockheight:blockwidth;
            float linelength = thisblocklength-roundRectRadious;
            float alllength = linelength + roundlength;
            float linepercent = linelength/alllength;//直线百分比
            float rountpercent = roundlength/alllength;
            float hourpercent = minite/60f;
            float startpercent = thisround[6]>0?linepercent:1-linepercent;//开始有可能是直线也有可能是弯线
            LogUtils.e(roundlength,linelength,alllength,linepercent,startpercent,hourpercent,thisround[6]);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
            //需要画2段
            if(hourpercent>startpercent){

                //先画直线
                if(thisround[6]>0){
                    canvas.drawLine(
                            reallyPoints[hour][0],
                            reallyPoints[hour][1],
                            reallyPoints[hour][0]+thislinedirect[0]*thisblocklength*linepercent,
                            reallyPoints[hour][1]+thislinedirect[1]*thisblocklength*linepercent,
                            paint
                    );
                    canvas.drawArc(thisround[0],
                            thisround[1],
                            thisround[2],
                            thisround[3],
                            thisround[7]*45,
                            (float) Math.toDegrees((hourpercent*alllength - linelength )/roundRectRadious),
                            false,
                            paint
                    );
                }else{
                    //先画弯线
                    canvas.drawArc(thisround[0],
                            thisround[1],
                            thisround[2],
                            thisround[3],
                            thisround[7]*45,
                            45,
                            false,
                            paint
                    );

                    float[] thisstartpoint = new float[]{reallyPoints[hour+1][0]-thislinedirect[0]*linelength,reallyPoints[hour+1][1]-thislinedirect[1]*linelength};
                    canvas.drawLine(
                            thisstartpoint[0],
                            thisstartpoint[1],
                            thisstartpoint[0]+thislinedirect[0]*(hourpercent*alllength-roundlength),
                            thisstartpoint[1]+thislinedirect[1]*(hourpercent*alllength-roundlength),
                            paint
                    );
                }
            }else{
                //先画直线
                if(thisround[6]>0){
                    canvas.drawLine(
                            reallyPoints[hour][0],
                            reallyPoints[hour][1],
                            reallyPoints[hour][0]+thislinedirect[0]*thisblocklength*hourpercent,
                            reallyPoints[hour][1]+thislinedirect[1]*thisblocklength*hourpercent,
                            paint
                    );
                }else{
                    canvas.drawArc(thisround[0],
                            thisround[1],
                            thisround[2],
                            thisround[3],
                            thisround[7]*45,
                            (float) Math.toDegrees((hourpercent*alllength  )/roundRectRadious),
                            false,
                            paint
                    );
                }
            }
        }else{
            canvas.drawLine(
                    reallyPoints[hour][0],
                    reallyPoints[hour][1],
                    reallyPoints[hour][0]+dirct[points[hour][0]][0]*blockwidth*minite/60,
                    reallyPoints[hour][1]+dirct[points[hour][0]][1]*blockwidth*minite/60,
                    paint
            );
        }
    }
}
