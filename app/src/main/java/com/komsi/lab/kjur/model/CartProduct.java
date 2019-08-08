package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartProduct {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("pembeli_id")
    @Expose
    private String pembeliId;

    @SerializedName("stok_barang_id")
    @Expose
    private String stokBarangId;

    @SerializedName("kuantitas")
    @Expose
    private int quantity;

    @SerializedName("barang")
    @Expose
    private String nameProduct;

    @SerializedName("harga")
    @Expose
    private int priceProduct;

    @SerializedName("foto")
    @Expose
    private String picProduct;

    @SerializedName("lokasi")
    @Expose
    private String locProduct;

    @SerializedName("stok")
    @Expose
    private int stockPoduct;

    @SerializedName("status")
    @Expose
    private String statusProduct;

    @SerializedName("toko")
    @Expose
    private String storeNameProduct;

    public CartProduct(int id, String pembeliId, String stokBarangId, int quantity, String nameProduct, int priceProduct, String picProduct, String locProduct, int stockPoduct, String statusProduct, String storeNameProduct) {
        this.id = id;
        this.pembeliId = pembeliId;
        this.stokBarangId = stokBarangId;
        this.quantity = quantity;
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
        this.picProduct = picProduct;
        this.locProduct = locProduct;
        this.stockPoduct = stockPoduct;
        this.statusProduct = statusProduct;
        this.storeNameProduct = storeNameProduct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPembeliId() {
        return pembeliId;
    }

    public void setPembeliId(String pembeliId) {
        this.pembeliId = pembeliId;
    }

    public String getStokBarangId() {
        return stokBarangId;
    }

    public void setStokBarangId(String stokBarangId) {
        this.stokBarangId = stokBarangId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(int priceProduct) {
        this.priceProduct = priceProduct;
    }

    public String getPicProduct() {
        return picProduct;
    }

    public void setPicProduct(String picProduct) {
        this.picProduct = picProduct;
    }

    public String getLocProduct() {
        return locProduct;
    }

    public void setLocProduct(String locProduct) {
        this.locProduct = locProduct;
    }

    public int getStockPoduct() {
        return stockPoduct;
    }

    public void setStockPoduct(int stockPoduct) {
        this.stockPoduct = stockPoduct;
    }

    public String getStatusProduct() {
        return statusProduct;
    }

    public void setStatusProduct(String statusProduct) {
        this.statusProduct = statusProduct;
    }

    public String getStoreNameProduct() {
        return storeNameProduct;
    }

    public void setStoreNameProduct(String storeNameProduct) {
        this.storeNameProduct = storeNameProduct;
    }
}
