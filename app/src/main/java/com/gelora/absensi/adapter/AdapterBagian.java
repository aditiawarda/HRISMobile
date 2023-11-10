package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.MapsActivity;
import com.gelora.absensi.MonitoringAbsensiBagianActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.Bagian;
import com.gelora.absensi.model.ShiftAbsen;

public class AdapterBagian extends RecyclerView.Adapter<AdapterBagian.MyViewHolder> {

    private Bagian[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterBagian(Bagian[] data, MonitoringAbsensiBagianActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bagian,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final Bagian bagian = data[i];

        myViewHolder.namaBagian.setText(bagian.getKdDept());
        myViewHolder.descBagian.setText(bagian.getNmDept());

        if (sharedPrefAbsen.getSpIdBagian().equals(bagian.getIdDept())) {
            myViewHolder.markPilih.setVisibility(View.VISIBLE);
            myViewHolder.parentPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option_choice));
        } else {
            myViewHolder.markPilih.setVisibility(View.GONE);
            myViewHolder.parentPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option));
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {

                notifyDataSetChanged();

                Intent intent = new Intent("bagian_broad");
                intent.putExtra("id_bagian",bagian.getIdDept());
                intent.putExtra("nama_bagian",bagian.getKdDept());
                intent.putExtra("desc_bagian",bagian.getNmDept());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_BAGIAN, bagian.getIdDept());
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_BAGIAN, bagian.getKdDept());

            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namaBagian;
        TextView descBagian;
        LinearLayout parentPart, markPilih;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            markPilih = itemView.findViewById(R.id.mark_status);
            namaBagian = itemView.findViewById(R.id.nama_bagian);
            descBagian = itemView.findViewById(R.id.desc_bagian);
        }
    }
}
