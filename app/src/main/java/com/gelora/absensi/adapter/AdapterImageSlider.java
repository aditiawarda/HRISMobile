package com.gelora.absensi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.gelora.absensi.R;
import com.gelora.absensi.model.DataDokumentasiProject;
import com.gelora.absensi.model.DataImageSlider;
import com.github.chrisbanes.photoview.PhotoView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterImageSlider extends SliderViewAdapter<AdapterImageSlider.SliderAdapterVH> {

    private Context context;
    private List<DataImageSlider> mSliderItems = new ArrayList<>();

    public AdapterImageSlider(List<DataImageSlider> sliderItems) {
        this.mSliderItems = sliderItems;
    }

    public void renewItems(List<DataImageSlider> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(DataImageSlider sliderItems) {
        this.mSliderItems.add(sliderItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(AdapterImageSlider.SliderAdapterVH viewHolder, final int position) {
        DataImageSlider image = mSliderItems.get(position);
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
