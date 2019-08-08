package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Afyad Kafa on 1/28/2019.
 */

public class Payment {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("jenis_pembayaran")
    @Expose
    private String jenisBayar;

    @SerializedName("deskripsi")
    @Expose
    private String desc;

    public Payment(int id, String jenisBayar, String desc) {
        this.id = id;
        this.jenisBayar = jenisBayar;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public String getJenisBayar() {
        return jenisBayar;
    }

    public String getDesc() {
        return desc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setJenisBayar(String jenisBayar) {
        this.jenisBayar = jenisBayar;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
