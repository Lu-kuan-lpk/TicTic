package com.example.tictok.Init;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.LinearInterpolator;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tictok.Broad.BroadActivity;
import com.example.tictok.R;
import com.example.tictok.Stream.StreamActivity;

public class InitActivity extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        lottieAnimationView= findViewById(R.id.init);
        //5s之后进入主界面
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), StreamActivity.class);

                //设定淡入淡出
                // 这里会在 5s 后执行
                ObjectAnimator in = ObjectAnimator.ofFloat(lottieAnimationView,"alpha",1f,0f);
                in.setDuration(5000);
                in.setRepeatCount(0);
                in.setRepeatMode(ObjectAnimator.RESTART);
                in.setInterpolator(new LinearInterpolator());


                in.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation, boolean isReverse) {
                        startActivity(intent);
                    }
                });
                in.start();
            }
        },3000);




    }
}