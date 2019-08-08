package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Afyad Kafa on 1/28/2019.
 */

public class ProductDetailResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("result")
    @Expose
    private Product product;

    public ProductDetailResponse(String status, String message, Product product) {
        this.status = status;
        this.message = message;
        this.product = product;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Product getProduct() {
        return product;
    }
}
