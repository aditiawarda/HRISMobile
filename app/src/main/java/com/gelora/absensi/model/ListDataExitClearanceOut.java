package com.gelora.absensi.model;


public class ListDataExitClearanceOut {

    private String id_core;
    private String NIK;
    private String nama_karyawan;
    private String tgl_keluar;
    private String status_notifikasi;
    private String time_notifikasi;

    public String getId_core() {
        return id_core;
    }

    public void setId_core(String id_core) {
        this.id_core = id_core;
    }

    public String getNIK() {
        return NIK;
    }

    public void setNIK(String NIK) {
        this.NIK = NIK;
    }

    public String getTgl_keluar() {
        return tgl_keluar;
    }

    public void setTgl_keluar(String tgl_keluar) {
        this.tgl_keluar = tgl_keluar;
    }

    public String getStatus_notifikasi() {
        return status_notifikasi;
    }

    public void setStatus_notifikasi(String status_notifikasi) {
        this.status_notifikasi = status_notifikasi;
    }

    public String getTime_notifikasi() {
        return time_notifikasi;
    }

    public void setTime_notifikasi(String time_notifikasi) {
        this.time_notifikasi = time_notifikasi;
    }

    public String getNama_karyawan() {
        return nama_karyawan;
    }

    public void setNama_karyawan(String nama_karyawan) {
        this.nama_karyawan = nama_karyawan;
    }
}
