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
import com.gelora.absensi.DetailTidakCheckoutActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataNoCheckout;
import com.gelora.absensi.model.DataTerlambat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterDataNoCheckout extends RecyclerView.Adapter<AdapterDataNoCheckout.MyViewHolder> {

    private DataNoCheckout[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterDataNoCheckout(DataNoCheckout[] data, DetailTidakCheckoutActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_data_no_checkout,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataNoCheckout dataNoCheckout = data[i];

        String input_date = dataNoCheckout.getTanggal_masuk();
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
        myViewHolder.shiftAbsen.setText(dataNoCheckout.getShift());
        myViewHolder.jamShift.setText(dataNoCheckout.getJam_shift());
        myViewHolder.dateAbsenCheckin.setText(dataNoCheckout.getTanggal_masuk());
        myViewHolder.jamMasuk.setText(dataNoCheckout.getJam_masuk());
        myViewHolder.checkinPoint.setText(dataNoCheckout.getCheckin_point());
        myViewHolder.ket.setText(dataNoCheckout.getKet());

        if (myViewHolder.checkinPoint.getText().toString().equals("")){
            myViewHolder.checkinPoint.setText(sharedPrefManager.getSpNama());
        } else {
            myViewHolder.checkinPoint.setText(dataNoCheckout.getCheckin_point());
        }

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateAbsen, shiftAbsen, jamShift, dateAbsenCheckin, jamMasuk, checkinPoint, ket;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dateAbsen = itemView.findViewById(R.id.date_absen_tv_no_checkout);
            shiftAbsen = itemView.findViewById(R.id.nama_shift_no_checkout);
            jamShift = itemView.findViewById(R.id.jam_shift_no_checkout);
            dateAbsenCheckin = itemView.findViewById(R.id.date_checkin_tv_no_checkout);
            jamMasuk = itemView.findViewById(R.id.time_checkin_tv_no_checkout);
            checkinPoint = itemView.findViewById(R.id.checkin_point_tv_no_checkout);
            ket = itemView.findViewById(R.id.ket_tv);
        }
    }


}