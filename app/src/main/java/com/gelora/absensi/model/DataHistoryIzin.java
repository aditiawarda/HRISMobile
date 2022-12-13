package com.gelora.absensi.model;


public class DataHistoryIzin {

    private String id;
    private String NIK;
    private String tipe_izin;
    private String kode_izin;
    private String deskripsi_izin;
    private String tanggal_mulai;
    private String tanggal_akhir;
    private String tipe_pengajuan;
    private String keterangan;

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

    public String getTipe_izin() {
        return tipe_izin;
    }

    public void setTipe_izin(String tipe_izin) {
        this.tipe_izin = tipe_izin;
    }

    public String getKode_izin() {
        return kode_izin;
    }

    public void setKode_izin(String kode_izin) {
        this.kode_izin = kode_izin;
    }

    public String getDeskripsi_izin() {
        return deskripsi_izin;
    }

    public void setDeskripsi_izin(String deskripsi_izin) {
        this.deskripsi_izin = deskripsi_izin;
    }

    public String getTanggal_mulai() {
        return tanggal_mulai;
    }

    public void setTanggal_mulai(String tanggal_mulai) {
        this.tanggal_mulai = tanggal_mulai;
    }

    public String getTanggal_akhir() {
        return tanggal_akhir;
    }

    public void setTanggal_akhir(String tanggal_akhir) {
        this.tanggal_akhir = tanggal_akhir;
    }

    public String getTipe_pengajuan() {
        return tipe_pengajuan;
    }

    public void setTipe_pengajuan(String tipe_pengajuan) {
        this.tipe_pengajuan = tipe_pengajuan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setK(String keterangan) {
        this.keterangan = keterangan;
    }

}
