package com.example.customview.customview.inputnumberview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.customview.R;

public class InputNumberView extends RelativeLayout {


    private static final String TAG = "InputNumberView";

    private int mCurrentNumber = 0;
    private EditText mValueEdt;
    private View mMinusBtn;
    private View mPlusBtn;
    private OnNumberValueChangeListener mOnNumberValueChangeListener = null;
    private int mMax;
    private int mMin;
    private int mDefaultValue;
    private int mStep;
    private boolean mDisable;
    private int mBtnBgRes;


    public InputNumberView(Context context) {
        this(context,null);
    }

    public InputNumberView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public InputNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs);
        initView(context);

        // 处理事件
        setEvent();

    }

    private void initAttrs(Context context, AttributeSet attrs) {
        // 获取相关属性
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.InputNumberView);

        mMax = a.getInt(R.styleable.InputNumberView_max, 0);
        mMin = a.getInt(R.styleable.InputNumberView_min, 0);
        mDefaultValue = a.getInt(R.styleable.InputNumberView_defaultValue, 0);

        this.mCurrentNumber = mDefaultValue;


        mStep = a.getInt(R.styleable.InputNumberView_step, 1);
        mDisable = a.getBoolean(R.styleable.InputNumberView_disable, false);
        mBtnBgRes = a.getResourceId(R.styleable.InputNumberView_btnBackground, -1);

        Log.d(TAG,"InputNumberView mMax -> " + mMax);
        Log.d(TAG,"InputNumberView mMin -> " + mMin);
        Log.d(TAG,"InputNumberView mDefaultValue -> " + mDefaultValue);
        Log.d(TAG,"InputNumberView mStep -> " + mStep);
        Log.d(TAG,"InputNumberView mDisable -> " + mDisable);
        Log.d(TAG,"InputNumberView mBtnBgRes -> " + mBtnBgRes);

        a.recycle();
    }

    private void setEvent() {

        mMinusBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlusBtn.setEnabled(true);

                mCurrentNumber -= mStep;
                if (mCurrentNumber <= mMin) {
                    mCurrentNumber = mMin;
                    v.setEnabled(false);
                    if (mOnNumberValueChangeListener != null) {
                        mOnNumberValueChangeListener.onNumberValueMin(mMin);
                    }
                }
                updateText();
            }
        });


        mPlusBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mMinusBtn.setEnabled(true);

                mCurrentNumber += mStep;
                if (mCurrentNumber >= mMax) {
                    mCurrentNumber = mMax;
                    v.setEnabled(false);
                    if (mOnNumberValueChangeListener != null) {
                        mOnNumberValueChangeListener.onNumberValueMax(mMax);
                    }
                }
                updateText();
            }
        });


    }

    private void initView(Context context) {
        // 以下3中代码等价，都是把View添加到当前容器里

        // LayoutInflater.from(context).inflate(R.layout.input_number_view, this, true);
        //
        // LayoutInflater.from(context).inflate(R.layout.input_number_view, this);
        //
        View view = LayoutInflater.from(context).inflate(R.layout.input_number_view, this, false);
        addView(view);
        //

        mMinusBtn = this.findViewById(R.id.minus_btn);
        mValueEdt = this.findViewById(R.id.value_edt);
        mPlusBtn = this.findViewById(R.id.plus_btn);

        // 初始化控件值
        updateText();
        mMinusBtn.setEnabled(!mDisable);
        mPlusBtn.setEnabled(!mDisable);

    }




    /**
     * 更新数据
     */
    private void updateText() {
        mValueEdt.setText(String.valueOf(mCurrentNumber));
        if (mOnNumberValueChangeListener != null) {
            mOnNumberValueChangeListener.onNumberValueChange(mCurrentNumber);
        }
    }


    public void setOnNumberValueChangeListener(OnNumberValueChangeListener listener) {
        mOnNumberValueChangeListener = listener;
    }

    public interface OnNumberValueChangeListener{
        void onNumberValueChange(int value);
        void onNumberValueMax(int maxValue);
        void onNumberValueMin(int minValue);
    }



    public int getNumber() {
        return mCurrentNumber;
    }

    public void setNumber(int value) {
        mCurrentNumber = value;
        updateText();
    }


    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        mMax = max;
    }

    public int getMin() {
        return mMin;
    }

    public void setMin(int min) {
        mMin = min;
    }

    public int getDefaultValue() {
        return mDefaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        mDefaultValue = defaultValue;
        mCurrentNumber = defaultValue;
        updateText();
    }

    public int getStep() {
        return mStep;
    }

    public void setStep(int step) {
        mStep = step;
    }

    public boolean isDisable() {
        return mDisable;
    }

    public void setDisable(boolean disable) {
        mDisable = disable;

        mMinusBtn.setEnabled(!mDisable);
        mPlusBtn.setEnabled(!mDisable);
    }
}
