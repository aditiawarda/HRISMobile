package com.gelora.absensi.adapter;

import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.R;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.List;

public class AdapterProductInputSuma extends RecyclerView.Adapter<AdapterProductInputSuma.ViewHolder> {

    private List<String> dataProduct;

    public AdapterProductInputSuma(List<String> dataProduct) {
        this.dataProduct = dataProduct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk_report_suma, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String data = dataProduct.get(position);

        String[] arrayData = data.split("/");
        holder.productNameTV.setText(String.valueOf(arrayData[1]));
        holder.productPriceTV.setText(String.valueOf(arrayData[2]));
        holder.qtyTV.setText(String.valueOf(Integer.parseInt(arrayData[3])));
        holder.subTotalTV.setText(String.valueOf(Integer.parseInt(arrayData[4])));
    }

    @Override
    public int getItemCount() {
        return dataProduct.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView qtyTV, productNameTV, productPriceTV, subTotalTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTV = itemView.findViewById(R.id.product_name_tv);
            productPriceTV = itemView.findViewById(R.id.product_price_tv);
            qtyTV = itemView.findViewById(R.id.qty_tv);
            subTotalTV = itemView.findViewById(R.id.subtotal_tv);
        }
    }
}
