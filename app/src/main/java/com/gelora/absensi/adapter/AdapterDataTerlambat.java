package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailTerlambatActivity;
import com.gelora.absensi.DetailTidakHadirActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataAlpa;
import com.gelora.absensi.model.DataTerlambat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterDataTerlambat extends RecyclerView.Adapter<AdapterDataTerlambat.MyViewHolder> {

    private DataTerlambat[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterDataTerlambat(DataTerlambat[] data, DetailTerlambatActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_data_terlambat,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataTerlambat dataTerlambat = data[i];

        String input_date = dataTerlambat.getTanggal_masuk();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
        Date dt1= null;
        try {
            dt1 = format1.parse(input_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        @SuppressLint("SimpleDateFormat")
        DateFormat format2=new SimpleDateFormat("EEE");
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

        String dayDate = input_date.substring(8,10);
        String yearDate = input_date.substring(0,4);
        String bulanValue = input_date.substring(5,7);
        String bulanName;

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

        myViewHolder.dateAbsen.setText(hariName+", "+dayDate+" "+bulanName+" "+yearDate);
        myViewHolder.shiftAbsen.setText(dataTerlambat.getShift());
        myViewHolder.jamShift.setText(dataTerlambat.getJam_shift());
        myViewHolder.dateAbsenCheckin.setText(dataTerlambat.getTanggal_masuk());
        myViewHolder.jamMasuk.setText(dataTerlambat.getJam_masuk());
        myViewHolder.checkinPoint.setText(dataTerlambat.getCheckin_point());

        if (myViewHolder.checkinPoint.getText().toString().equals("")){
            myViewHolder.checkinPoint.setText(sharedPrefManager.getSpNama());
        } else {
            myViewHolder.checkinPoint.setText(dataTerlambat.getCheckin_point());
        }

        if(!dataTerlambat.getWaktu_terlambat().substring(0, 2).equals("00")){ // 01:01:01
            if (!dataTerlambat.getWaktu_terlambat().substring(3, 5).equals("00")){ // 01:01:01
                myViewHolder.keterlambatanJam.setText(dataTerlambat.getWaktu_terlambat().substring(0,2)+" jam "+dataTerlambat.getWaktu_terlambat().substring(3,5)+" menit "+dataTerlambat.getWaktu_terlambat().substring(6,8)+" detik");
            } else { // 01:00:01
                myViewHolder.keterlambatanJam.setText(dataTerlambat.getWaktu_terlambat().substring(0,2)+" jam "+dataTerlambat.getWaktu_terlambat().substring(6,8)+" detik");
            }
        } else { // 00:01:01
            if (!dataTerlambat.getWaktu_terlambat().substring(3, 5).equals("00")){ // 00:01:01
                myViewHolder.keterlambatanJam.setText(dataTerlambat.getWaktu_terlambat().substring(3,5)+" menit "+dataTerlambat.getWaktu_terlambat().substring(6,8)+" detik");
            } else { // 00:00:01
                myViewHolder.keterlambatanJam.setText(dataTerlambat.getWaktu_terlambat().substring(6,8)+" detik");
            }
        }

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateAbsen, shiftAbsen, jamShift, dateAbsenCheckin, jamMasuk, checkinPoint, keterlambatanJam;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dateAbsen = itemView.findViewById(R.id.date_absen_tv_late);
            shiftAbsen = itemView.findViewById(R.id.nama_shift_late);
            jamShift = itemView.findViewById(R.id.jam_shift_late);
            dateAbsenCheckin = itemView.findViewById(R.id.date_checkin_tv_late);
            jamMasuk = itemView.findViewById(R.id.time_checkin_tv_late);
            checkinPoint = itemView.findViewById(R.id.checkin_point_tv_late);
            keterlambatanJam = itemView.findViewById(R.id.keterlambatan_tv);
        }
    }


}