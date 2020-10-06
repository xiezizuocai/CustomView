package com.example.customview.customview.loginpage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customview.App;
import com.example.customview.R;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * 点击获取验证码，条件：手机号码是正确
 *
 * 点击登录-> 条件： 手机号 + 验证码 + 同意
 *
 */



public class LoginPageView extends FrameLayout {

    private static final String TAG = "LoginPageView";

    //手机号码的规则
    public static final String REGEX_MOBILE_EXACT =
            "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";


    private static final int SIZE_VERIFY_CODE_DEFAULT = 4;

    private int mColor;
    private int mVerifyCodeSize = SIZE_VERIFY_CODE_DEFAULT;
    private CheckBox mIsConfirm;
    private EditText mVerifyCodeInput;
    private OnLoginPageActionListener mOnLoginPageActionListener = null;
    private TextView mGetVerifyCodeBtn;
    private TextView mLoginBtn;
    private EditText mPhoneEditText;
    private LoginKeyboardView mLoginKeyboardView;


    private boolean isPhoneNumOk = false;
    private boolean isAgreementOk = false;
    private boolean isVerifyCodeOk = false;



    /**
     * 获取验证码倒计时：
     * ->
     * 倒计时多长时间；
     * 时间间隔： 1s、...
     * 通知UI更新
     */

    private static final int DURATION_COUNT_DOWN = 60 * 1000;
    private static final int DURATION_D_TIME = 1000;

    // 毫秒ms
    private int mCountDownDuration = DURATION_COUNT_DOWN;

    // 毫秒ms
    private int mDTime = DURATION_D_TIME;
    private Handler mHandler;
    private int mRest = mCountDownDuration;

    private boolean isCountDowning = false;
    private CountDownTimer mCountDownTimer;
    private TextView mCheckText;

    public void startCountDown() {
        mHandler = App.getHandler();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mRest = mRest - mDTime;
                Log.d(TAG,"startCountDown rest -> " + mRest);
                if (mRest >0) {

                    isCountDowning = true;

                    mHandler.postDelayed(this,mDTime);

                    // 更新UI
                    mGetVerifyCodeBtn.setText("(" + mRest/1000 + "）秒");
                    mGetVerifyCodeBtn.setEnabled(false);
                } else {

                    mRest = mCountDownDuration;

                    isCountDowning = false;

                    // 更新UI
                    mGetVerifyCodeBtn.setText("获取验证码");
                    mGetVerifyCodeBtn.setEnabled(true);

                    updateAllBtnState();
                }




            }
        });
    }




    private void beginCountDown() {
        isCountDowning = true;
        mGetVerifyCodeBtn.setEnabled(false);
        mCountDownTimer = new CountDownTimer(mCountDownDuration,mDTime) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 更新UI
                mGetVerifyCodeBtn.setText("( " + (int)millisUntilFinished/1000 + " ）秒");
            }

            @Override
            public void onFinish() {
                isCountDowning = false;

                // 更新UI
                mGetVerifyCodeBtn.setText("获取验证码");
                mGetVerifyCodeBtn.setEnabled(true);

                updateAllBtnState();

                mCountDownTimer = null;
            }
        }.start();
    }


    public LoginPageView(@NonNull Context context) {
        this(context,null);
    }

    public LoginPageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoginPageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初始化属性
        initAttrs(context, attrs);

        // 初始化控件
        initView();

        disableEditFocusToKeypad();

        initEvent();


    }

    private void disableEditFocusToKeypad() {

        mPhoneEditText.setShowSoftInputOnFocus(false);
        mVerifyCodeInput.setShowSoftInputOnFocus(false);

    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.login_page_view,this);

        mPhoneEditText = this.findViewById(R.id.phone_edit_text);
        mPhoneEditText.requestFocus();  // 初始化完成，请求焦点

        mIsConfirm = this.findViewById(R.id.report_check_box);
        if (mColor!=-1) {
            mIsConfirm.setTextColor(mColor);
        }

        mVerifyCodeInput = this.findViewById(R.id.verify_code_input_box);
        mVerifyCodeInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mVerifyCodeSize)});

        mGetVerifyCodeBtn = this.findViewById(R.id.get_verify_code_btn);

        mCheckText = this.findViewById(R.id.report_check_text);

        mLoginBtn = this.findViewById(R.id.login_btn);

        mLoginKeyboardView = this.findViewById(R.id.num_key_pad);


        disableCopyAndPaste(mPhoneEditText);
        disableCopyAndPaste(mVerifyCodeInput);


        updateAllBtnState();
    }


    private EditText getFocusEdit() {
        View focus = this.findFocus();
        if (focus instanceof EditText) {
            return (EditText) focus;
        }

        return null;
    }

    private void initEvent() {

        mLoginKeyboardView.setOnKeyPressListener(new LoginKeyboardView.OnKeyPressListener() {
            @Override
            public void onNumberPress(int number) {
                Log.d(TAG,"onNumberPress -----> " + number);
                EditText focusEdit = getFocusEdit();
                if (focusEdit != null) {
                    Editable text = focusEdit.getText();
                    int selectionEnd = focusEdit.getSelectionEnd();
                    text.insert(selectionEnd,String.valueOf(number));
                }
            }

            @Override
            public void onBackPress() {
                Log.d(TAG,"onBackPress -----> " );

                EditText focusEdit = getFocusEdit();
                if (focusEdit != null) {
                    Editable text = focusEdit.getText();
                    int selectionEnd = focusEdit.getSelectionEnd();
                    Log.d(TAG,"onBackPress index -> " + selectionEnd);
                    text.delete(selectionEnd-1,selectionEnd);
                }
            }
        });

        mGetVerifyCodeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOnLoginPageActionListener != null) {
//                    startCountDown();
                    beginCountDown();

                    String phoneNum = mPhoneEditText.getText().toString().trim();
                    mOnLoginPageActionListener.onGetVerifyCodeClick(phoneNum);
                } else {
                    throw new IllegalArgumentException("No set OnLoginPageActionListener!");
                }
            }
        });

        mIsConfirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                isAgreementOk = isChecked;
                updateAllBtnState();


            }
        });

        mCheckText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                updateAllBtnState();
//
//                if (mOnLoginPageActionListener != null) {
//                    if (isAgreementOk) {
//                        mOnLoginPageActionListener.onOpenAgreementClick();
//                    }
//                }
            }
        });

        mLoginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNum = mPhoneEditText.getText().toString().trim();
                String verifyCode = mVerifyCodeInput.getText().toString().trim();

                if (mOnLoginPageActionListener != null) {
                    mOnLoginPageActionListener.onConfirmClick(verifyCode,phoneNum);
                }
            }
        });

        mPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 检查手机号

                String phoneNum = s.toString();
                boolean matches = phoneNum.matches(REGEX_MOBILE_EXACT);

                isPhoneNumOk = phoneNum.length() == 11 && matches;
                updateAllBtnState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mVerifyCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String verifyCode = s.toString();
                isVerifyCodeOk = (verifyCode.length() == mVerifyCodeSize);
                updateAllBtnState();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }




    private void initAttrs(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoginPageView);

        mColor = a.getColor(R.styleable.LoginPageView_mainColor, -1);
        mVerifyCodeSize = a.getInt(R.styleable.LoginPageView_verifyCodeSize, SIZE_VERIFY_CODE_DEFAULT);

        mCountDownDuration = a.getInt(R.styleable.LoginPageView_countDownDuration,DURATION_COUNT_DOWN);

        mDTime = a.getInt(R.styleable.LoginPageView_dTime, DURATION_D_TIME);

        a.recycle();
    }


    /**
     * 验证码错误
     */
    public void onVerifyCodeError() {


        // 2.清空内容
        mVerifyCodeInput.getText().clear();

        // 3.清空倒计时
        if (isCountDowning && mCountDownTimer!=null ) {
            mCountDownTimer.cancel();
            mCountDownTimer.onFinish();
            isCountDowning = false;
//            updateAllBtnState();
        }
    }



    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public int getVerifyCodeSize() {
        return mVerifyCodeSize;
    }

    public void setVerifyCodeSize(int verifyCodeSize) {
        mVerifyCodeSize = verifyCodeSize;
    }

    public int getCountDownDuration() {
        return mCountDownDuration;
    }

    public void setCountDownDuration(int countDownDuration) {
        mCountDownDuration = countDownDuration;
    }

    public int getDTime() {
        return mDTime;
    }

    public void setDTime(int DTime) {
        mDTime = DTime;
    }

    private void updateAllBtnState() {
        if (!isCountDowning) {
            mGetVerifyCodeBtn.setEnabled(isPhoneNumOk);
        }
        mLoginBtn.setEnabled(isPhoneNumOk && isVerifyCodeOk && isAgreementOk);
        mCheckText.setTextColor(isAgreementOk ? getResources().getColor(R.color.mainColor) : getResources().getColor(R.color.mainDeepColor));
    }



    public void setLoginPageActionListener(OnLoginPageActionListener listener){
        this.mOnLoginPageActionListener = listener;
    }

    public interface OnLoginPageActionListener{

        void onGetVerifyCodeClick(String num);
        void onOpenAgreementClick();
        void onConfirmClick(String verify,String phoneNum);
    }


    @SuppressLint("ClickableViewAccessibility")
    public void disableCopyAndPaste(final EditText editText) {
        try {
            if (editText == null) {
                return ;
            }

            editText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
            editText.setLongClickable(false);
            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // setInsertionDisabled when user touches the view
                        setInsertionDisabled(editText);
                    }

                    return false;
                }
            });
            editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setInsertionDisabled(EditText editText) {
        try {
            Field editorField = TextView.class.getDeclaredField("mEditor");
            editorField.setAccessible(true);
            Object editorObject = editorField.get(editText);

            // if this view supports insertion handles
            Class editorClass = Class.forName("android.widget.Editor");
            Field mInsertionControllerEnabledField = editorClass.getDeclaredField("mInsertionControllerEnabled");
            mInsertionControllerEnabledField.setAccessible(true);
            mInsertionControllerEnabledField.set(editorObject, false);

            // if this view supports selection handles
            Field mSelectionControllerEnabledField = editorClass.getDeclaredField("mSelectionControllerEnabled");
            mSelectionControllerEnabledField.setAccessible(true);
            mSelectionControllerEnabledField.set(editorObject, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
