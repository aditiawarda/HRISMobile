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

import com.gelora.absensi.FormInfoPelatihanActivity;
import com.gelora.absensi.InfoPengalamanDanPelatihanActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.ResumeKaryawanActivity;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataPelatihan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterListPelatihanResume extends RecyclerView.Adapter<AdapterListPelatihanResume.MyViewHolder> {

    private DataPelatihan[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterListPelatihanResume(DataPelatihan[] data, ResumeKaryawanActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pelatihan_resume,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataPelatihan dataPengalaman = data[i];

        myViewHolder.namaPelatihan.setText(dataPengalaman.getNama_pelatihan());
        myViewHolder.lembagaPelatihan.setText("Dari : "+ dataPengalaman.getLembaga_pelatihan());
        myViewHolder.tahunPelatihan.setText(dataPengalaman.getTahun());

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
        TextView namaPelatihan, lembagaPelatihan, tahunPelatihan;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            namaPelatihan = itemView.findViewById(R.id.nama_pelatihan);
            lembagaPelatihan = itemView.findViewById(R.id.lembaga_pelatihan);
            tahunPelatihan = itemView.findViewById(R.id.tahun);
        }
    }

}