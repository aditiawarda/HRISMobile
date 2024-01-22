package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class PMToolActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmtool);

        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();

        String id_project = getIntent().getExtras().getString("id_project");

        // Enable JavaScript (optional)
        webSettings.setJavaScriptEnabled(true);

        // Load a web page
        webView.loadUrl("https://appointment.geloraaksara.co.id/dashboard/detail_task?id="+id_project);

    }
}