package com.gelora.absensi.model;


public class DataReportSuma {
    private String id;
    private String tipeLaporan;
    private String keterangan;
    private String tipePelanggan;
    private String idPelanggan;
    private String idSales;
    private String createdAt;
    private String totalPesanan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipeLaporan() {
        return tipeLaporan;
    }

    public void setTipeLaporan(String tipeLaporan) {
        this.tipeLaporan = tipeLaporan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTipePelanggan() {
        return tipePelanggan;
    }

    public void setTipePelanggan(String tipePelanggan) {
        this.tipePelanggan = tipePelanggan;
    }

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public String getIdSales() {
        return idSales;
    }

    public void setIdSales(String idSales) {
        this.idSales = idSales;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTotalPesanan() {
        return totalPesanan;
    }

    public void setTotalPesanan(String totalPesanan) {
        this.totalPesanan = totalPesanan;
    }

}
