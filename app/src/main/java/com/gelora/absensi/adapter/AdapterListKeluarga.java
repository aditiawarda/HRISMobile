package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailKeluargaActivity;
import com.gelora.absensi.FormKontakDaruratActivity;
import com.gelora.absensi.InfoKeluargaActivity;
import com.gelora.absensi.InfoKontakDaruratActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataKeluarga;
import com.gelora.absensi.model.DataKontakDarurat;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterListKeluarga extends RecyclerView.Adapter<AdapterListKeluarga.MyViewHolder> {

    private DataKeluarga[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterListKeluarga(DataKeluarga[] data, InfoKeluargaActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_keluarga,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataKeluarga dataKeluarga = data[i];

        myViewHolder.nama.setText(dataKeluarga.getNama());

        if(dataKeluarga.getHubungan().equals("Lainnya")){
            myViewHolder.hubungan.setText(dataKeluarga.getHubungan_lainnya());
        } else {
            myViewHolder.hubungan.setText(dataKeluarga.getHubungan());
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailKeluargaActivity.class);
                intent.putExtra("id_data",dataKeluarga.getId());
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
        LinearLayout parentPart, viewBTN;
        TextView nama, hubungan;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            nama = itemView.findViewById(R.id.nama);
            hubungan = itemView.findViewById(R.id.hubungan);
            viewBTN = itemView.findViewById(R.id.view_btn);
        }
    }

}