package com.gelora.absensi.model;


public class DataPersonalNotification {

    private String id_record;
    private String nik_pecuti;
    private String nama_pecuti;
    private String nik_pengganti;
    private String nama_pengganti;
    private String tanggal_mulai;
    private String tanggal_akhir;
    private String created_at;
    private String status_read;

    public String getId_record() {
        return id_record;
    }

    public void setId_record(String id_record) {
        this.id_record = id_record;
    }

    public String getNik_pecuti() {
        return nik_pecuti;
    }

    public void setNik_pecuti(String nik_pecuti) {
        this.nik_pecuti = nik_pecuti;
    }

    public String getNama_pecuti() {
        return nama_pecuti;
    }

    public void setNama_pecuti(String nama_pecuti) {
        this.nama_pecuti = nama_pecuti;
    }

    public String getNik_pengganti() {
        return nik_pengganti;
    }

    public void setNik_pengganti(String nik_pengganti) {
        this.nik_pengganti = nik_pengganti;
    }

    public String getNama_pengganti() {
        return nama_pengganti;
    }

    public void setNama_pengganti(String nama_pengganti) {
        this.nama_pengganti = nama_pengganti;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus_read() {
        return status_read;
    }

    public void setStatus_read(String status_read) {
        this.status_read = status_read;
    }
}
