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

        if(dataReportSuma.getTipeLaporan().equals("1")){
            myViewHolder.reportCategoryTV.setText("Rencana Kunjungan");
        } else if(dataReportSuma.getTipeLaporan().equals("2")){
            myViewHolder.reportCategoryTV.setText("Kunjungan");
        } else if(dataReportSuma.getTipeLaporan().equals("3")){
            myViewHolder.reportCategoryTV.setText("Penawaran");
        } else if(dataReportSuma.getTipeLaporan().equals("4")){
            myViewHolder.reportCategoryTV.setText("Penagihan");
        }

        myViewHolder.keteranganTV.setText(dataReportSuma.getKeterangan());
        myViewHolder.namaPelangganTV.setText(dataReportSuma.getNamaPelanggan());
        myViewHolder.totalPesananTV.setText(decimalFormat.format(Integer.parseInt(dataReportSuma.getTotalPesanan())));
        myViewHolder.tanggalLaporanTV.setText(dataReportSuma.getCreatedAt().substring(8,10)+"/"+dataReportSuma.getCreatedAt().substring(5,7)+"/"+dataReportSuma.getCreatedAt().substring(0,4)+" "+dataReportSuma.getCreatedAt().substring(10,16));

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namaPelangganTV;
        TextView reportCategoryTV, keteranganTV, totalPesananTV, tanggalLaporanTV;
        LinearLayout parentPart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            reportCategoryTV = itemView.findViewById(R.id.report_kategori_tv);
            keteranganTV = itemView.findViewById(R.id.keterangan_report_tv);
            namaPelangganTV = itemView.findViewById(R.id.nama_pelanggan_tv);
            totalPesananTV = itemView.findViewById(R.id.total_pesanan_tv);
            tanggalLaporanTV = itemView.findViewById(R.id.tanggal_laporan_tv);
        }
    }

}
