package com.gelora.absensi.model;

import com.google.gson.annotations.SerializedName;
public class ApprovalSatpamResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("status_approve_satpam")
    private String statusApproveSatpam;

    @SerializedName("nik")
    private String nik;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusApproveSatpam() {
        return statusApproveSatpam;
    }

    public void setStatusApproveSatpam(String statusApproveSatpam) {
        this.statusApproveSatpam = statusApproveSatpam;
    }


    public ApprovalSatpamResponse( String id, String nik, String statusApproveSatpam) {
        this.nik = nik;
        this.id = id;
        this.statusApproveSatpam = statusApproveSatpam;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

}
