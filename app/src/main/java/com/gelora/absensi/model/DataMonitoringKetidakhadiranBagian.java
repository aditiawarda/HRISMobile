package com.gelora.absensi.model;


public class DataMonitoringKetidakhadiranBagian {

    private String NIK;
    private String NmKaryawan;
    private String keterangan;
    private String kode;

    public String getNIK() {
        return NIK;
    }

    public void setNIK(String NIK) {
        this.NIK = NIK;
    }

    public String getNmKaryawan() {
        return NmKaryawan;
    }

    public void setNmKaryawan(String nmKaryawan) {
        NmKaryawan = nmKaryawan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }
}
