package com.gelora.absensi.model;


public class ShiftAbsen {

    private String id_shift;
    private String nama_shift;
    private String datang;
    private String pulang;

    public String getId_shift() {
        return id_shift;
    }

    public void setId(String id) {
        this.id_shift = id;
    }

    public String getNama_shift() {
        return nama_shift;
    }

    public void setNama_shift(String nama_shift) {
        this.nama_shift = nama_shift;
    }

    public String getDatang() {
        return datang;
    }

    public void setDatang(String datang) {
        this.datang = datang;
    }

    public String getPulang() {
        return pulang;
    }

    public void setPulang(String pulang) {
        this.pulang = pulang;
    }

}
