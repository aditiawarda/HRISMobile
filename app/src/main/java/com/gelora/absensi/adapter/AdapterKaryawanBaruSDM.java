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

import com.gelora.absensi.FormSdmActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.KaryawanSDM;

public class AdapterKaryawanBaruSDM extends RecyclerView.Adapter<AdapterKaryawanBaruSDM.MyViewHolder> {

    private KaryawanSDM[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterKaryawanBaruSDM(KaryawanSDM[] data, FormSdmActivity context) {
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
        final KaryawanSDM karyawanSDM = data[i];

        myViewHolder.karyawanName.setText(karyawanSDM.getNama());
        myViewHolder.karyawanNik.setText(karyawanSDM.getNIK());
        myViewHolder.karyawanDesc.setText(karyawanSDM.getJabatan()+" | "+karyawanSDM.getBagian()+" | "+karyawanSDM.getDepartemen());

        if (sharedPrefAbsen.getSpIdKaryawanSdmBaru().equals(karyawanSDM.getNIK())) {
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

                Intent intent = new Intent("f2_karyawan_baru_broad");
                intent.putExtra("nik_karyawan_baru",karyawanSDM.getNIK());
                intent.putExtra("nama_karyawan_baru",karyawanSDM.getNama());
                intent.putExtra("departemen_karyawan_baru",karyawanSDM.getDepartemen());
                intent.putExtra("id_departemen_karyawan_baru",karyawanSDM.getId_departemen());
                intent.putExtra("bagian_karyawan_baru",karyawanSDM.getBagian());
                intent.putExtra("id_bagian_karyawan_baru",karyawanSDM.getId_bagian());
                intent.putExtra("jabatan_karyawan_baru",karyawanSDM.getJabatan());
                intent.putExtra("id_jabatan_karyawan_baru",karyawanSDM.getId_jabatan());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_KARYAWAN_SDM_BARU, karyawanSDM.getNIK());

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
