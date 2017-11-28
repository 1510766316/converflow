package com.azoft.carousellayoutmanager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 控制fling速度的RecyclerView
 *
 * Created by fangshengqiang on 9/29/17.
 */
public class SpeedRecyclerView extends RecyclerView {
    private static final float FLING_SCALE_DOWN_FACTOR = 0.5f; // 减速因子
    private static final int FLING_MAX_VELOCITY = 8000; // 最大顺时滑动速度
    private boolean mFirstLayoutComplete = false;

    public SpeedRecyclerView(Context context) {
        super(context);
    }

    public SpeedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SpeedRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX = solveVelocity(solveVelocity(velocityX));
        velocityY = solveVelocity(solveVelocity(velocityY));
        return super.fling(velocityX, velocityY);
    }

    private int solveVelocity(int velocity) {
        if (velocity > 0) {
            return Math.min(velocity, FLING_MAX_VELOCITY);
        } else {
            return Math.max(velocity, -FLING_MAX_VELOCITY);
        }
    }

    private float mDownX;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mMoveValue[0] = mDownX;
                getParent().requestDisallowInterceptTouchEvent(true); //设置父类不拦截滑动事件
                break;
            case MotionEvent.ACTION_MOVE:
                if ((ev.getX() > mDownX && getCoverFlowLayout().getCenterItemPosition() == 0) ||
                        (ev.getX() < mDownX && getCoverFlowLayout().getCenterItemPosition() ==
                                getCoverFlowLayout().getItemCount() -1)) {
                    //如果是滑动到了最前和最后，开放父类滑动事件拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    //滑动到中间，设置父类不拦截滑动事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mMoveValue[1] = ev.getX();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 获取LayoutManger，并强制转换为CoverFlowLayoutManger
     */
    public CarouselLayoutManager getCoverFlowLayout() {
        return ((CarouselLayoutManager)getLayoutManager());
    }

    private float[] mMoveValue = new float[2];
    public float[] getScrollMove(){
        return mMoveValue;
    }
    
    public void clearScrollMove(){
        mMoveValue[1] = mMoveValue[0];
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mFirstLayoutComplete = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFirstLayoutComplete = false;
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);
        mFirstLayoutComplete = true;
    }
}
