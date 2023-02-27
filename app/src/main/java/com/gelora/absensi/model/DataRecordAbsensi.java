package com.gelora.absensi.model;


public class DataRecordAbsensi {

    private String ket_kehadiran;    // Semua
    private String tanggal;          // Semua
    private String tanggal_masuk;    // Hadir
    private String tanggal_pulang;   // Hadir
    private String jam_masuk;        // Hadir
    private String status_terlambat; // Hadir
    private String waktu_terlambat;  // Hadir
    private String timezone_masuk;   // Hadir
    private String jam_pulang;       // Hadir
    private String timezone_pulang;  // Hadir
    private String checkin_point;    // Hadir
    private String checkout_point;   // Hadir
    private String status_pulang;    // Hadir
    private String kelebihan_jam;    // Hadir
    private String jam_shift;        // Hadir
    private String shift;            // Hadir
    private String tipe_izin;        // Izin/Cuti
    private String alasan;           // Izin/Cuti
    private String dalam_rangka;     // Libur

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

    public String getJam_shift() {
        return jam_shift;
    }

    public void setJam_shift(String jam_shift) {
        this.jam_shift = jam_shift;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
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

    public String getKet_kehadiran() {
        return ket_kehadiran;
    }

    public void setKet_kehadiran(String ket_kehadiran) {
        this.ket_kehadiran = ket_kehadiran;
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    public String getDalam_rangka() {
        return dalam_rangka;
    }

    public void setDalam_rangka(String dalam_rangka) {
        this.dalam_rangka = dalam_rangka;
    }

    public String getTipe_izin() {
        return tipe_izin;
    }

    public void setTipe_izin(String tipe_izin) {
        this.tipe_izin = tipe_izin;
    }

    public String getStatus_terlambat() {
        return status_terlambat;
    }

    public void setStatus_terlambat(String status_terlambat) {
        this.status_terlambat = status_terlambat;
    }

    public String getWaktu_terlambat() {
        return waktu_terlambat;
    }

    public void setWaktu_terlambat(String waktu_terlambat) {
        this.waktu_terlambat = waktu_terlambat;
    }

    public String getKelebihan_jam() {
        return kelebihan_jam;
    }

    public void setKelebihan_jam(String kelebihan_jam) {
        this.kelebihan_jam = kelebihan_jam;
    }
}
