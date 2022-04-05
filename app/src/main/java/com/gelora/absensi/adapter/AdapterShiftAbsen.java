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
import com.gelora.absensi.model.ShiftAbsen;

public class AdapterShiftAbsen extends RecyclerView.Adapter<AdapterShiftAbsen.MyViewHolder> {

    private ShiftAbsen[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterShiftAbsen(ShiftAbsen[] data, MapsActivity context) {
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
        final ShiftAbsen shiftAbsen = data[i];

        myViewHolder.statusName.setText(shiftAbsen.getNama_shift());
        myViewHolder.statusDesc.setText(shiftAbsen.getDatang()+" - "+shiftAbsen.getPulang());

        if (sharedPrefAbsen.getSpIdShift().equals(shiftAbsen.getId_shift())) {
            myViewHolder.markStatusAbsen.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.markStatusAbsen.setVisibility(View.GONE);
        }

        myViewHolder.statusParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notifyDataSetChanged();

                Intent intent = new Intent("shift_absen_broad");
                intent.putExtra("id_shift_absen",shiftAbsen.getId_shift());
                intent.putExtra("nama_shift_absen",shiftAbsen.getNama_shift());
                intent.putExtra("datang_shift_absen",shiftAbsen.getDatang());
                intent.putExtra("pulang_shift_absen",shiftAbsen.getPulang());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_SHIFT, shiftAbsen.getId_shift());

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
