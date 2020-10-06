package com.example.customview.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.example.customview.R;
import com.example.customview.beans.MoreTypeBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MoreTypeAdapter extends RecyclerView.Adapter {


    // 三个常量，三中类型
    public static final int TYPE_FULL_IMAGE = 0;
    public static final int TYPE_RIGHT_IMAGE = 1;
    public static final int TYPE_THREE_IMAGE = 2;

    private List<MoreTypeBean> mData;


    public MoreTypeAdapter(List<MoreTypeBean> data) {
        this.mData = data;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;

        // 创建ViewHolder
        switch (viewType) {
            case TYPE_FULL_IMAGE:
                view = View.inflate(parent.getContext(), R.layout.item_type_full_image,null);
                return new FullImageHolder(view);

            case TYPE_RIGHT_IMAGE:
                view = View.inflate(parent.getContext(),R.layout.item_type_right_image,null);
                return new RightImageHolder(view);

            case TYPE_THREE_IMAGE:
                view = View.inflate(parent.getContext(),R.layout.item_type_three_image,null);
                return new ThreeImageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }


    // 复写一个方法，这个方法根据条件来返回条目类型


    @Override
    public int getItemViewType(int position) {

        MoreTypeBean moreTypeBean = mData.get(position);
        int type = moreTypeBean.type;

        switch (type) {
            case 0:
                return TYPE_FULL_IMAGE;
            case 1:
                return TYPE_RIGHT_IMAGE;
            case 2:
                return TYPE_THREE_IMAGE;
        }

        return TYPE_THREE_IMAGE;
    }


    private class FullImageHolder extends RecyclerView.ViewHolder{

        public FullImageHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class RightImageHolder extends RecyclerView.ViewHolder{

        public RightImageHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class ThreeImageHolder extends RecyclerView.ViewHolder{

        public ThreeImageHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
