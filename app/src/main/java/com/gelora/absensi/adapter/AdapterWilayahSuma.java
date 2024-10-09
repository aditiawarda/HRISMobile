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

import com.gelora.absensi.ListDataReportSumaActivity;
import com.gelora.absensi.MapsActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.StatusAbsen;
import com.gelora.absensi.model.WilayahSuma;

public class AdapterWilayahSuma extends RecyclerView.Adapter<AdapterWilayahSuma.MyViewHolder> {

    private WilayahSuma[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterWilayahSuma(WilayahSuma[] data, ListDataReportSumaActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_wilayah_suma,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final WilayahSuma wilayahSuma = data[i];

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.roboto);
            myViewHolder.name.setTypeface(typeface);
        }

        if(wilayahSuma.getNama_wilayah().equals("Jakarta 1") || wilayahSuma.getNama_wilayah().equals("Jakarta 2") || wilayahSuma.getNama_wilayah().equals("Jakarta 3") || wilayahSuma.getNama_wilayah().equals("Bandung") || wilayahSuma.getNama_wilayah().equals("Semarang") || wilayahSuma.getNama_wilayah().equals("Surabaya")){
            myViewHolder.name.setText("Suma "+wilayahSuma.getNama_wilayah());
        } else {
            myViewHolder.name.setText(wilayahSuma.getNama_wilayah());
        }

        if (sharedPrefAbsen.getSpWilayahSuma().equals(wilayahSuma.getId())) {
            myViewHolder.mark.setVisibility(View.VISIBLE);
            myViewHolder.parent.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option_choice));
        } else {
            myViewHolder.mark.setVisibility(View.GONE);
            myViewHolder.parent.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option));
        }

        myViewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();

                Intent intent = new Intent("wilayah_suma_broad");
                intent.putExtra("id_wilayah_suma",wilayahSuma.getId());
                intent.putExtra("nama_wilayah_suma",wilayahSuma.getNama_wilayah());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_WILAYAH_SUMA, wilayahSuma.getId());
            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout parent, mark;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.wilayah_suma_part);
            mark = itemView.findViewById(R.id.mark_status);
            name = itemView.findViewById(R.id.nama_wilayah);
        }
    }

}
