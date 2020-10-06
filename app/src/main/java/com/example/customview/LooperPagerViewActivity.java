package com.example.customview;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.customview.beans.LooperPagerItem;
import com.example.customview.customview.LooperPager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LooperPagerViewActivity extends AppCompatActivity {

    private static final String TAG = "LooperPagerViewActivity";
    private List<LooperPagerItem> mData = new ArrayList<>();

    private LooperPager mLooperPager;
    private LooperPager.InnerAdapter mLooperPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looper_pager_view);
        initData();
        initView();
        initEvent();
    }



    private void initData() {
        mData.add(new LooperPagerItem("第1张图片",R.mipmap.pic0));
        mData.add(new LooperPagerItem("第2张图片",R.mipmap.pic1));
        mData.add(new LooperPagerItem("第3张图片",R.mipmap.pic2));
        mData.add(new LooperPagerItem("第4张图片",R.mipmap.pic3));
    }


    private void initView() {
        mLooperPager = this.findViewById(R.id.looper_Pager);

        mLooperPagerAdapter = new LooperPager.InnerAdapter() {

            @Override
            protected int getDataSize() {
                return mData.size();
            }

            @Override
            public View getSubView(ViewGroup container, int position) {

                ImageView imageView = new ImageView(container.getContext());
                imageView.setImageResource(mData.get(position).getPicResId());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                return imageView;
            }
        };

        mLooperPager.setData(mLooperPagerAdapter, new LooperPager.BindTitleListener() {
            @Override
            public String getTitle(int position) {
                Log.d(TAG,"zjx LooperPager.BindTitleListener getTitle -> " + mData.get(position).getTitle());
                return mData.get(position).getTitle();
            }
        });

    }

    private void initEvent() {
        mLooperPager.setOnItemClickListener(new LooperPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG,"zjx LooperPager.initEvent onItemClick position -> " + (position + 1) );

                Toast.makeText(LooperPagerViewActivity.this,"点击了第" + (position + 1) + "个Item !",Toast.LENGTH_SHORT).show();

            }
        });
    }


}
