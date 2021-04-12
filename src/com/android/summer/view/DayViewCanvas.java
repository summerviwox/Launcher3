package com.android.summer.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;

public class DayViewCanvas {

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

    int[][] reallyPoints = new int[24][2];
    int[][] adjustPoints = new int[24][2];
    int timearroundwidthheight = 35;
    int pointwidthheight = timearroundwidthheight;//整点宽高
    int roundRectRadious = 80;//边角矩形半径
    int adjustRoundRectTimePoint = roundRectRadious*30/100;
    int timepointlinewidth = 10;
    int borderColor = Color.parseColor("#4a4a4a");
    int timeTextSize = SizeUtils.dp2px(11);

    public DayViewCanvas(DayView dayView) {
        this.dayView = dayView;
        init();
    }

    void init(){
        paint.setAntiAlias(true);
        int blockwidth = dayView.getWidth()/4;
        int blockheight = dayView.getHeight()/8;
        for(int i=0;i<points.length;i++){
            reallyPoints[i] = new int[]{points[i][1]*blockwidth,points[i][2]*blockheight};
        }
        for(int i=0;i<points.length;i++){
            adjustPoints[i] = new int[]{reallyPoints[i][0]+points[i][3]*timearroundwidthheight/2,reallyPoints[i][1]+points[i][4]*timearroundwidthheight/2};
        }
        dayView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void draw(Canvas canvas){
        canvas.save();
        drawBorder2(canvas);
//        drawBorder(canvas);
//        draw4_8_16_20(canvas);
          drawTimePoint(canvas);
//        drawTimeLine(canvas);
//        drawTimeText(canvas);
        canvas.restore();

    }

    private void drawBorder2(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(timearroundwidthheight);
        paint.setColor(borderColor);
        int gap = timearroundwidthheight/2;
        canvas.drawRoundRect(gap,gap,dayView.getWidth()-gap,dayView.getHeight()-gap,roundRectRadious,roundRectRadious,paint);
    }

    private void drawBorder(Canvas canvas){
        canvas.save();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(timearroundwidthheight);
        paint.setColor(borderColor);
        for(int i=0;i<points.length;i++){
            if(i==3||i==4||i==7||i==8||i==15||i==16||i==19||i==20){
                continue;
            }
            canvas.drawLine(adjustPoints[i][0],adjustPoints[i][1],adjustPoints[(i+1)%points.length][0],adjustPoints[(i+1)%points.length][1],paint);
        }
        canvas.restore();

    }

    private void drawTimePoint(Canvas canvas){
        canvas.save();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
       for(int i=0;i<adjustPoints.length;i++){
           if(i==4||i==8||i==16||i==20){
               canvas.drawCircle(adjustPoints[i][0]+adjustRoundRectTimePoint*points[i][3],adjustPoints[i][1]+adjustRoundRectTimePoint*points[i][4],timearroundwidthheight/2,paint);
           }else{
               canvas.drawCircle(adjustPoints[i][0],adjustPoints[i][1],timearroundwidthheight/2,paint);
           }
       }
        canvas.restore();
    }

    private void drawTimeLine(Canvas canvas){
        canvas.save();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);

        paint.setStrokeWidth(timepointlinewidth);
        for(int i=0;i<adjustPoints.length;i++){
            if(i==4||i==8||i==16||i==20){
                int adjust = (int) ((Math.sqrt(2)/2)*(timearroundwidthheight/2));
                canvas.drawLine(
                        adjustPoints[i][0]+adjustRoundRectTimePoint*points[i][3]-points[i][3]*adjust,
                        adjustPoints[i][1]+adjustRoundRectTimePoint*points[i][4]-points[i][4]*adjust,
                        adjustPoints[i][0]+adjustRoundRectTimePoint*points[i][3]+points[i][3]*adjust,
                        adjustPoints[i][1]+adjustRoundRectTimePoint*points[i][4]+points[i][4]*adjust,
                        paint
                );
               // canvas.drawCircle(adjustPoints[i][0]+adjustRoundRectTimePoint*points[i][3],adjustPoints[i][1]+adjustRoundRectTimePoint*points[i][4],timearroundwidthheight/2,paint);
            }else{
                canvas.drawLine(
                        adjustPoints[i][0]-points[i][3]*timearroundwidthheight/2,
                        adjustPoints[i][1]-points[i][4]*timearroundwidthheight/2,
                        adjustPoints[i][0]+points[i][3]*timearroundwidthheight/2,
                        adjustPoints[i][1]+points[i][4]*timearroundwidthheight/2,
                        paint
                );
            }
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
        for(int i=0;i<adjustPoints.length;i++){
            if(i==4||i==8||i==16||i==20){
                canvas.drawText(i+"",adjustPoints[i][0]+adjustRoundRectTimePoint*points[i][3],adjustPoints[i][1]-textshift+adjustRoundRectTimePoint*points[i][4],paint);
            }else{
                canvas.drawText(i+"",adjustPoints[i][0],adjustPoints[i][1]-textshift,paint);
            }
        }
        canvas.restore();
    }

    private void draw4_8_16_20(Canvas canvas){
        canvas.save();
        paint.setStrokeWidth(timearroundwidthheight);
        paint.setStyle(Paint.Style.STROKE);
        int[][] point = new int[][]{
                {
                        adjustPoints[6][0],
                        adjustPoints[2][1],
                        adjustPoints[2][0],
                        adjustPoints[6][1],
                        0,

                        reallyPoints[5][0],
                        reallyPoints[3][1],
                        reallyPoints[3][0],
                        reallyPoints[5][1],
                },
                {
                        adjustPoints[10][0],
                        adjustPoints[10][1],
                        adjustPoints[6][0],
                        adjustPoints[6][1],
                        90,

                        reallyPoints[9][0],
                        reallyPoints[9][1],
                        reallyPoints[7][0],
                        reallyPoints[7][1],
                },
                {
                        adjustPoints[14][0],
                        adjustPoints[18][1],
                        adjustPoints[18][0],
                        adjustPoints[14][1],
                        180,

                        reallyPoints[15][0],
                        reallyPoints[17][1],
                        reallyPoints[17][0],
                        reallyPoints[15][1],
                },
                {
                        adjustPoints[18][0],
                        adjustPoints[18][1],
                        adjustPoints[22][0],
                        adjustPoints[22][1],
                        270,

                        reallyPoints[19][0],
                        reallyPoints[19][1],
                        reallyPoints[21][0],
                        reallyPoints[21][1],
                },
        };
 /*       for(int i=0;i<4;i++){
            canvas.drawArc(
                    point[i][0],
                    point[i][1],
                    point[i][2],
                    point[i][3],
                    point[i][4],
                    90,
                    false,
                    paint
            );
        }*/
        for(int i=0;i<4;i++){

            paint.setStrokeWidth(timearroundwidthheight);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(borderColor);
            canvas.saveLayer(0,0,dayView.getWidth(),dayView.getHeight(),paint);
            Bitmap bitmap1 = Bitmap.createBitmap(dayView.getWidth(),dayView.getHeight(),Bitmap.Config.ARGB_8888);
            Canvas canvas1 = new Canvas(bitmap1);
            canvas1.drawRoundRect(
                    point[i][0],
                    point[i][1],
                    point[i][2],
                    point[i][3],
                    roundRectRadious,
                    roundRectRadious,
                    paint
            );
            canvas.drawBitmap(bitmap1,0,0,paint);

            Bitmap bitmap2 = Bitmap.createBitmap(dayView.getWidth(),dayView.getHeight(),Bitmap.Config.ARGB_8888);
            Canvas canvas2 = new Canvas(bitmap2);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas2.drawRect(
                    point[i][5],
                    point[i][6],
                    point[i][7],
                    point[i][8],
                    paint
            );

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(bitmap2,0,0,paint);
            paint.setXfermode(null);
        }
        canvas.restore();
    }
}
