package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterProductInputSumaRealisasi extends RecyclerView.Adapter<AdapterProductInputSumaRealisasi.ViewHolder> {

    private List<String> dataProductRealisasi;

    public AdapterProductInputSumaRealisasi(List<String> dataProductRealisasi) {
        this.dataProductRealisasi = dataProductRealisasi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk_report_suma, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int index = position;
        String data = dataProductRealisasi.get(position);

        Locale localeID = new Locale("id", "ID");
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(localeID);
        decimalFormat.applyPattern("¤ #,##0;-¤ #,##0");
        decimalFormat.setMaximumFractionDigits(0);

        String[] arrayData = data.split("/");
        holder.productNameTV.setText(String.valueOf(arrayData[1]));
        holder.productPriceTV.setText(decimalFormat.format(Integer.parseInt(arrayData[2])));
        holder.qtyTV.setText(String.valueOf(Integer.parseInt(arrayData[3])));
        holder.subTotalTV.setText(decimalFormat.format(Integer.parseInt(arrayData[4])));

        holder.deleteBTN.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                Intent intent = new Intent("product_delete_broad_realisasi");
                intent.putExtra("index",String.valueOf(index));
                LocalBroadcastManager.getInstance(v.getContext()).sendBroadcast(intent);
            }

        });

    }

    @Override
    public int getItemCount() {
        return dataProductRealisasi.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView qtyTV, productNameTV, productPriceTV, subTotalTV;
        LinearLayout deleteBTN;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTV = itemView.findViewById(R.id.product_name_tv);
            productPriceTV = itemView.findViewById(R.id.product_price_tv);
            qtyTV = itemView.findViewById(R.id.qty_tv);
            subTotalTV = itemView.findViewById(R.id.subtotal_tv);
            deleteBTN = itemView.findViewById(R.id.delete_btn);
        }
    }
}
