package com.example.tictok.Mine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tictok.Camera.CameraActivity;
import com.example.tictok.MainActivity;
import com.example.tictok.R;
import com.example.tictok.Stream.StreamActivity;
import com.example.tictok.Upload.UploadActivity;
import com.google.android.material.tabs.TabLayout;

public class MineActivity extends AppCompatActivity {

    public Button main;
    public Button upload;
    public Button message;
    private ViewPager pager;
    private TabLayout tabLayout;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        setView();

        setlistener();

    }

    public void setView(){

        //添加tablayout和viewpager页面  和相应的三个fragment
        pager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        //找到对应的button
        upload=findViewById(R.id.mine_upload);
        main=findViewById(R.id.mine_main);
        message=findViewById(R.id.mine_message);
        imageButton=findViewById(R.id.mine_camera);
    }

    public void setlistener(){
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //返回第i个页面中的fragment
            @Override
            public Fragment getItem(int position) {
                if(position==2)
                    return new LikeFragment();
                else return new MineFragment(position);
            }

            @Override
            public int getCount() {
                return 3;
            }

            //表示在第i个页面中上面的tab显示的文字
            @Override
            public CharSequence getPageTitle(int position) {
                String s = new String();
                switch (position){
                    case 0:
                        s="作品";
                        break;
                    case 1:
                        s= "私密";
                        break;
                    case 2:
                        s= "喜欢";
                        break;
                }
                return s;
            }
        });
        //将两者combine到一起
        tabLayout.setupWithViewPager(pager);

        //初始化摄像按钮

//        //初始化三个button
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MineActivity.this, StreamActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, UploadActivity.class);
                startActivity(intent);
                finish();
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MineActivity.this,"对不起，此功能暂未开放！",Toast.LENGTH_SHORT).show();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
    }
}