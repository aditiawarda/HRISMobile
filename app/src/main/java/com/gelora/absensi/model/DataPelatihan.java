package com.gelora.absensi.model;


public class DataPelatihan {

    private String id;
    private String NIK;
    private String nama_pelatihan;
    private String lembaga_pelatihan;
    private String tahun;
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

    public String getNama_pelatihan() {
        return nama_pelatihan;
    }

    public void setNama_pelatihan(String nama_pelatihan) {
        this.nama_pelatihan = nama_pelatihan;
    }

    public String getLembaga_pelatihan() {
        return lembaga_pelatihan;
    }

    public void setLembaga_pelatihan(String lembaga_pelatihan) {
        this.lembaga_pelatihan = lembaga_pelatihan;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getStatus_action() {
        return status_action;
    }

    public void setStatus_action(String status_action) {
        this.status_action = status_action;
    }
}
