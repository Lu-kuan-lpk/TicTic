package com.example.tictok.network;

public interface PullEndListener{
    public void Finish();
    public void DataReturn(MessageListResponse messageListResponse);
}