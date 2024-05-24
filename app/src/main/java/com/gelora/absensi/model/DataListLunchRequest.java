package com.gelora.absensi.model;


public class DataListLunchRequest {

    private String id;
    private String tanggal;
    private String bagian;
    private String nama_requester;
    private String pusat_siang_k;
    private String pusat_siang_s;
    private String pusat_sore;
    private String pusat_malam;
    private String gapprint_siang;
    private String gapprint_sore;
    private String gapprint_malam;
    private String created_at;

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

    public String getBagian() {
        return bagian;
    }

    public void setBagian(String bagian) {
        this.bagian = bagian;
    }

    public String getNama_requester() {
        return nama_requester;
    }

    public void setNama_requester(String nama_requester) {
        this.nama_requester = nama_requester;
    }

    public String getPusat_siang_k() {
        return pusat_siang_k;
    }

    public void setPusat_siang_k(String pusat_siang_k) {
        this.pusat_siang_k = pusat_siang_k;
    }

    public String getPusat_siang_s() {
        return pusat_siang_s;
    }

    public void setPusat_siang_s(String pusat_siang_s) {
        this.pusat_siang_s = pusat_siang_s;
    }

    public String getPusat_sore() {
        return pusat_sore;
    }

    public void setPusat_sore(String pusat_sore) {
        this.pusat_sore = pusat_sore;
    }

    public String getPusat_malam() {
        return pusat_malam;
    }

    public void setPusat_malam(String pusat_malam) {
        this.pusat_malam = pusat_malam;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getGapprint_siang() {
        return gapprint_siang;
    }

    public void setGapprint_siang(String gapprint_siang) {
        this.gapprint_siang = gapprint_siang;
    }

    public String getGapprint_sore() {
        return gapprint_sore;
    }

    public void setGapprint_sore(String gapprint_sore) {
        this.gapprint_sore = gapprint_sore;
    }

    public String getGapprint_malam() {
        return gapprint_malam;
    }

    public void setGapprint_malam(String gapprint_malam) {
        this.gapprint_malam = gapprint_malam;
    }
}
