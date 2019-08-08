package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Afyad Kafa on 1/28/2019.
 */

public class Location {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("nama")
    @Expose
    private String nama;

    @SerializedName("deskripsi")
    @Expose
    private String desc;

    private int color = -1;

    public Location(int id, String nama, String desc) {
        this.id = id;
        this.nama = nama;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getDesc() {
        return desc;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
