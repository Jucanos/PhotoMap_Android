package com.jucanos.photomap.Structure;

public class CreateMapRequest {
    String name;

    public CreateMapRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
