package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Afyad Kafa on 1/28/2019.
 */

public class ProductListResponse {
    @SerializedName("barang_jual")
    @Expose
    private ArrayList<Product> product = null;

    public ProductListResponse(ArrayList<Product> product) {
        this.product = product;
    }

    public ArrayList<Product> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<Product> product) {
        this.product = product;
    }
}
