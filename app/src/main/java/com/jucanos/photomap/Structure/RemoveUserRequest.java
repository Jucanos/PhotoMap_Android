package com.jucanos.photomap.Structure;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class RemoveUserRequest {
    @SerializedName("remove")
    @Expose
    String remove;

    public RemoveUserRequest(String remove) {
        this.remove = remove;
    }

    public String getRemove() {
        return remove;
    }

    public void setRemove(String remove) {
        this.remove = remove;
    }
}
