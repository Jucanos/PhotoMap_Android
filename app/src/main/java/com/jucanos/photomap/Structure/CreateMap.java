package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateMap {
    @SerializedName("data")
    @Expose
    CreateMapData createMapData;

    @SerializedName("message")
    @Expose
    String message;

    public CreateMap(CreateMapData createMapData, String message) {
        this.createMapData = createMapData;
        this.message = message;
    }

    public CreateMapData getCreateMapData() {
        return createMapData;
    }

    public void setCreateMapData(CreateMapData createMapData) {
        this.createMapData = createMapData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
