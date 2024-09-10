package com.gelora.absensi.model;

import com.google.gson.annotations.SerializedName;

public class ApprovalAtasanResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("status_approve")
    private String statusApprove;

    @SerializedName("nik")
    private String nik;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusApprove() {
        return statusApprove;
    }

    public ApprovalAtasanResponse( String id, String nik, String statusApprove) {
        this.nik = nik;
        this.id = id;
        this.statusApprove = statusApprove;
    }

    public void setStatusApprove(String statusApprove) {
        this.statusApprove = statusApprove;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }
}
