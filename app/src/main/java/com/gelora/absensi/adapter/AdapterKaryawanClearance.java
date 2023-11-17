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

import com.gelora.absensi.ExitClearanceActivity;
import com.gelora.absensi.FormExitClearanceActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.KaryawanClearance;

public class AdapterKaryawanClearance extends RecyclerView.Adapter<AdapterKaryawanClearance.MyViewHolder> {

    private KaryawanClearance[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterKaryawanClearance(KaryawanClearance[] data, ExitClearanceActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_karyawan_pengganti,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final KaryawanClearance karyawanClearance = data[i];

        myViewHolder.karyawanName.setText(karyawanClearance.getNama());
        myViewHolder.karyawanNik.setText(karyawanClearance.getNIK());
        myViewHolder.karyawanDesc.setText(karyawanClearance.getJabatan()+" | "+karyawanClearance.getBagian()+" | "+karyawanClearance.getDepartemen());

        if (sharedPrefAbsen.getSpIdKaryawanPengganti().equals(karyawanClearance.getNIK())) {
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
                Intent intent_broad = new Intent("karyawan_broad");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent_broad);

                Intent intent = new Intent(mContext, FormExitClearanceActivity.class);
                intent.putExtra("nik", karyawanClearance.getNIK());
                intent.putExtra("nama", karyawanClearance.getNama());
                intent.putExtra("id_bagian", karyawanClearance.getId_bagian());
                intent.putExtra("id_dept", karyawanClearance.getId_departemen());
                intent.putExtra("id_jabatan", karyawanClearance.getId_jabatan());
                mContext.startActivity(intent);
            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView karyawanName, karyawanNik, karyawanDesc;
        LinearLayout parentPart, markStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.kategori_cuti_part);
            markStatus = itemView.findViewById(R.id.mark_status);
            karyawanName = itemView.findViewById(R.id.nama_karyawan);
            karyawanNik = itemView.findViewById(R.id.nik_karyawan);
            karyawanDesc = itemView.findViewById(R.id.detail_karyawan);
        }
    }
}
