package com.gelora.absensi.model;


public class DataFormSDM {

    private String id;
    private String keterangan;
    private String status_approve_kabag;
    private String status_approve_kadept;
    private String status_approve_direktur;
    private String created_at;
    private String updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getStatus_approve_kabag() {
        return status_approve_kabag;
    }

    public void setStatus_approve_kabag(String status_approve_kabag) {
        this.status_approve_kabag = status_approve_kabag;
    }

    public String getStatus_approve_kadept() {
        return status_approve_kadept;
    }

    public void setStatus_approve_kadept(String status_approve_kadept) {
        this.status_approve_kadept = status_approve_kadept;
    }

    public String getStatus_approve_direktur() {
        return status_approve_direktur;
    }

    public void setStatus_approve_direktur(String status_approve_direktur) {
        this.status_approve_direktur = status_approve_direktur;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

}
