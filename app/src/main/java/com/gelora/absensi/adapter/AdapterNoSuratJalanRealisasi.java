package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailReportSumaActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.ReportSumaActivity;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.DataNoSuratJalan;

public class AdapterNoSuratJalanRealisasi extends RecyclerView.Adapter<AdapterNoSuratJalanRealisasi.MyViewHolder> {

    private DataNoSuratJalan[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterNoSuratJalanRealisasi(DataNoSuratJalan[] data, DetailReportSumaActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_no_sj,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataNoSuratJalan dataNoSuratJalan = data[i];

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.roboto);
            myViewHolder.noSJ.setTypeface(typeface);
        }

        myViewHolder.noSJ.setText(String.valueOf(dataNoSuratJalan.getNoSuratJalan()));

        if (sharedPrefAbsen.getSpNoSj().equals(dataNoSuratJalan.getNoSuratJalan())) {
            myViewHolder.markStatus.setVisibility(View.VISIBLE);
            myViewHolder.parentPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option_choice));
        } else {
            myViewHolder.markStatus.setVisibility(View.GONE);
            myViewHolder.parentPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option));
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                Intent intent = new Intent("list_no_sj");
                intent.putExtra("no_sj",dataNoSuratJalan.getNoSuratJalan());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_NO_SJ, dataNoSuratJalan.getNoSuratJalan());
            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView noSJ;
        LinearLayout parentPart, markStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            markStatus = itemView.findViewById(R.id.mark_status);
            noSJ = itemView.findViewById(R.id.no_sj_tv);
        }
    }
}
