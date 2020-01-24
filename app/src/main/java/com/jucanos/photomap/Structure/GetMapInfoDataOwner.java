package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMapInfoDataOwner {
    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("uid")
    @Expose
    private String uid;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
