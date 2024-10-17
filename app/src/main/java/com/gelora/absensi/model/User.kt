package com.gelora.absensi.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("NIK")
    val nik: String,
    @SerializedName("NmKaryawan")
    val NmKaryawan: String,
    @SerializedName("status_karyawan")
    val status_karyawan: String,
    @SerializedName("currentYear")
    val emaicurrentYearl: String,
    @SerializedName("tahun_bergabung")
    val tahun_bergabung: String,
    @SerializedName("bulan_bergabung")
    val bulan_bergabung: String,
    @SerializedName("tanggal_bergabung")
    val tanggal_bergabung: String,
    @SerializedName("KdDept")
    val KdDept: String,
    @SerializedName("NmDept")
    val NmDept: String,
    @SerializedName("NmHeadDept")
    val NmHeadDept: String,
    @SerializedName("NmJabatan")
    val NmJabatan: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("IdJabatan")
    val IdJabatan: String,
    @SerializedName("wilayah_suma")
    val wilayah_suma: String,

    ) {
    override fun toString(): String {
        return NmKaryawan
    }
}
