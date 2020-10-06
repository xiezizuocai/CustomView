package com.example.customview;

import android.os.Bundle;
import android.util.Log;

import com.example.customview.adapters.MoreTypeAdapter;
import com.example.customview.beans.MoreTypeBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MoreTypeActivity extends AppCompatActivity {


    private static final String TAG = "MoreTypeActivity";
    private RecyclerView mRecyclerView;
    private List<MoreTypeBean> mDatas;
    private MoreTypeAdapter mMoreTypeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_type);

        mRecyclerView = this.findViewById(R.id.more_type_list);

        // 准备数据
        mDatas = new ArrayList<>();
        initDatas();

        // 创建设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        // 创建设置适配器
        mMoreTypeAdapter = new MoreTypeAdapter(mDatas);
        mRecyclerView.setAdapter(mMoreTypeAdapter);

    }

    private void initDatas() {
        Random random = new Random();

        for (int i = 0; i < 50; i++) {
            MoreTypeBean moreTypeBean = new MoreTypeBean();

            moreTypeBean.pic = 0;
            moreTypeBean.type = random.nextInt(3);

            Log.d(TAG,"onCreate moreTypeBean.type -> " + moreTypeBean.type);

            mDatas.add(moreTypeBean);
        }
    }
}
