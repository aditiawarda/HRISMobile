package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailHadirActivity;
import com.gelora.absensi.DetailPermohonanIzinActivity;
import com.gelora.absensi.ListNotifikasiActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataHadir;
import com.gelora.absensi.model.ListPermohonanIzin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterPermohonanIzin extends RecyclerView.Adapter<AdapterPermohonanIzin.MyViewHolder> {

    private ListPermohonanIzin[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterPermohonanIzin(ListPermohonanIzin[] data, ListNotifikasiActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_permohonan_izin,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ListPermohonanIzin listPermohonanIzin = data[i];

        String tipe_izin = listPermohonanIzin.getTipe_izin();
        if (tipe_izin.equals("5")){
            tipe_izin = "Tidak Masuk (Sakit)";
        } else {
            tipe_izin = "Permohonan Izin";
        }

        myViewHolder.namaKaryawanTV.setText(listPermohonanIzin.getNmKaryawan().toUpperCase());
        myViewHolder.nikKaryawanTV.setText("NIK "+listPermohonanIzin.getNIK());
        myViewHolder.deskrisiPermohonan.setText(tipe_izin);

        String input_date = listPermohonanIzin.getTanggal();
        String dayDate = input_date.substring(8,10);
        String yearDate = input_date.substring(0,4);
        String bulanValue = input_date.substring(5,7);
        String bulanName;

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1= null;
        try {
            dt1 = format.parse(input_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat")
        DateFormat format2 = new SimpleDateFormat("EEE");
        String finalDay = format2.format(dt1);
        String hariName = "";

        if (finalDay.equals("Mon") || finalDay.equals("Sen")) {
            hariName = "Senin";
        } else if (finalDay.equals("Tue") || finalDay.equals("Sel")) {
            hariName = "Selasa";
        } else if (finalDay.equals("Wed") || finalDay.equals("Rab")) {
            hariName = "Rabu";
        } else if (finalDay.equals("Thu") || finalDay.equals("Kam")) {
            hariName = "Kamis";
        } else if (finalDay.equals("Fri") || finalDay.equals("Jum")) {
            hariName = "Jumat";
        } else if (finalDay.equals("Sat") || finalDay.equals("Sab")) {
            hariName = "Sabtu";
        } else if (finalDay.equals("Sun") || finalDay.equals("Min")) {
            hariName = "Minggu";
        }

        switch (bulanValue) {
            case "01":
                bulanName = "Januari";
                break;
            case "02":
                bulanName = "Februari";
                break;
            case "03":
                bulanName = "Maret";
                break;
            case "04":
                bulanName = "April";
                break;
            case "05":
                bulanName = "Mei";
                break;
            case "06":
                bulanName = "Juni";
                break;
            case "07":
                bulanName = "Juli";
                break;
            case "08":
                bulanName = "Agustus";
                break;
            case "09":
                bulanName = "September";
                break;
            case "10":
                bulanName = "Oktober";
                break;
            case "11":
                bulanName = "November";
                break;
            case "12":
                bulanName = "Desember";
                break;
            default:
                bulanName = "Not found";
                break;
        }

        String time = listPermohonanIzin.getCreated_at().substring(10,16);

        myViewHolder.tanggalKirimPermohonan.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate+" "+time);

        if (!listPermohonanIzin.getStatus_approve().equals("0")){
            myViewHolder.namaKaryawanTV.setTextColor(Color.parseColor("#919191"));
            myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.NORMAL);
            myViewHolder.nikKaryawanTV.setTextColor(Color.parseColor("#919191"));
            myViewHolder.deskrisiPermohonan.setTextColor(Color.parseColor("#919191"));
            myViewHolder.tanggalKirimPermohonan.setTextColor(Color.parseColor("#919191"));
            myViewHolder.lineLimit.setBackgroundColor(Color.parseColor("#EAEAEA"));
        } else {
            myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.BOLD);
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailPermohonanIzinActivity.class);
                intent.putExtra("kode", "notif");
                intent.putExtra("id_izin",listPermohonanIzin.getId());
                mContext.startActivity(intent);
            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namaKaryawanTV, nikKaryawanTV, deskrisiPermohonan, tanggalKirimPermohonan, lineLimit;
        LinearLayout parentPart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            deskrisiPermohonan = itemView.findViewById(R.id.deskrisi_permohonan);
            tanggalKirimPermohonan = itemView.findViewById(R.id.tanggal_kirim_permohonan);
            nikKaryawanTV = itemView.findViewById(R.id.nik_karyawan_tv);
            namaKaryawanTV = itemView.findViewById(R.id.nama_karyawan_tv);
            lineLimit = itemView.findViewById(R.id.line_limit);
            parentPart = itemView.findViewById(R.id.parent_part);
        }
    }


}