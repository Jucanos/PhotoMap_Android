package com.jucanos.photomap.Structure;

//"uid": 1136574007,
//        "nickname": "문주호",
//        "thumbnail":

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("uid")
    @Expose
    private String uid;

    @SerializedName("nickname")
    @Expose
    private String nickname;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public Data(String uid, String nickname, String thumbnail) {
        this.uid = uid;
        this.nickname = nickname;
        this.thumbnail = thumbnail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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
}
