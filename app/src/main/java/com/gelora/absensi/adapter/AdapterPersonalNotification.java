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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailPermohonanCutiActivity;
import com.gelora.absensi.DetailPermohonanIzinActivity;
import com.gelora.absensi.ListNotifikasiActivity;
import com.gelora.absensi.PersonalNotificationActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataPersonalNotification;
import com.gelora.absensi.model.ListPermohonanIzin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterPersonalNotification extends RecyclerView.Adapter<AdapterPersonalNotification.MyViewHolder> {

    private DataPersonalNotification[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterPersonalNotification(DataPersonalNotification[] data, PersonalNotificationActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_personal_notifications,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataPersonalNotification dataPersonalNotification = data[i];

        myViewHolder.statusNotifikasiTV.setText("Delegasi/Pengganti "+dataPersonalNotification.getNama_pecuti());

        String input_date_mulai = dataPersonalNotification.getTanggal_mulai();
        String dayDateMulai = input_date_mulai.substring(8,10);
        String yearDateMulai = input_date_mulai.substring(0,4);
        String bulanValueMulai = input_date_mulai.substring(5,7);
        String bulanNameMulai;

        switch (bulanValueMulai) {
            case "01":
                bulanNameMulai = "Januari";
                break;
            case "02":
                bulanNameMulai = "Februari";
                break;
            case "03":
                bulanNameMulai = "Maret";
                break;
            case "04":
                bulanNameMulai = "April";
                break;
            case "05":
                bulanNameMulai = "Mei";
                break;
            case "06":
                bulanNameMulai = "Juni";
                break;
            case "07":
                bulanNameMulai = "Juli";
                break;
            case "08":
                bulanNameMulai = "Agustus";
                break;
            case "09":
                bulanNameMulai = "September";
                break;
            case "10":
                bulanNameMulai = "Oktober";
                break;
            case "11":
                bulanNameMulai = "November";
                break;
            case "12":
                bulanNameMulai = "Desember";
                break;
            default:
                bulanNameMulai = "Not found";
                break;
        }

        String input_date_akhir = dataPersonalNotification.getTanggal_akhir();
        String dayDateAkhir = input_date_akhir.substring(8,10);
        String yearDateAkhir = input_date_akhir.substring(0,4);
        String bulanValueAkhir = input_date_akhir.substring(5,7);
        String bulanNameAkhir;

        switch (bulanValueAkhir) {
            case "01":
                bulanNameAkhir = "Januari";
                break;
            case "02":
                bulanNameAkhir = "Februari";
                break;
            case "03":
                bulanNameAkhir = "Maret";
                break;
            case "04":
                bulanNameAkhir = "April";
                break;
            case "05":
                bulanNameAkhir = "Mei";
                break;
            case "06":
                bulanNameAkhir = "Juni";
                break;
            case "07":
                bulanNameAkhir = "Juli";
                break;
            case "08":
                bulanNameAkhir = "Agustus";
                break;
            case "09":
                bulanNameAkhir = "September";
                break;
            case "10":
                bulanNameAkhir = "Oktober";
                break;
            case "11":
                bulanNameAkhir = "November";
                break;
            case "12":
                bulanNameAkhir = "Desember";
                break;
            default:
                bulanNameAkhir = "Not found";
                break;
        }

        myViewHolder.detailNotifikasiTV.setText("Untuk tanggal "+String.valueOf(Integer.parseInt(dayDateMulai))+" "+bulanNameMulai+" "+yearDateMulai+" s/d "+String.valueOf(Integer.parseInt(dayDateAkhir))+" "+bulanNameAkhir+" "+yearDateAkhir);

        String input_date = dataPersonalNotification.getCreated_at().substring(0,10);
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

        String time = dataPersonalNotification.getCreated_at().substring(10,16);

        myViewHolder.tanggalNotifikasi.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate+" "+time);

        if (dataPersonalNotification.getStatus_read().equals("1")){
            myViewHolder.statusNotifikasiTV.setTextColor(Color.parseColor("#7d7d7d"));
            myViewHolder.statusNotifikasiTV.setTypeface(myViewHolder.statusNotifikasiTV.getTypeface(), Typeface.NORMAL);
            myViewHolder.detailNotifikasiTV.setTextColor(Color.parseColor("#7d7d7d"));
            myViewHolder.detailNotifikasiTV.setTextColor(Color.parseColor("#7d7d7d"));
            myViewHolder.tanggalNotifikasi.setTextColor(Color.parseColor("#7d7d7d"));
            myViewHolder.lineLimit.setBackgroundColor(Color.parseColor("#EAEAEA"));
        } else {
            myViewHolder.statusNotifikasiTV.setTypeface(myViewHolder.statusNotifikasiTV.getTypeface(), Typeface.BOLD);
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Intent intent = new Intent(mContext, DetailPermohonanCutiActivity.class);
//                    intent.putExtra("kode", "notif");
//                    intent.putExtra("id_izin",dataPersonalNotification.getId_record());
//                    mContext.startActivity(intent);
            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView statusNotifikasiTV, detailNotifikasiTV, tanggalNotifikasi, lineLimit;
        LinearLayout parentPart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            statusNotifikasiTV = itemView.findViewById(R.id.status_notifikasi_tv);
            detailNotifikasiTV = itemView.findViewById(R.id.detail_notifikasi_tv);
            tanggalNotifikasi = itemView.findViewById(R.id.tanggal_notifikasi);
            lineLimit = itemView.findViewById(R.id.line_limit);
            parentPart = itemView.findViewById(R.id.parent_part);
        }
    }


}