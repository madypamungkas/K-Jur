package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("barang_jual_id")
    @Expose
    private String barangJualId;

    @SerializedName("toko")
    @Expose
    private String storeProduct;

    @SerializedName("barang")
    @Expose
    private String nameProduct;

    @SerializedName("harga")
    @Expose
    private int priceProduct;

    @SerializedName("deskripsi")
    @Expose
    private String descProduct;

    @SerializedName("foto")
    @Expose
    private String picProduct;

    @SerializedName("stok")
    @Expose
    private int stockProduct;

    @SerializedName("lokasi")
    @Expose
    private String locProduct;

    public Product(String id, String barangJualId, String storeProduct, String nameProduct, int priceProduct, String descProduct, String picProduct, int stockProduct, String locProduct) {
        this.id = id;
        this.barangJualId = barangJualId;
        this.storeProduct = storeProduct;
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
        this.descProduct = descProduct;
        this.picProduct = picProduct;
        this.stockProduct = stockProduct;
        this.locProduct = locProduct;
    }

    public String getId() {
        return id;
    }

    public String getBarangJualId() {
        return barangJualId;
    }

    public String getStoreProduct() {
        return storeProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public int getPriceProduct() {
        return priceProduct;
    }

    public String getDescProduct() {
        return descProduct;
    }

    public String getPicProduct() {
        return picProduct;
    }

    public int getStockProduct() {
        return stockProduct;
    }

    public String getLocProduct() {
        return locProduct;
    }
}
