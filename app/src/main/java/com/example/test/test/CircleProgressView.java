package com.example.test.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by xinheng on 2018/5/3.<br/>
 * describe：
 */
public class CircleProgressView extends View {
    private Paint mPaint,paint1,paint2;
    private final RectF rect;
    private int progressValue;
    private int[]mColors={Color.GREEN,Color.RED};
    public CircleProgressView(Context context) {
        this(context,null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        rect = new RectF();
        progressValue=25;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int mWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();

        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth((float) mWidth/10 );
        mPaint.setStyle(Paint.Style.STROKE);
        //mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.YELLOW);
        paint1=new Paint(mPaint);
        paint2=new Paint(mPaint);
        paint1.setStrokeCap(Paint.Cap.ROUND);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paint1.setColor(mColors[0]);
        paint2.setColor(mColors[1]);
        rect.set(20, 20, mWidth - 20, mHeight - 20);
        canvas.drawArc(rect, 0, 360, false, mPaint);
        mPaint.setColor(Color.BLACK);
//should: 0<= rank <=mRankCount, rank=7, mRankCount=6, 7
        float section = ((float)progressValue) / 100;
        int count = mColors.length;
        int[] colors = new int[count];
        System.arraycopy(mColors, 0, colors, 0, count);

        LinearGradient shader = new LinearGradient(mWidth - mWidth/4, mWidth/4,  mWidth/4, mHeight - mWidth/4, colors, null,
                Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        float section_ = section * 360;
        if(section_>110) {
            float v = section_ - 110;
            float v1 = v / 2;
            Log.e("TAG", "onDraw: "+v1+", "+section_ );
            canvas.drawArc(rect, -v1, v1, false, paint1);
            canvas.drawArc(rect, 90, v1, false, paint2);
            canvas.drawArc(rect, -10, 110, false, mPaint);
        }else{
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            float v = section_ / 2;
            canvas.drawArc(rect, 45-v, 45+v, false, mPaint);
        }
    }
}
