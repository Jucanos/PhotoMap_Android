package com.jucanos.photomap.ListView;

import java.util.Date;

public class NoticeListViewItem {
    private String context;
    private Date createdAt;
    private String id;
    private String title;
    private Date updatedAt;

    public NoticeListViewItem(String context, Date createdAt, String id, String title, Date updatedAt) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
