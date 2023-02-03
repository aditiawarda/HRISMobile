package com.gelora.absensi.model;


public class DataHistoryFinger {

    private String id;
    private String tanggal_permohonan;
    private String tanggal_masuk;
    private String shift;
    private String detail_keterangan;
    private String alasan;
    private String status_approve_kabag;
    private String status_approve_hrd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTanggal_permohonan() {
        return tanggal_permohonan;
    }

    public void setTanggal_permohonan(String tanggal_permohonan) {
        this.tanggal_permohonan = tanggal_permohonan;
    }

    public String getTanggal_masuk() {
        return tanggal_masuk;
    }

    public void setTanggal_masuk(String tanggal_masuk) {
        this.tanggal_masuk = tanggal_masuk;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getDetail_keterangan() {
        return detail_keterangan;
    }

    public void setDetail_keterangan(String detail_keterangan) {
        this.detail_keterangan = detail_keterangan;
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    public String getStatus_approve_kabag() {
        return status_approve_kabag;
    }

    public void setStatus_approve_kabag(String status_approve_kabag) {
        this.status_approve_kabag = status_approve_kabag;
    }

    public String getStatus_approve_hrd() {
        return status_approve_hrd;
    }

    public void setStatus_approve_hrd(String status_approve_hrd) {
        this.status_approve_hrd = status_approve_hrd;
    }
}
