package com.example.tictok.Broad;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.example.tictok.R;

/**
 * 飘心效果
 * 1.创建ImageView
 * 2.ImageView执行组合动画
 * 3.动画执行完成后销毁View
 */
public class FlyHeart extends RelativeLayout {

    private int defoutWidth = 200;//默认控件宽度
    private long mDuration = 3000;//默认动画时间
    //颜色集合 从中获取颜色
    private int[] color = {
            0xFFd81e06, 0xDDd81e06, 0xCCd81e06, 0xAA81e06,
            0x99d81e06, 0x77d81e06, 0x66d81e06, 0x55d81e06,
            0x44d81e06, 0x33d81e06, 0x22d81e06, 0x11d81e06
    };

    public FlyHeart(Context context) {
        super(context);
        initFrameLayout();
    }

    public FlyHeart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFrameLayout();
    }

    private void initFrameLayout() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(defoutWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
    }

    /**
     * 创建一个心形的view视图
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ImageView createHeartView() {
        ImageView heartIv = new ImageView(getContext());
        LayoutParams params = new LayoutParams(defoutWidth / 2, defoutWidth / 2);

        //控件位置
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);

        heartIv.setLayoutParams(params);
        heartIv.setImageResource(R.drawable.heart);
        //改变颜色
        heartIv.setImageTintList(ColorStateList.valueOf(color[(int) (color.length * Math.random())]));
        return heartIv;
    }

    /**
     * 执行动画
     * 在展示调用该方法
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startFly() {
        final ImageView heartIv = createHeartView();
        addView(heartIv);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(createTranslationX(heartIv))
                .with(createTranslationY(heartIv))
                .with(createScale(heartIv))
                .with(createRotation(heartIv))
                .with(createAlpha(heartIv));
        //执行动画
        animatorSet.start();
        //销毁view
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView(heartIv);
            }
        });
    }

    /**
     * 横向正弦位移动画
     *
     * @return
     */
    private Animator createTranslationX(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", 0, (float) (defoutWidth * Math.random() / 4));
        animator.setDuration(mDuration);
        //CycleInterpolator cycles 正弦曲线数
        animator.setInterpolator(new CycleInterpolator((float) (3 * Math.random())));
        return animator;
    }


    /**
     * 纵向加速位移动画
     *
     * @return
     */
    private Animator createTranslationY(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, -1000);
        animator.setDuration(mDuration);
        animator.setInterpolator(new AccelerateInterpolator());
        return animator;
    }


    /**
     * 加速放大动画
     *
     * @return
     */
    private Animator createScale(View view) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", 1, 1.5f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", 1, 1.5f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mDuration);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.play(animatorX).with(animatorY);
        return animatorSet;
    }

    /**
     * 透明度动画
     *
     * @return
     */
    private Animator createAlpha(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1, 0.1f);
        animator.setDuration(mDuration);
        animator.setInterpolator(new AccelerateInterpolator());
        return animator;
    }

    /**
     * 旋转动画
     *
     * @return
     */
    private Animator createRotation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, (float) (25f * Math.random()));
        animator.setDuration(mDuration);
        animator.setInterpolator(new CycleInterpolator((float) (6 * Math.random())));
        return animator;
    }


}
