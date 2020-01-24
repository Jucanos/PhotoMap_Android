package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetRep {
    @SerializedName("data")
    @Expose
    SetRepData data;

    @SerializedName("message")
    @Expose
    String message;

    public SetRepData getData() {
        return data;
    }

    public void setData(SetRepData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
