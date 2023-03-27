package com.gelora.absensi.model;


public class DataKontakDarurat {

    private String id;
    private String NIK;
    private String notelp;
    private String nama_kontak;
    private String hubungan;
    private String hubungan_lainnya;
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

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }

    public String getNama_kontak() {
        return nama_kontak;
    }

    public void setNama_kontak(String nama_kontak) {
        this.nama_kontak = nama_kontak;
    }

    public String getHubungan() {
        return hubungan;
    }

    public void setHubungan(String hubungan) {
        this.hubungan = hubungan;
    }

    public String getHubungan_lainnya() {
        return hubungan_lainnya;
    }

    public void setHubungan_lainnya(String hubungan_lainnya) {
        this.hubungan_lainnya = hubungan_lainnya;
    }

    public String getStatus_action() {
        return status_action;
    }

    public void setStatus_action(String status_action) {
        this.status_action = status_action;
    }
}
