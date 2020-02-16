package com.jucanos.photomap.ListView;

import java.util.Date;

public class GroupListViewItem {
    private String mid;
    private String title;
    private Date updatedAt;
    private Long log;
    private OnlogCb callback;

    public interface OnlogCb{
        void onSetLog(long log);
    }

    public GroupListViewItem( ) {
        mid = "";
        title = "";
        updatedAt = null;
        log = (long)0;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getLog() {
        return log;
    }

    public void setLog(long log) {
        if(callback != null){
            callback.onSetLog(log);
        }
    }

    public void setOnLogCb(OnlogCb callback){
        this.callback = callback;
    }

    public void setInitLog(long log){
        this.log = log;
    }
}