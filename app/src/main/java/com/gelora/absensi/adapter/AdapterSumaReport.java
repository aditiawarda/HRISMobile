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
import com.gelora.absensi.ListDataReportSumaActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.DataReportSuma;
import com.gelora.absensi.model.KategoriIzin;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class AdapterSumaReport extends RecyclerView.Adapter<AdapterSumaReport.MyViewHolder> {

    private DataReportSuma[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterSumaReport(DataReportSuma[] data, ListDataReportSumaActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_report_suma,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataReportSuma dataReportSuma = data[i];

        Locale localeID = new Locale("id", "ID");
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
        decimalFormat.applyPattern("¤ #,##0;-¤ #,##0");
        decimalFormat.setMaximumFractionDigits(0);

        if (dataReportSuma.getTipeLaporan().equals("1")) {
            myViewHolder.reportCategoryTV.setText("Rencana Kunjungan");
        } else if (dataReportSuma.getTipeLaporan().equals("2")) {
            myViewHolder.reportCategoryTV.setText("Kunjungan");
        } else if (dataReportSuma.getTipeLaporan().equals("3")) {
            myViewHolder.reportCategoryTV.setText("Penawaran");
        } else if (dataReportSuma.getTipeLaporan().equals("4")) {
            myViewHolder.reportCategoryTV.setText("Penagihan");
        }

        myViewHolder.keteranganTV.setText(dataReportSuma.getKeterangan());
        myViewHolder.namaPelangganTV.setText(dataReportSuma.getNamaPelanggan());

        if(dataReportSuma.getTipeLaporan().equals("1")){
            myViewHolder.rencanaKunjunganPart.setVisibility(View.VISIBLE);
            myViewHolder.kunjunganPart.setVisibility(View.GONE);
            if(String.valueOf(dataReportSuma.getTgl_rencana()).equals("")||String.valueOf(dataReportSuma.getTgl_rencana()).equals("null")){
                myViewHolder.f1TanggalRencanaTV.setText("Tidak tersedia");
            } else {
                myViewHolder.f1TanggalRencanaTV.setText(dataReportSuma.getTgl_rencana().substring(8,10)+"/"+dataReportSuma.getTgl_rencana().substring(5,7)+"/"+dataReportSuma.getTgl_rencana().substring(0,4));
            }
            myViewHolder.f1TanggalLaporanTV.setText(dataReportSuma.getCreatedAt().substring(8,10)+"/"+dataReportSuma.getCreatedAt().substring(5,7)+"/"+dataReportSuma.getCreatedAt().substring(0,4)+" "+dataReportSuma.getCreatedAt().substring(10,16));
        } else if(dataReportSuma.getTipeLaporan().equals("2")){
            myViewHolder.rencanaKunjunganPart.setVisibility(View.GONE);
            myViewHolder.kunjunganPart.setVisibility(View.VISIBLE);
            myViewHolder.f2TotalPesananTV.setText(decimalFormat.format(Integer.parseInt(dataReportSuma.getTotalPesanan())));
            myViewHolder.f2TanggalLaporanTV.setText(dataReportSuma.getCreatedAt().substring(8,10)+"/"+dataReportSuma.getCreatedAt().substring(5,7)+"/"+dataReportSuma.getCreatedAt().substring(0,4)+" "+dataReportSuma.getCreatedAt().substring(10,16));
        }

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namaPelangganTV;
        TextView reportCategoryTV, keteranganTV, f1TanggalRencanaTV, f1TanggalLaporanTV, f2TotalPesananTV, f2TanggalLaporanTV;
        LinearLayout parentPart, kunjunganPart, rencanaKunjunganPart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            reportCategoryTV = itemView.findViewById(R.id.report_kategori_tv);
            keteranganTV = itemView.findViewById(R.id.keterangan_report_tv);
            namaPelangganTV = itemView.findViewById(R.id.nama_pelanggan_tv);

            f1TanggalRencanaTV = itemView.findViewById(R.id.f1_tgl_kunjungan_tv);
            f1TanggalLaporanTV = itemView.findViewById(R.id.f1_tanggal_laporan_tv);
            f2TotalPesananTV = itemView.findViewById(R.id.f2_total_pesanan_tv);
            f2TanggalLaporanTV = itemView.findViewById(R.id.f2_tanggal_laporan_tv);

            kunjunganPart = itemView.findViewById(R.id.kunjungan_part);
            rencanaKunjunganPart = itemView.findViewById(R.id.rencana_kunjungan_part);
        }
    }

}