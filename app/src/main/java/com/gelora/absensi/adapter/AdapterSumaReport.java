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

import com.gelora.absensi.DetailReportSumaActivity;
import com.gelora.absensi.ListDataReportSumaActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataReportSuma;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class AdapterSumaReport extends RecyclerView.Adapter<AdapterSumaReport.MyViewHolder> {

    private DataReportSuma[] data;
    private Context mContext;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterSumaReport(DataReportSuma[] data, ListDataReportSumaActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefManager = new SharedPrefManager(mContext);
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
            if(!String.valueOf(dataReportSuma.getIdRealisasi()).equals("null")){
                myViewHolder.f1StatusRealisasi.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.f1StatusRealisasi.setVisibility(View.GONE);
            }
        } else if (dataReportSuma.getTipeLaporan().equals("2")) {
            myViewHolder.reportCategoryTV.setText("Aktivitas Promosi");
            myViewHolder.f1StatusRealisasi.setVisibility(View.GONE);
        } else if (dataReportSuma.getTipeLaporan().equals("3")) {
            myViewHolder.reportCategoryTV.setText("Aktivitas Penagihan");
            myViewHolder.f1StatusRealisasi.setVisibility(View.GONE);
        } else if (dataReportSuma.getTipeLaporan().equals("4")) {
            myViewHolder.reportCategoryTV.setText("Aktivitas Pengiriman");
            myViewHolder.f1StatusRealisasi.setVisibility(View.GONE);
        }

        myViewHolder.namaSalesTV.setText(dataReportSuma.getNamaKaryawan());

        if(dataReportSuma.getNmJabatan().equals("Group Leader Suma 1") || dataReportSuma.getNmJabatan().equals("Salesman Suma 1")){
            myViewHolder.regionTV.setText("Jakarta 1");
        } else if(dataReportSuma.getNmJabatan().equals("Group Leader Suma 2") || dataReportSuma.getNmJabatan().equals("Salesman Suma 2")){
            myViewHolder.regionTV.setText("Jakarta 2");
        } else if(dataReportSuma.getNmJabatan().equals("Group Leader Suma 3") || dataReportSuma.getNmJabatan().equals("Salesman Suma 3")){
            myViewHolder.regionTV.setText("Jakarta 3");
        } else if(dataReportSuma.getNmJabatan().equals("Group Leader AE") || dataReportSuma.getNmJabatan().equals("Staff AE")){
            myViewHolder.regionTV.setText("Account Executive");
        } else {
            String[] jabatan = dataReportSuma.getNmJabatan().split("\\s+");
            String region = jabatan[jabatan.length - 1];

            myViewHolder.regionTV.setText(region);
        }

        myViewHolder.namaPelangganTV.setText(dataReportSuma.getNamaPelanggan());

        if(dataReportSuma.getTipeLaporan().equals("1")){
            myViewHolder.rencanaKunjunganPart.setVisibility(View.VISIBLE);
            myViewHolder.kunjunganPart.setVisibility(View.GONE);
            myViewHolder.penagihanPart.setVisibility(View.GONE);
            myViewHolder.pengirimanPart.setVisibility(View.GONE);
            if(String.valueOf(dataReportSuma.getTgl_rencana()).equals("")||String.valueOf(dataReportSuma.getTgl_rencana()).equals("null")){
                myViewHolder.f1TanggalRencanaTV.setText("Tidak tersedia");
            } else {
                myViewHolder.f1TanggalRencanaTV.setText(dataReportSuma.getTgl_rencana().substring(8,10)+"/"+dataReportSuma.getTgl_rencana().substring(5,7)+"/"+dataReportSuma.getTgl_rencana().substring(0,4));
            }
            myViewHolder.f1TanggalLaporanTV.setText(dataReportSuma.getCreatedAt().substring(8,10)+"/"+dataReportSuma.getCreatedAt().substring(5,7)+"/"+dataReportSuma.getCreatedAt().substring(0,4)+" "+dataReportSuma.getCreatedAt().substring(10,16));
        } else if(dataReportSuma.getTipeLaporan().equals("2")){
            myViewHolder.rencanaKunjunganPart.setVisibility(View.GONE);
            myViewHolder.kunjunganPart.setVisibility(View.VISIBLE);
            myViewHolder.penagihanPart.setVisibility(View.GONE);
            myViewHolder.pengirimanPart.setVisibility(View.GONE);
            if(!dataReportSuma.getTotalPesanan().equals("null") && !dataReportSuma.getTotalPesanan().equals("") && !dataReportSuma.equals(null)){
                if(!dataReportSuma.getTotalPesanan().equals("0")){
                    myViewHolder.f2TotalPesananTV.setText(decimalFormat.format(Integer.parseInt(dataReportSuma.getTotalPesanan())));
                } else {
                    myViewHolder.f2TotalPesananTV.setText("Tidak dicantumkan");
                }
            } else {
                myViewHolder.f2TotalPesananTV.setText("Tidak dicantumkan");
            }
            myViewHolder.f2TanggalLaporanTV.setText(dataReportSuma.getCreatedAt().substring(8,10)+"/"+dataReportSuma.getCreatedAt().substring(5,7)+"/"+dataReportSuma.getCreatedAt().substring(0,4)+" "+dataReportSuma.getCreatedAt().substring(10,16));
        } else if(dataReportSuma.getTipeLaporan().equals("3")){
            myViewHolder.rencanaKunjunganPart.setVisibility(View.GONE);
            myViewHolder.kunjunganPart.setVisibility(View.GONE);
            myViewHolder.penagihanPart.setVisibility(View.VISIBLE);
            myViewHolder.pengirimanPart.setVisibility(View.GONE);
            if(!dataReportSuma.getTotalTagihan().equals("null") && !dataReportSuma.getTotalTagihan().equals("") && !dataReportSuma.equals(null)){
                if(!dataReportSuma.getTotalTagihan().equals("0")){
                    myViewHolder.f3TotalTagihanTV.setText(decimalFormat.format(Integer.parseInt(dataReportSuma.getTotalTagihan())));
                } else {
                    myViewHolder.f3TotalTagihanTV.setText("Tidak dicantumkan");
                }
            } else {
                myViewHolder.f2TotalPesananTV.setText("Tidak dicantumkan");
            }
            myViewHolder.f3TanggalLaporanTV.setText(dataReportSuma.getCreatedAt().substring(8,10)+"/"+dataReportSuma.getCreatedAt().substring(5,7)+"/"+dataReportSuma.getCreatedAt().substring(0,4)+" "+dataReportSuma.getCreatedAt().substring(10,16));
        } else if(dataReportSuma.getTipeLaporan().equals("4")){
            myViewHolder.rencanaKunjunganPart.setVisibility(View.GONE);
            myViewHolder.kunjunganPart.setVisibility(View.GONE);
            myViewHolder.penagihanPart.setVisibility(View.GONE);
            myViewHolder.pengirimanPart.setVisibility(View.VISIBLE);
            if(String.valueOf(dataReportSuma.getNoSuratJalan()).equals("null")||String.valueOf(dataReportSuma.getNoSuratJalan()).equals("")){
                myViewHolder.f4NoSJTV.setText("Tidak dicantumkan");
            } else {
                myViewHolder.f4NoSJTV.setText(dataReportSuma.getNoSuratJalan());
            }
            myViewHolder.f4TanggalLaporanTV.setText(dataReportSuma.getCreatedAt().substring(8,10)+"/"+dataReportSuma.getCreatedAt().substring(5,7)+"/"+dataReportSuma.getCreatedAt().substring(0,4)+" "+dataReportSuma.getCreatedAt().substring(10,16));
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailReportSumaActivity.class);
                intent.putExtra("report_id",dataReportSuma.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namaSalesTV, namaPelangganTV, f1StatusRealisasi, regionTV;
        TextView f4NoSJTV, f4TanggalLaporanTV, f3TanggalLaporanTV, f3TotalTagihanTV, reportCategoryTV, f1TanggalRencanaTV, f1TanggalLaporanTV, f2TotalPesananTV, f2TanggalLaporanTV;
        LinearLayout pengirimanPart, parentPart, kunjunganPart, rencanaKunjunganPart, penagihanPart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            reportCategoryTV = itemView.findViewById(R.id.report_kategori_tv);
            f1StatusRealisasi = itemView.findViewById(R.id.status_realisasi);
            namaPelangganTV = itemView.findViewById(R.id.nama_pelanggan_tv);
            namaSalesTV = itemView.findViewById(R.id.nama_sales_tv);
            regionTV = itemView.findViewById(R.id.jabatan_tv);

            f1TanggalRencanaTV = itemView.findViewById(R.id.f1_tgl_kunjungan_tv);
            f1TanggalLaporanTV = itemView.findViewById(R.id.f1_tanggal_laporan_tv);
            f2TotalPesananTV = itemView.findViewById(R.id.f2_total_pesanan_tv);
            f2TanggalLaporanTV = itemView.findViewById(R.id.f2_tanggal_laporan_tv);
            f3TotalTagihanTV = itemView.findViewById(R.id.f3_total_tagihan_tv);
            f3TanggalLaporanTV = itemView.findViewById(R.id.f3_tanggal_laporan_tv);
            f4TanggalLaporanTV = itemView.findViewById(R.id.f4_tanggal_laporan_tv);
            f4NoSJTV = itemView.findViewById(R.id.f4_no_sj_tv);

            kunjunganPart = itemView.findViewById(R.id.kunjungan_part);
            rencanaKunjunganPart = itemView.findViewById(R.id.rencana_kunjungan_part);
            penagihanPart = itemView.findViewById(R.id.penagihan_part);
            pengirimanPart = itemView.findViewById(R.id.pengiriman_part);
        }
    }

}
