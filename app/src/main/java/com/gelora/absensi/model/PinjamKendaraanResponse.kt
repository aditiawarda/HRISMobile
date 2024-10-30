package com.gelora.absensi.model

import com.google.gson.annotations.SerializedName

data class PinjamKendaraanResponse(
    @SerializedName("id_pk")
    val id: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("approved_1_at")
    val app1: String,
    @SerializedName("nama_pemohon")
    val namaPemohon: String,
    @SerializedName("nama_bagian_pemohon")
    val bagianPemohon: String,
    @SerializedName("jenis_kendaraan")
    val jenisKendaraan: String,


    ) {
    override fun toString(): String {
        return "$namaPemohon, $bagianPemohon, $jenisKendaraan, $status"
    }
}

