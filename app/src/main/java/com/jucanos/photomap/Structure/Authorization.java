package com.jucanos.photomap.Structure;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Authorization {
    @SerializedName("data")
    @Expose
    private UserData userData;
    @SerializedName("message")
    @Expose
    private String message;

    public Authorization(UserData userData, String message) {
        this.userData = userData;
        this.message = message;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setData(UserData userData) {
        this.userData = userData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
