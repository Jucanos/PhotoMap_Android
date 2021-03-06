package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class GetStoryListData {
    @SerializedName("createdAt")
    @Expose
    private Date createdAt;

    @SerializedName("updatedAt")
    @Expose
    private Date updatedAt;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("context")
    @Expose
    private String context;

    @SerializedName("files")
    @Expose
    private ArrayList<String> files;

    @SerializedName("sid")
    @Expose
    private String sid;

    @SerializedName("mid")
    @Expose
    private String mid;

    @SerializedName("creator")
    @Expose
    private String creator;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
