package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetMapRepRequest {
    @SerializedName("remove")
    @Expose
    private String remove;

    public SetMapRepRequest(String remove) {
        this.remove = remove;
    }

    public String getRemove() {
        return remove;
    }

    public void setRemove(String remove) {
        this.remove = remove;
    }
}
