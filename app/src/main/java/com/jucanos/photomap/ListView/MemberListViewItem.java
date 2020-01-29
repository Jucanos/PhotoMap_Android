package com.jucanos.photomap.ListView;

public class MemberListViewItem {
    private String thumbnail;
    private String name;

    public MemberListViewItem(String thumbnail, String name) {
        this.thumbnail = thumbnail;
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}