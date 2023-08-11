package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailCuacaActivity;
import com.gelora.absensi.DetailHadirActivity;
import com.gelora.absensi.DetailPermohonanCutiActivity;
import com.gelora.absensi.DetailPermohonanIzinActivity;
import com.gelora.absensi.NewsActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataHadir;
import com.gelora.absensi.model.NewsData;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterDataNews extends RecyclerView.Adapter<AdapterDataNews.MyViewHolder> {

    private NewsData[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterDataNews(NewsData[] data, NewsActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_news,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final NewsData dataNews = data[i];
        String namaStatus = "";

        myViewHolder.titleTV.setText(dataNews.getTitle());

        URL url = null;
        try {
            url = new URL(String.valueOf(dataNews.getLink()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String path = url.getFile().substring(0, url.getFile().lastIndexOf('/'));
        String base = url.getProtocol() + "://" + url.getHost() + path;

        myViewHolder.creatorTV.setText(url.getHost());

        String input_date = dataNews.getPubDate();
        String dayDate = input_date.substring(8,10);
        String yearDate = input_date.substring(0,4);
        String bulanValue = input_date.substring(5,7);
        String bulanName;

        switch (bulanValue) {
            case "01":
                bulanName = "Jan";
                break;
            case "02":
                bulanName = "Feb";
                break;
            case "03":
                bulanName = "Mar";
                break;
            case "04":
                bulanName = "Apr";
                break;
            case "05":
                bulanName = "Mei";
                break;
            case "06":
                bulanName = "Jun";
                break;
            case "07":
                bulanName = "Jul";
                break;
            case "08":
                bulanName = "Agu";
                break;
            case "09":
                bulanName = "Sep";
                break;
            case "10":
                bulanName = "Okt";
                break;
            case "11":
                bulanName = "Nov";
                break;
            case "12":
                bulanName = "Des";
                break;
            default:
                bulanName = "Not found";
                break;
        }

        String bulan = input_date.substring(0,4)+"-"+input_date.substring(5,7);
        String bulansekarang = getDate().substring(0,4)+"-"+getDate().substring(5,7);

        if(bulan.equals(bulansekarang)){
            String hari = input_date.substring(8,10);
            String hari_sekarang = getDate().substring(8,10);
            int selisih_hari = Integer.parseInt(hari_sekarang) - Integer.parseInt(hari);
            if(selisih_hari==0){
                myViewHolder.pubDateTV.setText("Hari ini, "+dataNews.getPubDate().substring(11,16));
            } else if(selisih_hari==1){
                myViewHolder.pubDateTV.setText("Kemarin, "+dataNews.getPubDate().substring(11,16));
            } else {
                myViewHolder.pubDateTV.setText(String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
            }
        } else {
            myViewHolder.pubDateTV.setText(String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
        }

        Picasso.get().load(String.valueOf(dataNews.getImage_url())).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .resize(300, 300)
                .into(myViewHolder.imgNews);

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse(dataNews.getLink()));
                try {
                    mContext.startActivity(webIntent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                    new KAlertDialog(mContext, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Terjadi kesalahan")
                            .setContentText("Tidak dapat membuka browser")
                            .setConfirmText("    OK    ")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }
            }

        });

    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parentPart;
        TextView titleTV, creatorTV, pubDateTV;
        ImageView imgNews;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            titleTV = itemView.findViewById(R.id.title_tv);
            creatorTV = itemView.findViewById(R.id.creator_tv);
            pubDateTV = itemView.findViewById(R.id.pub_date_tv);
            imgNews = itemView.findViewById(R.id.img_news);
        }
    }


}