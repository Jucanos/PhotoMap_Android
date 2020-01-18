package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CreateStoryData{
    @SerializedName("mid")
    @Expose
    String mid;

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("context")
    @Expose
    String context;


    @SerializedName("files")
    @Expose
    ArrayList<String> files;

    @SerializedName("sid")
    @Expose
    String sid;

    public CreateStoryData(String mid, String title, String context, ArrayList<String> files, String sid) {
        this.mid = mid;
        this.title = title;
        this.context = context;
        this.files = files;
        this.sid = sid;
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
}