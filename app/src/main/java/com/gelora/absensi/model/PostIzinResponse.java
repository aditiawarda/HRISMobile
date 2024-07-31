package com.gelora.absensi.model;

public class PostIzinResponse {

    private String nik;
    private String jamKeluar;
    private String durasi;
    private String keperluan;

    // Constructor
    public PostIzinResponse(String nik, String jamKeluar, String durasi, String keperluan) {
        this.nik = nik;
        this.jamKeluar = jamKeluar;
        this.durasi = durasi;
        this.keperluan = keperluan;
    }

    // Getters
    public String getNik() {
        return nik;
    }

    public String getJamKeluar() {
        return jamKeluar;
    }

    public String getDurasi() {
        return durasi;
    }

    public String getKeperluan() {
        return keperluan;
    }

}
