package com.example.tictok.network;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MyMessage {

    @SerializedName("_id")
    public String Id;
    @SerializedName("student_id")
    public String studentId;
    @SerializedName("user_name")
    public String userName;
    @SerializedName("extra_value")
    public String extraValue;
    @SerializedName("video_url")
    public String videoUrl;
    @SerializedName("image_url")
    public String imageUrl;
    @SerializedName("image_w")
    public int imageWidth;
    @SerializedName("image_h")
    public int imageHeight;
    @SerializedName("createdAt")
    public Date createAt;
    @SerializedName("updatedAt")
    public Date updateAt;
}