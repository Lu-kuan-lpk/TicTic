package com.example.tictok.network;

import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import retrofit2.Retrofit;

public class PullData extends Thread{
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    private static final String BASE_URL =
            "https://api-android-camp.bytedance.com/zju/invoke/";
    private static final String TOKEN =
            "WkpVLWJ5dGVkYW5jZS1hbmRyb2lk";

    MessageListResponse result = null;

    private static final String TAG = "InMainActivity";

    private Retrofit retrofit = null;
    private IApi api;
    private Uri coverImageUri;
    private Uri videoUri;

    private PullEndListener pullEndListener;

    public void setPullEndListener(PullEndListener pullEndListener){
        this.pullEndListener=pullEndListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        try {
            URL url = new URL(BASE_URL+"video");
            HttpURLConnection connection =(HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(6000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("token", "WkpVLWJ5dGVkYW5jZS1hbmRyb2lk");
            if(connection.getResponseCode() == 200){
                InputStream in = connection.getInputStream();
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        in,
                                        StandardCharsets.UTF_8
                                )
                        );

                result = new Gson().fromJson(
                        reader,
                        new TypeToken<MessageListResponse>(){}.getType()
                );

                reader.close();
                in.close();

                Log.d(TAG, "return: "+result.success);
                pullTest();

            }else {
                Log.e(TAG, "run: connection fails");
            }
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "run: "+e.toString());
        }
        pullEndListener.DataReturn(result);
        pullEndListener.Finish();

    }

    private void pullTest(){
//        String pullTag = "InPull";
//        for(MyMessage myMessage :result.feeds){
//            Log.d(pullTag, "id: "+myMessage.Id);
//            Log.d(pullTag, "student_id: "+myMessage.studentId);
//            Log.d(pullTag, "userName: "+myMessage.userName);
//            Log.d(pullTag, "extraValue: "+myMessage.extraValue);
//            Log.d(pullTag, "videoUrl: "+myMessage.videoUrl);
//            Log.d(pullTag, "imageUrl: "+myMessage.imageUrl);
//            Log.d(pullTag, "imageW: "+myMessage.imageWidth);
//            Log.d(pullTag, "imageH: "+myMessage.imageHeight);
//            Log.d(pullTag, "createAt: "+myMessage.createAt);
//            Log.d(pullTag, "updateAt: "+myMessage.updateAt);
//        }
    }
}


