package com.jucanos.photomap.Structure;

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
