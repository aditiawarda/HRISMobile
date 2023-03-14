package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.Calendar;

public class FormInfoPengalamanActivity extends AppCompatActivity {

    LinearLayout dariTahunBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_info_pengalaman);

        dariTahunBTN = findViewById(R.id.dari_tahun_btn);

        dariTahunBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();

                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(FormInfoPengalamanActivity.this,
                    new MonthPickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(int selectedMonth, int selectedYear) { // on date set
                            Toast.makeText(FormInfoPengalamanActivity.this, String.valueOf(selectedYear), Toast.LENGTH_SHORT).show();
                        }
                    }, now.get(Calendar.YEAR), now.get(Calendar.MONTH));

                builder.setMinYear(1952)
                    .setActivatedYear(now.get(Calendar.YEAR))
                    .setMaxYear(now.get(Calendar.YEAR))
                    .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                        @Override
                        public void onYearChanged(int selectedYear) { // on year selected
                        }
                    }).build()
                    .show();
            }
        });

    }
}