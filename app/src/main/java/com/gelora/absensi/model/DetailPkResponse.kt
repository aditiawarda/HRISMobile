package com.gelora.absensi.model

import com.google.gson.annotations.SerializedName

data class DetailPkResponse(
    @SerializedName("id_pk")
    val id: String,
    @SerializedName("no_surat")
    val noSurat: String,
    @SerializedName("bulan_surat")
    val bulanSurat: String,
    @SerializedName("tahun_surat")
    val tahunSurat: String,
    @SerializedName("id_jabatan_pemohon")
    val idJabatanPemohon: String,
    @SerializedName("id_bagian_pemohon")
    val idBagianPemohon: String,
    @SerializedName("id_dept_pemohon")
    val idDeptPemohon: String,
    @SerializedName("nama_pemohon")
    val namaPemohon: String,
    @SerializedName("jabatan_pemohon")
    val jabatanPemohon: String,
    @SerializedName("nama_bagian_pemohon")
    val bagianPemohon: String,

    @SerializedName("nama_dept_pemohon")
    val deptPemohon: String,
    @SerializedName("tanggal_keluar")
    val tanggalKeluar: String,
    @SerializedName("tanngal_masuk")
    val tanggalMasuk: String,
    @SerializedName("tujuan")
    val tujuan: String,
    @SerializedName("keperluan")
    val keperluan: String,
    @SerializedName("id_kendaraan")
    val idKendaraan: String,
    @SerializedName("plat_nomor")
    val platNomer: String,
    @SerializedName("nama_kendaraan")
    val namaKendaraan: String,
    @SerializedName("approved_1_nik")
    val app1Nik: String,
    @SerializedName("approved_2_nik")
    val app2Nik: String,
    @SerializedName("approved_3_nik")
    val app3Nik: String,
    @SerializedName("approved_4_nik")
    val app4Nik: String,
    @SerializedName("approved_1_at")
    val app1At: String,
    @SerializedName("approved_2_at")
    val app2At: String,
    @SerializedName("approved_3_at")
    val app3At: String,
    @SerializedName("approved_4_at")
    val app4At: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("jam_keluar")
    val jamKeluar: String,
    @SerializedName("km_keluar")
    val kmKeluar: String,
    @SerializedName("bk_keluar")
    val bkKeluar: String,
    @SerializedName("bbm_keluar")
    val bbmKeluar: String,

    @SerializedName("jam_masuk")
    val jamMasuk: String,
    @SerializedName("km_masuk")
    val kmMasuk: String,
    @SerializedName("bk_masuk")
    val bkMasuk: String,
    @SerializedName("bbm_masuk")
    val bbmMasuk: String,

    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("update_at")
    val updateAt: String,


    ) {
    override fun toString(): String {
        return "$namaPemohon, $bagianPemohon, $status"
    }





}

