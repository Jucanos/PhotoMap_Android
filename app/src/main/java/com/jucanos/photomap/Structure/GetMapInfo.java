package com.jucanos.photomap.Structure;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class GetMapInfo {
    @SerializedName("data")
    @Expose
    private GetMapInfoData data;

    @SerializedName("message")
    @Expose
    private String message;

    public GetMapInfoData getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
