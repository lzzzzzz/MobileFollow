package com.lz.mobilefollow.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.lz.mobilefollow.R;

/**
 * Created by LZ on 2016/12/9.
 */

public class CustomeView extends View {
    public CustomeView(Context context) {
        super(context);
    }

    public CustomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 创建画笔
        Paint paint=new Paint();
        paint.setColor(Color.RED);

        canvas.drawText("画圆：",10,20,paint);//画文本
        canvas.drawCircle(60,20,10,paint);//画小圆

        paint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
        canvas.drawCircle(120, 20, 20, paint);// 大圆

        canvas.drawText("画线及弧线：", 10, 60, paint);
        paint.setColor(Color.BLUE);

        canvas.drawLine(60, 80, 100, 80, paint);// 画线
        canvas.drawLine(110, 80, 190, 120, paint);// 斜线

        //画笑脸弧线
        paint.setStyle(Paint.Style.STROKE);//设置空心
        RectF oval1=new RectF(150,60,180,80);
        canvas.drawArc(oval1, 180, 180, false, paint);//小弧形
        oval1.set(190, 60, 220, 80);
        canvas.drawArc(oval1, 180, 180, false, paint);//小弧形
        oval1.set(160, 70, 210, 100);
        canvas.drawArc(oval1, 0, 180, false, paint);//小弧形

        canvas.drawText("画矩形：", 10, 130, paint);
        paint.setColor(Color.GRAY);// 设置灰色
        paint.setStyle(Paint.Style.FILL);//设置填满
        canvas.drawRect(60, 140, 80, 160, paint);// 正方形
        canvas.drawRect(60, 170, 160, 180, paint);// 长方形

        canvas.drawText("画扇形和椭圆:", 10, 220, paint);
        /* 设置渐变色 这个正方形的颜色是改变的 */
        Shader mShader = new LinearGradient(0, 0, 100, 100,
                new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                        Color.LTGRAY }, null, Shader.TileMode.REPEAT); // 一个材质,打造出一个线性梯度沿著一条线。
        paint.setShader(mShader);
        // p.setColor(Color.BLUE);
        RectF oval2 = new RectF(60, 240, 220, 440);// 设置个新的长方形，扫描测量
        canvas.drawArc(oval2, 200, 130, true, paint);
        // 画弧，第一个参数是RectF：该类是第二个参数是角度的开始，第三个参数是多少度，第四个参数是真的时候画扇形，是假的时候画弧线
        //画椭圆，把oval改一下
        oval2.set(240,250,360,340);
        canvas.drawOval(oval2, paint);

        canvas.drawText("画三角形：", 10, 380, paint);
        // 绘制这个三角形,你可以绘制任意多边形
        Path path = new Path();
        path.moveTo(80, 400);// 此点为多边形的起点
        path.lineTo(120, 450);
        path.lineTo(80, 450);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, paint);

        // 你可以绘制很多任意多边形，比如下面画六连形
        paint.reset();//重置
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);//设置空心
        Path path1=new Path();
        path1.moveTo(180, 400);
        path1.lineTo(240, 400);
        path1.lineTo(280, 430);
        path1.lineTo(240, 450);
        path1.lineTo(180, 450);
        path1.lineTo(140, 430);
        path1.close();//封闭
        canvas.drawPath(path1, paint);

        /*
         * Path类封装复合(多轮廓几何图形的路径
         * 由直线段*、二次曲线,和三次方曲线，也可画以油画。drawPath(路径、油漆),要么已填充的或抚摸
         * (基于油漆的风格),或者可以用于剪断或画画的文本在路径。
         */

        //画圆角矩形
        paint.setStyle(Paint.Style.FILL);//充满
        paint.setColor(Color.LTGRAY);
        paint.setAntiAlias(true);// 设置画笔的锯齿效果
        canvas.drawText("画圆角矩形:", 10, 490, paint);
        RectF oval3 = new RectF(80, 500, 200, 540);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 20, 15, paint);//第二个参数是x半径，第三个参数是y半径

        //画贝塞尔曲线
        canvas.drawText("画贝塞尔曲线:", 10, 580, paint);
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        Path path2=new Path();
        path2.moveTo(100, 600);//设置Path的起点
        path2.quadTo(150, 590, 170, 680); //设置贝塞尔曲线的控制点坐标和终点坐标
        canvas.drawPath(path2, paint);//画出贝塞尔曲线

        //画点
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("画点：", 10, 700, paint);
        canvas.drawPoint(120, 720, paint);//画一个点
        canvas.drawPoints(new float[]{120,740,135,740,150,740}, paint);//画多个点

        //画图片，就是贴图
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        canvas.drawBitmap(bitmap, 250,760, paint);
    }
}
