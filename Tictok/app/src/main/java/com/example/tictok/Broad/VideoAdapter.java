package com.example.tictok.Broad;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.tictok.network.MessageListResponse;
import com.example.tictok.network.MyMessage;
import com.example.tictok.R;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private Context mContext;
    private MessageListResponse messageListResponse;
    private int tpos;
    public OnHeartClickListener onHeartClickListener;

    void setOnHeartClickListener(OnHeartClickListener onHeartClickListener){
        this.onHeartClickListener=onHeartClickListener;
    }

    public VideoAdapter(Context context, MessageListResponse messageListResponse,int tpos) {
        this.mContext = context;
        this.messageListResponse=messageListResponse;
        this.tpos=tpos;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater= LayoutInflater.from(viewGroup.getContext());
        View itemv= layoutInflater.inflate(R.layout.items_onbroad,viewGroup,false);
        return new ViewHolder(itemv);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int pos) {
        holder.Bind(pos);
        onHeartClickListener.click(tpos+pos);

    }

    @Override
    public int getItemCount() {
        return messageListResponse.feeds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mRootView;
        ImageView mThumb;
        ImageView mPlay;
        TextView mTitle;
        TextView mMarquee;
        CusVideoView mVideoView;
        TextView user;

        public ViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView.findViewById(R.id.mRootView);
            mThumb = itemView.findViewById(R.id.mThumb);
            mPlay = itemView.findViewById(R.id.mPlay);
            mVideoView = itemView.findViewById(R.id.mVideoView);
            mTitle = itemView.findViewById(R.id.mTitle);
            mMarquee = itemView.findViewById(R.id.mMarquee);
            user = itemView.findViewById(R.id.User_Name);
        }

        public void Bind(int pos){
            MyMessage mes=messageListResponse.feeds.get(pos+tpos);

            Log.d("VideoAdapter", "Bind: "+tpos+pos);

            //设置第一帧作为封面
//            MediaMetadataRetriever media = new MediaMetadataRetriever();
//            media.setDataSource(mContext,Uri.parse(mes.videoUrl));
//            mThumb.setImageBitmap(media.getFrameAtTime());
            Glide.with(mContext)
                    .load(mes.imageUrl)
                    .placeholder(R.drawable.loading)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(80)))
                    .transition(withCrossFade())
                    .into(mThumb);

            Log.d("VideoAdapter", "Bind: "+mes.videoUrl);
            //设置相关参数
            mVideoView.setVideoURI(Uri.parse(mes.videoUrl));
            user.setText(mes.userName);
            mTitle.setText(mes.extraValue);
            mMarquee.setText("好听的音乐，就在tictic，动人的歌声，还在tictic，本project由芦宽和卢凯炫共同完成");
            mMarquee.setSelected(true);
        }
    }

}
