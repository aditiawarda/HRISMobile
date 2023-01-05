package com.gelora.absensi.model;


public class KategoriIzin {

    private String id;
    private String deskripsi;
    private String kode;
    private String tipe;
    private String lampiran;
    private String jumlah_potongan;

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

    public String getLampiran() {
        return lampiran;
    }

    public void setLampiran(String lampiran) {
        this.lampiran = lampiran;
    }

    public String getJumlah_potongan() {
        return jumlah_potongan;
    }

    public void setJumlah_potongan(String jumlah_potongan) {
        this.jumlah_potongan = jumlah_potongan;
    }
}
