package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailPengumumanActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataPengumuman;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterListPengumumanNew extends RecyclerView.Adapter<AdapterListPengumumanNew.MyViewHolder> {

    private DataPengumuman[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterListPengumumanNew(DataPengumuman[] data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pengumuman,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataPengumuman dataPengumumanNew = data[i];
        String namaStatus = "";

        myViewHolder.titleTV.setText(dataPengumumanNew.getPengumuman_title().toUpperCase());

        String input_date = dataPengumumanNew.getPengumuman_date();
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

        String bulan = input_date.substring(0,4)+"-"+input_date.substring(5,7);
        String bulansekarang = getDate().substring(0,4)+"-"+getDate().substring(5,7);

        if(bulan.equals(bulansekarang)){
            String hari = input_date.substring(8,10);
            String hari_sekarang = getDate().substring(8,10);
            int selisih_hari = Integer.parseInt(hari_sekarang) - Integer.parseInt(hari);
            if(selisih_hari==0){
                myViewHolder.timeTV.setText("Hari ini, "+dataPengumumanNew.getPengumuman_time().substring(0,5));
            } else if(selisih_hari==1){
                myViewHolder.timeTV.setText("Kemarin, "+dataPengumumanNew.getPengumuman_time().substring(0,5));
            } else {
                myViewHolder.timeTV.setText(String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
            }
        } else {
            myViewHolder.timeTV.setText(String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailPengumumanActivity.class);
                if(String.valueOf(dataPengumumanNew.getPengumuman_image()).equals("")||String.valueOf(dataPengumumanNew.getPengumuman_image()).equals("null")||dataPengumumanNew.getPengumuman_image()==null){
                    intent.putExtra("image", String.valueOf(dataPengumumanNew.getPengumuman_image()));
                } else {
                    intent.putExtra("image", "https://geloraaksara.co.id/absen-online/assets/img/pengumuman/"+String.valueOf(dataPengumumanNew.getPengumuman_image()));
                }
                intent.putExtra("title", String.valueOf(dataPengumumanNew.getPengumuman_title()));
                intent.putExtra("deskripsi", String.valueOf(dataPengumumanNew.getPengumuman_desc()));
                intent.putExtra("date", String.valueOf(dataPengumumanNew.getPengumuman_date()));
                intent.putExtra("time", String.valueOf(dataPengumumanNew.getPengumuman_time()));
                mContext.startActivity(intent);
            }
        });

    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parentPart;
        TextView titleTV, timeTV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            titleTV = itemView.findViewById(R.id.title_tv);
            timeTV = itemView.findViewById(R.id.time_tv);
        }
    }

}