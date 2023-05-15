package com.gelora.absensi.model;


public class DataPenilaianSDM {

    private String id;
    private String tanggal_buat;
    private String NIK;
    private String nama;
    private String jabatan;
    private String bagian;
    private String departemen;
    private String avatar;
    private String total_nilai;
    private String predikat;
    private String approver_kabag;
    private String approver_kadept;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTanggal_buat() {
        return tanggal_buat;
    }

    public void setTanggal_buat(String tanggal_buat) {
        this.tanggal_buat = tanggal_buat;
    }

    public String getNIK() {
        return NIK;
    }

    public void setNIK(String NIK) {
        this.NIK = NIK;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getBagian() {
        return bagian;
    }

    public void setBagian(String bagian) {
        this.bagian = bagian;
    }

    public String getDepartemen() {
        return departemen;
    }

    public void setDepartemen(String departemen) {
        this.departemen = departemen;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTotal_nilai() {
        return total_nilai;
    }

    public void setTotal_nilai(String total_nilai) {
        this.total_nilai = total_nilai;
    }

    public String getPredikat() {
        return predikat;
    }

    public void setPredikat(String predikat) {
        this.predikat = predikat;
    }

    public String getApprover_kabag() {
        return approver_kabag;
    }

    public void setApprover_kabag(String approver_kabag) {
        this.approver_kabag = approver_kabag;
    }

    public String getApprover_kadept() {
        return approver_kadept;
    }

    public void setApprover_kadept(String approver_kadept) {
        this.approver_kadept = approver_kadept;
    }
}
