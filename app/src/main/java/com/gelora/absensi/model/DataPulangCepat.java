package com.gelora.absensi.model;


public class DataPulangCepat {

    private String shift;
    private String jam_shift;
    private String tanggal_masuk;
    private String tanggal_pulang;
    private String jam_masuk;
    private String timezone_masuk;
    private String jam_pulang;
    private String timezone_pulang;
    private String status_pulang;
    private String alasan;
    private String checkin_point;
    private String checkout_point;

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

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }
}
