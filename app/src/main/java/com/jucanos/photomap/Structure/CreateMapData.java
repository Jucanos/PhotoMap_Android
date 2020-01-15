package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateMapData {
    @SerializedName("mid")
    @Expose
    String mid;

    @SerializedName("represents")
    @Expose
    CreateMapDataRepresents createMapDataRepresents;

    public CreateMapData(String mid, CreateMapDataRepresents createMapDataRepresents) {
        this.mid = mid;
        this.createMapDataRepresents = createMapDataRepresents;
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
