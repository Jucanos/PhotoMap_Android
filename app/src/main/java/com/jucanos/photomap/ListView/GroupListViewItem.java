package com.jucanos.photomap.ListView;

import android.graphics.Bitmap;

public class GroupListViewItem {
    private String mid;
    private String title;

    public GroupListViewItem(String mid, String title) {
        this.mid = mid;
        this.title = title;
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
}