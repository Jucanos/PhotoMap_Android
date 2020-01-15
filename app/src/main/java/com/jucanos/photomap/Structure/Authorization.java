package com.jucanos.photomap.Structure;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Authorization {
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("message")
    @Expose
    private String message;

    public Authorization(Data data, String message) {
        this.data = data;
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
