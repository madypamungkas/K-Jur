package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StoresListResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("barang_jual")
    @Expose
    private ArrayList<Stores> stores = null;

    public StoresListResponse(String status, ArrayList<Stores> stores) {
        this.status = status;
        this.stores = stores;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Stores> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Stores> stores) {
        this.stores = stores;
    }
}
