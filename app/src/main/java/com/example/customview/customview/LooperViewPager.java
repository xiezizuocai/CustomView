package com.example.customview.customview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class LooperViewPager extends ViewPager {

    private static final String TAG = "LooperViewPager";

    private long delayTime = 1000;

    private boolean isClick = false;
    private long downTime;
    private float downX;
    private float downY;

    private OnPagerItemClickListener mOnPagerItemClickListener = null;

    private Handler mHandler;

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);

//            mHandler.postDelayed(this,1000);
            postDelayed(this,delayTime);
        }
    };

    public LooperViewPager(@NonNull Context context) {
        this(context,null);
    }

    public LooperViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

//        mHandler = new Handler(Looper.getMainLooper());

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        downTime = System.currentTimeMillis();
                        downX = event.getX();
                        downY = event.getY();

                        stopLoop();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:

                        float dx = Math.abs(event.getX() - downX);
                        float dy = Math.abs(event.getY() - downY);
                        long dTime = System.currentTimeMillis() - downTime;

                        isClick = (dx<=5 && dy<=5 && dTime<=1000);

                        Log.d(TAG,"zjx LooperViewPager onTouch() ACTION_UP ---> ");
                        Log.d(TAG,"zjx LooperViewPager onTouch() ACTION_UP  isClick ---> " + isClick);

                        if (isClick && mOnPagerItemClickListener != null) {
                            mOnPagerItemClickListener.onPagerItemClick(getCurrentItem());

                            Log.d(TAG,"zjx LooperViewPager getCurrentItem() position -> " + getCurrentItem() % 4);

                        }

//                        if(dx<=5 && dy<=5 && dTime<=1000) {
//                            isClick = true;
//                        } else {
//                            isClick = false;
//                        }

                        startLoop();
                        break;
                }

                return false;
            }
        });

    }




    public void setOnPagerItemClickListener(OnPagerItemClickListener listener) {
        this.mOnPagerItemClickListener = listener;
    }

    public interface OnPagerItemClickListener{
        void onPagerItemClick(int position);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG,"onAttachedToWindow...");
        startLoop();
    }


    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    private void startLoop() {
        removeCallbacks(mTask);
//        mHandler.postDelayed(mTask,1000);
        postDelayed(mTask,delayTime);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG,"onDetachedFromWindow...");
        stopLoop();
    }

    private void stopLoop() {
//        mHandler.removeCallbacks(mTask);
        removeCallbacks(mTask);
    }
}
