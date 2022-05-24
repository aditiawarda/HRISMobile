package com.gelora.absensi.model;


public class DataNoCheckout {

    private String id_masuk;
    private String shift;
    private String jam_shift;
    private String tanggal_masuk;
    private String jam_masuk;
    private String checkin_point;
    private String ket;

    public String getId_masuk() {
        return id_masuk;
    }

    public void setId_masuk(String id_masuk) {
        this.id_masuk = id_masuk;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getJam_shift() {
        return jam_shift;
    }

    public void setJam_shift(String jam_shift) {
        this.jam_shift = jam_shift;
    }

    public String getTanggal_masuk() {
        return tanggal_masuk;
    }

    public void setTanggal_masuk(String tanggal_masuk) {
        this.tanggal_masuk = tanggal_masuk;
    }

    public String getJam_masuk() {
        return jam_masuk;
    }

    public void setJam_masuk(String jam_masuk) {
        this.jam_masuk = jam_masuk;
    }

    public String getCheckin_point() {
        return checkin_point;
    }

    public void setCheckin_point(String checkin_point) {
        this.checkin_point = checkin_point;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }
}
