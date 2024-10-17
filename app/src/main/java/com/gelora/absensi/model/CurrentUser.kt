package com.gelora.absensi.model

import com.google.gson.annotations.SerializedName

data class CurrentUser(
    @SerializedName("NIK")
    val nik: String,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("jabatan")
    val jabatan: String,
    @SerializedName("bagian")
    val bagian: String,
    @SerializedName("departemen")
    val departemen: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("signature")
    val signature: String,

    ) {
    override fun toString(): String {
        return "$nama, $jabatan, $bagian"
    }
}



