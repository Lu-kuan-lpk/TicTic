package com.example.tictok.Stream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tictok.Broad.BroadActivity;
import com.example.tictok.Camera.CameraActivity;
import com.example.tictok.Mine.MineActivity;
import com.example.tictok.network.MessageListResponse;
import com.example.tictok.network.PullData;
import com.example.tictok.network.PullEndListener;
import com.example.tictok.R;
import com.example.tictok.Upload.UploadActivity;

import java.util.ArrayList;

public class StreamActivity extends AppCompatActivity {

    private final int CAMERA_REQUEST = 1654;
    private RecyclerView stream;
    private ImageButton camera;
    public int flag=2; //表示开始的界面
    public static MessageListResponse tmpmessage =null; //里面储存网上扒拉下来的所有数据
    public static MessageListResponse likemessage =null;
    public StreamAdapter adapter;
    public Button mine;
    public Button upload;
    public Button message;
    int num =0;
    private static final String TAG = "StreamActivity";
    private ImageButton refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        Thread netpull = new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkRequesting();
                while(true){
                    if(flag==1){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SetView();

                                SetRecycler();

                                SetListener();
                            }
                        });
                        flag=2;
                        break;
                    }
                }

            }
        });
        netpull.start();
    }

    public void SetView(){
        upload=findViewById(R.id.stream_upload);
        mine=findViewById(R.id.stream_mine);
        message=findViewById(R.id.stream_message);
        likemessage = new MessageListResponse();
        likemessage.feeds = new ArrayList<>();
        refresh = findViewById(R.id.refresh);
    }

    public void NetworkRequesting(){
        //开启网络的线程
        PullData netthread = new PullData();
        netthread.setPullEndListener(new PullEndListener() {
            @Override
            public void Finish() {
                adapter = new StreamAdapter(tmpmessage,StreamActivity.this);
                adapter.refresh();
                Log.d(TAG, "Finish: refresh adapter");
                flag=1;
            }

            @Override
            public void DataReturn(MessageListResponse messageListResponse) {
                tmpmessage=messageListResponse;
                return;
            }
        });
        netthread.start();
    }


    void SetRecycler(){
        //初始化recycler和其监听
        stream = findViewById(R.id.stream);
        stream.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        adapter.setBroadClickListener(new BroadClickListener() {
            @Override
            public void jump(int position) {
                Intent intent = new Intent(StreamActivity.this, BroadActivity.class);
                intent.putExtra("pos",position);
                intent.putExtra("Activity","Stream");
                startActivity(intent);
            }
        });
        stream.setAdapter(adapter);
    }

    void SetListener(){
        //设置camera图标的监听
        camera = findViewById(R.id.stream_camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                boolean cameraPermission = ContextCompat.checkSelfPermission(StreamActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                boolean audioPermission = ContextCompat.checkSelfPermission(StreamActivity.this,
                        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
                if(cameraPermission&&audioPermission){
                    Intent intent = new Intent(StreamActivity.this, CameraActivity.class);
                    Log.d(TAG, "onClick: into if");
                    startActivity(intent);
                }else {
                    ActivityCompat.requestPermissions(StreamActivity.this,
                            new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.RECORD_AUDIO},CAMERA_REQUEST);
                    Log.d(TAG, "onClick: into else");
                }
            }
        });

        //
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkRequesting();
                Log.d(TAG, "onClick: refresh");
            }
        });

        //三个button的跳转事件
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StreamActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });

        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StreamActivity.this, MineActivity.class);
                startActivity(intent);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StreamActivity.this,"对不起此功能暂未开放！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    

    @Override
    protected void onResume() {
        super.onResume();
        flag=2;
        if(num!=0){
            Thread netrefresh = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: into refresh");
                    NetworkRequesting();
//                    Log.d(TAG, "run: "+tmpmessage.feeds.size());
//                    Log.d(TAG, "run:flag="+flag);
//                    while(true){
//                        if(flag==1){
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    adapter.refresh();
//                                    Log.d(TAG, "run: refresh adapter");
//                                }
//                            });
//                            flag=2;
//                            break;
//                        }
//                    }
                }
            });
            netrefresh.start();
        }
        num++;
//        NetworkRequesting();
//        while(true){
//            if(flag==1){
//                adapter.refresh();
//                break;
//            }
//        }
//        adapter.refresh();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_REQUEST){
            if(grantResults.length>0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(StreamActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        }
    }
}