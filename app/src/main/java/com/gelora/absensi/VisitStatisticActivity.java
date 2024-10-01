package com.gelora.absensi;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class VisitStatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_visit_statistic);

        PieChart pieChart = findViewById(R.id.piechart);

        // Data untuk PieChart
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(15f, "Jakarta 1"));
        entries.add(new PieEntry(25f, "Jakarta 2"));
        entries.add(new PieEntry(35f, "Jakarta 3"));
        entries.add(new PieEntry(9f, "Bandung"));
        entries.add(new PieEntry(9f, "Semarang"));
        entries.add(new PieEntry(9f, "Surabaya"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setValueTextSize(25f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Warna slice pie chart

        // Set data ke PieChart
        PieData data = new PieData(dataSet);
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);

        // Menghilangkan lingkaran putih di tengah
        pieChart.setDrawHoleEnabled(false);

        // Konfigurasi PieChart
        pieChart.setUsePercentValues(true); // Tampilkan nilai dalam persentase
        pieChart.getDescription().setEnabled(false); // Hapus deskripsi default
        pieChart.setEntryLabelTextSize(12f); // Ukuran teks label
        pieChart.setEntryLabelColor(Color.BLACK); // Warna teks label
        pieChart.animateY(1000); // Animasi pie chart
        pieChart.getLegend().setEnabled(false);

//        // Konfigurasi Legend di bawah PieChart
//        Legend legend = pieChart.getLegend();
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//        legend.setDrawInside(false); // Letakkan di luar chart
//        legend.setTextSize(14f);     // Ukuran teks legend
//        legend.setWordWrapEnabled(true); // Biarkan teks legend membu

    }
}