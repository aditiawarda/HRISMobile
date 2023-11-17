package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.HistoryCutiIzinActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataHistoryCutiBersama;

public class AdapterDataHistoryCutiBersama extends RecyclerView.Adapter<AdapterDataHistoryCutiBersama.MyViewHolder> {

    private DataHistoryCutiBersama[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterDataHistoryCutiBersama(DataHistoryCutiBersama[] data, HistoryCutiIzinActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_data_history_cuti,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataHistoryCutiBersama dataHistoryCutiBersama = data[i];

        String date = dataHistoryCutiBersama.getTanggal();
        myViewHolder.deskripsiHistoryTV.setText("Cuti Bersama");
        if(date==null){
            myViewHolder.dateRangeTV.setVisibility(View.GONE);
            myViewHolder.notedTV.setText("*)  Memotong hak cuti tahunan (Otomatis)  ");
        }else{
            myViewHolder.dateRangeTV.setVisibility(View.VISIBLE);
            myViewHolder.dateRangeTV.setText(date.substring(8,10)+"/"+date.substring(5,7)+"/"+date.substring(0,4));
            myViewHolder.notedTV.setText("*)  Memotong hak cuti tahunan  ");
        }
        myViewHolder.alasanAtauKeteranganTV.setText(dataHistoryCutiBersama.getNama());
        myViewHolder.notedTV2.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView deskripsiHistoryTV, dateRangeTV, notedTV, notedTV2, alasanAtauKeteranganTV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            deskripsiHistoryTV = itemView.findViewById(R.id.deskripsi_history_tv);
            dateRangeTV = itemView.findViewById(R.id.date_range_tv);
            alasanAtauKeteranganTV = itemView.findViewById(R.id.alasan_atau_keterangan_tv);
            notedTV = itemView.findViewById(R.id.noted_tv);
            notedTV2 = itemView.findViewById(R.id.noted2_tv);
        }
    }


}