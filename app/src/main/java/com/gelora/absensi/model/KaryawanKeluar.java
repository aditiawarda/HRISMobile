package com.gelora.absensi.model;

import com.google.gson.annotations.SerializedName;

public class KaryawanKeluar {

    @SerializedName("id")
    private String id;

    @SerializedName("tanggal")
    private String tanggal;

    @SerializedName("nama_karyawan")
    private String namaKaryawan;

    @SerializedName("bagian")
    private String bagian;

    @SerializedName("nik")
    private String nik;

    @SerializedName("jam_keluar")
    private String jamKeluar;

    @SerializedName("durasi")
    private String durasi;

    @SerializedName("keperluan")
    private String keperluan;

    @SerializedName("status_approval")
    private String statusApproval;

    @SerializedName("status_approval_satpam")
    private String statusApprovalSatpam;

    @SerializedName("verifikasi_kembali")
    private String verifikasiKembali;


    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNamaKaryawan() {
        return namaKaryawan;
    }

    public void setNamaKaryawan(String namaKaryawan) {
        this.namaKaryawan = namaKaryawan;
    }

    public String getBagian() {
        return bagian;
    }

    public void setBagian(String bagian) {
        this.bagian = bagian;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getJamKeluar() {
        return jamKeluar;
    }

    public void setJamKeluar(String jamKeluar) {
        this.jamKeluar = jamKeluar;
    }

    public String getDurasi() {
        return durasi;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }

    public String getKeperluan() {
        return keperluan;
    }

    public void setKeperluan(String keperluan) {
        this.keperluan = keperluan;
    }

    public String getStatusApproval() {
        return statusApproval;
    }

    public void setStatusApproval(String statusApproval) {
        this.statusApproval = statusApproval;
    }

    public String getStatusApprovalSatpam() {
        return statusApprovalSatpam;
    }

    public void setStatusApprovalSatpam(String statusApprovalSatpam) {
        this.statusApprovalSatpam = statusApprovalSatpam;
    }

    public String getVerifikatorKembali() {
        return verifikasiKembali;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


}

