package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemoveUser {
    @SerializedName("data")
    @Expose
    String data;

    @SerializedName("message")
    @Expose
    String message;

    public RemoveUser(String data, String message) {
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
