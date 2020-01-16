package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestUserRemove {
    @SerializedName("remove")
    @Expose
    String remove;

    public RequestUserRemove(String remove) {
        this.remove = remove;
    }

    public String getRemove() {
        return remove;
    }

    public void setRemove(String remove) {
        this.remove = remove;
    }
}
