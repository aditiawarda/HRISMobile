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
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.ListPermohonanIzin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterPermohonanSaya extends RecyclerView.Adapter<AdapterPermohonanSaya.MyViewHolder> {

    private ListPermohonanIzin[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterPermohonanSaya(ListPermohonanIzin[] data, ListNotifikasiActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_permohonan_saya,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ListPermohonanIzin listPermohonanIzin = data[i];

        String tipe_pengajuan = listPermohonanIzin.getTipe_pengajuan();
        String tipe_izin = listPermohonanIzin.getTipe_izin();
        String desc_izin = listPermohonanIzin.getDeskripsi_izin();

        if(tipe_pengajuan.equals("1")){
            if (tipe_izin.equals("5")){
                desc_izin = "Tidak Masuk (Sakit)";
            } else if (tipe_izin.equals("4")) {
                desc_izin = "Permohonan Izin";
            } else {
                desc_izin = listPermohonanIzin.getDeskripsi_izin();
            }
        } else if(tipe_pengajuan.equals("2")) {
            desc_izin = "Permohonan Cuti";
        }

        String statusPermohonan = "";

        if(tipe_pengajuan.equals("1")){
            if(listPermohonanIzin.getStatus_approve().equals("0")){
                statusPermohonan = "Permohonan Terkirim";
            } else if (listPermohonanIzin.getStatus_approve().equals("1")){
                if(listPermohonanIzin.getStatus_approve_hrd().equals("1")){
                    statusPermohonan = "Permohonan Disetujui HRD";
                } else if (listPermohonanIzin.getStatus_approve_hrd().equals("2")) {
                    statusPermohonan = "Permohonan Ditolak HRD";
                } else if (listPermohonanIzin.getStatus_approve_hrd().equals("0")) {
                    statusPermohonan = "Permohonan Disetujui Atasan";
                }
            } else if (listPermohonanIzin.getStatus_approve().equals("2")){
                statusPermohonan = "Permohonan Ditolak Atasan";
            }
        } else if(tipe_pengajuan.equals("2")) {
            if(listPermohonanIzin.getStatus_approve().equals("0")){
                statusPermohonan = "Permohonan Terkirim";
            } else if (listPermohonanIzin.getStatus_approve().equals("1")){
                if(listPermohonanIzin.getStatus_approve_hrd().equals("1")){
                    statusPermohonan = "Permohonan Disetujui HRD";
                } else if (listPermohonanIzin.getStatus_approve_hrd().equals("2")) {
                    statusPermohonan = "Permohonan Ditolak HRD";
                } else if (listPermohonanIzin.getStatus_approve_hrd().equals("0")) {
                    if(listPermohonanIzin.getStatus_approve_kadept().equals("1")){
                            statusPermohonan = "Menunggu Persetujuan HRD";
                    } else if(listPermohonanIzin.getStatus_approve_kadept().equals("2")){
                        if(sharedPrefManager.getSpIdJabatan().equals("41") || sharedPrefManager.getSpIdJabatan().equals("10") || (sharedPrefManager.getSpNik().equals("0015141287")||sharedPrefManager.getSpNik().equals("3294031022"))) {
                            statusPermohonan = "Permohonan Ditolak Direktur Utama";
                        } else {
                            if(sharedPrefManager.getSpNik().equals("0687260508") || sharedPrefManager.getSpNik().equals("0113010500") || sharedPrefManager.getSpNik().equals("0499070507") || sharedPrefManager.getSpNik().equals("0056010793") || sharedPrefManager.getSpNik().equals("0829030809") || sharedPrefManager.getSpNik().equals("0552260707") || sharedPrefManager.getSpNik().equals("3318060323")) {
                                statusPermohonan = "Permohonan Ditolak General Manager";
                            } else {
                                statusPermohonan = "Permohonan Ditolak Kepala Departemen";
                            }
                        }
                    } else if(listPermohonanIzin.getStatus_approve_kadept().equals("0")){
                        if(sharedPrefManager.getSpIdJabatan().equals("41") || sharedPrefManager.getSpIdJabatan().equals("10") || (sharedPrefManager.getSpNik().equals("0015141287")||sharedPrefManager.getSpNik().equals("3294031022"))) {
                            statusPermohonan = "Menunggu Persetujuan Direktur Utama";
                        } else {
                            if(sharedPrefManager.getSpNik().equals("0687260508") || sharedPrefManager.getSpNik().equals("0113010500") || sharedPrefManager.getSpNik().equals("0499070507") || sharedPrefManager.getSpNik().equals("0056010793") || sharedPrefManager.getSpNik().equals("0829030809") || sharedPrefManager.getSpNik().equals("0552260707") || sharedPrefManager.getSpNik().equals("3318060323")) {
                                statusPermohonan = "Menunggu Persetujuan General Manager";
                            } else {
                                statusPermohonan = "Permohonan Disetujui Atasan";
                            }
                        }
                    }
                }
            } else if (listPermohonanIzin.getStatus_approve().equals("2")){
                statusPermohonan = "Permohonan Ditolak Atasan";
            }
        }

        myViewHolder.statusPermohonanTV.setText(statusPermohonan);
        myViewHolder.deskrisiPermohonan.setText(desc_izin);

        String input_date_mulai = listPermohonanIzin.getTanggal_mulai();
        String dayDateMulai = input_date_mulai.substring(8,10);
        String yearDateMulai = input_date_mulai.substring(0,4);
        String bulanValueMulai = input_date_mulai.substring(5,7);
        String bulanNameMulai;

        switch (bulanValueMulai) {
            case "01":
                bulanNameMulai = "Jan";
                break;
            case "02":
                bulanNameMulai = "Feb";
                break;
            case "03":
                bulanNameMulai = "Mar";
                break;
            case "04":
                bulanNameMulai = "Apr";
                break;
            case "05":
                bulanNameMulai = "Mei";
                break;
            case "06":
                bulanNameMulai = "Jun";
                break;
            case "07":
                bulanNameMulai = "Jul";
                break;
            case "08":
                bulanNameMulai = "Agu";
                break;
            case "09":
                bulanNameMulai = "Sep";
                break;
            case "10":
                bulanNameMulai = "Okt";
                break;
            case "11":
                bulanNameMulai = "Nov";
                break;
            case "12":
                bulanNameMulai = "Des";
                break;
            default:
                bulanNameMulai = "Not found";
                break;
        }

        String input_date_akhir = listPermohonanIzin.getTanggal_akhir();
        String dayDateAkhir = input_date_akhir.substring(8,10);
        String yearDateAkhir = input_date_akhir.substring(0,4);
        String bulanValueAkhir = input_date_akhir.substring(5,7);
        String bulanNameAkhir;

        switch (bulanValueAkhir) {
            case "01":
                bulanNameAkhir = "Jan";
                break;
            case "02":
                bulanNameAkhir = "Feb";
                break;
            case "03":
                bulanNameAkhir = "Mar";
                break;
            case "04":
                bulanNameAkhir = "Apr";
                break;
            case "05":
                bulanNameAkhir = "Mei";
                break;
            case "06":
                bulanNameAkhir = "Jun";
                break;
            case "07":
                bulanNameAkhir = "Jul";
                break;
            case "08":
                bulanNameAkhir = "Agu";
                break;
            case "09":
                bulanNameAkhir = "Sep";
                break;
            case "10":
                bulanNameAkhir = "Okt";
                break;
            case "11":
                bulanNameAkhir = "Nov";
                break;
            case "12":
                bulanNameAkhir = "Des";
                break;
            default:
                bulanNameAkhir = "Not found";
                break;
        }

        myViewHolder.detailTanggalIzinTV.setText(String.valueOf(Integer.parseInt(dayDateMulai))+" "+bulanNameMulai+" "+yearDateMulai+" s/d "+String.valueOf(Integer.parseInt(dayDateAkhir))+" "+bulanNameAkhir+" "+yearDateAkhir);

        String input_date = listPermohonanIzin.getUpdated_at().substring(0,10);
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

        String time = listPermohonanIzin.getUpdated_at().substring(10,16);

        myViewHolder.tanggalNotifikasi.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate+" "+time);

        if (listPermohonanIzin.getStatus_read().equals("1")){
            myViewHolder.statusPermohonanTV.setTextColor(Color.parseColor("#7d7d7d"));
            myViewHolder.statusPermohonanTV.setTypeface(myViewHolder.statusPermohonanTV.getTypeface(), Typeface.NORMAL);
            myViewHolder.deskrisiPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
            myViewHolder.detailTanggalIzinTV.setTextColor(Color.parseColor("#7d7d7d"));
            myViewHolder.tanggalNotifikasi.setTextColor(Color.parseColor("#7d7d7d"));
            myViewHolder.lineLimit.setBackgroundColor(Color.parseColor("#EAEAEA"));
        } else {
            myViewHolder.statusPermohonanTV.setTypeface(myViewHolder.statusPermohonanTV.getTypeface(), Typeface.BOLD);
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tipe_pengajuan.equals("1")){
                    Intent intent = new Intent(mContext, DetailPermohonanIzinActivity.class);
                    intent.putExtra("kode", "notif");
                    intent.putExtra("id_izin",listPermohonanIzin.getId());
                    mContext.startActivity(intent);
                } else if(tipe_pengajuan.equals("2")) {
                    Intent intent = new Intent(mContext, DetailPermohonanCutiActivity.class);
                    intent.putExtra("kode", "notif");
                    intent.putExtra("id_izin",listPermohonanIzin.getId());
                    mContext.startActivity(intent);
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView statusPermohonanTV, deskrisiPermohonan, detailTanggalIzinTV, tanggalNotifikasi, lineLimit;
        LinearLayout parentPart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            statusPermohonanTV = itemView.findViewById(R.id.status_permohonan_tv);
            deskrisiPermohonan = itemView.findViewById(R.id.deskrisi_permohonan);
            detailTanggalIzinTV = itemView.findViewById(R.id.detail_tanggal_izin_tv);
            tanggalNotifikasi = itemView.findViewById(R.id.tanggal_notifikasi);
            lineLimit = itemView.findViewById(R.id.line_limit);
            parentPart = itemView.findViewById(R.id.parent_part);
        }
    }

}