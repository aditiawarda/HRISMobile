package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codesgood.views.JustifiedTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailPengumumanActivity extends AppCompatActivity {

    LinearLayout backBTN, imagePart;
    TextView titleTV, timeTV;
    JustifiedTextView deskripsiTV;
    String image = "", title = "", deskripsi = "", time = "", date = "";
    ImageView imagePengumuman;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pengumuman);

        backBTN = findViewById(R.id.back_btn);
        imagePart = findViewById(R.id.image_part);
        titleTV = findViewById(R.id.title_tv);
        deskripsiTV = findViewById(R.id.deskripsi_tv);
        timeTV = findViewById(R.id.time_tv);
        imagePengumuman = findViewById(R.id.image_pengumuman);

        image = getIntent().getExtras().getString("image");
        title = getIntent().getExtras().getString("title");
        deskripsi = getIntent().getExtras().getString("deskripsi");
        date = getIntent().getExtras().getString("date");
        time = getIntent().getExtras().getString("time");

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(image.equals("")||image.equals("null")||image==null){
            imagePart.setVisibility(View.GONE);
        } else {
            imagePart.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext())
                    .load(image).into(imagePengumuman);
        }

        titleTV.setText(title.toUpperCase());
        deskripsiTV.setText(deskripsi);

        String input_date = date;
        String dayDate = input_date.substring(8,10);
        String yearDate = input_date.substring(0,4);
        String bulanValue = input_date.substring(5,7);
        String bulanName;

        switch (bulanValue) {
            case "01":
                bulanName = "Januari";
                break;
            case "02":
                bulanName = "Februari";
                break;
            case "03":
                bulanName = "Maret";
                break;
            case "04":
                bulanName = "April";
                break;
            case "05":
                bulanName = "Mei";
                break;
            case "06":
                bulanName = "Juni";
                break;
            case "07":
                bulanName = "Juli";
                break;
            case "08":
                bulanName = "Agustus";
                break;
            case "09":
                bulanName = "September";
                break;
            case "10":
                bulanName = "Oktober";
                break;
            case "11":
                bulanName = "November";
                break;
            case "12":
                bulanName = "Desember";
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
                timeTV.setText("Hari ini, "+time.substring(0,5));
            } else if(selisih_hari==1){
                timeTV.setText("Kemarin, "+time.substring(0,5));
            } else {
                timeTV.setText(String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
            }
        } else {
            timeTV.setText(String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
        }

    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

}