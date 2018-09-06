package com.example.test.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by xinheng on 2017/11/22.
 * describe：
 * 思路：维护一个Matrix 和 多个类型的Paint 进行图片的显示
 * 永远最后画中间的（需要被显示的）图片
 */

public class ScaleAndTranslateImageView extends View {

    private Bitmap mBitmap;
    private Matrix mMatrix;
    private Paint mPaint;
    private Bitmap mBitmap1;
    private Paint mPaint1;
    private Bitmap mBitmap2;
    private PaintFlagsDrawFilter mDrawFilter;

    private float mOffset;
    public ScaleAndTranslateImageView(Context context) {
        super(context);
        init();

    }

    public ScaleAndTranslateImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public ScaleAndTranslateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.b);
        mBitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.c);
        mBitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.d);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaint1.setAlpha(230);
        //mPaint1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
        //mPaint1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
        //mMatrix =getMatrix();
        mMatrix = new Matrix();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) (mBitmap.getWidth()) + mBitmap1.getWidth(), (int) (mBitmap.getHeight()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(mDrawFilter);
        super.onDraw(canvas);
        //canvas.drawColor(Color.WHITE);
        //canvas.save();
        final float translate=mOffset;
        mMatrix.setTranslate(mBitmap.getWidth() - 80-translate, 0);
        mMatrix.preScale(0.9f,0.9f);
        mMatrix.postTranslate(0,getHeight()*0.1f);
        canvas.drawBitmap(mBitmap1, mMatrix, mPaint);
        mMatrix.reset();
        mMatrix.setTranslate(-translate, 0);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint1);
        /*canvas.drawBitmap(mBitmap,mMatrix,mPaint);
        mMatrix.preScale(0.9f,0.9f);
        mMatrix.setTranslate(mBitmap.getWidth() - 80, 0);
        mMatrix.postTranslate(0,getHeight()*0.1f);
        canvas.drawBitmap(mBitmap1,mMatrix,mPaint1);*/
        //canvas.restore();
    }

    private float f = 1f;

    public void setParmars(float scale) {
        //mMatrix.postScale(scale,scale);
        mMatrix.setScale(scale, scale);
        mMatrix.preTranslate(0, -getHeight() / 2f);
        mMatrix.postTranslate(0, getHeight() / 2f);
        f = scale;
        invalidate();
    }

    public void setAlphas(float a) {
        int a1 = (int) (a * 254);
        if (a1 != mPaint1.getAlpha()) {
            Log.e("TAG", "setAlphas: " + a1);
            if(mPaint1.getAlpha()!=255) {
                mOffset = (1-(mPaint1.getAlpha() - a)/255f)*getWidth()*0.25f;
                Log.e("TAG", "setAlphastr: "+mOffset );
            }
            mPaint1.reset();
            mPaint1.setAlpha(a1);
            mMatrix.reset();
            invalidate();
        }
    }
}
