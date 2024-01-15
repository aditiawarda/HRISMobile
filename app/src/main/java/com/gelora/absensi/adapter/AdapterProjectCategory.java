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

import com.gelora.absensi.MapsActivity;
import com.gelora.absensi.ProjectViewActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.ProjectCategory;
import com.gelora.absensi.model.StatusAbsen;

public class AdapterProjectCategory extends RecyclerView.Adapter<AdapterProjectCategory.MyViewHolder> {

    private ProjectCategory[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterProjectCategory(ProjectCategory[] data, ProjectViewActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_kategori_project,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ProjectCategory projectCategory = data[i];

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.roboto);
            myViewHolder.namaKategori.setTypeface(typeface);
        }

        myViewHolder.namaKategori.setText(projectCategory.getCategoryName());

        if (sharedPrefAbsen.getSpKategoriProject().equals(projectCategory.getId())) {
            myViewHolder.markPart.setVisibility(View.VISIBLE);
            myViewHolder.parentPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option_choice));
        } else {
            myViewHolder.markPart.setVisibility(View.GONE);
            myViewHolder.parentPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option));
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {

                notifyDataSetChanged();

                Intent intent = new Intent("category_broad");
                intent.putExtra("id_kategori",projectCategory.getId());
                intent.putExtra("nama_kategori",projectCategory.getCategoryName());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_KATEGORI_PROJECT, projectCategory.getId());

            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namaKategori;
        LinearLayout parentPart, markPart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            markPart = itemView.findViewById(R.id.mark_part);
            namaKategori = itemView.findViewById(R.id.nama_kategori_project);
        }
    }

}
