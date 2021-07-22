package com.example.tictok.Stream;

import android.content.Context;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.tictok.network.MessageListResponse;
import com.example.tictok.network.MyMessage;
import com.example.tictok.R;

import java.util.ArrayList;
        import java.util.List;

public class StreamAdapter extends RecyclerView.Adapter<StreamAdapter.ViewHolder> {
    String tryurl = "https://lf1-cdn-tos.bytescm.com/obj/static/ies/bytedance_official_cn/_next/static/images/0-390b5def140dc370854c98b8e82ad394.png";
    String TAG="MyAdapter";
    MessageListResponse messageListResponse;

    private Context context;
    private AdapterView.OnItemClickListener mListen;
    private List<String> list=new ArrayList<>();
    private BroadClickListener broadClickListener;


    public void setBroadClickListener(BroadClickListener broadClickListener) {
        this.broadClickListener = broadClickListener;
    }

    //初始化数据
    public StreamAdapter(MessageListResponse messageListResponse,Context context){
        this.context=context;
        this.messageListResponse=messageListResponse;
    }

    @Override
    public StreamAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.items_stream,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StreamAdapter.ViewHolder holder, int position) {
        holder.onBind(position);
        holder.mimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到对应的视频界面
                broadClickListener.jump(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageListResponse.feeds.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageButton mimagebutton;
        private ImageView id_pic;
        private TextView id;
        private TextView content;
        private View background;

        public ViewHolder(View itemView){
            super(itemView);
            mimagebutton=itemView.findViewById(R.id.stream_iv);
            id_pic= itemView.findViewById(R.id.stream_pic);
            id = itemView.findViewById(R.id.stream_id);
            content = itemView.findViewById(R.id.stream_content);
            background= itemView.findViewById(R.id.stream_back);
        }
        public void onBind(int position){
            MyMessage mes=messageListResponse.feeds.get(position);
            String imageUrl=mes.imageUrl;
            String video_url=mes.videoUrl;
            String Username=mes.userName;
            String sextra=mes.extraValue;

            Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.loading)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(40)))
                        .transition(withCrossFade())
                        .into(mimagebutton);

            content.setText(sextra);
            id.setText(Username);

        }
    }

    public void refresh(){
        notifyDataSetChanged();
    }
}
