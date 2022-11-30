package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.barteksc.pdfviewer.PDFView;

public class PdfViewerActivity extends AppCompatActivity {

    WebView WebviewPdf;
    String file_url = "https://geloraaksara.co.id/absen-online/absen/pdf_form_izin";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        WebviewPdf = findViewById(R.id.webview);

        WebviewPdf.getSettings().setLoadsImagesAutomatically(true);
        WebviewPdf.getSettings().setJavaScriptEnabled(true);
        WebviewPdf.getSettings().setDomStorageEnabled(true);
        // Baris di bawah untuk menambahkan scrollbar di dalam WebView-nya
        WebviewPdf.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebviewPdf.setWebViewClient(new WebViewClient());
        WebviewPdf.loadUrl(file_url);

    }
}