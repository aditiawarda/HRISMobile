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

import com.gelora.absensi.FormPermohonanCutiActivity;
import com.gelora.absensi.MapsActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.KategoriIzin;
import com.gelora.absensi.model.StatusAbsen;

public class AdapterKategoriIzin extends RecyclerView.Adapter<AdapterKategoriIzin.MyViewHolder> {

    private KategoriIzin[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterKategoriIzin(KategoriIzin[] data, FormPermohonanCutiActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_kategori_cuti,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final KategoriIzin kategoriIzin = data[i];

        myViewHolder.cutiName.setText(kategoriIzin.getKode());
        myViewHolder.cituDesc.setText(kategoriIzin.getDeskripsi());

        if (sharedPrefAbsen.getSpIdCuti().equals(kategoriIzin.getId())) {
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

                Intent intent = new Intent("kategori_cuti_broad");
                intent.putExtra("id_kategori_cuti",kategoriIzin.getId());
                intent.putExtra("kode_kategori_cuti",kategoriIzin.getKode());
                intent.putExtra("desc_kategori_cuti",kategoriIzin.getDeskripsi());
                intent.putExtra("tipe_kategori_cuti",kategoriIzin.getTipe());
                intent.putExtra("status_lampiran",kategoriIzin.getStatus_lampiran());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_CUTI, kategoriIzin.getId());

            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cutiName, cituDesc;
        LinearLayout parentPart, markStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.kategori_cuti_part);
            markStatus = itemView.findViewById(R.id.mark_status);
            cutiName = itemView.findViewById(R.id.nama_kategori_cuti);
            cituDesc = itemView.findViewById(R.id.desc_kategori_cuti);
        }
    }
}
