package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.VisitStatisticActivity;
import com.gelora.absensi.model.SalesVisitStatistic;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListDataVisitStatisticSales extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_EMPTY = 1;

    private SalesVisitStatistic[] data;
    private SalesVisitStatistic[] allData;
    private List<Pair<SalesVisitStatistic, Integer>> indexedData;
    private Context mContext;
    SharedPrefManager sharedPrefManager;

    public AdapterListDataVisitStatisticSales(SalesVisitStatistic[] data, VisitStatisticActivity context) {
        this.allData = data;
        this.mContext = context;
        setIndexedData(data);
    }

    private void setIndexedData(SalesVisitStatistic[] data) {
        indexedData = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            indexedData.add(new Pair<>(data[i], i + 1));
        }
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        if (indexedData.isEmpty()) {
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_empty_view, viewGroup, false);
            return new EmptyViewHolder(view);
        } else {
            sharedPrefManager = new SharedPrefManager(mContext);
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_sales_visit_statistic, viewGroup, false);
            return new MyViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            final Pair<SalesVisitStatistic, Integer> itemWithIndex = indexedData.get(position);
            final SalesVisitStatistic salesVisitStatistic = itemWithIndex.first;
            final int originalIndex = itemWithIndex.second;

            Picasso.get().load(salesVisitStatistic.getAvatar()).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize(100, 100)
                    .into(((MyViewHolder) holder).profileImage);

            ((MyViewHolder) holder).noTV.setText(String.valueOf(originalIndex));
            ((MyViewHolder) holder).namaTV.setText(salesVisitStatistic.getNamaKaryawan().toUpperCase());
            ((MyViewHolder) holder).nikTV.setText(salesVisitStatistic.getIdSales());

            if (salesVisitStatistic.getWilayah().equals("Jakarta 1") || salesVisitStatistic.getWilayah().equals("Jakarta 2") ||
                    salesVisitStatistic.getWilayah().equals("Jakarta 3") || salesVisitStatistic.getWilayah().equals("Bandung") ||
                    salesVisitStatistic.getWilayah().equals("Semarang") || salesVisitStatistic.getWilayah().equals("Surabaya") || salesVisitStatistic.getWilayah().equals("Palembang")) {
                ((MyViewHolder) holder).wilayahTV.setText("Suma " + salesVisitStatistic.getWilayah());
            } else {
                ((MyViewHolder) holder).wilayahTV.setText(salesVisitStatistic.getWilayah());
            }
            ((MyViewHolder) holder).jumlahKunjunganTV.setText(salesVisitStatistic.getJumlah_kunjungan());

            ((MyViewHolder) holder).parrentPart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("to_detail");
                    intent.putExtra("NIK",salesVisitStatistic.getIdSales());
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            });
        } else if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).emptyTextView.setText("Tidak ada hasil ditemukan.");
        }
    }

    @Override
    public int getItemCount() {
        if (indexedData.isEmpty()) {
            return 1;
        }
        return indexedData.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterByName(String name) {
        if (name.isEmpty()) {
            setIndexedData(allData);
        } else {
            List<Pair<SalesVisitStatistic, Integer>> filteredList = new ArrayList<>();
            for (int i = 0; i < allData.length; i++) {
                if (allData[i].getNamaKaryawan().toLowerCase().contains(name.toLowerCase())) {
                    filteredList.add(new Pair<>(allData[i], i + 1));
                }
            }
            indexedData = filteredList;
            data = new SalesVisitStatistic[filteredList.size()];
            for (int i = 0; i < filteredList.size(); i++) {
                data[i] = filteredList.get(i).first;
            }
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parrentPart;
        TextView noTV, namaTV, nikTV, wilayahTV, jumlahKunjunganTV;
        CircleImageView profileImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parrentPart = itemView.findViewById(R.id.parent_part);
            namaTV = itemView.findViewById(R.id.nama_tv);
            nikTV = itemView.findViewById(R.id.nik_tv);
            wilayahTV = itemView.findViewById(R.id.wilayah_tv);
            jumlahKunjunganTV = itemView.findViewById(R.id.jumlah_kunjungan);
            profileImage = itemView.findViewById(R.id.profile_image);
            noTV = itemView.findViewById(R.id.no_tv);
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        TextView emptyTextView;
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            emptyTextView = itemView.findViewById(R.id.empty_text_view);
        }
    }
}
