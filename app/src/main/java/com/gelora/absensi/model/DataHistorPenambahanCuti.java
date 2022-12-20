package com.gelora.absensi.model;


public class DataHistorPenambahanCuti {

    private String id;
    private String tanggal;
    private String stok_masuk;
    private String keterangan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStok_masuk() {
        return stok_masuk;
    }

    public void setStok_masuk(String stok_masuk) {
        this.stok_masuk = stok_masuk;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

}
