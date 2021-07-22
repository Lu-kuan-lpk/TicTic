package com.example.tictok.Mine;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tictok.R;

public class MineFragment extends Fragment {
    private String text = new String();
    private String TAG = "MineFragment";
    private LottieAnimationView lottieAnimationView;
    private TextView tv;

    public MineFragment(int position){
        if(position==1)  text="目前私密空间为空~";
        else if (position==0) text="目前暂无作品~";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.minefragment_placeholder,container,false);
        lottieAnimationView=v.findViewById(R.id.animation_view);
        tv=v.findViewById(R.id.tv);
        tv.setText(text);
        Log.d(TAG, "onCreateView:");
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 1s 后执行
                ObjectAnimator in = ObjectAnimator.ofFloat(lottieAnimationView,"alpha",1f,0f);
                in.setDuration(1000);
                in.setRepeatCount(0);
                in.setRepeatMode(ObjectAnimator.RESTART);
                in.setInterpolator(new LinearInterpolator());

                in.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation, boolean isReverse) {
                        ObjectAnimator out = ObjectAnimator.ofFloat(tv,"alpha",0f,1f);
                        out.setDuration(1000);
                        out.setRepeatCount(0);
                        out.setRepeatMode(ObjectAnimator.RESTART);
                        out.setInterpolator(new LinearInterpolator());
                        out.start();
                    }
                });
                in.start();
            }
        }, 1000);
    }
}
