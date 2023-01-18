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

import com.gelora.absensi.DetailPulangCepatActivity;
import com.gelora.absensi.MonitoringAbsensiBagianActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataMonitoringKehadiranBagian;
import com.gelora.absensi.model.DataPulangCepat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterKehadiranBagian extends RecyclerView.Adapter<AdapterKehadiranBagian.MyViewHolder> {

    private DataMonitoringKehadiranBagian[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterKehadiranBagian(DataMonitoringKehadiranBagian[] data, MonitoringAbsensiBagianActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_kehadiran_karyawan,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataMonitoringKehadiranBagian dataMonitoringKehadiranBagian = data[i];
        String namaStatus = "";

        myViewHolder.namaKaryawan.setText(dataMonitoringKehadiranBagian.getNmKaryawan().toUpperCase());
        myViewHolder.nikKaryawan.setText(dataMonitoringKehadiranBagian.getNIK());
        myViewHolder.jamShift.setText(dataMonitoringKehadiranBagian.getDatang_shift()+" - "+dataMonitoringKehadiranBagian.getPulang_shift());

        String input_date_checkin = dataMonitoringKehadiranBagian.getTanggal_masuk();
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

        myViewHolder.namaShift.setText(dataMonitoringKehadiranBagian.getNama_status()+" - "+dataMonitoringKehadiranBagian.getNama_shift());

        if (dataMonitoringKehadiranBagian.getStatus_pulang().equals("0")){
            myViewHolder.dateCheckout.setText("---- - -- - --");
        } else {
            String input_date_checkout = dataMonitoringKehadiranBagian.getTanggal_pulang();
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

        if (String.valueOf(dataMonitoringKehadiranBagian.getTimezone_masuk()).equals("null")){
            myViewHolder.checkinTime.setText(dataMonitoringKehadiranBagian.getJam_masuk());
        } else {
            myViewHolder.checkinTime.setText(dataMonitoringKehadiranBagian.getJam_masuk()+" "+dataMonitoringKehadiranBagian.getTimezone_masuk());
        }

        if (dataMonitoringKehadiranBagian.getStatus_pulang().equals("0")){
            myViewHolder.checkoutTime.setText("-- : -- : -- ---");
        } else {
            if (String.valueOf(dataMonitoringKehadiranBagian.getTimezone_pulang()).equals("null")){
                myViewHolder.checkoutTime.setText(dataMonitoringKehadiranBagian.getJam_pulang());
            } else {
                myViewHolder.checkoutTime.setText(dataMonitoringKehadiranBagian.getJam_pulang()+" "+dataMonitoringKehadiranBagian.getTimezone_pulang());
            }
        }

        myViewHolder.checkinPoint.setText(dataMonitoringKehadiranBagian.getCheckin_point());

        if (myViewHolder.checkinPoint.getText().toString().equals("")){
            myViewHolder.checkinPoint.setText(sharedPrefManager.getSpNama());
        } else {
            myViewHolder.checkinPoint.setText(dataMonitoringKehadiranBagian.getCheckin_point());
        }

        if (dataMonitoringKehadiranBagian.getStatus_pulang().equals("0")){
            myViewHolder.checkoutPoint.setText("-");
        } else {
            myViewHolder.checkoutPoint.setText(dataMonitoringKehadiranBagian.getCheckout_point());
        }

        if (dataMonitoringKehadiranBagian.getStatus_terlambat().equals("2")){
           myViewHolder.checkinPart.setBackgroundResource(R.drawable.shape_pulang_cepat_detail);
           myViewHolder.checkoutPart.setBackgroundResource(R.drawable.shape_pulang_cepat_detail);
           myViewHolder.keteranganAbsensi.setVisibility(View.VISIBLE);
           myViewHolder.keterlambatanPart.setVisibility(View.VISIBLE);

            if(!dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(0, 2).equals("00")){ // 01:01:01
                if (!dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(3, 5).equals("00")){ // 01:01:01
                    if(!dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8).equals("00")){
                        myViewHolder.keterlambatanCheckin.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(3,5)))+" menit "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8)))+" detik");
                    } else {
                        myViewHolder.keterlambatanCheckin.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(3,5)))+" menit");
                    }
                } else { // 01:00:01
                    if(!dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8).equals("00")){
                        myViewHolder.keterlambatanCheckin.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8)))+" detik");
                    } else {
                        myViewHolder.keterlambatanCheckin.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(0,2)))+" jam");
                    }
                }
            } else { // 00:01:01
                if (!dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(3, 5).equals("00")){ // 00:01:01
                    if(!dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8).equals("00")){
                        myViewHolder.keterlambatanCheckin.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(3,5)))+" menit "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8)))+" detik");
                    } else {
                        myViewHolder.keterlambatanCheckin.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(3,5)))+" menit");
                    }
                } else { // 00:00:01
                    if(!dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8).equals("00")){
                        myViewHolder.keterlambatanCheckin.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8)))+" detik");
                    }
                }
            }

           if (dataMonitoringKehadiranBagian.getStatus_pulang().equals("3")){
               myViewHolder.kelebihanJamPart.setVisibility(View.VISIBLE);

               if(!dataMonitoringKehadiranBagian.getKelebihan_jam().substring(0, 2).equals("00")){ // 01:01:01
                   if (!dataMonitoringKehadiranBagian.getKelebihan_jam().substring(3, 5).equals("00")){ // 01:01:01
                       if(!dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8).equals("00")){
                           myViewHolder.kelebihanJam.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(3,5)))+" menit "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8)))+" detik");
                       } else {
                           myViewHolder.kelebihanJam.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(3,5)))+" menit");
                       }
                   } else { // 01:00:01
                       if(!dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8).equals("00")){
                           myViewHolder.kelebihanJam.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8)))+" detik");
                       } else {
                           myViewHolder.kelebihanJam.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(0,2)))+" jam");
                       }
                   }
               } else { // 00:01:01
                   if (!dataMonitoringKehadiranBagian.getKelebihan_jam().substring(3, 5).equals("00")){ // 00:01:01
                       if(!dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8).equals("00")){
                           myViewHolder.kelebihanJam.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(3,5)))+" menit "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8)))+" detik");
                       } else {
                           myViewHolder.kelebihanJam.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(3,5)))+" menit");
                       }
                   } else { // 00:00:01
                       if(!dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8).equals("00")){
                           myViewHolder.kelebihanJam.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8)))+" detik");
                       }
                   }
               }

           } else {
               myViewHolder.kelebihanJamPart.setVisibility(View.GONE);
           }
        } else if (dataMonitoringKehadiranBagian.getStatus_pulang().equals("3")) {
            myViewHolder.checkinPart.setBackgroundResource(R.drawable.shape_pulang_cepat_detail);
            myViewHolder.checkoutPart.setBackgroundResource(R.drawable.shape_pulang_cepat_detail);
            myViewHolder.keteranganAbsensi.setVisibility(View.VISIBLE);
            myViewHolder.kelebihanJamPart.setVisibility(View.VISIBLE);

            if(!dataMonitoringKehadiranBagian.getKelebihan_jam().substring(0, 2).equals("00")){ // 01:01:01
                if (!dataMonitoringKehadiranBagian.getKelebihan_jam().substring(3, 5).equals("00")){ // 01:01:01
                    if(!dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8).equals("00")){
                        myViewHolder.kelebihanJam.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(3,5)))+" menit "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8)))+" detik");
                    } else {
                        myViewHolder.kelebihanJam.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(3,5)))+" menit");
                    }
                } else { // 01:00:01
                    if(!dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8).equals("00")){
                        myViewHolder.kelebihanJam.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8)))+" detik");
                    } else {
                        myViewHolder.kelebihanJam.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(0,2)))+" jam");
                    }
                }
            } else { // 00:01:01
                if (!dataMonitoringKehadiranBagian.getKelebihan_jam().substring(3, 5).equals("00")){ // 00:01:01
                    if(!dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8).equals("00")){
                        myViewHolder.kelebihanJam.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(3,5)))+" menit "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8)))+" detik");
                    } else {
                        myViewHolder.kelebihanJam.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(3,5)))+" menit");
                    }
                } else { // 00:00:01
                    if(!dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8).equals("00")){
                        myViewHolder.kelebihanJam.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getKelebihan_jam().substring(6,8)))+" detik");
                    }
                }
            }

            if (dataMonitoringKehadiranBagian.getStatus_terlambat().equals("2")){
                myViewHolder.keterlambatanPart.setVisibility(View.VISIBLE);

                if(!dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(0, 2).equals("00")){ // 01:01:01
                    if (!dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(3, 5).equals("00")){ // 01:01:01
                        if(!dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8).equals("00")){
                            myViewHolder.keterlambatanCheckin.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(3,5)))+" menit "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8)))+" detik");
                        } else {
                            myViewHolder.keterlambatanCheckin.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(3,5)))+" menit");
                        }
                    } else { // 01:00:01
                        if(!dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8).equals("00")){
                            myViewHolder.keterlambatanCheckin.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8)))+" detik");
                        } else {
                            myViewHolder.keterlambatanCheckin.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(0,2)))+" jam");
                        }
                    }
                } else { // 00:01:01
                    if (!dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(3, 5).equals("00")){ // 00:01:01
                        if(!dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8).equals("00")){
                            myViewHolder.keterlambatanCheckin.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(3,5)))+" menit "+String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8)))+" detik");
                        } else {
                            myViewHolder.keterlambatanCheckin.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(3,5)))+" menit");
                        }
                    } else { // 00:00:01
                        if(!dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8).equals("00")){
                            myViewHolder.keterlambatanCheckin.setText(String.valueOf(Integer.parseInt(dataMonitoringKehadiranBagian.getWaktu_terlambat().substring(6,8)))+" detik");
                        }
                    }
                }

            } else {
                myViewHolder.keterlambatanPart.setVisibility(View.GONE);
            }
        } else {
            myViewHolder.checkinPart.setBackgroundResource(R.drawable.shape_checkin_3);
            myViewHolder.checkoutPart.setBackgroundResource(R.drawable.shape_checkout_3);
            myViewHolder.keteranganAbsensi.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView keterlambatanCheckin, kelebihanJam, namaShift, jamShift, namaKaryawan, nikKaryawan, dateCheckin, dateCheckout, checkinTime, checkinPoint, checkoutTime, checkoutPoint;
        LinearLayout keteranganAbsensi, keterlambatanPart, kelebihanJamPart, checkinPart, checkoutPart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            namaKaryawan = itemView.findViewById(R.id.nama_staff_tv);
            nikKaryawan = itemView.findViewById(R.id.nik_staff_tv);
            dateCheckin = itemView.findViewById(R.id.date_checkin_tv);
            dateCheckout = itemView.findViewById(R.id.date_checkout_tv);
            checkinTime = itemView.findViewById(R.id.time_checkin_tv);
            checkinPoint = itemView.findViewById(R.id.checkin_point_tv);
            checkoutTime = itemView.findViewById(R.id.time_checkout_tv);
            checkoutPoint = itemView.findViewById(R.id.checkout_point_tv);
            jamShift = itemView.findViewById(R.id.jam_shift);
            namaShift = itemView.findViewById(R.id.nama_shift);
            keteranganAbsensi = itemView.findViewById(R.id.keterangan_absensi);
            keterlambatanCheckin = itemView.findViewById(R.id.keterlambatan_checkin);
            kelebihanJam = itemView.findViewById(R.id.kelebihan_jam);
            keterlambatanPart = itemView.findViewById(R.id.keterlambatan_part);
            kelebihanJamPart = itemView.findViewById(R.id.kelebihan_jam_part);
            checkinPart = itemView.findViewById(R.id.checkin_part);
            checkoutPart = itemView.findViewById(R.id.checkout_part);
        }
    }


}