package com.example.test.test;


import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by xinheng on 2017/11/21.
 * describe：
 */

public class MySwipeRefreshLayout extends SwipeRefreshLayout {
    private int mActivePointerId;
    private float mInitialMotionY;
    private int mTouchSlop;

    public MySwipeRefreshLayout(Context context) {
        super(context);
    }

    private final String TAG = "TestFragment";

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.e(TAG, "onInterceptTouchEvent: ");
        boolean b = super.onInterceptTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                final float initialMotionY = getMotionEventY(event, mActivePointerId);
                Log.e(TAG, "onInterceptTouchEvent: initialMotionY=" + initialMotionY);
                if (initialMotionY != -1) {
                    //return false;
                    mInitialMotionY = initialMotionY;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float y = getMotionEventY(event, mActivePointerId);
                Log.e(TAG, "onInterceptTouchEvent: y="+y );
                if (y != -1) {
                    final float yDiff = y - mInitialMotionY;
                    mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                    Log.e(TAG, "onInterceptTouchEvent: mTouchSlop=" + (yDiff > mTouchSlop));
                }
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        Log.e(TAG, "onInterceptTouchEvent: " + b + ", " + getString(event));
        return b;
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e(TAG, "dispatchTouchEvent: ");
        boolean b = super.dispatchTouchEvent(ev);
        Log.e(TAG, "dispatchTouchEvent: " + b + ", " + getString(ev));
        return b;
        //return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean b = super.onTouchEvent(ev);
        String s = getString(ev);
        Log.e(TAG, "onTouchEvent: " + s + ", " + b);
        return b;
    }

    private String getString(MotionEvent ev) {
        String s = "";
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                s = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                s = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:
                s = "ACTION_UP";
                break;
        }
        return s;
    }
}
