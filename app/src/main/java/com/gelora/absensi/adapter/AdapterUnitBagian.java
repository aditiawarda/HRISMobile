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

import com.gelora.absensi.FormSdmActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.UnitBagian;
import com.gelora.absensi.model.UnitDepartemen;

public class AdapterUnitBagian extends RecyclerView.Adapter<AdapterUnitBagian.MyViewHolder> {

    private UnitBagian[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterUnitBagian(UnitBagian[] data, FormSdmActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_unit_bagian,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final UnitBagian unitBagian = data[i];

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.roboto);
            myViewHolder.bagianName.setTypeface(typeface);
        }

        myViewHolder.bagianName.setText(unitBagian.getKdDept());
        myViewHolder.descBagian.setText(unitBagian.getNmDept());

        if (sharedPrefAbsen.getSpIdUnitBagian().equals(unitBagian.getIdDept())) {
            myViewHolder.markStatus.setVisibility(View.VISIBLE);
            myViewHolder.statusParent.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option_choice));
        } else {
            myViewHolder.markStatus.setVisibility(View.GONE);
            myViewHolder.statusParent.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option));
        }

        myViewHolder.statusParent.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {

                notifyDataSetChanged();

                Intent intent = new Intent("f1_bagian_board");
                intent.putExtra("id_bagian",unitBagian.getIdDept());
                intent.putExtra("nama_bagian",unitBagian.getNmDept());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_BAGIAN, unitBagian.getIdDept());

            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bagianName, descBagian;
        LinearLayout statusParent, markStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            statusParent = itemView.findViewById(R.id.unit_bagian_part);
            markStatus = itemView.findViewById(R.id.mark_status);
            bagianName = itemView.findViewById(R.id.nama_unit_bagian);
            descBagian = itemView.findViewById(R.id.desc_unit_bagian);
        }
    }
}
