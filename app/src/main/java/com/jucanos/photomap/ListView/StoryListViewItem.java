package com.jucanos.photomap.ListView;

import java.util.ArrayList;

public class StoryListViewItem {
//    String createdAt = response.body().getGetStoryListItems().get(i).getCreatedAt();
//    String updatedAt = response.body().getGetStoryListItems().get(i).getUpdatedAt();
//    String title = response.body().getGetStoryListItems().get(i).getTitle();
//    String context = response.body().getGetStoryListItems().get(i).getContext();
//    ArrayList<String> files = response.body().getGetStoryListItems().get(i).getFiles();
//    String sid = response.body().getGetStoryListItems().get(i).getSid();
//    String mid = response.body().getGetStoryListItems().get(i).getMid();

    String thumbnail;
    String createdAt;
    String updatedAt;
    String title;
    String context;
    ArrayList<String> files;
    String sid;
    String mid;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
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
}
