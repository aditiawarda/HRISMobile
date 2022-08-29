package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ViewImageActivity extends AppCompatActivity {

    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView mainImage;
    LinearLayout backBTN,loadingPart;
    TextView titlePageTV;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        mainImage = findViewById(R.id.main_image);
        backBTN = findViewById(R.id.back_btn);
        titlePageTV = findViewById(R.id.title_page);
        loadingPart = findViewById(R.id.loading_part);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        String url = getIntent().getExtras().getString("url");
        String kode = getIntent().getExtras().getString("kode");

        if(kode.equals("form")){
            titlePageTV.setText("SURAT SAKIT");
            Picasso.get().load(url).into(mainImage);
        } else if (kode.equals("profile")){
            titlePageTV.setText("FOTO PROFIL");
            Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(mainImage);
        } else if (kode.equals("detail")){
            titlePageTV.setText("SURAT SAKIT");
            Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(mainImage);
        } else if (kode.equals("chat_mate")){
            String nameChatMate = getIntent().getExtras().getString("name_chat_mate");
            titlePageTV.setText(nameChatMate.toUpperCase());
            Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(mainImage);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingPart.setVisibility(View.GONE);
            }
        }, 1000);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            mainImage.setScaleX(mScaleFactor);
            mainImage.setScaleY(mScaleFactor);
            return true;
        }
    }

}