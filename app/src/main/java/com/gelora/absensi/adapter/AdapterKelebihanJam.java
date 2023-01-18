package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailKelebihanJamActivity;
import com.gelora.absensi.DetailPulangCepatActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataKelebihanJam;
import com.gelora.absensi.model.DataPulangCepat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterKelebihanJam extends RecyclerView.Adapter<AdapterKelebihanJam.MyViewHolder> {

    private DataKelebihanJam[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterKelebihanJam(DataKelebihanJam[] data, DetailKelebihanJamActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_data_kelebihan_jam,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataKelebihanJam dataKelebihanJam = data[i];
        String namaStatus = "";

        myViewHolder.dateAbsen.setText(dataKelebihanJam.getTanggal_masuk());
        myViewHolder.jamShift.setText(dataKelebihanJam.getJam_shift());

        String input_date_checkin = dataKelebihanJam.getTanggal_masuk();
        String dayDateCheckin = input_date_checkin.substring(8,10);
        String yearDateCheckin = input_date_checkin.substring(0,4);
        String bulanValueCheckin = input_date_checkin.substring(5,7);
        String bulanNameCheckin;

        switch (bulanValueCheckin) {
            case "01":
                bulanNameCheckin = "Januari";
                break;
            case "02":
                bulanNameCheckin = "Februari";
                break;
            case "03":
                bulanNameCheckin = "Maret";
                break;
            case "04":
                bulanNameCheckin = "April";
                break;
            case "05":
                bulanNameCheckin = "Mei";
                break;
            case "06":
                bulanNameCheckin = "Juni";
                break;
            case "07":
                bulanNameCheckin = "Juli";
                break;
            case "08":
                bulanNameCheckin = "Agustus";
                break;
            case "09":
                bulanNameCheckin = "September";
                break;
            case "10":
                bulanNameCheckin = "Oktober";
                break;
            case "11":
                bulanNameCheckin = "November";
                break;
            case "12":
                bulanNameCheckin = "Desember";
                break;
            default:
                bulanNameCheckin = "Not found";
                break;
        }

        myViewHolder.dateCheckin.setText(String.valueOf(Integer.parseInt(dayDateCheckin))+" "+bulanNameCheckin+" "+yearDateCheckin);
        myViewHolder.dateCheckout.setText(dataKelebihanJam.getTanggal_pulang());

        myViewHolder.namaShift.setText(dataKelebihanJam.getShift());

        if (myViewHolder.dateCheckout.getText().toString().equals("")){
            myViewHolder.dateCheckout.setText("---- - -- - --");
        } else {
            String input_date_checkout = dataKelebihanJam.getTanggal_pulang();
            String dayDateCheckout = input_date_checkout.substring(8,10);
            String yearDateCheckout = input_date_checkout.substring(0,4);
            String bulanValueCheckout = input_date_checkout.substring(5,7);
            String bulanNameCheckout;

            switch (bulanValueCheckout) {
                case "01":
                    bulanNameCheckout = "Januari";
                    break;
                case "02":
                    bulanNameCheckout = "Februari";
                    break;
                case "03":
                    bulanNameCheckout = "Maret";
                    break;
                case "04":
                    bulanNameCheckout = "April";
                    break;
                case "05":
                    bulanNameCheckout = "Mei";
                    break;
                case "06":
                    bulanNameCheckout = "Juni";
                    break;
                case "07":
                    bulanNameCheckout = "Juli";
                    break;
                case "08":
                    bulanNameCheckout = "Agustus";
                    break;
                case "09":
                    bulanNameCheckout = "September";
                    break;
                case "10":
                    bulanNameCheckout = "Oktober";
                    break;
                case "11":
                    bulanNameCheckout = "November";
                    break;
                case "12":
                    bulanNameCheckout = "Desember";
                    break;
                default:
                    bulanNameCheckout = "Not found";
                    break;
            }
            myViewHolder.dateCheckout.setText(String.valueOf(Integer.parseInt(dayDateCheckout))+" "+bulanNameCheckout+" "+yearDateCheckout);
        }

        if (myViewHolder.dateAbsen.getText().toString().equals("")){
            myViewHolder.dateAbsen.setText("Hari ini");
        } else {
            String input_date = dataKelebihanJam.getTanggal_masuk();
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

            myViewHolder.dateAbsen.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);

        }

        if (String.valueOf(dataKelebihanJam.getTimezone_masuk()).equals("null")){
            myViewHolder.checkinTime.setText(dataKelebihanJam.getJam_masuk());
        } else {
            myViewHolder.checkinTime.setText(dataKelebihanJam.getJam_masuk()+" "+dataKelebihanJam.getTimezone_masuk());
        }

        if (dataKelebihanJam.getStatus_pulang().equals("0")){
            myViewHolder.checkoutTime.setText("-- : -- : -- ---");
        } else {
            if (String.valueOf(dataKelebihanJam.getTimezone_pulang()).equals("null")){
                myViewHolder.checkoutTime.setText(dataKelebihanJam.getJam_pulang());
            } else {
                myViewHolder.checkoutTime.setText(dataKelebihanJam.getJam_pulang()+" "+dataKelebihanJam.getTimezone_pulang());
            }
        }

        myViewHolder.checkinPoint.setText(dataKelebihanJam.getCheckin_point());

        if (myViewHolder.checkinPoint.getText().toString().equals("")){
            myViewHolder.checkinPoint.setText(sharedPrefManager.getSpNama());
        } else {
            myViewHolder.checkinPoint.setText(dataKelebihanJam.getCheckin_point());
        }

        if (dataKelebihanJam.getStatus_pulang().equals("0")){
            myViewHolder.checkoutPoint.setText("-");
        } else {
            myViewHolder.checkoutPoint.setText(dataKelebihanJam.getCheckout_point());
        }

        if(!dataKelebihanJam.getOvertime().substring(0, 2).equals("00")){ // 01:01:01
            if (!dataKelebihanJam.getOvertime().substring(3, 5).equals("00")){ // 01:01:01
                if(!dataKelebihanJam.getOvertime().substring(6,8).equals("00")){
                    myViewHolder.overTimeTV.setText(String.valueOf(Integer.parseInt(dataKelebihanJam.getOvertime().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataKelebihanJam.getOvertime().substring(3,5)))+" menit "+String.valueOf(Integer.parseInt(dataKelebihanJam.getOvertime().substring(6,8)))+" detik");
                } else {
                    myViewHolder.overTimeTV.setText(String.valueOf(Integer.parseInt(dataKelebihanJam.getOvertime().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataKelebihanJam.getOvertime().substring(3,5)))+" menit");
                }
            } else { // 01:00:01
                if(!dataKelebihanJam.getOvertime().substring(6,8).equals("00")){
                    myViewHolder.overTimeTV.setText(String.valueOf(Integer.parseInt(dataKelebihanJam.getOvertime().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataKelebihanJam.getOvertime().substring(6,8)))+" detik");
                } else {
                    myViewHolder.overTimeTV.setText(String.valueOf(Integer.parseInt(dataKelebihanJam.getOvertime().substring(0,2)))+" jam");
                }
            }
        } else { // 00:01:01
            if (!dataKelebihanJam.getOvertime().substring(3, 5).equals("00")){ // 00:01:01
                if(!dataKelebihanJam.getOvertime().substring(6,8).equals("00")){
                    myViewHolder.overTimeTV.setText(String.valueOf(Integer.parseInt(dataKelebihanJam.getOvertime().substring(3,5)))+" menit "+String.valueOf(Integer.parseInt(dataKelebihanJam.getOvertime().substring(6,8)))+" detik");
                } else {
                    myViewHolder.overTimeTV.setText(String.valueOf(Integer.parseInt(dataKelebihanJam.getOvertime().substring(3,5)))+" menit");
                }
            } else { // 00:00:01
                if(!dataKelebihanJam.getOvertime().substring(6,8).equals("00")){
                    myViewHolder.overTimeTV.setText(String.valueOf(Integer.parseInt(dataKelebihanJam.getOvertime().substring(6,8)))+" detik");
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView overTimeTV, namaShift, jamShift, dateAbsen, dateCheckin, dateCheckout, checkinTime, checkinPoint, checkoutTime, checkoutPoint;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dateAbsen = itemView.findViewById(R.id.date_absen_tv);
            dateCheckin = itemView.findViewById(R.id.date_checkin_tv);
            dateCheckout = itemView.findViewById(R.id.date_checkout_tv);
            checkinTime = itemView.findViewById(R.id.time_checkin_tv);
            checkinPoint = itemView.findViewById(R.id.checkin_point_tv);
            checkoutTime = itemView.findViewById(R.id.time_checkout_tv);
            checkoutPoint = itemView.findViewById(R.id.checkout_point_tv);
            jamShift = itemView.findViewById(R.id.jam_shift);
            namaShift = itemView.findViewById(R.id.nama_shift);
            overTimeTV = itemView.findViewById(R.id.overtime_tv);
        }
    }


}