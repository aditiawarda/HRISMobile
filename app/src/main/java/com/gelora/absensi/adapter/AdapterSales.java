package com.gelora.absensi.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gelora.absensi.R;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterSales extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_EMPTY = 1;

    private List<String> listSales;
    private List<String> allSales;

    public AdapterSales(List<String> listSales) {
        this.listSales = listSales;
        this.allSales = new ArrayList<>(listSales);
    }

    @Override
    public int getItemViewType(int position) {
        if (listSales.isEmpty()) {
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_view, parent, false);
            return new EmptyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sales, parent, false);
            return new ViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder viewHolder) {
            String data = listSales.get(position);
            String[] arrayData = data.split("~");

            long pending = Long.parseLong(arrayData[1]);
            long process = Long.parseLong(arrayData[2]);
            long complete = Long.parseLong(arrayData[3]);
            long total = Long.parseLong(arrayData[4]);

            viewHolder.tvNamaSales.setText(arrayData[0]);
            animateCurrency(viewHolder, "pending", pending);
            animateCurrency(viewHolder, "process", process);
            animateCurrency(viewHolder, "complete", complete);
            animateCurrency(viewHolder, "total", total);
        } else if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).emptyTextView.setText("Tidak ada hasil ditemukan.");
        }
    }

    @Override
    public int getItemCount() {
        return listSales.isEmpty() ? 1 : listSales.size();
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

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        TextView emptyTextView;

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            emptyTextView = itemView.findViewById(R.id.empty_text_view);
        }
    }

    private void animateCurrency(@NonNull ViewHolder holder, String key, long end) {
        long delta = end - (long) 0;
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(3000);
        animator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            long animatedValue = (long) 0 + (long) (delta * fraction);

            if (animatedValue > end) {
                animatedValue = end;
            }

            Log.d("Final", "Animated value: " + String.valueOf(animatedValue));

            switch (key) {
                case "pending":
                    holder.tvPending.setText(formatToRupiah(animatedValue));
                    break;
                case "process":
                    holder.tvInProcess.setText(formatToRupiah(animatedValue));
                    break;
                case "complete":
                    holder.tvComplete.setText(formatToRupiah(animatedValue));
                    break;
                case "total":
                    holder.tvTotal.setText(formatToRupiah(animatedValue));
                    break;
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.d("Final", "Final value: " + String.valueOf(end));
                switch (key) {
                    case "pending":
                        holder.tvPending.setText(formatToRupiah(end));
                        break;
                    case "process":
                        holder.tvInProcess.setText(formatToRupiah(end));
                        break;
                    case "complete":
                        holder.tvComplete.setText(formatToRupiah(end));
                        break;
                    case "total":
                        holder.tvTotal.setText(formatToRupiah(end));
                        break;
                }
            }
        });

        animator.start();
    }

    private String formatToRupiah(long amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatter.setMaximumFractionDigits(0);
        return formatter.format(amount);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterByName(String name) {
        if (name.isEmpty()) {
            listSales = new ArrayList<>(allSales);
        } else {
            List<String> filteredList = new ArrayList<>();
            for (String data : allSales) {
                String[] arrayData = data.split("~");
                if (arrayData[0].toLowerCase().contains(name.toLowerCase())) {
                    filteredList.add(data);
                }
            }
            listSales = filteredList;
        }
        notifyDataSetChanged();
    }

}