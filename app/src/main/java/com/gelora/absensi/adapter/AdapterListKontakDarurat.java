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
import com.gelora.absensi.InfoKontakDaruratActivity;
import com.gelora.absensi.ListAllPengumumanActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataKontakDarurat;
import com.gelora.absensi.model.DataPengumumanAll;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterListKontakDarurat extends RecyclerView.Adapter<AdapterListKontakDarurat.MyViewHolder> {

    private DataKontakDarurat[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterListKontakDarurat(DataKontakDarurat[] data, InfoKontakDaruratActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_kontak_darurat,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataKontakDarurat dataKontakDarurat = data[i];

        myViewHolder.namaKontak.setText(dataKontakDarurat.getNama_kontak());

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        myViewHolder.kontakBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        myViewHolder.menuBTN.setOnClickListener(new View.OnClickListener() {
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
        LinearLayout kontakBTN, menuBTN, parentPart;
        TextView namaKontak, hubunganKontak, noKontak;
        ExpandableLayout expandableLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            parentPart = itemView.findViewById(R.id.parent_part);
            namaKontak = itemView.findViewById(R.id.nama_kontak);
            hubunganKontak = itemView.findViewById(R.id.hubungan);
            noKontak = itemView.findViewById(R.id.no_kontak);
            kontakBTN = itemView.findViewById(R.id.phone_btn);
            menuBTN = itemView.findViewById(R.id.menu_btn);
        }
    }

}