package com.gelora.absensi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefAbsen {

    public static final String SP_ABSENSI_APP = "spAbsensiApp";
    public static final String SP_ID_STATUS = "spIdStatus";
    public static final String SP_ID_SHIFT = "spIdShift";
    public static final String SP_STATUS_ABSEN = "spStatusAbsen";
    public static final String SP_SHIFT_ABSEN = "spShiftAbsen";

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

    public String getSpStatusAbsen(){
        return sp.getString(SP_STATUS_ABSEN, "");
    }

    public String getSpShiftAbsen(){
        return sp.getString(SP_SHIFT_ABSEN, "");
    }

}
