package com.example.customview.customview.loginpage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.customview.R;

import androidx.annotation.Nullable;

public class LoginKeyboardView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "LoginKeyboard";
    private TextView mNumView;
    private OnKeyPressListener mOnKeyPressListener = null;

    public LoginKeyboardView(Context context) {
        this(context,null);
    }

    public LoginKeyboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoginKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.num_key_pad, this);

        initView();
    }

    private void initView() {

        TextView number1 = this.findViewById(R.id.number_1);
        number1.setOnClickListener(this);

        TextView number2 = this.findViewById(R.id.number_2);
        number2.setOnClickListener(this);

        TextView number3 = this.findViewById(R.id.number_3);
        number3.setOnClickListener(this);


        TextView number4 = this.findViewById(R.id.number_4);
        number4.setOnClickListener(this);

        TextView number5 = this.findViewById(R.id.number_5);
        number5.setOnClickListener(this);

        TextView number6 = this.findViewById(R.id.number_6);
        number6.setOnClickListener(this);


        TextView number7 = this.findViewById(R.id.number_7);
        number7.setOnClickListener(this);

        TextView number8 = this.findViewById(R.id.number_8);
        number8.setOnClickListener(this);

        TextView number9 = this.findViewById(R.id.number_9);
        number9.setOnClickListener(this);


        TextView number0 = this.findViewById(R.id.number_0);
        number0.setOnClickListener(this);

        ImageView back = this.findViewById(R.id.back);
        back.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();

            if (mOnKeyPressListener == null) {
                return;
            }

            if (viewId == R.id.back) {
                //
                Log.d(TAG,"onClick view onBackPress -> ");
                mOnKeyPressListener.onBackPress();
            } else {
                // 数字
                if (v instanceof TextView) {
                    CharSequence text = ((TextView) v).getText();
                    Log.d(TAG,"onClick view text -> " + text);
                    mOnKeyPressListener.onNumberPress(Integer.parseInt(text.toString()));
                }
            }





//        switch (viewId) {
//
//            case R.id.number_1:
//
//                break;
//
//            case R.id.number_2:
//
//                break;
//
//            case R.id.number_3:
//
//                break;
//
//            case R.id.number_4:
//
//                break;
//
//            case R.id.number_5:
//
//                break;
//
//            case R.id.number_6:
//
//                break;
//
//            case R.id.number_7:
//
//                break;
//
//            case R.id.number_8:
//                break;
//
//            case R.id.number_9:
//
//                break;
//
//            case R.id.number_0:
//
//                break;
//
//            case R.id.back:
//
//                break;
//
//            default:
//                break;
//        }

    }


    public void setOnKeyPressListener(OnKeyPressListener listener){
        this.mOnKeyPressListener = listener;
    }

    public interface OnKeyPressListener{
        void onNumberPress(int number);
        void onBackPress();
    }

}
