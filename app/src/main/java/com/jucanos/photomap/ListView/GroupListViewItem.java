package com.jucanos.photomap.ListView;

import android.graphics.Bitmap;

public class GroupListViewItem {
    private String mid;
    private String title;
    private Bitmap thumbnail;

    public GroupListViewItem(String mid, String title, Bitmap thumbnail) {
        this.mid = mid;
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}