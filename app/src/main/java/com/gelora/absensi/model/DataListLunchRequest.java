package com.gelora.absensi.model;


public class DataListLunchRequest {

    private String id;
    private String tanggal;
    private String bagian;
    private String nama_requester;
    private String siang_k;
    private String siang_s;
    private String sore;
    private String malam;
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

    public String getSiang_k() {
        return siang_k;
    }

    public void setSiang_k(String siang_k) {
        this.siang_k = siang_k;
    }

    public String getSiang_s() {
        return siang_s;
    }

    public void setSiang_s(String siang_s) {
        this.siang_s = siang_s;
    }

    public String getSore() {
        return sore;
    }

    public void setSore(String sore) {
        this.sore = sore;
    }

    public String getMalam() {
        return malam;
    }

    public void setMalam(String malam) {
        this.malam = malam;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}
