package com.example.customview;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class LooperPagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private List<Integer> mData = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looper_pager);


        initView();

        initData();
    }

    private void initData() {
        mData.add(R.mipmap.pic0);
        mData.add(R.mipmap.pic1);
        mData.add(R.mipmap.pic2);
        mData.add(R.mipmap.pic3);

        mPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(Integer.MAX_VALUE/2 + 1);   // 初始化设置在中间,并且在第一张图
    }

    private void initView() {
        mViewPager = this.findViewById(R.id.view_pager);
        mViewPager.setAdapter(mPagerAdapter);

    }


    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
//            return mData.size();
            return Integer.MAX_VALUE;  // 非常多的数据，近似无限循环
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_pager, container, false);

            ImageView iv = itemView.findViewById(R.id.cover);

            int realPosition = position % mData.size();

            iv.setImageResource(mData.get(realPosition));

            container.addView(itemView);

            return itemView;
        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    };

}
