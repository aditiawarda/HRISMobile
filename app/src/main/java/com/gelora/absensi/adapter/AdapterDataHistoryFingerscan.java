package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailTerlambatActivity;
import com.gelora.absensi.HistoryFingerscanActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataHistoryFinger;
import com.gelora.absensi.model.DataTerlambat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterDataHistoryFingerscan extends RecyclerView.Adapter<AdapterDataHistoryFingerscan.MyViewHolder> {

    private DataHistoryFinger[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterDataHistoryFingerscan(DataHistoryFinger[] data, HistoryFingerscanActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_data_finger,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataHistoryFinger dataHistoryFinger = data[i];

        String input_date = dataHistoryFinger.getTanggal_permohonan();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1= null;
        try {
            dt1 = format1.parse(input_date);
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

        myViewHolder.datePermohonan.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
        myViewHolder.dateMasuk.setText(dataHistoryFinger.getTanggal_masuk().substring(8,10)+"/"+dataHistoryFinger.getTanggal_masuk().substring(5,7)+"/"+dataHistoryFinger.getTanggal_masuk().substring(0,4));
        myViewHolder.shift.setText(dataHistoryFinger.getShift());
        myViewHolder.keterangan.setText(dataHistoryFinger.getDetail_keterangan());
        myViewHolder.alasan.setText(dataHistoryFinger.getAlasan());

        if(dataHistoryFinger.getStatus_approve_kabag().equals("1")){
            if(dataHistoryFinger.getStatus_approve_hrd().equals("1")){
                myViewHolder.status.setText("Permohonan disetujui");
                myViewHolder.acceptedMark.setVisibility(View.VISIBLE);
                myViewHolder.rejectedMark.setVisibility(View.GONE);
                myViewHolder.waitingMark.setVisibility(View.GONE);
            } else if(dataHistoryFinger.getStatus_approve_hrd().equals("2")){
                myViewHolder.status.setText("Permohonan ditolak HRD");
                myViewHolder.acceptedMark.setVisibility(View.GONE);
                myViewHolder.rejectedMark.setVisibility(View.VISIBLE);
                myViewHolder.waitingMark.setVisibility(View.GONE);
            } else {
                myViewHolder.status.setText("Menunggu persetujuan HRD");
                myViewHolder.acceptedMark.setVisibility(View.GONE);
                myViewHolder.rejectedMark.setVisibility(View.GONE);
                myViewHolder.waitingMark.setVisibility(View.VISIBLE);
            }
        } else if(dataHistoryFinger.getStatus_approve_kabag().equals("2")){
            myViewHolder.status.setText("Permohonan ditolak Atasan");
            myViewHolder.acceptedMark.setVisibility(View.GONE);
            myViewHolder.rejectedMark.setVisibility(View.VISIBLE);
            myViewHolder.waitingMark.setVisibility(View.GONE);
        } else {
            myViewHolder.status.setText("Menunggu persetujuan Atasan");
            myViewHolder.acceptedMark.setVisibility(View.GONE);
            myViewHolder.rejectedMark.setVisibility(View.GONE);
            myViewHolder.waitingMark.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView datePermohonan, dateMasuk, shift, keterangan, alasan, status;
        LinearLayout acceptedMark, rejectedMark, waitingMark;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            datePermohonan = itemView.findViewById(R.id.date_permohonan_tv);
            dateMasuk = itemView.findViewById(R.id.d_tanggal_masuk_tv);
            shift = itemView.findViewById(R.id.d_shift_tv);
            keterangan = itemView.findViewById(R.id.d_keterangan_tv);
            alasan = itemView.findViewById(R.id.d_alasan_tv);
            status = itemView.findViewById(R.id.d_status_tv);
            acceptedMark = itemView.findViewById(R.id.accepted_mark);
            rejectedMark = itemView.findViewById(R.id.rejected_mark);
            waitingMark = itemView.findViewById(R.id.waiting_mark);
        }
    }


}