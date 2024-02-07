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

    private List<String> names;

    public AdapterProductInputSuma(List<String> names) {
        this.names = names;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk_report_suma, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String qty = names.get(position);
        holder.qtyPicker.setValue(Integer.parseInt(qty));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        NumberPicker qtyPicker;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            qtyPicker = itemView.findViewById(R.id.qty_picker);
        }
    }
}
