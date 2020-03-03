package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateMapData {
    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("mid")
    @Expose
    String mid;

    @SerializedName("represents")
    @Expose
    CreateMapDataRepresents createMapDataRepresents;

    public CreateMapData(String name, String mid, CreateMapDataRepresents createMapDataRepresents) {
        this.name = name;
        this.mid = mid;
        this.createMapDataRepresents = createMapDataRepresents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public CreateMapDataRepresents getCreateMapDataRepresents() {
        return createMapDataRepresents;
    }

    public void setCreateMapDataRepresents(CreateMapDataRepresents createMapDataRepresents) {
        this.createMapDataRepresents = createMapDataRepresents;
    }
}
