package com.gelora.absensi.adapter;

import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterSales extends RecyclerView.Adapter<AdapterSales.ViewHolder> {

    private List<String> listSales;

    public AdapterSales(List<String> listSales) {
        this.listSales = listSales;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int index = position;
        String data = listSales.get(position);
        String[] arrayData = data.split("~");

        long pending = Long.parseLong(String.valueOf(arrayData[1]));
        long process = Long.parseLong(String.valueOf(arrayData[2]));
        long complete = Long.parseLong(String.valueOf(arrayData[3]));
        long total = Long.parseLong(String.valueOf(arrayData[4]));

        holder.tvNamaSales.setText(String.valueOf(arrayData[0]));
        animateCurrency(holder,"pending", 0, pending, 3000);
        animateCurrency(holder,"process", 0, process, 3000);
        animateCurrency(holder,"complete", 0, complete, 3000);
        animateCurrency(holder,"total", 0, total, 3000);

    }

    @Override
    public int getItemCount() {
        return listSales.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaSales, tvPending, tvInProcess, tvComplete, tvTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaSales = itemView.findViewById(R.id.tvNamaSales);
            tvPending = itemView.findViewById(R.id.tvPending);
            tvInProcess = itemView.findViewById(R.id.tvInProcess);
            tvComplete = itemView.findViewById(R.id.tvComplete);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }

    private void animateCurrency(@NonNull ViewHolder holder, String key, long start, long end, int duration) {
        long delta = end - start;
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            long animatedValue = start + (long) (delta * fraction);
            if(key.equals("pending")){
                holder.tvPending.setText(formatToRupiah(animatedValue));
            } else if(key.equals("process")){
                holder.tvInProcess.setText(formatToRupiah(animatedValue));
            } else if(key.equals("complete")){
                holder.tvComplete.setText(formatToRupiah(animatedValue));
            } else if(key.equals("total")) {
                holder.tvTotal.setText(formatToRupiah(animatedValue));
            }
        });
        animator.start();
    }

    private String formatToRupiah(long amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatter.setMaximumFractionDigits(0);
        return formatter.format(amount);
    }

}
