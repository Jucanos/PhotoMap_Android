package com.jucanos.photomap.Structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateMapDataRepresents {
    @SerializedName("map_gyeonggi")
    @Expose
    private String gyeonggi;

    @SerializedName("map_gangwon")
    @Expose
    private String gangwon;

    @SerializedName("map_chungbuk")
    @Expose
    private String chungbuk;

    @SerializedName("map_chungnam")
    @Expose
    private String chungnam;

    @SerializedName("map_jeonbuk")
    @Expose
    private String jeonbuk;

    @SerializedName("map_jeonnam")
    @Expose
    private String jeonnam;

    @SerializedName("map_gyeongbuk")
    @Expose
    private String gyeongbuk;

    @SerializedName("map_gyeongnam")
    @Expose
    private String gyeongnam;

    @SerializedName("map_jeju")
    @Expose
    private String jeju;

    public CreateMapDataRepresents(String gyeonggi, String gangwon, String chungbuk, String chungnam, String jeonbuk, String jeonnam, String gyeongbuk, String gyeongnam, String jeju) {
        this.gyeonggi = gyeonggi;
        this.gangwon = gangwon;
        this.chungbuk = chungbuk;
        this.chungnam = chungnam;
        this.jeonbuk = jeonbuk;
        this.jeonnam = jeonnam;
        this.gyeongbuk = gyeongbuk;
        this.gyeongnam = gyeongnam;
        this.jeju = jeju;
    }

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
