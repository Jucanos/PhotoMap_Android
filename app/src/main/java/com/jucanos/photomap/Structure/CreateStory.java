package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CreateStory {
    @SerializedName("data")
    @Expose
    CreateStoryData createStoryData;

    @SerializedName("message")
    @Expose
    String message;

    public CreateStory(CreateStoryData createStoryData, String message) {
        this.createStoryData = createStoryData;
        this.message = message;
    }

    public CreateStoryData getCreateStoryData() {
        return createStoryData;
    }

    public void setCreateStoryData(CreateStoryData createStoryData) {
        this.createStoryData = createStoryData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}

