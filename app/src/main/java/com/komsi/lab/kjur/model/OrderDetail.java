package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("pembeli_id")
    @Expose
    private String pembeliId;

    @SerializedName("jenis_pembayaran_id")
    @Expose
    private int jenisBayarId;

    @SerializedName("jumlah_bayar")
    @Expose
    private int jumlahBayar;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("created_at")
    @Expose
    private String dateCreate;

    @SerializedName("updated_at")
    @Expose
    private String dateUpdate;

    @SerializedName("jenis_pembayaran")
    @Expose
    private String jenisBayar;

    public OrderDetail(String id, String pembeliId, int jenisBayarId, int jumlahBayar, String status, String dateCreate, String dateUpdate, String jenisBayar) {
        this.id = id;
        this.pembeliId = pembeliId;
        this.jenisBayarId = jenisBayarId;
        this.jumlahBayar = jumlahBayar;
        this.status = status;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.jenisBayar = jenisBayar;
    }

    public String getId() {
        return id;
    }

    public String getPembeliId() {
        return pembeliId;
    }

    public int getJenisBayarId() {
        return jenisBayarId;
    }

    public int getJumlahBayar() {
        return jumlahBayar;
    }

    public String getStatus() {
        return status;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public String getJenisBayar() {
        return jenisBayar;
    }
}
