package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.MonitoringAbsensiBagianActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.ReportSumaActivity;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.Bagian;
import com.gelora.absensi.model.PelangganList;

public class AdapterPelangganList extends RecyclerView.Adapter<AdapterPelangganList.MyViewHolder> {

    private PelangganList[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterPelangganList(PelangganList[] data, ReportSumaActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pelanggan_suma,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final PelangganList pelangganList = data[i];
        ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorAccent2));

        myViewHolder.noTV.setText(String.valueOf(i+1));
        if(pelangganList.getKategoriPelanggan().equals("1")){
            myViewHolder.kategoriPelangganTV.setText("BARU");
            myViewHolder.kategoriPelangganTV.setTextColor(Color.parseColor("#37b422"));
        } else if(pelangganList.getKategoriPelanggan().equals("2")){
            myViewHolder.kategoriPelangganTV.setText("LAMA");
            myViewHolder.kategoriPelangganTV.setTextColor(Color.parseColor("#6b8ad1"));
        }

        String dataAgendainput = pelangganList.getRencanAgenda().replaceAll("\\[|\\]|\"", ""); // Hapus kurung siku dan tanda kutip
        String[] dataArray = dataAgendainput.split(",");

        for (int x=0; x<dataArray.length; x++){
            if(dataArray[x].equals("2")){
                myViewHolder.agendaCB1.setButtonTintList(colorStateList);
                myViewHolder.agendaCB1.setChecked(true);
            } else if(dataArray[x].equals("3")){
                myViewHolder.agendaCB2.setButtonTintList(colorStateList);
                myViewHolder.agendaCB2.setChecked(true);
            } else if(dataArray[x].equals("4")){
                myViewHolder.agendaCB3.setButtonTintList(colorStateList);
                myViewHolder.agendaCB3.setChecked(true);
            } else if(dataArray[x].equals("5")){
                myViewHolder.agendaCB4.setButtonTintList(colorStateList);
                myViewHolder.agendaCB4.setChecked(true);
            }

        }

        myViewHolder.namaPelangganTV.setText(pelangganList.getNamaPelanggan());
        myViewHolder.alamatPelangganTV.setText(pelangganList.getAlamatPelanggan());
        myViewHolder.keteranganKunjunganTV.setText(pelangganList.getKeteranganKunjunganPelanggan());

        myViewHolder.deleteBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                Intent intent = new Intent("remove_pelanggan_list");
                intent.putExtra("nama_pelanggan",pelangganList.getNamaPelanggan());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView noTV, kategoriPelangganTV, namaPelangganTV, alamatPelangganTV, keteranganKunjunganTV;
        LinearLayout parentPart, deleteBTN;
        CheckBox agendaCB1, agendaCB2, agendaCB3, agendaCB4;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            noTV = itemView.findViewById(R.id.no_tv);
            kategoriPelangganTV = itemView.findViewById(R.id.kategori_pelanggan_tv);
            namaPelangganTV = itemView.findViewById(R.id.nama_pelanggan_tv);
            alamatPelangganTV = itemView.findViewById(R.id.alamat_pelanggan_tv);
            keteranganKunjunganTV = itemView.findViewById(R.id.keterangan_kunjungan_tv);
            deleteBTN = itemView.findViewById(R.id.delete_btn);
            agendaCB1 = itemView.findViewById(R.id.agenda_cb_1);
            agendaCB2 = itemView.findViewById(R.id.agenda_cb_2);
            agendaCB3 = itemView.findViewById(R.id.agenda_cb_3);
            agendaCB4 = itemView.findViewById(R.id.agenda_cb_4);
        }
    }
}
