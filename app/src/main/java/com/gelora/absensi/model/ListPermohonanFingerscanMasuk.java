package com.gelora.absensi.model;


public class ListPermohonanFingerscanMasuk {

    private String id;
    private String NIK;
    private String NmKaryawan;
    private String avatar;
    private String KdDept;
    private String keterangan;
    private String tanggal;
    private String status_approve;
    private String status_approve_hrd;
    private String status_read;
    private String update_at;

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

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus_read() {
        return status_read;
    }

    public void setStatus_read(String status_read) {
        this.status_read = status_read;
    }

    public String getStatus_approve() {
        return status_approve;
    }

    public void setStatus_approve(String status_approve) {
        this.status_approve = status_approve;
    }

    public String getStatus_approve_hrd() {
        return status_approve_hrd;
    }

    public void setStatus_approve_hrd(String status_approve_hrd) {
        this.status_approve_hrd = status_approve_hrd;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }

    public String getNmKaryawan() {
        return NmKaryawan;
    }

    public void setNmKaryawan(String nmKaryawan) {
        NmKaryawan = nmKaryawan;
    }

    public String getKdDept() {
        return KdDept;
    }

    public void setKdDept(String kdDept) {
        KdDept = kdDept;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
