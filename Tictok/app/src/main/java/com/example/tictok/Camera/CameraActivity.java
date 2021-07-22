package com.example.tictok.Camera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.Image;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictok.R;
import com.example.tictok.Upload.UploadActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final String TAG = "InCameraActivity";

    private boolean isRecording = false;

    private SurfaceView surfaceView;
    private Camera camera;
    private MediaRecorder mediaRecorder;
    private SurfaceHolder surfaceHolder;

    public static String mp4Path = null;
    public static boolean isRecorded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

//        requestPermission();
        initMp4Path();

        ImageButton btn_back = findViewById(R.id.camera_back);
        ImageButton btn_shot = findViewById(R.id.camera_shot);
        TextView shot_hint = findViewById(R.id.text_shot_hint);

        surfaceView = findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        initCamera();
        surfaceHolder.addCallback(this);

        btn_shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRecording){
                    if(prepareVideoRecorder()){
                        shot_hint.setText("停止");
                        btn_shot.setImageResource(R.drawable.shot);
                        mediaRecorder.start();
                        isRecording = true;
                    }
                }else {
                    mediaRecorder.setOnErrorListener(null);
                    mediaRecorder.setOnInfoListener(null);
                    mediaRecorder.setPreviewDisplay(null);
                    shot_hint.setText("拍摄");
                    btn_shot.setImageResource(R.drawable.record2);
                    try {
                        mediaRecorder.stop();
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e(TAG, "after recording: "+e.toString());
                    }
                    mediaRecorder.reset();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    camera.lock();
                    isRecording = false;
                    isRecorded = true;
                    Toast.makeText(CameraActivity.this,
                            "拍摄成功",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CameraActivity.this, UploadActivity.class);
                    startActivity(intent);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(camera == null){
            initCamera();
        }
        camera.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stopPreview();
    }

    private void initMp4Path(){
        mp4Path = getCacheDir() + File.separator + "tmp.mp4";
    }

    private void initCamera(){
        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.set("orientation", "portrait");
        parameters.set("rotation", 90);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "surfaceCreated: "+e.toString());
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface()==null){
            return;
        }
        camera.stopPreview();
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "surfaceChanged: "+e.toString());
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private boolean prepareVideoRecorder(){
        mediaRecorder = new MediaRecorder();

        camera.unlock();
        mediaRecorder.setCamera(camera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        mediaRecorder.setOutputFile(mp4Path);

        mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        mediaRecorder.setOrientationHint(90);

        try {
            mediaRecorder.prepare();
        }catch (Exception e){
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            Log.e(TAG, "prepareVideoRecorder: "+e.toString());
            return false;
        }
        return true;
    }
}