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

import com.gelora.absensi.FormInfoPelatihanActivity;
import com.gelora.absensi.InfoPengalamanDanPelatihanActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataPelatihan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterListPelatihan extends RecyclerView.Adapter<AdapterListPelatihan.MyViewHolder> {

    private DataPelatihan[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterListPelatihan(DataPelatihan[] data, InfoPengalamanDanPelatihanActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pelatihan,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataPelatihan dataPengalaman = data[i];

        myViewHolder.namaPelatihan.setText(dataPengalaman.getNama_pelatihan());
        myViewHolder.lembagaPelatihan.setText("Dari : "+ dataPengalaman.getLembaga_pelatihan());
        myViewHolder.tahunPelatihan.setText(dataPengalaman.getTahun());

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FormInfoPelatihanActivity.class);
                intent.putExtra("tipe","edit");
                intent.putExtra("id_pelatihan",dataPengalaman.getId());
                mContext.startActivity(intent);
            }
        });

        myViewHolder.editBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FormInfoPelatihanActivity.class);
                intent.putExtra("tipe","edit");
                intent.putExtra("id_pelatihan",dataPengalaman.getId());
                mContext.startActivity(intent);
            }
        });

        if(dataPengalaman.getStatus_action().equals("1")){
            myViewHolder.editBTN.setVisibility(View.VISIBLE);
        } else if(dataPengalaman.getStatus_action().equals("0")) {
            myViewHolder.editBTN.setVisibility(View.GONE);
        }

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
        LinearLayout parentPart, editBTN;
        TextView namaPelatihan, lembagaPelatihan, tahunPelatihan;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            namaPelatihan = itemView.findViewById(R.id.nama_pelatihan);
            lembagaPelatihan = itemView.findViewById(R.id.lembaga_pelatihan);
            tahunPelatihan = itemView.findViewById(R.id.tahun);
            editBTN = itemView.findViewById(R.id.edit_btn);
        }
    }

}