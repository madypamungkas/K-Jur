package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CartListResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("result")
    @Expose
    private ArrayList<CartProduct> cartProduct = null;

    public CartListResponse(String status, ArrayList<CartProduct> cartProduct) {
        this.status = status;
        this.cartProduct = cartProduct;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<CartProduct> getCartProduct() {
        return cartProduct;
    }

    public void setCartProduct(ArrayList<CartProduct> cartProduct) {
        this.cartProduct = cartProduct;
    }
}
