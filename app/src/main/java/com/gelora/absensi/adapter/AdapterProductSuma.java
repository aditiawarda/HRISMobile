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

import com.gelora.absensi.R;
import com.gelora.absensi.ReportSumaActivity;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.PelangganLama;
import com.gelora.absensi.model.ProductSuma;

public class AdapterProductSuma extends RecyclerView.Adapter<AdapterProductSuma.MyViewHolder> {

    private ProductSuma[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterProductSuma(ProductSuma[] data, ReportSumaActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_product_suma,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ProductSuma productSuma = data[i];

        myViewHolder.namaProduct.setText(productSuma.getJudul());
        myViewHolder.hargaProduct.setText(productSuma.getPrice());

        if (sharedPrefAbsen.getSpProductActive().equals(productSuma.getId_produk())) {
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

                Intent intent = new Intent("product_suma_broad");
                intent.putExtra("id_product",productSuma.getId_produk());
                intent.putExtra("nama_product",productSuma.getJudul());
                intent.putExtra("harga_satuan",productSuma.getPrice());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_PRODUCT_ACTIVE, productSuma.getId_produk());

            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namaProduct, hargaProduct;
        LinearLayout parentPart, markStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            markStatus = itemView.findViewById(R.id.mark_status);
            namaProduct = itemView.findViewById(R.id.nama_product);
            hargaProduct = itemView.findViewById(R.id.harga_product);
        }
    }

}
