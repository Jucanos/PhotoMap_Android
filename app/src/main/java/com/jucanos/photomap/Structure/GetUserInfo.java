package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserInfo {
    @SerializedName("data")
    @Expose
    private GetUserInfoData userData;
    @SerializedName("message")
    @Expose
    private String message;

    public GetUserInfo(GetUserInfoData userData, String message) {
        this.userData = userData;
        this.message = message;
    }

    public GetUserInfoData getUserData() {
        return userData;
    }

    public void setData(GetUserInfoData userData) {
        this.userData = userData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
