package com.example.customview.beans;

public class LooperPagerItem {

    private String title;

    private Integer picResId;


    public LooperPagerItem(String title, Integer picResId) {
        this.title = title;
        this.picResId = picResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPicResId() {
        return picResId;
    }

    public void setPicResId(Integer picResId) {
        this.picResId = picResId;
    }

    @Override
    public String toString() {
        return "LooperPagerItem{" +
                "title='" + title + '\'' +
                ", picResId=" + picResId +
                '}';
    }
}
