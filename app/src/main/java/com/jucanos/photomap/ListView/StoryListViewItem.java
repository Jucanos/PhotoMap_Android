package com.jucanos.photomap.ListView;

import java.util.ArrayList;

public class StoryListViewItem {
    String thumnail_realPath;
    ArrayList<String> image_realPahts;
    String time_upload;
    String time_edit;
    String description;
    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumnail_realPath() {
        return thumnail_realPath;
    }

    public void setThumnail_realPath(String thumnail_realPath) {
        this.thumnail_realPath = thumnail_realPath;
    }

    public ArrayList<String> getImage_realPahts() {
        return image_realPahts;
    }

    public void setImage_realPahts(ArrayList<String> image_realPahts) {
        this.image_realPahts = image_realPahts;
    }

    public String getTime_upload() {
        return time_upload;
    }

    public void setTime_upload(String time_upload) {
        this.time_upload = time_upload;
    }

    public String getTime_edit() {
        return time_edit;
    }

    public void setTime_edit(String time_edit) {
        this.time_edit = time_edit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
