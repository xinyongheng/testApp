package slideView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.LinkedList;

/**
 * Created by xinheng on 2017/11/10.
 * describe：
 */

public class SlideView extends ViewGroup {
    private Scroller mSlide;
    private GestureDetector mGestureDetector;
    private final int padding_space = 2;
    private final String TAG = "SlideView";
    private float oldY;
    private float downY;
    private LinkedList<View> mScrapViews = new LinkedList<>();
    private SlideAdapter mAdapter;
    /**
     * 间隔线的高度
     */
    private int mDividerHeight;
    /**
     * 头部的view position
     */
    private int topViewPosition;
    /**
     * 尾部的view position
     */
    private int bottomViewPosition;
    /**
     * 充满标志
     */
    private boolean fillStatue;
    /**
     * 是否需要onLayout
     */
    private boolean needReLayout;

    public SlideView(Context context) {
        this(context, null);
    }

    public SlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSlide = new Scroller(getContext());
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                Log.e(TAG, e.getX() + ", " + e.getY());
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
//                mSlide.startScroll(getScrollX(),0,);
                Log.e(TAG, "onSingleTapUp" + e.getX() + ", " + e.getY());
                float scaleY = getScaleY();
                if (Math.abs(scaleY) < getWidth() / 2) {
                    int y = (int) e.getY();
                    int x = (int) e.getX();
                    mSlide.startScroll(x, y, x, y + (int) scaleY, 1000);
                    invalidate();
                } else {
                    Log.e(TAG, "scaleY=" + scaleY + ", getWidth2=" + getWidth() / 2);
                }
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.e(TAG, "distanceX=" + distanceX + ", distanceY=" + distanceY);
                scrollBy(0, (int) distanceY);
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        //mSlide.
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        /*if(mGestureDetector.onTouchEvent(event))
        {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }*/
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        //return mGestureDetector.onTouchEvent(event);
        if (getChildCount() <= 0) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldY = event.getY();
                downY = oldY;
                break;
            case MotionEvent.ACTION_MOVE:
                float newY = event.getY();
                float moveLengthY = newY - oldY;
                Log.e(TAG, "old=" + oldY + ", new=" + newY + ", moveLengthY=" + moveLengthY+", "+(int) moveLengthY);
                oldY = newY;
                //scrollBy(0, -moveLengthY);
                if ((topViewPosition == 0&&moveLengthY>0) || (bottomViewPosition == mAdapter.getCount() - 1&&moveLengthY<0)) {
                    return true;
                }
                offsetChildrenTopAndBottom((int) moveLengthY);
                int start = 0;
                int count = 0;
                int childCount = getChildCount();
                boolean down = moveLengthY > 0;
                if (down) {//向下滑
                    for (int i = childCount - 1; i >= 0; i--) {
                        final View child = getChildAt(i);
                        if (child.getTop() <= getMeasuredHeight()) {
                            break;
                        } else {
                            start = i;
                            count++;
                            mScrapViews.add(child);
                            bottomViewPosition--;
                        }
                    }
                } else {
                    for (int i = 0; i < childCount; i++) {
                        final View child = getChildAt(i);
                        if (child.getBottom() >= 0) {
                            break;
                        } else {
                            count++;
                            mScrapViews.add(child);
                            topViewPosition++;
                            Log.e(TAG, "onTouchEvent: count="+count+", topViewPosition="+topViewPosition );
                        }
                    }
                }
                if (count > 0) {
                    detachViewsFromParent(start, count);
                    //mRecycler.removeSkippedScrap();
                }
                fillGap(down);
                break;
            case MotionEvent.ACTION_UP:
                float slideLength = event.getY() - downY;//移动距离
                int scrollY = getScrollY();//距顶部距离
                Log.e(TAG, "onSingleTapUp-" + event.getX() + ", " + event.getY() + ",slideLength=" + slideLength + ",scrollY=" + scrollY);
                //if (Math.abs(slideLength) > 0 && Math.abs(scrollY) < getHeight() / 2) {
                if (topViewPosition == 0) {//到顶部了或最底部了
                    //int y = (int) event.getY();
                    //int x= (int) event.getX();
//                    if(slideLength+scrollY!=0){
//                        slideLength=-scrollY;
//                    }
                    View firstView = getFirstView();
                    Log.e(TAG, "topViewPosition = "+topViewPosition+", " + firstView.getTop());
                    //mSlide.startScroll(0, firstView.getTop(), 0, 0, 1000);
                    //invalidate();
                    offsetChildrenTopAndBottom(firstView.getTop(),1000);
                } else if (bottomViewPosition == mAdapter.getCount() - 1) {
                    View lastView = getLastView();
                    Log.e(TAG, "bottomViewPosition = "+bottomViewPosition+", " + lastView.getBottom());
                    //mSlide.startScroll(0, lastView.getBottom(), 0, getMeasuredHeight(), 1000);
                    //invalidate();
                    offsetChildrenTopAndBottom(lastView.getBottom(),1000);
                } else {
                    Log.e(TAG, "scrollY=" + scrollY + ", getWidth2=" + getHeight() / 2);
                }
                Log.e(TAG, "onTouchEvent: size="+mScrapViews.size());
                break;

        }
        return true;
    }

    private void offsetChildrenTopAndBottom(int moveLengthY) {
        for (int i = 0; i < getChildCount(); i++) {
            final View v = getChildAt(i);
            v.layout(v.getLeft(),v.getTop()+moveLengthY,v.getRight(),v.getBottom()+moveLengthY);
        }
    }
    private void offsetChildrenTopAndBottom(int moveLengthY,int delay) {
        ValueAnimator valueAnimator=ValueAnimator.ofInt(0,moveLengthY);
        valueAnimator.setDuration(delay);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                for (int i = 0; i < getChildCount(); i++) {
                    final View v = getChildAt(i);
                    v.layout(v.getLeft(), v.getTop() + value, v.getRight(), v.getBottom() + value);
                }
            }
        });
    }

    /**
     * 填充屏幕外的
     *
     * @param down
     */
    private void fillGap(boolean down) {
        if (down) {
            fillDown(getFirstView().getTop());
        } else {
            fillUp(getLastView().getBottom());
        }
        /*if(needReLayout){
            //invalidate();
            //requestLayout();
            needReLayout=false;
        }*/
    }

    private void fillDown(int nextTop) {
        //int end = getBottom() - getTop() - getPaddingBottom() - getPaddingTop();
        LayoutParams p;
        while (nextTop > 0 && topViewPosition > 0) {
            topViewPosition--;
            View child = obtainView(topViewPosition);
            p = (LayoutParams) child.getLayoutParams();
            if (p == null) {
                p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                Log.e(TAG, "fillViews: p == null");
            }
            p.viewType = mAdapter.getItemViewType(topViewPosition);
            addViewInLayout(child, 0, p, true);
            child.requestLayout();
            needReLayout=true;
            nextTop -= p.height;
        }
    }

    private void fillUp(int nextBottom) {
        LayoutParams p;
        while (nextBottom < getMeasuredHeight() && bottomViewPosition < mAdapter.getCount()-1) {
            bottomViewPosition++;
            View child = obtainView(bottomViewPosition);
            p = (LayoutParams) child.getLayoutParams();
            if (p == null) {
                p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                Log.e(TAG, "fillViews: p == null");
            }
            p.viewType = mAdapter.getItemViewType(bottomViewPosition);
            addViewInLayout(child, -1, p, true);
            child.requestLayout();
            needReLayout=true;
            Log.e(TAG, "fillUp: nextBottom="+nextBottom+", height=50 ," +getMeasuredHeight());
            nextBottom += p.height;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         /*int h_sum=0;黑芝麻核桃粉
        int width_max=0;
        Log.e("TAG","数量="+getChildCount());
        for(int i=0;i<getChildCount();i++){
            View childView = getChildAt(i);
            int bottom_child = childView.getMeasuredHeight() + h_sum;
            if(width_max<childView.getMeasuredWidth()){
                width_max=childView.getMeasuredWidth();
            }
            childView.layout(0,h_sum,childView.getWidth(), bottom_child);
            h_sum=bottom_child+padding_space;
        }
        setMeasuredDimension(width_max+20,h_sum);*/
    }

    /*@Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int h_sum=0;
        //垂直排列
        for(int i=0;i<childCount;i++){
            View childView = getChildAt(i);
            int bottom_child = childView.getMeasuredHeight() + h_sum;
            Log.e("TAG","top="+h_sum+", right="+childView.getMeasuredWidth()+", bottom="+bottom_child);
            childView.layout(20,h_sum,childView.getMeasuredWidth()+20, bottom_child);
            h_sum=bottom_child+padding_space;
        }
    }*/
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //super.onLayout(changed, l, t, r, b);
        Log.e(TAG, "onLayout: " );
        int childCount = getChildCount();
        int h_sum = 0;
        //垂直排列
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int bottom_child = childView.getMeasuredHeight() + h_sum;
            Log.e("TAG", "top=" + h_sum + ", right=" + childView.getMeasuredWidth() + ", bottom=" + bottom_child);
            childView.layout(20, h_sum, childView.getMeasuredWidth() - 20, bottom_child);
            h_sum = bottom_child + padding_space;
        }
        if (changed&&!needReLayout) {
            //int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).forceLayout();
            }
        }
        needReLayout=false;
        layoutChildren(true);
    }

    private void layoutChildren(boolean flowDown) {
        //if(getChildCount())
        Log.e(TAG,"layoutChildren, "+fillStatue);
        if (!fillStatue) {
            fillViews(0, flowDown);
        }
    }

    /**
     * 填充view
     *
     * @param firstPosition 起始位置
     * @param flowDown      false头部 true底部
     */
    private void fillViews(int firstPosition, boolean flowDown) {
        if (mAdapter != null && mAdapter.getCount() > 0) {
            int end = getBottom() - getTop() - getPaddingBottom() - getPaddingTop();
            int nextTop = 0;
            LayoutParams p;
            topViewPosition = firstPosition;
            for (int i = firstPosition; nextTop < end && i < mAdapter.getCount(); i++) {
                View child = obtainView(i);
                p = (LayoutParams) child.getLayoutParams();
                if (p == null) {
                    p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    Log.e(TAG, "fillViews: p == null");
                }
                p.viewType = mAdapter.getItemViewType(i);
                addViewInLayout(child, flowDown ? -1 : 0, p, true);
//                nextTop = child.getBottom() + mDividerHeight;
//                nextTop += child.getMeasuredHeight() + mDividerHeight;
                nextTop += p.height + mDividerHeight;
                Log.e(TAG, "fillViews: nextTop=" + child.getMeasuredHeight() + ", " + p.height);
                bottomViewPosition = i;
            }
            fillStatue = true;
            Log.e(TAG, "fillViews: nextTop=" + nextTop + ", bottomViewPosition=" + bottomViewPosition + " topViewPosition=" + topViewPosition + " size=" + mScrapViews.size());
            //invalidate();
            //requestLayout();
        }
    }

    @Override
    public void computeScroll() {
        //Log.e(TAG,"computeScroll");
        super.computeScroll();
        if (mSlide.computeScrollOffset()) {
            scrollTo(mSlide.getCurrX(), mSlide.getCurrY());
            invalidate();
        }
    }

    private View getFirstView() {
        View childAt = getPositionView(0);
        int childCount = getChildCount();
        LayoutParams layoutParams;
        int length=0;
        for (int i=0;i<childCount;i++){
            View childAt1 = getChildAt(i);
            layoutParams = (LayoutParams) childAt.getLayoutParams();
            length+=layoutParams.height;
        }
        View childBottom = getChildAt(childCount - 1);
        childBottom.getBottom();
        return childAt;
    }

    private View getLastView() {
        View childAt = getPositionView(getChildCount() - 1);
        return childAt;
    }

    @Nullable
    private View getPositionView(int tag) {
        int childCount = getChildCount();
        if (childCount > 0 && tag < childCount) {
            View childAt = getChildAt(tag);
//            float childAtY = childAt.getY();
            Log.e(TAG, "getPositionView: "+tag+", "+childAt.getTop()+", "+childAt.getBottom() );
            return childAt;
        }
        return null;
    }

    private View obtainView(int position) {
        View view = null;
        if (mScrapViews.size() > 0) {
            view = mScrapViews.removeLast();
        }
        if (view == null) {
            return mAdapter.getView(position, null, this);
        } else {
            Log.e(TAG, "obtainView: 啦啦啦啦" );
            View view1 = mAdapter.getView(position, view, this);
            if (view1 != view) {
                mScrapViews.add(view);
            }
            return view1;
        }
    }

    public void setAdapter(SlideAdapter adapter) {
        if (adapter == null)
            throw new RuntimeException("adapter is null");
        this.mAdapter = adapter;
    }

    public void clear() {
        if (mScrapViews != null) {
            mScrapViews.clear();
            mScrapViews = null;
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public int viewType;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(int w, int h, int viewType) {
            super(w, h);
            this.viewType = viewType;
        }
    }

}
