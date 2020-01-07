package com.jucanos.photomap.ListView;

import android.graphics.Bitmap;

public class MemberListViewItem {
    private Bitmap thumbnail;
    private String name;

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setNmae(String title) {
        this.name = title;
    }
}