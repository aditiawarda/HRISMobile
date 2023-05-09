package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefAbsen {

    public static final String SP_ABSENSI_APP = "spAbsensiApp";
    public static final String SP_ID_STATUS = "spIdStatus";
    public static final String SP_ID_SHIFT = "spIdShift";
    public static final String SP_ID_BAGIAN = "spIdBagian";
    public static final String SP_ID_CUTI = "spIdCuti";
    public static final String SP_ID_KARYAWAN_PENGGANTI = "spIdKaryawanPengganti";
    public static final String SP_ID_KARYAWAN_PENILAIAN = "spIdKaryawanPenilaian";
    public static final String SP_ID_TITIK_ABSENSI = "spIdTitikAbsensi";
    public static final String SP_STATUS_ABSEN = "spStatusAbsen";
    public static final String SP_SHIFT_ABSEN = "spShiftAbsen";
    public static final String SP_BAGIAN = "spBagian";
    public static final String SP_CUTI = "spCuti";
    public static final String SP_KARYAWAN_PENGGANTI = "spKaryawanPengganti";
    public static final String SP_KARYAWAN_PENILAIAN = "spKaryawanPenilaian";
    public static final String SP_TITIK_ABSENSI = "spTitikAbsensi";
    public static final String SP_NOTIF_ULTAH = "spNotifUltah";
    public static final String SP_NOTIF_PENGUMUMAN = "spNotifPengumuman";
    public static final String SP_NOTIF_JOIN_REMAINDER = "spNotifJoinReminder";
    public static final String SP_NOTIF_MESSENGER = "spNotifMessenger";
    public static final String SP_YET_BEFORE_MESSENGER = "spYetBeforeMessenger";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    @SuppressLint("CommitPrefEdits")
    public SharedPrefAbsen(Context context){
        sp = context.getSharedPreferences(SP_ABSENSI_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSpIdStatus(){
        return sp.getString(SP_ID_STATUS, "");
    }

    public String getSpIdShift(){
        return sp.getString(SP_ID_SHIFT, "");
    }

    public String getSpIdBagian(){
        return sp.getString(SP_ID_BAGIAN, "");
    }

    public String getSpIdCuti(){
        return sp.getString(SP_ID_CUTI, "");
    }

    public String getSpIdKaryawanPengganti(){
        return sp.getString(SP_ID_KARYAWAN_PENGGANTI, "");
    }

    public String getSpIdTitikAbsensi(){
        return sp.getString(SP_ID_TITIK_ABSENSI, "");
    }

    public String getSpStatusAbsen(){
        return sp.getString(SP_STATUS_ABSEN, "");
    }

    public String getSpShiftAbsen(){
        return sp.getString(SP_SHIFT_ABSEN, "");
    }

    public String getSpBagian(){
        return sp.getString(SP_BAGIAN, "");
    }

    public String getSpCuti(){
        return sp.getString(SP_CUTI, "");
    }

    public String getSpKaryawanPengganti(){
        return sp.getString(SP_KARYAWAN_PENGGANTI, "");
    }

    public String getSpKaryawanPenilaian(){
        return sp.getString(SP_KARYAWAN_PENILAIAN, "");
    }

    public String getSpTitikAbsensi(){
        return sp.getString(SP_ID_TITIK_ABSENSI, "");
    }

    public String getSpNotifUltah(){
        return sp.getString(SP_NOTIF_ULTAH, "");
    }

    public String getSpNotifPengumuman(){
        return sp.getString(SP_NOTIF_PENGUMUMAN, "");
    }

    public String getSpNotifJoinRemainder(){
        return sp.getString(SP_NOTIF_JOIN_REMAINDER, "");
    }

    public String getSpYetBeforeMessenger(){
        return sp.getString(SP_YET_BEFORE_MESSENGER, "");
    }

    public String getSpNotifMessenger(){
        return sp.getString(SP_NOTIF_MESSENGER, "");
    }

}
