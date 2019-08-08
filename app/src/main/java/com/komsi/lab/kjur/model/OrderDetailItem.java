package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailItem {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("stok_barang_id")
    @Expose
    private String stokBarangId;

    @SerializedName("transaksi_id")
    @Expose
    private String transaksiId;

    @SerializedName("kuantitas")
    @Expose
    private int qty;

    @SerializedName("total_bayar")
    @Expose
    private int totalBayar;

    @SerializedName("barang")
    @Expose
    private String productName;

    @SerializedName("harga")
    @Expose
    private String productPrice;

    @SerializedName("created_at")
    @Expose
    private String dateCreate;

    @SerializedName("updated_at")
    @Expose
    private String dateUpdate;

    @SerializedName("lokasi")
    @Expose
    private String productLoc;

    @SerializedName("stok")
    @Expose
    private String productStock;

    @SerializedName("toko")
    @Expose
    private String productStoreName;

    public OrderDetailItem(String id, String stokBarangId, String transaksiId, int qty, int totalBayar, String productName, String productPrice, String dateCreate, String dateUpdate, String productLoc, String productStock, String productStoreName) {
        this.id = id;
        this.stokBarangId = stokBarangId;
        this.transaksiId = transaksiId;
        this.qty = qty;
        this.totalBayar = totalBayar;
        this.productName = productName;
        this.productPrice = productPrice;
        this.dateCreate = dateCreate;
        this.dateUpdate = dateUpdate;
        this.productLoc = productLoc;
        this.productStock = productStock;
        this.productStoreName = productStoreName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStokBarangId() {
        return stokBarangId;
    }

    public void setStokBarangId(String stokBarangId) {
        this.stokBarangId = stokBarangId;
    }

    public String getTransaksiId() {
        return transaksiId;
    }

    public void setTransaksiId(String transaksiId) {
        this.transaksiId = transaksiId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getTotalBayar() {
        return totalBayar;
    }

    public void setTotalBayar(int totalBayar) {
        this.totalBayar = totalBayar;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getProductLoc() {
        return productLoc;
    }

    public void setProductLoc(String productLoc) {
        this.productLoc = productLoc;
    }

    public String getProductStock() {
        return productStock;
    }

    public void setProductStock(String productStock) {
        this.productStock = productStock;
    }

    public String getProductStoreName() {
        return productStoreName;
    }

    public void setProductStoreName(String productStoreName) {
        this.productStoreName = productStoreName;
    }
}
