package com.gelora.absensi.model;


public class StatusAbsen {

    private String id_status;
    private String deskripsi_status;
    private String nama_status;

    public String getId_status() {
        return id_status;
    }

    public void setId(String id) {
        this.id_status = id;
    }

    public String getDeskripsi_status() {
        return deskripsi_status;
    }

    public void setDeskripsi_status(String deskripsi_status) {
        this.deskripsi_status = deskripsi_status;
    }

    public String getNama_status() {
        return nama_status;
    }

    public void setNama_status(String nama_status) {
        this.nama_status = nama_status;
    }
}
