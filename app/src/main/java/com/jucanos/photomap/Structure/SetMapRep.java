package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetMapRep {
    @SerializedName("data")
    @Expose
    private String data;

    @SerializedName("message")
    @Expose
    private String message;

    public SetMapRep(String data, String message) {
        this.data = data;
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
