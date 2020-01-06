package com.jucanos.photomap.ListView;

import android.graphics.Bitmap;

public class ListViewItem {
    private Bitmap thumbnail;
    private String title;

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}