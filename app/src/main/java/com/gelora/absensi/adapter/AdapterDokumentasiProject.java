package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.gelora.absensi.R;
import com.gelora.absensi.model.DataDokumentasiProject;
import com.github.chrisbanes.photoview.PhotoView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterDokumentasiProject extends SliderViewAdapter<AdapterDokumentasiProject.SliderAdapterVH> {

    private Context context;
    private List<DataDokumentasiProject> mSliderItems = new ArrayList<>();

    public AdapterDokumentasiProject(List<DataDokumentasiProject> sliderItems) {
        this.mSliderItems = sliderItems;
    }

    public void renewItems(List<DataDokumentasiProject> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(DataDokumentasiProject sliderItems) {
        this.mSliderItems.add(sliderItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        @SuppressLint("InflateParams")
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dokumentasi_project, null);
        return new SliderAdapterVH(inflate);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(AdapterDokumentasiProject.SliderAdapterVH viewHolder, final int position) {
        DataDokumentasiProject image = mSliderItems.get(position);

        viewHolder.timeUploadTV.setText("Update : "+image.getCreatedAt()+" WIB");

        Glide.with(viewHolder.itemView)
                .load(image.getImageUrl())
                .into(viewHolder.image);
        viewHolder.deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("remove_dokumentasi");
                intent.putExtra("url",image.getImageUrl());
                LocalBroadcastManager.getInstance(v.getContext()).sendBroadcast(intent);
            }
        });

    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public static class SliderAdapterVH extends ViewHolder {

        View itemView;
        private PhotoView image;
        LinearLayout deleteBTN;
        TextView timeUploadTV;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_dokumentasi);
            deleteBTN = itemView.findViewById(R.id.delete_image_btn);
            timeUploadTV = itemView.findViewById(R.id.time_upload);
            this.itemView = itemView;
        }
    }
}
