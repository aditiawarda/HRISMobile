package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.FormFingerscanActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.ShiftAbsen;
import com.gelora.absensi.model.TitikAbsensi;

public class AdapterTitikAbsenFinger extends RecyclerView.Adapter<AdapterTitikAbsenFinger.MyViewHolder> {

    private TitikAbsensi[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterTitikAbsenFinger(TitikAbsensi[] data, FormFingerscanActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_titik_absen,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final TitikAbsensi titikAbsensi = data[i];

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.roboto);
            myViewHolder.titikName.setTypeface(typeface);
        }

        myViewHolder.titikName.setText(titikAbsensi.getNama_lokasi());

        if (sharedPrefAbsen.getSpIdTitikAbsensi().equals(titikAbsensi.getId())) {
            myViewHolder.markStatusTitik.setVisibility(View.VISIBLE);
            myViewHolder.statusParent.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option_choice));
        } else {
            myViewHolder.markStatusTitik.setVisibility(View.GONE);
            myViewHolder.statusParent.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option));
        }

        myViewHolder.statusParent.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {

                notifyDataSetChanged();

                Intent intent = new Intent("titik_absen_broad");
                intent.putExtra("latitude_titik",titikAbsensi.getLatitude());
                intent.putExtra("longitude_titik",titikAbsensi.getLongitude());
                intent.putExtra("nama_titik",titikAbsensi.getNama_lokasi());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_TITIK_ABSENSI, titikAbsensi.getId());

            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titikName;
        LinearLayout statusParent, markStatusTitik;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            statusParent = itemView.findViewById(R.id.titik_absen_part);
            markStatusTitik = itemView.findViewById(R.id.mark_status);
            titikName = itemView.findViewById(R.id.nama_titk);
        }
    }
}
