package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailTidakHadirActivity;
import com.gelora.absensi.HistoryCutiIzinActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataAlpa;
import com.gelora.absensi.model.DataHistoryCuti;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterDataHistoryCuti extends RecyclerView.Adapter<AdapterDataHistoryCuti.MyViewHolder> {

    private DataHistoryCuti[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterDataHistoryCuti(DataHistoryCuti[] data, HistoryCutiIzinActivity context) {
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
        final DataHistoryCuti dataHistoryCuti = data[i];

        String startDate = dataHistoryCuti.getTanggal_mulai();
        String finishDate = dataHistoryCuti.getTanggal_akhir();
        String tipePengajuan = dataHistoryCuti.getTipe_pengajuan();

        myViewHolder.deskripsiHistoryTV.setText(dataHistoryCuti.getDeskripsi_izin());
        myViewHolder.dateRangeTV.setText(startDate.substring(8,10)+"/"+startDate.substring(5,7)+"/"+startDate.substring(0,4)+"  s.d. "+finishDate.substring(8,10)+"/"+finishDate.substring(5,7)+"/"+finishDate.substring(0,4));

        if(tipePengajuan.equals("2")){
            myViewHolder.notedTV.setText("*)  Menggunakan Form Cuti Digital");
        } else if(tipePengajuan.equals("0")) {
            myViewHolder.notedTV.setText("*)  Menggunakan Form Cuti Manual");
        }

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView deskripsiHistoryTV, dateRangeTV, notedTV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            deskripsiHistoryTV = itemView.findViewById(R.id.deskripsi_history_tv);
            dateRangeTV = itemView.findViewById(R.id.date_range_tv);
            notedTV = itemView.findViewById(R.id.noted_tv);
        }
    }


}