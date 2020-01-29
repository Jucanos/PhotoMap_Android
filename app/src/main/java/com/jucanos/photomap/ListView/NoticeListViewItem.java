package com.jucanos.photomap.ListView;

public class NoticeListViewItem {
    private String context;
    private String createdAt;
    private String id;
    private String title;
    private String updatedAt;

    public NoticeListViewItem(String context, String createdAt, String id, String title, String updatedAt) {
        this.context = context;
        this.createdAt = createdAt;
        this.id = id;
        this.title = title;
        this.updatedAt = updatedAt;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
