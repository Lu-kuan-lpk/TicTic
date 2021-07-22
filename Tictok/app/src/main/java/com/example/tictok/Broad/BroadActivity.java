package com.example.tictok.Broad;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tictok.network.MessageListResponse;
import com.example.tictok.R;
import com.example.tictok.Stream.StreamActivity;

public class BroadActivity extends AppCompatActivity {
    private static final String TAG = "BroadActivity";
    private RecyclerView mRecycler;
    private CusLayoutManager mLayoutManager;
    private int tmppos; //表示此时应该播放的视频的编号
    private ImageButton back;
    public MessageListResponse message; //= StreamActivity.tmpmessage;
    public int[] isclick ;


    private ImageButton heart;
    public FlyHeart fheart;
    private int numclick;
    public VideoAdapter mAdapter;
    private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad);
        Intent intent = getIntent();
        tmppos= intent.getIntExtra("pos",0);
        String s= intent.getStringExtra("Activity");
        if(s.compareTo("Like")==0)
            message=StreamActivity.likemessage;
        else if(s.compareTo("Stream")==0)
            message=StreamActivity.tmpmessage;

        numclick=0;
        isclick = new int[message.feeds.size()];

        //进行界面 包括recycler的初始化
        initView();
        //进行监听的初始化
        initListener();
    }

    //初始化监听
    private void initListener() {

        mLayoutManager.setOnPageSlideListener(new OnPageSlideListener() {

            //当pagerelease 的时候发生的事情
            @Override
            public void onPageRelease(boolean isNext, int position) {
                int index;
                if (isNext) {
                    index = 0;
                    tmppos=tmppos+1;
                } else {
                    index = 1;
                    tmppos=tmppos-1;
                }
                releaseVideo(index);
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onPageSelected(int position, boolean isNext) {
                playVideo(position);
            }
        });

        //返回的单击相应事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    //初始化View
    private void initView() {
        mRecycler = findViewById(R.id.mRecycler);
        mLayoutManager = new CusLayoutManager(this, OrientationHelper.VERTICAL, false);
        mAdapter = new VideoAdapter(this,message,tmppos);
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mAdapter);
        back =findViewById(R.id.broad_back);
        heart=findViewById(R.id.click_heart);
        fheart=findViewById(R.id.Flyheart);

        mAdapter.setOnHeartClickListener(new OnHeartClickListener() {
            @Override
            public void click(int position) {
//                if(isclick[position]==0){
//                    likemessage.feeds.add(StreamActivity.tmpmessage.feeds.get(position));
//                    isclick[position]=1;
//                    Log.d(TAG, "onClick: "+position);
//                }
            }
        });

        for(int i=0;i<message.feeds.size();i++) isclick[i]=0;
    }

    //播放
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void playVideo(int position) {
        //初始化页面上的必要元素
        View itemView = mRecycler.getChildAt(0);
        final CusVideoView mVideoView = itemView.findViewById(R.id.mVideoView);
        final ImageView mPlay = itemView.findViewById(R.id.mPlay);
        final ImageView mThumb = itemView.findViewById(R.id.mThumb);
        imageButton = itemView.findViewById(R.id.onbroad_heart);
        final MediaPlayer[] mMediaPlayer = new MediaPlayer[1];
        //如果之前被点击过就显示橙色
        if(isclick[tmppos]==1) imageButton.setImageResource(R.drawable.hearto);
        mVideoView.start();

        //单击点赞的相应事件
        heart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                fheart.startFly();
                imageButton.setImageResource(R.drawable.hearto);
                //int pos= mAdapter.
                if(isclick[tmppos]==0){
                    StreamActivity.likemessage.feeds.add(StreamActivity.tmpmessage.feeds.get(tmppos));
                    isclick[tmppos]=1;
                    Log.d(TAG, "onClick: "+tmppos);
                }

            }
        });
        //播放设置
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                //将页面设置循环播放
                mMediaPlayer[0] = mp;
                mp.setLooping(true);
                //设置循环滚动的时间
                mThumb.animate().alpha(0).setDuration(200).start();
                return false;
            }
        });

        //暂停控制
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoView.isPlaying()) {
                    mPlay.animate().alpha(1f).start();
                    mVideoView.pause();
                } else {
                    mPlay.animate().alpha(0f).start();
                    mVideoView.start();
                }
            }
        });
    }

    //释放
    private void releaseVideo(int index) {
        View itemView = mRecycler.getChildAt(index);
        final CusVideoView mVideoView = itemView.findViewById(R.id.mVideoView);
        final ImageView mThumb = itemView.findViewById(R.id.mThumb);
        final ImageView mPlay = itemView.findViewById(R.id.mPlay);
        mVideoView.stopPlayback();
        mThumb.animate().alpha(1).start();
        mPlay.animate().alpha(0f).start();
    }


}