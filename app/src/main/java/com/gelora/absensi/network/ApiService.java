package com.gelora.absensi.network;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Query;

public interface ApiService {

    @DELETE("cancel_permohonan_izin_keluar_kantor/")
    Call<Void> cancelIzinKeluarKantor(@Query("id") String id);
}
