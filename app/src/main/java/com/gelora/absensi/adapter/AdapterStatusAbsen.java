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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.MapsActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.StatusAbsen;

public class AdapterStatusAbsen extends RecyclerView.Adapter<AdapterStatusAbsen.MyViewHolder> {

    private StatusAbsen[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterStatusAbsen(StatusAbsen[] data, MapsActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_status_absen,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final StatusAbsen statusAbsen = data[i];

        myViewHolder.statusName.setText(statusAbsen.getNama_status());
        myViewHolder.statusDesc.setText(statusAbsen.getDeskripsi_status());

        if (sharedPrefAbsen.getSpIdStatus().equals(statusAbsen.getId_status())) {
            myViewHolder.markStatusAbsen.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.markStatusAbsen.setVisibility(View.GONE);
        }

        myViewHolder.statusParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notifyDataSetChanged();

                Intent intent = new Intent("status_absen_broad");
                intent.putExtra("id_status_absen",statusAbsen.getId_status());
                intent.putExtra("nama_status_absen",statusAbsen.getNama_status());
                intent.putExtra("desc_status_absen",statusAbsen.getDeskripsi_status());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, statusAbsen.getId_status());

            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView statusName;
        TextView statusDesc;
        LinearLayout statusParent, markStatusAbsen;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            statusParent = itemView.findViewById(R.id.status_absen_part);
            markStatusAbsen = itemView.findViewById(R.id.mark_status);
            statusName = itemView.findViewById(R.id.nama_status);
            statusDesc = itemView.findViewById(R.id.desc_status);
        }
    }
}
