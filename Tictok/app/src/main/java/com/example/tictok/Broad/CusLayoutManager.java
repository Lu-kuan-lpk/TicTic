package com.example.tictok.Broad;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CusLayoutManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener {
    private int mDrift;//位移，用来判断移动方向

    private PagerSnapHelper mPagerSnapHelper;
    private OnPageSlideListener mOnPageSlideListener;

    //可以允许上下反复滑动，是垂直滑动的
    public CusLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        mPagerSnapHelper = new PagerSnapHelper();
    }

    //onCreate->onStart->onResume->onAttachedToWindow(在画图之前进行调用）->onDraw
    @Override
    public void onAttachedToWindow(RecyclerView view) {
        view.addOnChildAttachStateChangeListener(this);
        mPagerSnapHelper.attachToRecyclerView(view);
        super.onAttachedToWindow(view);
    }


    //下面两个方法在recycler中滑动出新元素的时候会被调用
    //当一个新的item被加入的时候调用
    //Item添加进来
    @Override
    public void onChildViewAttachedToWindow(@NonNull View view) {
        //播放视频操作，判断将要播放的是上一个视频，还是下一个视频
        if (mDrift > 0) { //向上
            if (mOnPageSlideListener != null)
                mOnPageSlideListener.onPageSelected(getPosition(view), true);
        } else { //向下
            if (mOnPageSlideListener != null)
                mOnPageSlideListener.onPageSelected(getPosition(view), false);
        }
    }

    //Item移除出去
    @Override
    public void onChildViewDetachedFromWindow(@NonNull View view) {
        //暂停播放操作
        if (mDrift >= 0) {
            if (mOnPageSlideListener != null)
                mOnPageSlideListener.onPageRelease(true, getPosition(view));
        } else {
            if (mOnPageSlideListener != null)
                mOnPageSlideListener.onPageRelease(false, getPosition(view));
        }
    }

    @Override
    public void onScrollStateChanged(int state) { //滑动状态监听
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                View view = mPagerSnapHelper.findSnapView(this);
                int position = getPosition(view);
                if (mOnPageSlideListener != null) {
                    mOnPageSlideListener.onPageSelected(position, position == getItemCount() - 1);
                }
                break;
        }
    }

    //得知滑动的方向
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    //与BroadActivity交互接口注入
    public void setOnPageSlideListener(OnPageSlideListener mOnViewPagerListener) {
        this.mOnPageSlideListener = mOnViewPagerListener;
    }
}