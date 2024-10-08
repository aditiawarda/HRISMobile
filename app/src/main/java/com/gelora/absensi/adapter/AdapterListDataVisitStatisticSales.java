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

import com.gelora.absensi.DataPenilaianSdmActivity;
import com.gelora.absensi.DetailPenilaianKaryawanActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataPenilaianSDM;
import com.gelora.absensi.model.SalesVisitStatistic;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListDataVisitStatisticSales extends RecyclerView.Adapter<AdapterListDataVisitStatisticSales.MyViewHolder> {

    private SalesVisitStatistic[] data;
    private Context mContext;
    SharedPrefManager sharedPrefManager;

    public AdapterListDataVisitStatisticSales(SalesVisitStatistic[] data, DataPenilaianSdmActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefManager = new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sales_visit_statistic,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final SalesVisitStatistic salesVisitStatistic = data[i];

        Picasso.get().load(salesVisitStatistic.getAvatar()).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .resize(100, 100)
                .into(myViewHolder.profileImage);

        myViewHolder.namaTV.setText(salesVisitStatistic.getNamaKaryawan().toUpperCase());
        myViewHolder.nikTV.setText(salesVisitStatistic.getIdSales());
        myViewHolder.detailTV.setText(salesVisitStatistic.getWilayah());
        myViewHolder.predikatTV.setText(salesVisitStatistic.getJumlah_kunjungan());

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parrentPart, waitingMark;
        TextView namaTV, nikTV, detailTV, predikatTV, tanggalBuatTV, waitingMarkTV, accMark, rejMark;
        CircleImageView profileImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parrentPart = itemView.findViewById(R.id.parent_part);
            namaTV = itemView.findViewById(R.id.nama_tv);
            nikTV = itemView.findViewById(R.id.nik_tv);
            detailTV = itemView.findViewById(R.id.detail_tv);
            predikatTV = itemView.findViewById(R.id.predikat);
            profileImage = itemView.findViewById(R.id.profile_image);
            tanggalBuatTV = itemView.findViewById(R.id.tanggal_buat_tv);
            waitingMark = itemView.findViewById(R.id.waiting_mark);
            waitingMarkTV = itemView.findViewById(R.id.waiting_mark_tv);
            accMark = itemView.findViewById(R.id.acc_mark);
            rejMark = itemView.findViewById(R.id.rej_mark);
        }
    }

}