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
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.R;
import com.gelora.absensi.ReportSumaActivity;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.PelangganLama;

public class AdapterTokoPelanggan extends RecyclerView.Adapter<AdapterTokoPelanggan.MyViewHolder> {

    private PelangganLama[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterTokoPelanggan(PelangganLama[] data, ReportSumaActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pelanggan_lama,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final PelangganLama pelangganToko = data[i];

        myViewHolder.namaPelanggan.setText(pelangganToko.getNama_customer());
        myViewHolder.alamatPelanggan.setText(pelangganToko.getAlamat_customer());

        if (sharedPrefAbsen.getSpPelangganLama().equals(pelangganToko.getId_customer())) {
            myViewHolder.markStatus.setVisibility(View.VISIBLE);
            myViewHolder.parentPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option_choice));
        } else {
            myViewHolder.markStatus.setVisibility(View.GONE);
            myViewHolder.parentPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option));
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                Intent intent = new Intent("pelanggan_toko_broad");
                intent.putExtra("nama_pelanggan_toko",pelangganToko.getNama_customer());
                intent.putExtra("id_pelanggan_toko",pelangganToko.getId_customer());
                intent.putExtra("alamat_pelanggan_toko",pelangganToko.getAlamat_customer());
                intent.putExtra("pic_pelanggan_toko",pelangganToko.getPic());
                intent.putExtra("telepon_pelanggan_toko",pelangganToko.getNo_telp());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PELANGGAN_LAMA, pelangganToko.getId_customer());
            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namaPelanggan, alamatPelanggan;
        LinearLayout parentPart, markStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            markStatus = itemView.findViewById(R.id.mark_status);
            namaPelanggan = itemView.findViewById(R.id.nama_pelanggan);
            alamatPelanggan = itemView.findViewById(R.id.alamat_pelanggan);
        }
    }

}