package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetMapList {
    @SerializedName("data")
    @Expose
    public ArrayList<GetMapListData> getMapListDatas;

    @SerializedName("message")
    @Expose
    String message;

    public GetMapList(ArrayList<GetMapListData> getMapListDatas, String message) {
        this.getMapListDatas = getMapListDatas;
        this.message = message;
    }

    public ArrayList<GetMapListData> getGetMapListDatas() {
        return getMapListDatas;
    }

    public void setGetMapListDatas(ArrayList<GetMapListData> getMapListDatas) {
        this.getMapListDatas = getMapListDatas;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
