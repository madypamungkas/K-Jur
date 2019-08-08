package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCartResponse {
    @SerializedName("status")
    @Expose
    private String status;

    public AddCartResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
