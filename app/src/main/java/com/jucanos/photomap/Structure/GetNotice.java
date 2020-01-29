package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetNotice {
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private ArrayList<GetNoticeData> getNoticeData;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<GetNoticeData> getGetNoticeData() {
        return getNoticeData;
    }

    public void setGetNoticeData(ArrayList<GetNoticeData> getNoticeData) {
        this.getNoticeData = getNoticeData;
    }
}
