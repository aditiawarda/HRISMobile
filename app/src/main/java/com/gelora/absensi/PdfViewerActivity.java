package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gelora.absensi.kalert.KAlertDialog;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.File;

public class PdfViewerActivity extends AppCompatActivity {

    PDFView pdfView;
    TextView titlePage;
    LinearLayout actionBar, loadingPart, backBTN;
    private View scrollIndicator;
    private Handler handler = new Handler();

    @SuppressLint({"SetJavaScriptEnabled", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        titlePage = findViewById(R.id.title_page);
        actionBar = findViewById(R.id.action_bar);
        backBTN = findViewById(R.id.back_btn);
        pdfView = findViewById(R.id.pdfView);
        scrollIndicator = findViewById(R.id.scrollIndicator);
        loadingPart = findViewById(R.id.loading_part);

        String initialisasi = getIntent().getExtras().getString("initialisasi");
        String kodeST = getIntent().getExtras().getString("kode_st");
        String uriFile = getIntent().getExtras().getString("uri");
        File pdfFile = new File(uriFile);

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

        if(kodeST.equals("1")){
            titlePage.setText("SERAH TERIMA TUGAS/PEKERJAAN");
        } else if(kodeST.equals("2")){
            titlePage.setText("SERAH TERIMA KENDARAAN");
        } else if(kodeST.equals("3")){
            titlePage.setText("SERAH TERIMA FASILITAS IT");
        } else if(kodeST.equals("4")){
            titlePage.setText("SERAH TERIMA KEUANGAN");
        } else if(kodeST.equals("5")){
            titlePage.setText("SERAH TERIMA KOPERASI");
        } else if(kodeST.equals("6")){
            titlePage.setText("SERAH TERIMA PERSONALIA");
        } else if(kodeST.equals("penilaian_tahunan")){
            titlePage.setText("PENILAIAN KARYAWAN TAHUNAN");
        }

        if(initialisasi.equals("form")){
            if (pdfFile.exists()) {
                pdfView.fromFile(pdfFile)
                        .defaultPage(0)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .onPageChange(new OnPageChangeListener() {
                            @Override
                            public void onPageChanged(int page, int pageCount) {
                                if(pageCount<=1){
                                    scrollIndicator.setVisibility(View.GONE);
                                } else {
                                    scrollIndicator.setVisibility(View.VISIBLE);
                                }
                                updateScrollIndicator(page, pageCount);
                            }
                        })
                        .load();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingPart.setVisibility(View.GONE);
                    }
                }, 2000);

            } else {
                Toast.makeText(this, "Kosong", Toast.LENGTH_SHORT).show();
            }

        } else if(initialisasi.equals("detail")){
            Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse(uriFile));
            try {
                startActivity(webIntent);
                finish();
            } catch (SecurityException e) {
                e.printStackTrace();
                new KAlertDialog(PdfViewerActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
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

    }

    private void updateScrollIndicator(int currentPage, int pageCount) {
        float progress = (float) (currentPage + 1) / pageCount;
        int indicatorHeight = (int) (pdfView.getHeight() * progress);
        scrollIndicator.getLayoutParams().height = indicatorHeight;
        scrollIndicator.requestLayout();
    }

}