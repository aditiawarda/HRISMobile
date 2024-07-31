package com.gelora.absensi.model;

public class DetailResponse {

    private KaryawanKeluar detail;

    public DetailResponse(String status, KaryawanKeluar detail) {
        this.detail = detail;
    }

    public KaryawanKeluar getDetailKaryawan() {
        return detail;
    }


}
