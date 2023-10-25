package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailDataSerahTerimaExitClearanceActivity;
import com.gelora.absensi.DetailTidakHadirActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataDetailSerahTerima;
import com.gelora.absensi.model.DataIzin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterDataDetailSerahTerima extends RecyclerView.Adapter<AdapterDataDetailSerahTerima.MyViewHolder> {

    private DataDetailSerahTerima[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterDataDetailSerahTerima(DataDetailSerahTerima[] data, DetailDataSerahTerimaExitClearanceActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_data_st_exit_clearance,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataDetailSerahTerima dataDetailSerahTerima = data[i];

        myViewHolder.pointStTV.setText(dataDetailSerahTerima.getSerah_terima_detail());

        if(dataDetailSerahTerima.getSaat_masuk().equals("1")){
            myViewHolder.sMcB.setChecked(true);
        } else {
            myViewHolder.sMcB.setChecked(false);
        }

        if(dataDetailSerahTerima.getSaat_keluar().equals("1")){
            myViewHolder.sKcb.setChecked(true);
        } else {
            myViewHolder.sKcb.setChecked(false);
        }

        if(String.valueOf(dataDetailSerahTerima.getKeterangan()).equals("null") || String.valueOf(dataDetailSerahTerima.getKeterangan()).equals("")){
            myViewHolder.ketTV.setText("Tidak tersedia");
        } else {
            myViewHolder.ketTV.setText(dataDetailSerahTerima.getKeterangan());
        }

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pointStTV, ketTV;
        CheckBox sMcB, sKcb;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pointStTV = itemView.findViewById(R.id.point_st);
            sMcB = itemView.findViewById(R.id.sm_cb);
            sKcb = itemView.findViewById(R.id.sk_cb);
            ketTV = itemView.findViewById(R.id.ket_tv);
        }
    }


}