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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

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

        if(listPermohonanIzin.getAvatar()!=null){
            Picasso.get().load("https://geloraaksara.co.id/absen-online/upload/avatar/"+listPermohonanIzin.getAvatar()).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize(80, 80)
                    .into(myViewHolder.profileImage);
        } else {
            Picasso.get().load("https://geloraaksara.co.id/absen-online/upload/avatar/default_profile.jpg").networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize(80, 80)
                    .into(myViewHolder.profileImage);
        }

        myViewHolder.namaKaryawanTV.setText(listPermohonanIzin.getNmKaryawan().toUpperCase());
        if(sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("3")||sharedPrefManager.getSpIdJabatan().equals("33")){ // Kadep Askadep
            myViewHolder.nikKaryawanTV.setText(listPermohonanIzin.getNIK()+" | "+listPermohonanIzin.getKdDept());
        } else if(sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25")||(sharedPrefManager.getSpIdJabatan().equals("4")&&(sharedPrefManager.getSpNik().equals("1309131210")||sharedPrefManager.getSpNik().equals("0172110302")))||sharedPrefManager.getSpIdJabatan().equals("35")) { // Kabag Supervisor
           // if(sharedPrefManager.getSpNik().equals("0056010793")){ // Bu Sorta
           //     myViewHolder.nikKaryawanTV.setText(listPermohonanIzin.getNIK()+" | "+listPermohonanIzin.getKdDept());
           //  } else {
                myViewHolder.nikKaryawanTV.setText("NIK "+listPermohonanIzin.getNIK());
           // }
        } else if(sharedPrefManager.getSpIdJabatan().equals("8")){ //Direksi
            myViewHolder.nikKaryawanTV.setText(listPermohonanIzin.getNIK()+" | "+listPermohonanIzin.getNmHeadDept());
        }
        myViewHolder.deskrisiPermohonan.setText(desc_izin);

        String input_date = listPermohonanIzin.getCreated_at().substring(0,10);
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

        String time = listPermohonanIzin.getCreated_at().substring(10,16);

        myViewHolder.tanggalKirimPermohonan.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate+" "+time);

        if(listPermohonanIzin.getTipe_pengajuan().equals("1")){
            if(sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25")||(sharedPrefManager.getSpIdJabatan().equals("4")&&(sharedPrefManager.getSpNik().equals("1309131210")||sharedPrefManager.getSpNik().equals("0172110302")))||sharedPrefManager.getSpIdJabatan().equals("35")){
                // if(sharedPrefManager.getSpNik().equals("0056010793")){ // Bu Sorta
                //     if(!listPermohonanIzin.getStatus_approve().equals("0")){
                //         myViewHolder.namaKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                //         myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.NORMAL);
                //         myViewHolder.nikKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                //         myViewHolder.deskrisiPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                //         myViewHolder.tanggalKirimPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                //         myViewHolder.lineLimit.setBackgroundColor(Color.parseColor("#EAEAEA"));
                //     } else {
                //        myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.BOLD);
                //    }
                // } else {
                    if (!listPermohonanIzin.getStatus_approve().equals("0")){
                        myViewHolder.namaKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                        myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.NORMAL);
                        myViewHolder.nikKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                        myViewHolder.deskrisiPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                        myViewHolder.tanggalKirimPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                        myViewHolder.lineLimit.setBackgroundColor(Color.parseColor("#EAEAEA"));
                    } else {
                        myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.BOLD);
                    }
                // }
            }
            else if(sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("3")||sharedPrefManager.getSpIdJabatan().equals("33")){
                if(!listPermohonanIzin.getStatus_approve().equals("0")){
                    myViewHolder.namaKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.NORMAL);
                    myViewHolder.nikKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.deskrisiPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.tanggalKirimPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.lineLimit.setBackgroundColor(Color.parseColor("#EAEAEA"));
                } else {
                    myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.BOLD);
                }
            }
            else if(sharedPrefManager.getSpIdJabatan().equals("8")){ //Direksi
                if(!listPermohonanIzin.getStatus_approve().equals("0")){
                    myViewHolder.namaKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.NORMAL);
                    myViewHolder.nikKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.deskrisiPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.tanggalKirimPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.lineLimit.setBackgroundColor(Color.parseColor("#EAEAEA"));
                } else {
                    myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.BOLD);
                }
            }
        } else if(listPermohonanIzin.getTipe_pengajuan().equals("2")){
            if(sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25")||(sharedPrefManager.getSpIdJabatan().equals("4")&&(sharedPrefManager.getSpNik().equals("1309131210")||sharedPrefManager.getSpNik().equals("0172110302")))||sharedPrefManager.getSpIdJabatan().equals("35")){
                // if(sharedPrefManager.getSpNik().equals("0056010793")){ // Bu Sorta
                //    if(!listPermohonanIzin.getStatus_approve_kadept().equals("0")){
                //        myViewHolder.namaKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                //        myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.NORMAL);
                //        myViewHolder.nikKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                //        myViewHolder.deskrisiPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                //        myViewHolder.tanggalKirimPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                //        myViewHolder.lineLimit.setBackgroundColor(Color.parseColor("#EAEAEA"));
                //    } else {
                //        myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.BOLD);
                //    }
                // } else {
                    if (!listPermohonanIzin.getStatus_approve().equals("0")){
                        myViewHolder.namaKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                        myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.NORMAL);
                        myViewHolder.nikKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                        myViewHolder.deskrisiPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                        myViewHolder.tanggalKirimPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                        myViewHolder.lineLimit.setBackgroundColor(Color.parseColor("#EAEAEA"));
                    } else {
                        myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.BOLD);
                    }
                // }
            }
            else if(sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("3")||sharedPrefManager.getSpIdJabatan().equals("33")){
                if(!listPermohonanIzin.getStatus_approve_kadept().equals("0")){
                    myViewHolder.namaKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.NORMAL);
                    myViewHolder.nikKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.deskrisiPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.tanggalKirimPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.lineLimit.setBackgroundColor(Color.parseColor("#EAEAEA"));
                } else {
                    myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.BOLD);
                }
            }
            else if(sharedPrefManager.getSpIdJabatan().equals("8")){ //Direksi
                if(!listPermohonanIzin.getStatus_approve_kadept().equals("0")){
                    myViewHolder.namaKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.NORMAL);
                    myViewHolder.nikKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.deskrisiPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.tanggalKirimPermohonan.setTextColor(Color.parseColor("#7d7d7d"));
                    myViewHolder.lineLimit.setBackgroundColor(Color.parseColor("#EAEAEA"));
                } else {
                    myViewHolder.namaKaryawanTV.setTypeface(myViewHolder.namaKaryawanTV.getTypeface(), Typeface.BOLD);
                }
            }
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
        TextView namaKaryawanTV, nikKaryawanTV, deskrisiPermohonan, tanggalKirimPermohonan, lineLimit;
        LinearLayout parentPart;
        CircleImageView profileImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            deskrisiPermohonan = itemView.findViewById(R.id.deskrisi_permohonan);
            tanggalKirimPermohonan = itemView.findViewById(R.id.tanggal_kirim_permohonan);
            nikKaryawanTV = itemView.findViewById(R.id.nik_karyawan_tv);
            namaKaryawanTV = itemView.findViewById(R.id.nama_karyawan_tv);
            lineLimit = itemView.findViewById(R.id.line_limit);
            parentPart = itemView.findViewById(R.id.parent_part);
            profileImage = itemView.findViewById(R.id.profile_image);
        }
    }

}