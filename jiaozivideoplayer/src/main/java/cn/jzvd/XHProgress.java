package cn.jzvd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

/**
 * Created by xinheng on 2017/11/28.
 * describe：
 */

public class XHProgress extends ProgressBar {

    private Paint mPaint;
    private int mWidth;
    private int mTextHeight;
    private String mText;
    private JZLoadingSpeed jzLoadingSpeed;

    public XHProgress(Context context) {
        super(context);
        init();
    }

    public XHProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XHProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        mPaint =new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(35);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        String s = "拼命加载...0000.00kb/s";
        Rect rect = new Rect();
        mPaint.getTextBounds(s,0,s.length(),rect);
        mWidth = rect.width();
        mTextHeight = rect.height();
        setPadding(getPaddingLeft(),getPaddingTop(),getPaddingRight(),getPaddingBottom()+ mTextHeight +10);
        jzLoadingSpeed=new JZLoadingSpeed(new MyHandler(this));

    }
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth,getMeasuredHeight());
    }

    private void setText(String s){
        if(!TextUtils.isEmpty(s)){
            mText=s;
            invalidate();
        }
    }
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(TextUtils.isEmpty(mText)){
            mText="拼命加载..0.0kb/s";
        }
        Paint.FontMetrics metrics = new Paint.FontMetrics();
        mPaint.getFontMetrics(metrics);
        canvas.drawText(mText,getWidth()/2,getHeight()-mTextHeight/2-metrics.top/2-metrics.bottom/2,mPaint);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(visibility== View.VISIBLE){
            jzLoadingSpeed.start();
        }else{
            jzLoadingSpeed.cancel();
        }
    }
    private static class MyHandler extends Handler {
        private WeakReference<XHProgress> reference;

        public MyHandler(XHProgress jzProgress) {
            reference=new WeakReference<XHProgress>(jzProgress);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case JZLoadingSpeed.UPDATE_LOADING_SPEED:
                    //Log.e("TAG", "handleMessage: "+msg.obj );
                    if(reference.get()!=null){
                        reference.get().setText("玩命加载中.."+(String)msg.obj);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
