package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.HistoryCutiIzinActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataHistoryPenambahanCuti;

public class AdapterDataHistoryPenambahanCuti extends RecyclerView.Adapter<AdapterDataHistoryPenambahanCuti.MyViewHolder> {

    private DataHistoryPenambahanCuti[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterDataHistoryPenambahanCuti(DataHistoryPenambahanCuti[] data, HistoryCutiIzinActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_data_penambahan_cuti,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataHistoryPenambahanCuti dataHistorPenambahanCuti = data[i];

        String dataDate = dataHistorPenambahanCuti.getTanggal();
        String ket = dataHistorPenambahanCuti.getKeterangan();

        if(dataHistorPenambahanCuti.getKeterangan().equals("AWAL PERIODE")){
            myViewHolder.titleDataTV.setText("Hak Cuti Awal Periode");
            myViewHolder.ketPart.setVisibility(View.GONE);
        } else {
            myViewHolder.titleDataTV.setText("Penambahan Hak Cuti");
            if(ket.equals("") || ket.equals(" ") || ket.equals("-") || ket.equals("null") || ket==null){
                myViewHolder.ketPart.setVisibility(View.GONE);
            } else {
                myViewHolder.ketPart.setVisibility(View.VISIBLE);
                myViewHolder.ketDataTV.setText(ket);
            }
        }

        myViewHolder.dateDataTV.setText(dataDate.substring(8,10)+"/"+dataDate.substring(5,7)+"/"+dataDate.substring(0,4));
        myViewHolder.fieldDataTV.setText(dataHistorPenambahanCuti.getStok_masuk()+" Hari");

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleDataTV, dateDataTV, fieldDataTV, ketDataTV;
        LinearLayout ketPart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleDataTV = itemView.findViewById(R.id.title_data_tv);
            dateDataTV = itemView.findViewById(R.id.date_data_tv);
            fieldDataTV = itemView.findViewById(R.id.field_data_tv);
            ketPart = itemView.findViewById(R.id.ket_part);
            ketDataTV = itemView.findViewById(R.id.ket_data_tv);
        }
    }


}