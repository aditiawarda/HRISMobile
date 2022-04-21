package com.gelora.absensi.model;


public class HistoryAbsen {

    private String tanggal;
    private String tanggal_masuk;
    private String tanggal_pulang;
    private String jam_masuk;
    private String jam_pulang;
    private String checkin_point;
    private String checkout_point;

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTanggal_masuk() {
        return tanggal_masuk;
    }

    public void setTanggal_masuk(String tanggal_masuk) {
        this.tanggal_masuk = tanggal_masuk;
    }

    public String getTanggal_pulang() {
        return tanggal_pulang;
    }

    public void setTanggal_pulang(String tanggal_pulang) {
        this.tanggal_pulang = tanggal_pulang;
    }

    public String getJam_masuk() {
        return jam_masuk;
    }

    public void setJam_masuk(String jam_masuk) {
        this.jam_masuk = jam_masuk;
    }

    public String getJam_pulang() {
        return jam_pulang;
    }

    public void setJam_pulang(String jam_pulang) {
        this.jam_pulang = jam_pulang;
    }

    public String getCheckin_point() {
        return checkin_point;
    }

    public void setCheckin_point(String checkin_point) {
        this.checkin_point = checkin_point;
    }

    public String getCheckout_point() {
        return checkout_point;
    }

    public void setCheckout_point(String checkout_point) {
        this.checkout_point = checkout_point;
    }

}
