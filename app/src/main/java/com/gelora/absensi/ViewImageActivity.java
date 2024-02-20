package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ViewImageActivity extends AppCompatActivity {

    private ImageView mainImage;
    LinearLayout actionBar, backBTN,loadingPart;
    TextView titlePageTV;
    PhotoView photoView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        mainImage = findViewById(R.id.main_image);
        backBTN = findViewById(R.id.back_btn);
        titlePageTV = findViewById(R.id.title_page);
        loadingPart = findViewById(R.id.loading_part);
        actionBar = findViewById(R.id.action_bar);
        photoView = findViewById(R.id.photo_view);

        String url = getIntent().getExtras().getString("url");
        String kode = getIntent().getExtras().getString("kode");

        if(kode.equals("form")){
            String jenis_form = getIntent().getExtras().getString("jenis_form");
            if(jenis_form.equals("izin")){
                titlePageTV.setText("SURAT SAKIT");
                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(photoView);
            } else if(jenis_form.equals("cuti")){
                titlePageTV.setText("LAMPIRAN");
                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(photoView);
            } else if(jenis_form.equals("suma")){
                titlePageTV.setText("LAMPIRAN");
                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(photoView);
            }
        } else if (kode.equals("profile")){
            titlePageTV.setText("FOTO PROFIL");
            Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(photoView);
        } else if (kode.equals("detail")){
            String jenis_detail = getIntent().getExtras().getString("jenis_detail");
            if(jenis_detail.equals("izin")){
                titlePageTV.setText("SURAT SAKIT");
                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(photoView);
            } else if(jenis_detail.equals("cuti")){
                titlePageTV.setText("LAMPIRAN CUTI");
                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(photoView);
            } else if(jenis_detail.equals("suma")){
                titlePageTV.setText("LAMPIRAN");
                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(photoView);
            }
        } else if (kode.equals("chat_mate")){
            String nameChatMate = getIntent().getExtras().getString("name_chat_mate");
            titlePageTV.setText(nameChatMate.toUpperCase());
            Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(photoView);
        } else if (kode.equals("pengumuman")){
            titlePageTV.setText("PENGUMUMAN");
            Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(photoView);
        }

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

}