package com.gelora.absensi.model;


public class KategoriIzin {

    private String id;
    private String deskripsi;
    private String kode;
    private String tipe;
    private String status_lampiran;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getStatus_lampiran() {
        return status_lampiran;
    }

    public void setStatus_lampiran(String status_lampiran) {
        this.status_lampiran = status_lampiran;
    }

}