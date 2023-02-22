package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gelora.absensi.support.StatusBarColorManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class DetailCuacaActivity extends AppCompatActivity {

    private StatusBarColorManager mStatusBarColorManager;
    String urlIcon = "", mainWaether = "", temp = "", feel_like = "", location;
    LinearLayout backBTN, moreBTN;
    TextView mainWeatherTV, weatherTempTV, feelsLikeTempTV, locationTV;
    ProgressBar loadingProgressBar;
    ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cuaca);

        backBTN = findViewById(R.id.back_btn);
        moreBTN = findViewById(R.id.more_btn);
        loadingProgressBar = findViewById(R.id.loadingProgressBar_cuaca);
        weatherIcon = findViewById(R.id.weather_icon);
        mainWeatherTV = findViewById(R.id.main_weather);
        weatherTempTV = findViewById(R.id.weather_temp);
        feelsLikeTempTV = findViewById(R.id.feels_like_temp);
        locationTV = findViewById(R.id.location_tv);

        mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);

        urlIcon = getIntent().getExtras().getString("url_icon");
        mainWaether = getIntent().getExtras().getString("main_weather");
        temp = getIntent().getExtras().getString("temp");
        feel_like = getIntent().getExtras().getString("feel_like");
        location = getIntent().getExtras().getString("location");

        Picasso.get().load(urlIcon).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(weatherIcon);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              loadingProgressBar.setVisibility(View.GONE);
              weatherIcon.setVisibility(View.VISIBLE);
              mainWeatherTV.setText(mainWaether);
              weatherTempTV.setText(temp);
              feelsLikeTempTV.setText(feel_like);
              locationTV.setText(location);
            }
        }, 2000);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        moreBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse("https://openweathermap.org/"));
                startActivity(webIntent);
            }
        });

    }
}