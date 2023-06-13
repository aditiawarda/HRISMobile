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
import com.gelora.absensi.model.UnitDepartemen;

public class AdapterUnitDepartemenLama extends RecyclerView.Adapter<AdapterUnitDepartemenLama.MyViewHolder> {

    private UnitDepartemen[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterUnitDepartemenLama(UnitDepartemen[] data, FormSdmActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_unit_departemen,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final UnitDepartemen unitDepartemen = data[i];

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.roboto);
            myViewHolder.departemenName.setTypeface(typeface);
        }

        myViewHolder.departemenName.setText(unitDepartemen.getNmHeadDept());
        myViewHolder.descDepartemen.setText(unitDepartemen.getDesHeadDept());

        if (sharedPrefAbsen.getSpIdUnitDepartemenLama().equals(unitDepartemen.getIdHeadDept())) {
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

                Intent intent = new Intent("f3_departemen_lama_board");
                intent.putExtra("id_departemen",unitDepartemen.getIdHeadDept());
                intent.putExtra("nama_departemen",unitDepartemen.getNmHeadDept());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_UNIT_DEPARTEMEN_LAMA, unitDepartemen.getIdHeadDept());

            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView departemenName, descDepartemen;
        LinearLayout statusParent, markStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            statusParent = itemView.findViewById(R.id.unit_departemen_part);
            markStatus = itemView.findViewById(R.id.mark_status);
            departemenName = itemView.findViewById(R.id.nama_unit_departemen);
            descDepartemen = itemView.findViewById(R.id.desc_unit_departemen);
        }
    }
}
