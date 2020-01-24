package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetMapInfoData {
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    @SerializedName("represents")
    @Expose
    private GetMapInfoDataRepresents getMapInfoDataRepresents;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("mid")
    @Expose
    private String mid;

    @SerializedName("owners")
    @Expose
    ArrayList<GetMapInfoDataOwner> getMapInfoDataOwners;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public GetMapInfoDataRepresents getGetMapInfoDataRepresents() {
        return getMapInfoDataRepresents;
    }

    public void setGetMapInfoDataRepresents(GetMapInfoDataRepresents getMapInfoDataRepresents) {
        this.getMapInfoDataRepresents = getMapInfoDataRepresents;
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

    public ArrayList<GetMapInfoDataOwner> getGetMapInfoDataOwners() {
        return getMapInfoDataOwners;
    }

    public void setGetMapInfoDataOwners(ArrayList<GetMapInfoDataOwner> getMapInfoDataOwners) {
        this.getMapInfoDataOwners = getMapInfoDataOwners;
    }
}
