package com.example.tictok.Mine;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tictok.Broad.BroadActivity;
import com.example.tictok.network.MessageListResponse;
import com.example.tictok.R;
import com.example.tictok.Stream.BroadClickListener;
import com.example.tictok.Stream.StreamActivity;

public class LikeFragment extends Fragment {
    private LottieAnimationView lottieAnimationView;
    private MessageListResponse likemes = StreamActivity.likemessage;
    private RecyclerView recyclerView;
    private LikeAdapter likeAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.likefragment_placeholder, container, false);
        lottieAnimationView=v.findViewById(R.id.animation_view);
        Log.d("likefragment", "onCreateView: "+likemes.feeds.size());
        InitRecycle(v);
        return v;
    }

    public void InitRecycle(View v){
        recyclerView = v.findViewById(R.id.like_recycle);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        likeAdapter = new LikeAdapter(v.getContext(),likemes);
        recyclerView.setAdapter(likeAdapter);

        //跳转动画
        likeAdapter.setBroadClickListener(new BroadClickListener() {
            @Override
            public void jump(int position) {
                Intent intent = new Intent(v.getContext(), BroadActivity.class);
                intent.putExtra("pos",position);
                intent.putExtra("Activity","Like");
                Log.d("Stream", "jump: "+position);
                startActivity(intent);
            }
        });
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
                        ObjectAnimator out = ObjectAnimator.ofFloat(recyclerView,"alpha",0f,1f);
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
