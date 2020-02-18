package com.jucanos.photomap.ListView;

import java.util.Date;

public class GroupListViewItem {
    private String mid;
    private String title;
    private Date updatedAt;
    private Long pastLog;
    private Long curLog;
    private OnlogCb callback;

    public interface OnlogCb {
        void onSetLog(long log, boolean own);
    }

    public GroupListViewItem() {
        mid = "";
        title = "";
        updatedAt = null;
        pastLog = (long) 0;
        curLog = (long) 0;
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

    public void setLog(long log, boolean own) {
        callback.onSetLog(log,own);
    }

    public void setOnLogCb(OnlogCb callback) {
        this.callback = callback;
    }

    public Long getPastLog() {
        return pastLog;
    }

    public void setPastLog(Long pastLog) {
        this.pastLog = pastLog;
    }

    public Long getCurLog() {
        return curLog;
    }

    public void setCurLog(Long curLog) {
        this.curLog = curLog;
    }
}