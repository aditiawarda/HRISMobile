package com.gelora.absensi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.gelora.absensi.R;
import com.gelora.absensi.model.DataDokumentasiProject;
import com.github.chrisbanes.photoview.PhotoView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yarolegovich on 07.03.2017.
 */

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
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dokumentasi_project, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(AdapterDokumentasiProject.SliderAdapterVH viewHolder, final int position) {
        DataDokumentasiProject image = mSliderItems.get(position);

        Glide.with(viewHolder.itemView)
                .load(image.getImageUrl())
                .centerCrop()
                .into(viewHolder.image);

    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public static class SliderAdapterVH extends ViewHolder {

        View itemView;
        private PhotoView image;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_dokumentasi);
            this.itemView = itemView;
        }
    }
}
