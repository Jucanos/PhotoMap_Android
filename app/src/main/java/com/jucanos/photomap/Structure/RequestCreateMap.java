package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestCreateMap {
    String name;

    public RequestCreateMap(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
