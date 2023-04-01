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
import com.gelora.absensi.model.DataHistoryCuti;
import com.gelora.absensi.model.DataHistoryIzin;

public class AdapterDataHistoryIzin extends RecyclerView.Adapter<AdapterDataHistoryIzin.MyViewHolder> {

    private DataHistoryIzin[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterDataHistoryIzin(DataHistoryIzin[] data, HistoryCutiIzinActivity context) {
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
        final DataHistoryIzin dataHistoryIzin = data[i];

        String startDate = dataHistoryIzin.getTanggal_mulai();
        String finishDate = dataHistoryIzin.getTanggal_akhir();
        String tipePengajuan = dataHistoryIzin.getTipe_pengajuan();

        myViewHolder.deskripsiHistoryTV.setText(dataHistoryIzin.getDeskripsi_izin());
        myViewHolder.dateRangeTV.setText(startDate.substring(8,10)+"/"+startDate.substring(5,7)+"/"+startDate.substring(0,4)+"  s.d. "+finishDate.substring(8,10)+"/"+finishDate.substring(5,7)+"/"+finishDate.substring(0,4));

        if(dataHistoryIzin.getKeterangan()==null){
            myViewHolder.alasanAtauKeteranganTV.setVisibility(View.GONE);
        } else {
            if(dataHistoryIzin.getKeterangan().length()==1){
                myViewHolder.alasanAtauKeteranganTV.setVisibility(View.GONE);
            } else {
                if(dataHistoryIzin.getKeterangan().equals("null")){
                    myViewHolder.alasanAtauKeteranganTV.setVisibility(View.GONE);
                } else {
                    myViewHolder.alasanAtauKeteranganTV.setVisibility(View.VISIBLE);
                    myViewHolder.alasanAtauKeteranganTV.setText(dataHistoryIzin.getKeterangan());
                }
            }
        }

        if(tipePengajuan.equals("1")){
            myViewHolder.notedTV.setText("*)  Menggunakan Form Izin Digital  ");
        } else if(tipePengajuan.equals("0")) {
            myViewHolder.notedTV.setText("*)  Menggunakan Form Izin Manual  ");
        }

        if(dataHistoryIzin.getTipe_izin().equals("5")){
            myViewHolder.notedTV2.setVisibility(View.GONE);
        } else if(tipePengajuan.equals("4")) {
            myViewHolder.notedTV2.setVisibility(View.VISIBLE);
        }

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