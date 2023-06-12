package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.MapsActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.RecordAbsensiActivity;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataRecordAbsensi;

import net.cachapa.expandablelayout.ExpandableLayout;

public class AdapterDataAbsensiMore extends RecyclerView.Adapter<AdapterDataAbsensiMore.MyViewHolder> {

    private final DataRecordAbsensi[] data;
    private final Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;
    String statusExpand = "0";

    public AdapterDataAbsensiMore(DataRecordAbsensi[] data, RecordAbsensiActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager = new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_data_absensi,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataRecordAbsensi dataAbsensi = data[i];

        String dayDate = dataAbsensi.getTanggal().substring(8,10);
        String yearDate = dataAbsensi.getTanggal().substring(0,4);
        String bulanValue = dataAbsensi.getTanggal().substring(5,7);
        String bulanName;

        switch (bulanValue) {
            case "01":
                bulanName = "JAN";
                break;
            case "02":
                bulanName = "FEB";
                break;
            case "03":
                bulanName = "MAR";
                break;
            case "04":
                bulanName = "APR";
                break;
            case "05":
                bulanName = "MEI";
                break;
            case "06":
                bulanName = "JUN";
                break;
            case "07":
                bulanName = "JUL";
                break;
            case "08":
                bulanName = "AGU";
                break;
            case "09":
                bulanName = "SEP";
                break;
            case "10":
                bulanName = "OKT";
                break;
            case "11":
                bulanName = "NOV";
                break;
            case "12":
                bulanName = "DES";
                break;
            default:
                bulanName = "Not found";
                break;
        }

        myViewHolder.dateAbsensi.setText(dayDate+" "+bulanName+" "+yearDate);

        if(dataAbsensi.getKet_kehadiran().equals("Hadir")){
            myViewHolder.partHadir.setVisibility(View.VISIBLE);
            myViewHolder.partDiliburkan.setVisibility(View.GONE);
            myViewHolder.partIzin.setVisibility(View.GONE);
            myViewHolder.partLibur.setVisibility(View.GONE);
            myViewHolder.partMinggu.setVisibility(View.GONE);
            myViewHolder.partAlpha.setVisibility(View.GONE);
            myViewHolder.warningMark.setVisibility(View.GONE);

            myViewHolder.higlightTV.setText(dataAbsensi.getShift()+" ("+dataAbsensi.getJam_shift()+")");
            myViewHolder.higlightTV.setTextColor(Color.parseColor("#309A35"));
            myViewHolder.dateAbsensi.setTextColor(Color.parseColor("#309A35"));

            String input_date_checkin = dataAbsensi.getTanggal_masuk();
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

            if (String.valueOf(dataAbsensi.getTimezone_masuk()).equals("null")){
                myViewHolder.timeCheckin.setText(dataAbsensi.getJam_masuk());
            } else {
                myViewHolder.timeCheckin.setText(dataAbsensi.getJam_masuk()+" "+dataAbsensi.getTimezone_masuk());
            }

            if (myViewHolder.checkinPoint.getText().toString().equals("")){
                myViewHolder.checkinPoint.setText(sharedPrefManager.getSpNama());
            } else {
                myViewHolder.checkinPoint.setText(dataAbsensi.getCheckin_point());
            }

            if(dataAbsensi.getStatus_terlambat().equals("2")){
                myViewHolder.partTerlambat.setVisibility(View.VISIBLE);
                myViewHolder.warningMark.setVisibility(View.VISIBLE);

                if(!dataAbsensi.getWaktu_terlambat().substring(0, 2).equals("00")){ // 01:01:01
                    if (!dataAbsensi.getWaktu_terlambat().substring(3, 5).equals("00")){ // 01:01:01
                        if(!dataAbsensi.getWaktu_terlambat().substring(6,8).equals("00")){
                            myViewHolder.terlambatTime.setText(String.valueOf(Integer.parseInt(dataAbsensi.getWaktu_terlambat().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataAbsensi.getWaktu_terlambat().substring(3,5)))+" menit "+String.valueOf(Integer.parseInt(dataAbsensi.getWaktu_terlambat().substring(6,8)))+" detik");
                        } else {
                            myViewHolder.terlambatTime.setText(String.valueOf(Integer.parseInt(dataAbsensi.getWaktu_terlambat().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataAbsensi.getWaktu_terlambat().substring(3,5)))+" menit");
                        }
                    } else { // 01:00:01
                        if(!dataAbsensi.getWaktu_terlambat().substring(6,8).equals("00")){
                            myViewHolder.terlambatTime.setText(String.valueOf(Integer.parseInt(dataAbsensi.getWaktu_terlambat().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataAbsensi.getWaktu_terlambat().substring(6,8)))+" detik");
                        } else {
                            myViewHolder.terlambatTime.setText(String.valueOf(Integer.parseInt(dataAbsensi.getWaktu_terlambat().substring(0,2)))+" jam");
                        }
                    }
                } else { // 00:01:01
                    if (!dataAbsensi.getWaktu_terlambat().substring(3, 5).equals("00")){ // 00:01:01
                        if(!dataAbsensi.getWaktu_terlambat().substring(6,8).equals("00")){
                            myViewHolder.terlambatTime.setText(String.valueOf(Integer.parseInt(dataAbsensi.getWaktu_terlambat().substring(3,5)))+" menit "+String.valueOf(Integer.parseInt(dataAbsensi.getWaktu_terlambat().substring(6,8)))+" detik");
                        } else {
                            myViewHolder.terlambatTime.setText(String.valueOf(Integer.parseInt(dataAbsensi.getWaktu_terlambat().substring(3,5)))+" menit");
                        }
                    } else { // 00:00:01
                        if(!dataAbsensi.getWaktu_terlambat().substring(6,8).equals("00")){
                            myViewHolder.terlambatTime.setText(String.valueOf(Integer.parseInt(dataAbsensi.getWaktu_terlambat().substring(6,8)))+" detik");
                        }
                    }
                }

            } else if(dataAbsensi.getStatus_terlambat().equals("1")){
                myViewHolder.partTerlambat.setVisibility(View.GONE);
            }

            if (dataAbsensi.getStatus_pulang().equals("0")){
                myViewHolder.dateCheckout.setText("---- - -- - --");
            } else {
                String input_date_checkout = dataAbsensi.getTanggal_pulang();
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

            if (dataAbsensi.getStatus_pulang().equals("0")){
                myViewHolder.timeCheckout.setText("-- : -- : -- ---");
            } else {
                if (String.valueOf(dataAbsensi.getTimezone_pulang()).equals("null")){
                    myViewHolder.timeCheckout.setText(dataAbsensi.getJam_pulang());
                } else {
                    myViewHolder.timeCheckout.setText(dataAbsensi.getJam_pulang()+" "+dataAbsensi.getTimezone_pulang());
                }
            }

            if (dataAbsensi.getStatus_pulang().equals("0")){
                myViewHolder.checkoutPoint.setText("-");
            } else {
                myViewHolder.checkoutPoint.setText(dataAbsensi.getCheckout_point());
            }

            if(dataAbsensi.getStatus_pulang().equals("3")) {
                myViewHolder.partPulangCepat.setVisibility(View.GONE);
                myViewHolder.partOvertime.setVisibility(View.VISIBLE);
                myViewHolder.partNoCheckout.setVisibility(View.GONE);

                if(!dataAbsensi.getKelebihan_jam().substring(0, 2).equals("00")){ // 01:01:01
                    if (!dataAbsensi.getKelebihan_jam().substring(3, 5).equals("00")){ // 01:01:01
                        if(!dataAbsensi.getKelebihan_jam().substring(6,8).equals("00")){
                            myViewHolder.overTime.setText(String.valueOf(Integer.parseInt(dataAbsensi.getKelebihan_jam().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataAbsensi.getKelebihan_jam().substring(3,5)))+" menit "+String.valueOf(Integer.parseInt(dataAbsensi.getKelebihan_jam().substring(6,8)))+" detik");
                        } else {
                            myViewHolder.overTime.setText(String.valueOf(Integer.parseInt(dataAbsensi.getKelebihan_jam().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataAbsensi.getKelebihan_jam().substring(3,5)))+" menit");
                        }
                    } else { // 01:00:01
                        if(!dataAbsensi.getKelebihan_jam().substring(6,8).equals("00")){
                            myViewHolder.overTime.setText(String.valueOf(Integer.parseInt(dataAbsensi.getKelebihan_jam().substring(0,2)))+" jam "+String.valueOf(Integer.parseInt(dataAbsensi.getKelebihan_jam().substring(6,8)))+" detik");
                        } else {
                            myViewHolder.overTime.setText(String.valueOf(Integer.parseInt(dataAbsensi.getKelebihan_jam().substring(0,2)))+" jam");
                        }
                    }
                } else { // 00:01:01
                    if (!dataAbsensi.getKelebihan_jam().substring(3, 5).equals("00")){ // 00:01:01
                        if(!dataAbsensi.getKelebihan_jam().substring(6,8).equals("00")){
                            myViewHolder.overTime.setText(String.valueOf(Integer.parseInt(dataAbsensi.getKelebihan_jam().substring(3,5)))+" menit "+String.valueOf(Integer.parseInt(dataAbsensi.getKelebihan_jam().substring(6,8)))+" detik");
                        } else {
                            myViewHolder.overTime.setText(String.valueOf(Integer.parseInt(dataAbsensi.getKelebihan_jam().substring(3,5)))+" menit");
                        }
                    } else { // 00:00:01
                        if(!dataAbsensi.getKelebihan_jam().substring(6,8).equals("00")){
                            myViewHolder.overTime.setText(String.valueOf(Integer.parseInt(dataAbsensi.getKelebihan_jam().substring(6,8)))+" detik");
                        }
                    }
                }

            } else if(dataAbsensi.getStatus_pulang().equals("2")){
                myViewHolder.partPulangCepat.setVisibility(View.VISIBLE);
                myViewHolder.partOvertime.setVisibility(View.GONE);
                myViewHolder.partNoCheckout.setVisibility(View.GONE);
            } else if(dataAbsensi.getStatus_pulang().equals("1")){
                myViewHolder.partPulangCepat.setVisibility(View.GONE);
                myViewHolder.partOvertime.setVisibility(View.GONE);
                myViewHolder.partNoCheckout.setVisibility(View.GONE);
            } else if(dataAbsensi.getStatus_pulang().equals("0")){
                myViewHolder.partPulangCepat.setVisibility(View.GONE);
                myViewHolder.partOvertime.setVisibility(View.GONE);
                myViewHolder.partNoCheckout.setVisibility(View.VISIBLE);
                myViewHolder.warningMark.setVisibility(View.VISIBLE);
            }

        } else if(dataAbsensi.getKet_kehadiran().equals("Diliburkan")){
            myViewHolder.partHadir.setVisibility(View.GONE);
            myViewHolder.partDiliburkan.setVisibility(View.VISIBLE);
            myViewHolder.partIzin.setVisibility(View.GONE);
            myViewHolder.partLibur.setVisibility(View.GONE);
            myViewHolder.partMinggu.setVisibility(View.GONE);
            myViewHolder.partAlpha.setVisibility(View.GONE);
            myViewHolder.warningMark.setVisibility(View.GONE);

            myViewHolder.higlightTV.setText("Diliburkan");
            myViewHolder.higlightTV.setTextColor(Color.parseColor("#4C9BE0"));
            myViewHolder.dateAbsensi.setTextColor(Color.parseColor("#4C9BE0"));

        } else if(dataAbsensi.getKet_kehadiran().equals("Izin")){
            myViewHolder.partHadir.setVisibility(View.GONE);
            myViewHolder.partDiliburkan.setVisibility(View.GONE);
            myViewHolder.partIzin.setVisibility(View.VISIBLE);
            myViewHolder.partLibur.setVisibility(View.GONE);
            myViewHolder.partMinggu.setVisibility(View.GONE);
            myViewHolder.partAlpha.setVisibility(View.GONE);
            myViewHolder.warningMark.setVisibility(View.GONE);

            myViewHolder.higlightTV.setText(dataAbsensi.getTipe_izin());
            myViewHolder.higlightTV.setTextColor(Color.parseColor("#835BDF"));
            myViewHolder.dateAbsensi.setTextColor(Color.parseColor("#835BDF"));
            myViewHolder.alasanTV.setText(dataAbsensi.getAlasan().replaceAll("\\s+$", ""));

        } else if(dataAbsensi.getKet_kehadiran().equals("Libur")){
            myViewHolder.partHadir.setVisibility(View.GONE);
            myViewHolder.partDiliburkan.setVisibility(View.GONE);
            myViewHolder.partIzin.setVisibility(View.GONE);
            myViewHolder.partLibur.setVisibility(View.VISIBLE);
            myViewHolder.partMinggu.setVisibility(View.GONE);
            myViewHolder.partAlpha.setVisibility(View.GONE);
            myViewHolder.warningMark.setVisibility(View.GONE);

            myViewHolder.higlightTV.setText("Libur");
            myViewHolder.higlightTV.setTextColor(Color.parseColor("#C58534"));
            myViewHolder.dateAbsensi.setTextColor(Color.parseColor("#C58534"));
            myViewHolder.dalamRangkaTV.setText(dataAbsensi.getDalam_rangka());

        } else if(dataAbsensi.getKet_kehadiran().equals("Minggu")){
            myViewHolder.partHadir.setVisibility(View.GONE);
            myViewHolder.partDiliburkan.setVisibility(View.GONE);
            myViewHolder.partIzin.setVisibility(View.GONE);
            myViewHolder.partLibur.setVisibility(View.GONE);
            myViewHolder.partMinggu.setVisibility(View.VISIBLE);
            myViewHolder.partAlpha.setVisibility(View.GONE);
            myViewHolder.warningMark.setVisibility(View.GONE);

            myViewHolder.higlightTV.setText("Libur");
            myViewHolder.higlightTV.setTextColor(Color.parseColor("#C58534"));
            myViewHolder.dateAbsensi.setTextColor(Color.parseColor("#C58534"));

        } else if(dataAbsensi.getKet_kehadiran().equals("Alpha")){
            myViewHolder.partHadir.setVisibility(View.GONE);
            myViewHolder.partDiliburkan.setVisibility(View.GONE);
            myViewHolder.partIzin.setVisibility(View.GONE);
            myViewHolder.partLibur.setVisibility(View.GONE);
            myViewHolder.partMinggu.setVisibility(View.GONE);
            myViewHolder.partAlpha.setVisibility(View.VISIBLE);
            myViewHolder.warningMark.setVisibility(View.VISIBLE);

            myViewHolder.higlightTV.setText("Alpha");
            myViewHolder.higlightTV.setTextColor(Color.parseColor("#B83633"));
            myViewHolder.dateAbsensi.setTextColor(Color.parseColor("#B83633"));
        }

        myViewHolder.expandBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myViewHolder.expandableLayout.isExpanded()){
                    myViewHolder.expandableLayout.collapse();
                } else {
                    myViewHolder.expandableLayout.expand();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView overTime, terlambatTime, dalamRangkaTV, alasanTV, dateAbsensi, higlightTV, dateCheckin, dateCheckout, timeCheckin, timeCheckout, checkinPoint, checkoutPoint;
        LinearLayout partTerlambat, partOvertime, partPulangCepat, partNoCheckout, warningMark, partHadir, partDiliburkan, partIzin, partLibur, partMinggu, partAlpha, expandBTN;
        ExpandableLayout expandableLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            partHadir = itemView.findViewById(R.id.part_hadir);
            partDiliburkan = itemView.findViewById(R.id.part_diliburkan);
            partIzin = itemView.findViewById(R.id.part_izin);
            partLibur = itemView.findViewById(R.id.part_libur);
            partMinggu = itemView.findViewById(R.id.part_minggu);
            partAlpha = itemView.findViewById(R.id.part_alpha);

            partTerlambat = itemView.findViewById(R.id.part_terlambat);
            terlambatTime = itemView.findViewById(R.id.terlambat_time_tv);
            partOvertime = itemView.findViewById(R.id.part_overtime);
            overTime = itemView.findViewById(R.id.overtime_tv);
            partPulangCepat = itemView.findViewById(R.id.part_pulang_cepat);
            partNoCheckout = itemView.findViewById(R.id.part_no_checkout);

            dateAbsensi = itemView.findViewById(R.id.tgl_absensi_tv);
            higlightTV = itemView.findViewById(R.id.higlight_tv);
            expandBTN = itemView.findViewById(R.id.expand_btn);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            dateCheckin = itemView.findViewById(R.id.date_checkin);
            timeCheckin = itemView.findViewById(R.id.time_checkin);
            checkinPoint = itemView.findViewById(R.id.checkin_point);
            dateCheckout = itemView.findViewById(R.id.date_checkout);
            timeCheckout = itemView.findViewById(R.id.time_checkout);
            checkoutPoint = itemView.findViewById(R.id.checkout_point);

            alasanTV = itemView.findViewById(R.id.alasan_izin_tv);
            dalamRangkaTV = itemView.findViewById(R.id.dalam_rangka_tv);
            warningMark = itemView.findViewById(R.id.warning_mark);

        }
    }


}