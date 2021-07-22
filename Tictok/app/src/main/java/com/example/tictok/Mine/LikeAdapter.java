package com.example.tictok.Mine;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.tictok.network.MessageListResponse;
import com.example.tictok.network.MyMessage;
import com.example.tictok.R;
import com.example.tictok.Stream.BroadClickListener;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.LikeViewHolder> {
    public MessageListResponse tmpmes;
    public Context context;
    private BroadClickListener broadClickListener;


    public void setBroadClickListener(BroadClickListener broadClickListener) {
        this.broadClickListener = broadClickListener;
    }

    public LikeAdapter(Context context, MessageListResponse messageListResponse){
        this.context=context;
        this.tmpmes=messageListResponse;
    }

    @Override
    public LikeAdapter.LikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v= layoutInflater.inflate(R.layout.items_like,parent,false);
        return new LikeViewHolder(v);
    }



    @Override
    public void onBindViewHolder(LikeAdapter.LikeViewHolder holder, int position) {
        holder.onbind(position);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                broadClickListener.jump(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tmpmes.feeds.size();
    }

    public class LikeViewHolder extends RecyclerView.ViewHolder{
        private ImageButton imageButton;
        private View contextview;

        public LikeViewHolder(View view){
            super(view);
            //super(view);
            imageButton=view.findViewById(R.id.like_ib);
            contextview=view;
        }

        public void onbind(int position){
            MyMessage mes = tmpmes.feeds.get(position);
            String imageUrl=mes.imageUrl;
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.loading)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(80)))
                    .transition(withCrossFade())
                    .into(imageButton);
            Log.d("MineAdapter", "onbind: "+imageUrl);

        }
    }

}
