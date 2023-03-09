package com.gelora.absensi.model;


public class DataKeluarga {

    private String id;
    private String NIK;
    private String nama;
    private String hubungan;
    private String hubungan_lainnya;

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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

}
