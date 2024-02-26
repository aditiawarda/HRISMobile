package com.gelora.absensi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.adapter.AdapterDokumentasiProject;
import com.gelora.absensi.adapter.AdapterImageSlider;
import com.gelora.absensi.model.DataDokumentasiProject;
import com.gelora.absensi.model.DataImageSlider;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewImageSliderActivity extends AppCompatActivity {

    LinearLayout actionBar, backBTN;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    RequestQueue requestQueue;
    String data = "";
    List<DataImageSlider> dataImageSliders = new ArrayList<>();
    private AdapterImageSlider adapterImageSlider;
    SliderView sliderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image_slider);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefAbsen = new SharedPrefAbsen(this);
        requestQueue = Volley.newRequestQueue(this);
        backBTN = findViewById(R.id.back_btn);
        actionBar = findViewById(R.id.action_bar);

        sliderView = findViewById(R.id.imageSlider);
        adapterImageSlider = new AdapterImageSlider(dataImageSliders);
        sliderView.setSliderAdapter(adapterImageSlider);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.SCROLL_AXIS_NONE);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(false);

        data = getIntent().getExtras().getString("data");

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

        getImage();

    }

    private void getImage() {
        String cleanString = data.substring(1, data.length() - 1);
        String[] parts = cleanString.split(",");
        String[] array = new String[parts.length];
        for (int i = 0; i < parts.length; i++) {
            array[i] = parts[i].trim();
        }

        for (String str : array) {
            DataImageSlider dataImage = new DataImageSlider();
            dataImage.setImageUrl(str);
            dataImageSliders.add(dataImage);
            adapterImageSlider.renewItems(dataImageSliders);
        }

    }

}