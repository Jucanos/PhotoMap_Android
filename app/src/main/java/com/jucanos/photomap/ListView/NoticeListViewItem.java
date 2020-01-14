package com.jucanos.photomap.ListView;

public class NoticeListViewItem {
    private String title;
    private String description;

    public NoticeListViewItem() {
    }

    public NoticeListViewItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
