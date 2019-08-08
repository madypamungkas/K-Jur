package com.komsi.lab.kjur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Afyad Kafa on 1/14/2019.
 */

public class DetailUser {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("email_verified_at")
    @Expose
    private String emailVerifyAt;

    @SerializedName("no_telepon")
    @Expose
    private String phone;

    @SerializedName("tanggal_lahir")
    @Expose
    private Date birthDate;

    @SerializedName("foto")
    @Expose
    private String pic;

    @SerializedName("saldo")
    @Expose
    private int balance;

    @SerializedName("status_id")
    @Expose
    private int statusId;

    public DetailUser(String id, String name, String email, String emailVerifyAt, String phone, Date birthDate, String pic, int balance, int statusId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.emailVerifyAt = emailVerifyAt;
        this.phone = phone;
        this.birthDate = birthDate;
        this.pic = pic;
        this.balance = balance;
        this.statusId = statusId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getEmailVerifyAt() {
        return emailVerifyAt;
    }

    public String getPhone() {
        return phone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getPic() {
        return pic;
    }

    public int getBalance() {
        return balance;
    }

    public int getStatusId() {
        return statusId;
    }
}
