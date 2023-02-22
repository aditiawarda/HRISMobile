package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    public static final String SP_ABSENSI_APP = "spAbsensiApp";
    public static final String SP_HALAMAN = "spHalaman";
    public static final String SP_SUDAH_LOGIN = "spSudahLogin";
    public static final String SP_ID = "spID";
    public static final String SP_NIK = "spNIK";
    public static final String SP_NAMA = "spNama";
    public static final String SP_ID_CAB = "spIdCab";
    public static final String SP_ID_HEAD_DEPT = "spIdHeadDept";
    public static final String SP_ID_DEPT = "spIdDept";
    public static final String SP_ID_JABATAN = "spIdJabatan";
    public static final String SP_STATUS_USER = "spIdStatusUser";
    public static final String SP_STATUS_AKTIF = "spIdStatusAktif";
    public static final String SP_REMINDER = "spReminder";
    public static final String SP_TGL_BERGABUNG = "spTglBergabung";
    public static final String SP_STATUS_KARYAWAN = "spStatusKaryawan";
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    @SuppressLint("CommitPrefEdits")
    public SharedPrefManager(Context context){
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

    public String getSpHalaman(){
        return sp.getString(SP_HALAMAN, "");
    }
    public Boolean getSpSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }
    public String getSpId(){
        return sp.getString(SP_ID, "");
    }
    public String getSpNik(){
        return sp.getString(SP_NIK, "");
    }
    public String getSpNama(){
        return sp.getString(SP_NAMA, "");
    }
    public String getSpIdCab(){
        return sp.getString(SP_ID_CAB, "");
    }
    public String getSpIdHeadDept(){
        return sp.getString(SP_ID_HEAD_DEPT, "");
    }
    public String getSpIdDept(){
        return sp.getString(SP_ID_DEPT, "");
    }
    public String getSpIdJabatan(){
        return sp.getString(SP_ID_JABATAN, "");
    }
    public String getSpStatusUser(){
        return sp.getString(SP_STATUS_USER, "");
    }
    public String getSpStatusAktif(){
        return sp.getString(SP_STATUS_USER, "");
    }
    public Boolean getSpReminder(){
        return sp.getBoolean(SP_REMINDER, false);
    }
    public String getSpTglBergabung(){
        return sp.getString(SP_TGL_BERGABUNG, "");
    }
    public String getSpStatusKaryawan(){
        return sp.getString(SP_STATUS_KARYAWAN, "");
    }

}
