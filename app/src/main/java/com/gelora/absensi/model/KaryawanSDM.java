package com.gelora.absensi.model;


public class KaryawanSDM {

    private String NIK;
    private String nama;
    private String jabatan;
    private String id_jabatan;
    private String bagian;
    private String id_bagian;
    private String departemen;
    private String id_departemen;
    private String status_karyawan;

    private String golongan_karyawan;

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

    public String getId_jabatan() {
        return id_jabatan;
    }

    public void setId_jabatan(String id_jabatan) {
        this.id_jabatan = id_jabatan;
    }

    public String getBagian() {
        return bagian;
    }

    public void setBagian(String bagian) {
        this.bagian = bagian;
    }

    public String getId_bagian() {
        return id_bagian;
    }

    public void setId_bagian(String id_bagian) {
        this.id_bagian = id_bagian;
    }

    public String getDepartemen() {
        return departemen;
    }

    public void setDepartemen(String departemen) {
        this.departemen = departemen;
    }

    public String getId_departemen() {
        return id_departemen;
    }

    public void setId_departemen(String id_departemen) {
        this.id_departemen = id_departemen;
    }

    public String getStatus_karyawan() {
        return status_karyawan;
    }

    public void setStatus_karyawan(String status_karyawan) {
        this.status_karyawan = status_karyawan;
    }

    public String getGolongan_karyawan() {
        return golongan_karyawan;
    }

    public void setGolongan_karyawan(String golongan_karyawan) {
        this.golongan_karyawan = golongan_karyawan;
    }

}
