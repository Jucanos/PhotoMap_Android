package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMapListData {
    @SerializedName("mid")
    @Expose
    private String mid;

    @SerializedName("name")
    @Expose
    private String name;

    public GetMapListData(String mid, String name) {
        this.mid = mid;
        this.name = name;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
