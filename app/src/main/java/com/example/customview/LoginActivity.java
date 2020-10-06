package com.example.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.customview.customview.loginpage.LoginPageView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private LoginPageView mLoginPageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 不要标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        mLoginPageView = this.findViewById(R.id.login_page_view);
        mLoginPageView.setLoginPageActionListener(new LoginPageView.OnLoginPageActionListener() {
            @Override
            public void onGetVerifyCodeClick(String num) {
                Log.d(TAG,"onGetVerifyCodeClick phoneNum -> " + num);
                Toast.makeText(LoginActivity.this,"验证码已发送",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onOpenAgreementClick() {
                Log.d(TAG,"onOpenAgreementClick -> " );
            }

            @Override
            public void onConfirmClick(String verify, String phoneNum) {
                Log.d(TAG,"onConfirmClick verify -> " + verify);
                Log.d(TAG,"onConfirmClick phoneNum -> " + phoneNum);

                App.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 1.给个toast提示
                        Toast.makeText(LoginActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                        mLoginPageView.onVerifyCodeError();
                    }
                },3000);
            }
        });
    }
}
