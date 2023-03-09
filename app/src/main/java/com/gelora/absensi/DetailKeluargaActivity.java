package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class DetailKeluargaActivity extends AppCompatActivity {

    String idData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_keluarga);

        idData = getIntent().getExtras().getString("id_data");

        Toast.makeText(this, idData, Toast.LENGTH_SHORT).show();

    }
}