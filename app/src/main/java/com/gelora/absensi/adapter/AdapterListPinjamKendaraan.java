package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailPinjamKendaraan;
import com.gelora.absensi.R;
import com.gelora.absensi.databinding.AdapterListPinjamKendaraanBinding;
import com.gelora.absensi.model.PinjamKendaraanResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterListPinjamKendaraan extends RecyclerView.Adapter<AdapterListPinjamKendaraan.MyViewHolder> {
    private Context context;

    private AdapterListPinjamKendaraanBinding binding;
    private List<PinjamKendaraanResponse> itemList;

    public void getData(Context context, List<PinjamKendaraanResponse> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = AdapterListPinjamKendaraanBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = binding.getRoot();

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PinjamKendaraanResponse item = itemList.get(position);
        binding.tvBagian.setText(item.getBagianPemohon() + " |");
        binding.tvNama.setText(item.getNamaPemohon().toUpperCase());
        String inputDateString = item.getApp1();

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));

        try {
            Date date = inputFormat.parse(inputDateString);
            String formattedDate = outputFormat.format(date);
            binding.tvTanggal.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            binding.tvTanggal.setText(inputDateString);
        }
        if (item.getStatus().equals("1") || item.getStatus().equals("3") || item.getStatus().equals("5")) {
            binding.detailStatus.setText("Pending");
            binding.cardDetailProses.setText("Menunggu Persetujuan");
            binding.imageRequest.setImageResource(R.drawable.waiting_key);
            binding.detailStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.selected_yellow));
            binding.cardDetailProses.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.selected_yellow));
        } else if (item.getStatus().equals("7") || item.getStatus().equals("8") || item.getStatus().equals("9")) {
            binding.detailStatus.setText("Accepted");
            binding.cardDetailProses.setText("Permohonan diterima");
            binding.imageRequest.setImageResource(R.drawable.accepted_key);
            binding.detailStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heavyGreen));
            binding.cardDetailProses.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heavyGreen));
        } else if (item.getStatus().equals("2") || item.getStatus().equals("4") || item.getStatus().equals("6")) {
            binding.detailStatus.setText("Rejected");
            binding.cardDetailProses.setText("Permohonan ditolak");
            binding.imageRequest.setImageResource(R.drawable.rejected_key);
            binding.detailStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heavyRed));
            binding.cardDetailProses.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heavyRed));
        } else if (item.getStatus().equals("0")) {
            binding.detailStatus.setText("Canceled");
            binding.cardDetailProses.setText("Permohonan dibatalkan");
            binding.imageRequest.setImageResource(R.drawable.canceled_key);
            binding.detailStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heavyRed));
            binding.cardDetailProses.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heavyRed));
        }

        binding.card.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), DetailPinjamKendaraan.class);
            intent.putExtra("current_id_pk", item.getId());

            view.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
