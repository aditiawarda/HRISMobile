package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.R;
import com.gelora.absensi.SearchKaryawanBagianActivity;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataMonitoringKetidakhadiranBagian;

public class AdapterKetidakhadiranBagianSearch extends RecyclerView.Adapter<AdapterKetidakhadiranBagianSearch.MyViewHolder> {

    private DataMonitoringKetidakhadiranBagian[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterKetidakhadiranBagianSearch(DataMonitoringKetidakhadiranBagian[] data, SearchKaryawanBagianActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_ketidakhadiran_karyawan,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataMonitoringKetidakhadiranBagian dataMonitoringKetidakhadiranBagian = data[i];

        myViewHolder.userName.setText(dataMonitoringKetidakhadiranBagian.getNmKaryawan().toUpperCase());
        myViewHolder.userNIK.setText(dataMonitoringKetidakhadiranBagian.getNIK());
        myViewHolder.ketTV.setText(dataMonitoringKetidakhadiranBagian.getKeterangan()+" ("+dataMonitoringKetidakhadiranBagian.getKode()+")");

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userNIK, ketTV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.nama_staff_tv);
            userNIK = itemView.findViewById(R.id.nik_staff_tv);
            ketTV = itemView.findViewById(R.id.keterangan_tv);
        }
    }

}