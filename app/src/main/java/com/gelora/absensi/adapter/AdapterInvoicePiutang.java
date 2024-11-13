package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.R;
import com.gelora.absensi.ReportSumaActivity;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.DataInvoicePiutang;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class AdapterInvoicePiutang extends RecyclerView.Adapter<AdapterInvoicePiutang.MyViewHolder> {

    private DataInvoicePiutang[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterInvoicePiutang(DataInvoicePiutang[] data, ReportSumaActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_inv_piutang,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataInvoicePiutang dataInvoicePiutang = data[i];

        Locale localeID = new Locale("id", "ID");
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
        decimalFormat.applyPattern("¤ #,##0;-¤ #,##0");
        decimalFormat.setMaximumFractionDigits(0);

        myViewHolder.noInvTV.setText(dataInvoicePiutang.getNoInvoice());
        myViewHolder.tglPemesananTV.setText(dataInvoicePiutang.getTglPemesanan().substring(8,10)+"/"+dataInvoicePiutang.getTglPemesanan().substring(5,7)+"/"+dataInvoicePiutang.getTglPemesanan().substring(0,4));
        myViewHolder.subTotalTV.setText(decimalFormat.format(Integer.parseInt(dataInvoicePiutang.getPiutang())));

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subTotalTV, tglPemesananTV, noInvTV;
        LinearLayout parentPart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            subTotalTV = itemView.findViewById(R.id.sub_total_tv);
            tglPemesananTV = itemView.findViewById(R.id.tgl_pemesanan_tv);
            noInvTV = itemView.findViewById(R.id.no_inv_tv);
        }
    }
}
