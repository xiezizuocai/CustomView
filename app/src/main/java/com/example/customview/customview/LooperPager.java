package com.example.customview.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customview.LooperPagerViewActivity;
import com.example.customview.R;
import com.example.customview.utils.SizeUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class LooperPager extends LinearLayout {


    private static final String TAG = "LooperPager";
    private static final Float INDICATOR_POINT_SIZE = 5.0f;

    private TextView mLooperTitle;
    private LooperViewPager mViewPager;
    private LinearLayout mLooperPointsContainer;
    private BindTitleListener mBindTitleListener = null;
    private InnerAdapter mInnerAdapter = null;
    private static int sDataSize;
    private OnItemClickListener mOnItemClickListener = null;
    private boolean mIsTitleShow;
    private int mPagerShowCount;
    private int mSwitchTime;

    public LooperPager(Context context) {
        this(context,null);
    }

    public LooperPager(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LooperPager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.looper_pager_layout, this, true);

        // 获取属性
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.looper_style);
        // 读取属性
        mIsTitleShow = a.getBoolean(R.styleable.looper_style_is_title_show, true);
        mPagerShowCount = a.getInt(R.styleable.looper_style_show_pager_count, 1);
        mSwitchTime = a.getInt(R.styleable.looper_style_switch_time, -1);

        Log.d(TAG,"zjx LooperPager mIsTitleShow -> " + mIsTitleShow);
        Log.d(TAG,"zjx LooperPager mPagerShowCount -> " + mPagerShowCount);
        Log.d(TAG,"zjx LooperPager mSwitchTime -> " + mSwitchTime);

        a.recycle();


        init();


    }

    private void init() {
        initView();
        initEvent();
    }

    private void initEvent() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 页面切换的一个回调方法

            }

            @Override
            public void onPageSelected(int position) {
                // 页面切换停下来了的回调
                if (mInnerAdapter != null) {
                    int realPosition = position % mInnerAdapter.getDataSize();

                    // 设置标题
                    if (mBindTitleListener != null) {
                        Log.d(TAG,"zjx initEvent onPageSelected -> " + mBindTitleListener.getTitle(realPosition));
                        mLooperTitle.setText(mBindTitleListener.getTitle(realPosition));

                        // 切换指示器焦点
                        updateIndicator();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 页面切换状态改变的回调

            }


        });

        mViewPager.setOnPagerItemClickListener(new LooperViewPager.OnPagerItemClickListener() {
            @Override
            public void onPagerItemClick(int position) {

                Log.d(TAG,"zjx LooperPager.initEvent onPagerItemClick position -> " + position % mInnerAdapter.getDataSize() );

                if (mOnItemClickListener != null && mInnerAdapter != null) {
                    mOnItemClickListener.onItemClick(position % mInnerAdapter.getDataSize());
                }

            }
        });
    }

    private void initView() {
        mLooperTitle = this.findViewById(R.id.looper_title);
        mViewPager = this.findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageMargin(SizeUtils.dip2px(getContext(),20));

        mLooperPointsContainer = this.findViewById(R.id.looper_points_container);

        if (!mIsTitleShow) {
            mLooperTitle.setVisibility(GONE);
        }

        if (mSwitchTime!=-1) {
            mViewPager.setDelayTime(mSwitchTime);
        }


    }


    public void setData(InnerAdapter innerAdapter, BindTitleListener bindTitleListener) {
        this.mBindTitleListener = bindTitleListener;
        mViewPager.setAdapter(innerAdapter);
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 + 1);
        mInnerAdapter = innerAdapter;

        // 设置标题
        if (mBindTitleListener != null && mInnerAdapter != null) {
            mLooperTitle.setText(mBindTitleListener.getTitle(mViewPager.getCurrentItem() % mInnerAdapter.getDataSize()));
        }
        // 根据数据动态创建 指示原点个数 indicator指示器
        updateIndicator();
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }


    public abstract static class InnerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            final int realPosition = position % getDataSize();

            View itemView = getSubView(container,realPosition);

            container.addView(itemView);

            return itemView;
        }



        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }




        public abstract View getSubView(ViewGroup container, int position);

        protected abstract int getDataSize();

    }



    private void updateIndicator() {
        if (mInnerAdapter != null && mBindTitleListener != null) {
            int count = mInnerAdapter.getDataSize();

            mLooperPointsContainer.removeAllViews(); // 设置之前清空

            for (int i = 0; i < count; i++) {
                View point = new View(getContext());

                if (mViewPager.getCurrentItem() % mInnerAdapter.getDataSize() == i) {
                    point.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.shape_circle_red));
//                    point.setBackgroundColor(Color.parseColor("#ff0000"));
                } else {
                    point.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.shape_circle_white));
//                    point.setBackgroundColor(Color.parseColor("#ffffff"));
                }

                // 设置大小
                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(SizeUtils.dip2px(getContext(),INDICATOR_POINT_SIZE),SizeUtils.dip2px(getContext(),INDICATOR_POINT_SIZE));

                layoutParams.setMargins(SizeUtils.dip2px(getContext(),INDICATOR_POINT_SIZE),0,SizeUtils.dip2px(getContext(),INDICATOR_POINT_SIZE),0);

                point.setLayoutParams(layoutParams);

                mLooperPointsContainer.addView(point);

            }
        }

    }




    public interface BindTitleListener {
        String getTitle(int position);
    }


}
