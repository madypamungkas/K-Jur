package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemoveAllCartResponse {
    @SerializedName("status")
    @Expose
    private String status;

    public RemoveAllCartResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
