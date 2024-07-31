package com.gelora.absensi.model;

import com.google.gson.annotations.SerializedName;

public class DetailKaryawanKeluar {
    @SerializedName("id")
    private String id;

    @SerializedName("tanggal")
    private String tanggal;

    @SerializedName("ttd_pemohon")
    private String ttdPemohon;

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

    @SerializedName("durasi_aktual")
    private String durasiAktual;

    @SerializedName("jam_kembali")
    private String jamKembali;

    @SerializedName("keperluan")
    private String keperluan;

    @SerializedName("status_approval")
    private String statusApproval;

    @SerializedName("nama_atasan")
    private String namaAtasan;

    @SerializedName("ttd_atasan")
    private String ttdAtasan;


    @SerializedName("time_approval")
    private String timeApproval;

    @SerializedName("status_approval_satpam")
    private String statusApprovalSatpam;

    @SerializedName("nama_satpam")
    private String namaSatpam;

    @SerializedName("ttd_satpam")
    private String ttdSatpam;

    @SerializedName("time_approval_satpam")
    private String timeApprovalSatpam;

    @SerializedName("verifikasi_kembali")
    private String verifikasiKembali;

    @SerializedName("verifikator_satpam")
    private String verifikatorSatpam;

    @SerializedName("nama_verifikator_satpam")
    private String namaVerifSatpam;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // Getters and setters (or use lombok for simplicity)

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

    public String getTtdPemohon() {
        return ttdPemohon;
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

    public String getDurasiAktual() {
        return durasiAktual;
    }

    public String getJamKembali() {
        return jamKembali;
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

    public String getNamaAtasan() {
        return namaAtasan;
    }

    public void setNamaAtasan(String namaAtasan) {
        this.namaAtasan = namaAtasan;
    }

    public String getTtdAtasan() {
        return ttdAtasan;
    }

    public String getTimeApproval() {
        return timeApproval;
    }

    public void setTimeApproval(String timeApproval) {
        this.timeApproval = timeApproval;
    }

    public String getStatusApprovalSatpam() {
        return statusApprovalSatpam;
    }

    public void setStatusApprovalSatpam(String statusApprovalSatpam) {
        this.statusApprovalSatpam = statusApprovalSatpam;
    }

    public String getNamaSatpam() {
        return namaSatpam;
    }

    public String getTtdSatpam() {
        return ttdSatpam;
    }

    public void setNamaSatpam(String namaSatpam) {
        this.namaSatpam = namaSatpam;
    }

    public String getTimeApprovalSatpam() {
        return timeApprovalSatpam;
    }

    public String getVerifikatorKembali() {
        return verifikasiKembali;
    }

    public String getVerifikatorSatpam() {
        return verifikatorSatpam;
    }

    public String getNamaVerifSatpam() {
        return namaVerifSatpam;
    }

    public void setTimeApprovalSatpam(String timeApprovalSatpam) {
        this.timeApprovalSatpam = timeApprovalSatpam;
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
