package com.gelora.absensi.model;


public class HistoryAbsen {

    private String tanggal;
    private String tanggal_masuk;
    private String tanggal_pulang;
    private String jam_masuk;
    private String timezone_masuk;
    private String jam_pulang;
    private String timezone_pulang;
    private String checkin_point;
    private String checkout_point;
    private String datang;
    private String pulang;
    private String status_pulang;
    private String status_absen;
    private String nama_shift;

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

    public String getDatang() {
        return datang;
    }

    public void setDatang(String datang) {
        this.datang = datang;
    }

    public String getPulang() {
        return pulang;
    }

    public void setPulang(String pulang) {
        this.pulang = pulang;
    }

    public String getStatus_absen() {
        return status_absen;
    }

    public void setStatus_absen(String status_absen) {
        this.status_absen = status_absen;
    }

    public String getNama_shift() {
        return nama_shift;
    }

    public void setNama_shift(String nama_shift) {
        this.nama_shift = nama_shift;
    }

    public String getTimezone_masuk() {
        return timezone_masuk;
    }

    public void setTimezone_masuk(String timezone_masuk) {
        this.timezone_masuk = timezone_masuk;
    }

    public String getTimezone_pulang() {
        return timezone_pulang;
    }

    public void setTimezone_pulang(String timezone_pulang) {
        this.timezone_pulang = timezone_pulang;
    }

    public String getStatus_pulang() {
        return status_pulang;
    }

    public void setStatus_pulang(String status_pulang) {
        this.status_pulang = status_pulang;
    }
}
