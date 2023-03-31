package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.FormInfoPengalamanActivity;
import com.gelora.absensi.InfoPengalamanDanPelatihanActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.ResumeKaryawanActivity;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataPengalaman;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterListPengalamanResume extends RecyclerView.Adapter<AdapterListPengalamanResume.MyViewHolder> {

    private DataPengalaman[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterListPengalamanResume(DataPengalaman[] data, ResumeKaryawanActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pengalaman_resume,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataPengalaman dataPengalaman = data[i];

        myViewHolder.posisi.setText(dataPengalaman.getDeskripsi_posisi());
        myViewHolder.periode.setText(dataPengalaman.getDari_tahun()+" s.d. "+dataPengalaman.getSampai_tahun());

    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView posisi, periode;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            posisi = itemView.findViewById(R.id.posisi);
            periode = itemView.findViewById(R.id.periode);
        }
    }

}