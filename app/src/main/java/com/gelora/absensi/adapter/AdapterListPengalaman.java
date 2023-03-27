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

import com.gelora.absensi.DetailKeluargaActivity;
import com.gelora.absensi.FormInfoPengalamanActivity;
import com.gelora.absensi.InfoKeluargaActivity;
import com.gelora.absensi.InfoPengalamanDanPelatihanActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataKeluarga;
import com.gelora.absensi.model.DataPengalaman;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterListPengalaman extends RecyclerView.Adapter<AdapterListPengalaman.MyViewHolder> {

    private DataPengalaman[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterListPengalaman(DataPengalaman[] data, InfoPengalamanDanPelatihanActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pengalaman,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataPengalaman dataPengalaman = data[i];

        myViewHolder.posisi.setText(dataPengalaman.getDeskripsi_posisi());
        myViewHolder.periode.setText(dataPengalaman.getDari_tahun()+" s.d. "+dataPengalaman.getSampai_tahun());

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FormInfoPengalamanActivity.class);
                intent.putExtra("tipe","edit");
                intent.putExtra("id_pengalaman",dataPengalaman.getId());
                mContext.startActivity(intent);
            }
        });

        myViewHolder.editBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FormInfoPengalamanActivity.class);
                intent.putExtra("tipe","edit");
                intent.putExtra("id_pengalaman",dataPengalaman.getId());
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
        TextView posisi, periode;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            posisi = itemView.findViewById(R.id.posisi);
            periode = itemView.findViewById(R.id.periode);
            editBTN = itemView.findViewById(R.id.edit_btn);
        }
    }

}