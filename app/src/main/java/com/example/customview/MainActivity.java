package com.example.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.customview.customview.inputnumberview.InputNumberView;
import com.example.customview.customview.loginpage.LoginKeyboardView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private InputNumberView mInputNumberView;
    private LoginKeyboardView mLoginKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void toLoginPage(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }


    public void toMoreTypeActivity(View view) {
        Intent intent = new Intent(this,MoreTypeActivity.class);
        startActivity(intent);
    }


    public void toLooperPagerActivity(View view) {
        Intent intent = new Intent(this,LooperPagerActivity.class);
        startActivity(intent);
    }

    public void toLooperPagerViewActivity(View view) {
        Intent intent = new Intent(this,LooperPagerViewActivity.class);
        startActivity(intent);
    }



}
