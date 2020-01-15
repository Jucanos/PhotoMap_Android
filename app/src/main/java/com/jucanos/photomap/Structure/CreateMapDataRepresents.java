package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateMapDataRepresents {
    @SerializedName("gyeonggi")
    @Expose
    private String gyeonggi;

    @SerializedName("gangwon")
    @Expose
    private String gangwon;

    @SerializedName("chungbuk")
    @Expose
    private String chungbuk;

    @SerializedName("chungnam")
    @Expose
    private String chungnam;

    @SerializedName("jeonbuk")
    @Expose
    private String jeonbuk;

    @SerializedName("jeonnam")
    @Expose
    private String jeonnam;

    @SerializedName("gyeongbuk")
    @Expose
    private String gyeongbuk;

    @SerializedName("gyeongnam")
    @Expose
    private String gyeongnam;

    @SerializedName("jeju")
    @Expose
    private String jeju;

    public String getGyeonggi() {
        return gyeonggi;
    }

    public void setGyeonggi(String gyeonggi) {
        this.gyeonggi = gyeonggi;
    }

    public String getGangwon() {
        return gangwon;
    }

    public void setGangwon(String gangwon) {
        this.gangwon = gangwon;
    }

    public String getChungbuk() {
        return chungbuk;
    }

    public void setChungbuk(String chungbuk) {
        this.chungbuk = chungbuk;
    }

    public String getChungnam() {
        return chungnam;
    }

    public void setChungnam(String chungnam) {
        this.chungnam = chungnam;
    }

    public String getJeonbuk() {
        return jeonbuk;
    }

    public void setJeonbuk(String jeonbuk) {
        this.jeonbuk = jeonbuk;
    }

    public String getJeonnam() {
        return jeonnam;
    }

    public void setJeonnam(String jeonnam) {
        this.jeonnam = jeonnam;
    }

    public String getGyeongbuk() {
        return gyeongbuk;
    }

    public void setGyeongbuk(String gyeongbuk) {
        this.gyeongbuk = gyeongbuk;
    }

    public String getGyeongnam() {
        return gyeongnam;
    }

    public void setGyeongnam(String gyeongnam) {
        this.gyeongnam = gyeongnam;
    }

    public String getJeju() {
        return jeju;
    }

    public void setJeju(String jeju) {
        this.jeju = jeju;
    }
}
