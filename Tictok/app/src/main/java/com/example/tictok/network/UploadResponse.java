package com.example.tictok.network;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("result")
    public MyMessage message;
    @SerializedName("url")
    public String url;

    @SerializedName("success")
    public Boolean success;
}