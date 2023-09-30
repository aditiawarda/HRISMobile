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
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailDataExitClearanceActivity;
import com.gelora.absensi.DetailPermohonanCutiActivity;
import com.gelora.absensi.DetailPermohonanIzinActivity;
import com.gelora.absensi.ExitClearanceActivity;
import com.gelora.absensi.ListNotifikasiActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.ListDataExitClearanceOut;
import com.gelora.absensi.model.ListPermohonanIzin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterExitClearanceOut extends RecyclerView.Adapter<AdapterExitClearanceOut.MyViewHolder> {

    private ListDataExitClearanceOut[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterExitClearanceOut(ListDataExitClearanceOut[] data, ExitClearanceActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_data_keluar_exit_clearance,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ListDataExitClearanceOut listDataExitClearanceOut = data[i];

        myViewHolder.statusDataTV.setText(listDataExitClearanceOut.getStatus_notifikasi());

        if(listDataExitClearanceOut.getStatus_notifikasi().equals("Menunggu verifikasi atasan langsung")) {
            myViewHolder.statusDataTV.setTextColor(Color.parseColor("#C16D02"));
        } else if(listDataExitClearanceOut.getStatus_notifikasi().equals("Menunggu verifikasi atasan langsung dan bagian terkait")){
            myViewHolder.statusDataTV.setTextColor(Color.parseColor("#C16D02"));
        } else if(listDataExitClearanceOut.getStatus_notifikasi().equals("Menunggu verifikasi bagian terkait")) {
            myViewHolder.statusDataTV.setTextColor(Color.parseColor("#C16D02"));
        } else if(listDataExitClearanceOut.getStatus_notifikasi().equals("Menunggu verifikasi akhir HRD")) {
            myViewHolder.statusDataTV.setTextColor(Color.parseColor("#C16D02"));
        } else if(listDataExitClearanceOut.getStatus_notifikasi().equals("Data exit clearance sudah diverifikasi HRD")) {
            myViewHolder.statusDataTV.setTextColor(Color.parseColor("#089b02"));
        }

        String input_date_keluar = listDataExitClearanceOut.getTgl_keluar();
        String dayDateKeluar = input_date_keluar.substring(8,10);
        String yearDateKeluar = input_date_keluar.substring(0,4);
        String bulanValueKeluar = input_date_keluar.substring(5,7);
        String bulanNameKeluar;

        switch (bulanValueKeluar) {
            case "01":
                bulanNameKeluar = "Jan";
                break;
            case "02":
                bulanNameKeluar = "Feb";
                break;
            case "03":
                bulanNameKeluar = "Mar";
                break;
            case "04":
                bulanNameKeluar = "Apr";
                break;
            case "05":
                bulanNameKeluar = "Mei";
                break;
            case "06":
                bulanNameKeluar = "Jun";
                break;
            case "07":
                bulanNameKeluar = "Jul";
                break;
            case "08":
                bulanNameKeluar = "Agu";
                break;
            case "09":
                bulanNameKeluar = "Sep";
                break;
            case "10":
                bulanNameKeluar = "Okt";
                break;
            case "11":
                bulanNameKeluar = "Nov";
                break;
            case "12":
                bulanNameKeluar = "Des";
                break;
            default:
                bulanNameKeluar = "Not found";
                break;
        }

        myViewHolder.tglKeluarTV.setText("Keluar : "+String.valueOf(Integer.parseInt(dayDateKeluar))+" "+bulanNameKeluar+" "+yearDateKeluar);

        String input_date = listDataExitClearanceOut.getTime_notifikasi().substring(0,10);
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
                bulanName = "Jan";
                break;
            case "02":
                bulanName = "Feb";
                break;
            case "03":
                bulanName = "Mar";
                break;
            case "04":
                bulanName = "Apr";
                break;
            case "05":
                bulanName = "Mei";
                break;
            case "06":
                bulanName = "Jun";
                break;
            case "07":
                bulanName = "Jul";
                break;
            case "08":
                bulanName = "Agu";
                break;
            case "09":
                bulanName = "Sep";
                break;
            case "10":
                bulanName = "Okt";
                break;
            case "11":
                bulanName = "Nov";
                break;
            case "12":
                bulanName = "Des";
                break;
            default:
                bulanName = "Not found";
                break;
        }

        String time = listDataExitClearanceOut.getTime_notifikasi().substring(10,16);

        myViewHolder.tanggalNotifikasi.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate+" "+time);

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailDataExitClearanceActivity.class);
//                intent.putExtra("kode", "notif");
//                intent.putExtra("id_izin",listPermohonanIzin.getId());
                mContext.startActivity(intent);
                Toast.makeText(mContext, String.valueOf(listDataExitClearanceOut.getId_core()), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView statusDataTV, tglKeluarTV, tanggalNotifikasi, lineLimit;
        LinearLayout parentPart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            statusDataTV = itemView.findViewById(R.id.status_data_tv);
            tglKeluarTV = itemView.findViewById(R.id.tanggal_resign_tv);
            tanggalNotifikasi = itemView.findViewById(R.id.tanggal_notifikasi);
            lineLimit = itemView.findViewById(R.id.line_limit);
            parentPart = itemView.findViewById(R.id.parent_part);
        }
    }

}