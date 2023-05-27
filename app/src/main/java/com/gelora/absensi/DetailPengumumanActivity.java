package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.codesgood.views.JustifiedTextView;
import com.gelora.absensi.kalert.KAlertDialog;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailPengumumanActivity extends AppCompatActivity {

    LinearLayout backBTN, imagePart, actionBar, viewBTN;
    TextView titleTV, timeTV, authorTV;
    TextView deskripsiTV;
    String id_pengumuman = "";
    ImageView imagePengumuman;
    SwipeRefreshLayout refreshLayout;
    CardView imageView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pengumuman);

        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        backBTN = findViewById(R.id.back_btn);
        viewBTN = findViewById(R.id.view_btn);
        imageView = findViewById(R.id.image_view);
        imagePart = findViewById(R.id.image_part);
        titleTV = findViewById(R.id.title_tv);
        deskripsiTV = findViewById(R.id.deskripsi_tv);
        timeTV = findViewById(R.id.time_tv);
        imagePengumuman = findViewById(R.id.image_pengumuman);
        authorTV = findViewById(R.id.author_tv);
        actionBar = findViewById(R.id.action_bar);

        id_pengumuman = getIntent().getExtras().getString("id_pengumuman");

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getData();
                    }
                }, 1000);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getData();

    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_detail_pengumuman";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                JSONObject dataArray = data.getJSONObject("data");
                                String pengumuman_author = dataArray.getString("pengumuman_author");
                                String pengumuman_image = dataArray.getString("pengumuman_image");
                                String pengumuman_title = dataArray.getString("pengumuman_title");
                                String pengumuman_desc = dataArray.getString("pengumuman_desc");
                                String pengumuman_date = dataArray.getString("pengumuman_date");
                                String pengumuman_time = dataArray.getString("pengumuman_time");

                                if(pengumuman_author.equals("")||pengumuman_author.equals("null")||pengumuman_author==null){
                                    authorTV.setVisibility(View.GONE);
                                } else {
                                    authorTV.setVisibility(View.VISIBLE);
                                    authorTV.setText(pengumuman_author);
                                }

                                if(pengumuman_image.equals("")||pengumuman_image.equals("null")||pengumuman_image==null){
                                    imagePart.setVisibility(View.GONE);
                                } else {
                                    imagePart.setVisibility(View.VISIBLE);
                                    Picasso.get().load("https://geloraaksara.co.id/absen-online/assets/img/pengumuman/"+pengumuman_image).networkPolicy(NetworkPolicy.NO_CACHE)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .into(imagePengumuman);

                                    // Glide.with(getApplicationContext()).load("https://geloraaksara.co.id/absen-online/assets/img/pengumuman/"+pengumuman_image).into(imagePengumuman);

                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailPengumumanActivity.this, ViewImageActivity.class);
                                            intent.putExtra("url", "https://geloraaksara.co.id/absen-online/assets/img/pengumuman/"+pengumuman_image);
                                            intent.putExtra("kode", "pengumuman");
                                            startActivity(intent);
                                        }
                                    });

                                    viewBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailPengumumanActivity.this, ViewImageActivity.class);
                                            intent.putExtra("url", "https://geloraaksara.co.id/absen-online/assets/img/pengumuman/"+pengumuman_image);
                                            intent.putExtra("kode", "pengumuman");
                                            startActivity(intent);
                                        }
                                    });

                                }

                                titleTV.setText(pengumuman_title.toUpperCase());
                                //deskripsiTV.setText(pengumuman_desc);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    deskripsiTV.setText(Html.fromHtml(pengumuman_desc, Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    deskripsiTV.setText(Html.fromHtml(pengumuman_desc));
                                }

                                String input_date = pengumuman_date;
                                String dayDate = input_date.substring(8,10);
                                String yearDate = input_date.substring(0,4);
                                String bulanValue = input_date.substring(5,7);
                                String bulanName;

                                switch (bulanValue) {
                                    case "01":
                                        bulanName = "Jan";
                                        break;
                                    case "02":
                                        bulanName = "Feb";
                                        break;
                                    case "03":
                                        bulanName = "Mar";
                                        break;
                                    case "04":
                                        bulanName = "Apr";
                                        break;
                                    case "05":
                                        bulanName = "Mei";
                                        break;
                                    case "06":
                                        bulanName = "Jun";
                                        break;
                                    case "07":
                                        bulanName = "Jul";
                                        break;
                                    case "08":
                                        bulanName = "Agu";
                                        break;
                                    case "09":
                                        bulanName = "Sep";
                                        break;
                                    case "10":
                                        bulanName = "Okt";
                                        break;
                                    case "11":
                                        bulanName = "Nov";
                                        break;
                                    case "12":
                                        bulanName = "Des";
                                        break;
                                    default:
                                        bulanName = "Not found";
                                        break;
                                }

                                String bulan = input_date.substring(0,4)+"-"+input_date.substring(5,7);
                                String bulansekarang = getDate().substring(0,4)+"-"+getDate().substring(5,7);

                                if(bulan.equals(bulansekarang)){
                                    String hari = input_date.substring(8,10);
                                    String hari_sekarang = getDate().substring(8,10);
                                    int selisih_hari = Integer.parseInt(hari_sekarang) - Integer.parseInt(hari);
                                    if(selisih_hari==0){
                                        timeTV.setText("Diposting "+"hari ini, "+pengumuman_time.substring(0,5));
                                    } else if(selisih_hari==1){
                                        timeTV.setText("Diposting "+"kemarin, "+pengumuman_time.substring(0,5));
                                    } else {
                                        timeTV.setText("Diposting pada "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                                    }
                                } else {
                                    timeTV.setText("Diposting pada "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate);
                                }


                            } else {
                                new KAlertDialog(DetailPengumumanActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Terjadi kesalahan saat mengakses data")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_pengumuman", id_pengumuman);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(DetailPengumumanActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

}