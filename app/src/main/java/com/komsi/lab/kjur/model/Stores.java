package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stores {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("toko")
    @Expose
    private String storeName;

    public Stores(String id, String storeName) {
        this.id = id;
        this.storeName = storeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
