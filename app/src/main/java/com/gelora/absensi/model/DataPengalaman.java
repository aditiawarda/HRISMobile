package com.gelora.absensi.model;


public class DataPengalaman {

    private String id;
    private String NIK;
    private String deskripsi_posisi;
    private String dari_tahun;
    private String sampai_tahun;
    private String status_action;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNIK() {
        return NIK;
    }

    public void setNIK(String NIK) {
        this.NIK = NIK;
    }

    public String getDeskripsi_posisi() {
        return deskripsi_posisi;
    }

    public void setDeskripsi_posisi(String deskripsi_posisi) {
        this.deskripsi_posisi = deskripsi_posisi;
    }

    public String getDari_tahun() {
        return dari_tahun;
    }

    public void setDari_tahun(String dari_tahun) {
        this.dari_tahun = dari_tahun;
    }

    public String getSampai_tahun() {
        return sampai_tahun;
    }

    public void setSampai_tahun(String sampai_tahun) {
        this.sampai_tahun = sampai_tahun;
    }

    public String getStatus_action() {
        return status_action;
    }

    public void setStatus_action(String status_action) {
        this.status_action = status_action;
    }
}
