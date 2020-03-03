package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetStoryList {
    @SerializedName("data")
    @Expose
    ArrayList<GetStoryListData> getStoryListItems;

    @SerializedName("message")
    @Expose
    String message;

    public ArrayList<GetStoryListData> getGetStoryListItems() {
        return getStoryListItems;
    }

    public void setGetStoryListItems(ArrayList<GetStoryListData> getStoryListItems) {
        this.getStoryListItems = getStoryListItems;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
