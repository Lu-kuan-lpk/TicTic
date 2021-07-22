package com.example.tictok.Upload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tictok.Camera.CameraActivity;
import com.example.tictok.MainActivity;
import com.example.tictok.R;
import com.example.tictok.network.IApi;
import com.example.tictok.network.UploadResponse;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity extends AppCompatActivity {

    private static final String TAG = "InUploadActivity";

    private static final int CHOOSE_PHOTO = 2;
    private static final int PICK_VIDEO_REQUEST = 89;

    private final String BASE_URL =
            "https://api-android-camp.bytedance.com/zju/invoke/";
    private final String TOKEN =
            "WkpVLWJ5dGVkYW5jZS1hbmRyb2lk";
    private String videoPath;
    private String coverPath;

    private ImageView imageCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        ImageButton back = findViewById(R.id.upload_back);
        Button submit = findViewById(R.id.upload_submit);
        imageCover = findViewById(R.id.image_cover);
        EditText editUpload = findViewById(R.id.edit_upload);
        Button btn_change_cover = findViewById(R.id.btn_change_cover);
        Button btn_change_video = findViewById(R.id.btn_change_video);
        LottieAnimationView lottie_loading = findViewById(R.id.lottie_loading);
        ImageView imageBackgroundCover = findViewById(R.id.background_cover);

        //init the path of video and cover image
        initPath();

        updateCoverPreview();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "start upload");
                Toast.makeText(UploadActivity.this,"开始上传，请耐心等待",Toast.LENGTH_SHORT).show();
                imageBackgroundCover.setVisibility(View.VISIBLE);
                submit.setEnabled(false);//avoid submit repeatably
                lottie_loading.setVisibility(View.VISIBLE);
                lottie_loading.playAnimation();



//                compressVideo();

                String uploadString = editUpload.getText().toString();
                uploadCompressedVideo(uploadString);
//                uploadVideo(uploadString);
            }
        });

        btn_change_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(UploadActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager
                        .PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(UploadActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},CHOOSE_PHOTO);
                }else {
                    openAlbum();
                }
            }
        });

        btn_change_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(UploadActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager
                        .PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(UploadActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PICK_VIDEO_REQUEST);
                }else {
                    selectVideo();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CHOOSE_PHOTO){
            if(grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED){
                openAlbum();
            }else {
                Toast.makeText(this,"你拒绝了权限申请",
                        Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == PICK_VIDEO_REQUEST){
            if(grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED){
                selectVideo();
            }else {
                Toast.makeText(this,"你拒绝了权限申请",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_PHOTO && resultCode==RESULT_OK){
            managePhotoReturn(data);
        }else if(requestCode == PICK_VIDEO_REQUEST &&resultCode == RESULT_OK){
            manageVideoReturn(data);
        }
    }

    private void initPath(){
        if(CameraActivity.isRecorded){
            videoPath = CameraActivity.mp4Path;
            coverPath = getCacheDir() + File.separator + "cover.png";
        }
    }

    private void updateCoverPreview(){
        if(videoPath!=null){
            Bitmap bitmap = getCoverBitmap();
            Glide.with(this)
                    .load(bitmap)
                    .apply(new RequestOptions().centerCrop())
                    .into(imageCover);
            //convert the bitmap
            convertCoverBitmap(bitmap);
        }
    }

    private Bitmap getCoverBitmap(){
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(videoPath);
        Bitmap bitmap = metadataRetriever.getFrameAtTime(1,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        return bitmap;
    }

    //convert bitmap into a png file to default path
    private void convertCoverBitmap(Bitmap bitmap){
        File file = new File(getCacheDir(), "cover.png");
        coverPath = file.getPath();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapData = bos.toByteArray();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bitmapData);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "covert cover "+e.toString());
                }

            }
        }).start();
    }

    private void uploadVideo(String uploadString){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IApi api = retrofit.create(IApi.class);
        File imageFile = new File(coverPath);
        RequestBody imageRequest =
                RequestBody.create(MediaType.parse("multipart/form-data"),imageFile);
        MultipartBody.Part imagePart =
                MultipartBody.Part.createFormData(
                        "cover_image",
                        imageFile.getName(),
                        imageRequest
                );

        File videoFile = new File(videoPath);
        RequestBody videoRequest =
                RequestBody.create(MediaType.parse("multipart/form-data"),videoFile);
        MultipartBody.Part videoPart =
                MultipartBody.Part.createFormData(
                        "video",
                        videoFile.getName(),
                        videoRequest
                );

        Call<UploadResponse> call =
                api.submitMessage("3190101918",
                        "wcbup",
                        uploadString,
                        imagePart,
                        videoPart,
                        TOKEN);

        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                if(!response.isSuccessful()){
                    Log.e(TAG, "update fails");
                    Toast.makeText(UploadActivity.this,
                            "上传失败",
                            Toast.LENGTH_SHORT).show();
                }else {
                    Log.d(TAG, "update success");
                    Toast.makeText(UploadActivity.this,
                            "上传成功",
                            Toast.LENGTH_SHORT).show();
                }
                //结束页面
                finish();
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.toString() );
                Toast.makeText(UploadActivity.this,
                        "网络出现问题",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    private void managePhotoReturn(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media
                        .EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri
                    .getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("" +
                                "content:downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri, null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        coverPath = imagePath;

        Glide.with(this)
                .load(imagePath)
                .apply(new RequestOptions().centerCrop())
                .into(imageCover);

    }
    private String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,
                null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore
                        .Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void selectVideo(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),
                PICK_VIDEO_REQUEST);
    }
    private void manageVideoReturn(Intent data){
        Uri videoUri = data.getData();
        String path = VideoUriUtils.getVideoPath(this,videoUri);
        if(path!=null){
            videoPath = path;
            updateCoverPreview();
        }
    }

    private void compressVideo(){
        String compressedVideoPath = null;
        File dir = new File(getCacheDir().toString());
        if(!dir.exists()){
            dir.mkdir();
        }
        try{
            compressedVideoPath
                    = SiliCompressor.with(UploadActivity.this)
                    .compressVideo(videoPath, getCacheDir().toString());
        }catch (Exception e){
            Log.e(TAG, "compressVideo: "+e.toString());
        }
        videoPath = compressedVideoPath;

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "start compress");
//                String compressedVideoPath = null;
//                File dir = new File(getCacheDir().toString());
//                if(!dir.exists()){
//                    dir.mkdir();
//                }
//
//                try{
//                    compressedVideoPath
//                            = SiliCompressor.with(UploadActivity.this)
//                            .compressVideo(videoPath, getCacheDir().toString());
//                }catch (Exception e){
//                    Log.e(TAG, "compressVideo: "+e.toString());
//                }
//                File beforeCompressed = new File(videoPath);
//                File afterCompressed = new File(compressedVideoPath);
//                Log.d(TAG, "before compressed "+beforeCompressed.length());
//                Log.d(TAG, "after compressed "+afterCompressed.length());
//            }
//        }).start();
    }

    private void uploadCompressedVideo(String uploadString){
        new Thread(new Runnable() {
            @Override
            public void run() {
                compressVideo();
                uploadVideo(uploadString);
            }
        }).start();
    }
}