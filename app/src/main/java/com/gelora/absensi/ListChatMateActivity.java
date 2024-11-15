package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.adapter.AdapterListChatMate;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.ListChatMate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListChatMateActivity extends AppCompatActivity {

    private RecyclerView listChatRV;
    private ListChatMate[] listChatMates;
    private AdapterListChatMate adapterListChatMate;
    SharedPrefManager sharedPrefManager;
    LinearLayout backBTN, noDataPart, loadingPart, actionBar;
    LinearLayout newChatBTN;
    KAlertDialog pDialog;
    private int i = -1;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chat_mate);

        sharedPrefManager = new SharedPrefManager(this);
        listChatRV = findViewById(R.id.data_list_chat_mate_rv);
        backBTN = findViewById(R.id.back_btn);
        noDataPart = findViewById(R.id.no_data_part);
        loadingPart = findViewById(R.id.loading_data_part);
        newChatBTN = findViewById(R.id.new_chat_btn);
        actionBar = findViewById(R.id.action_bar);

        LocalBroadcastManager.getInstance(this).registerReceiver(endChat,
                new IntentFilter("end_chat"));

        listChatRV.setLayoutManager(new LinearLayoutManager(this));
        listChatRV.setHasFixedSize(true);
        listChatRV.setNestedScrollingEnabled(false);
        listChatRV.setItemAnimator(new DefaultItemAnimator());

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

        newChatBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListChatMateActivity.this, ChatContactActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getListChatMate() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/get_list_chatmate";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")) {

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        noDataPart.setVisibility(View.GONE);
                                        loadingPart.setVisibility(View.GONE);
                                        listChatRV.setVisibility(View.VISIBLE);
                                    }
                                }, 500);

                                String data_list_chat = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                listChatMates = gson.fromJson(data_list_chat, ListChatMate[].class);
                                adapterListChatMate = new AdapterListChatMate(listChatMates, ListChatMateActivity.this);
                                listChatRV.setAdapter(adapterListChatMate);
                            } else {
                                noDataPart.setVisibility(View.VISIBLE);
                                loadingPart.setVisibility(View.GONE);
                                listChatRV.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        connectionFailed();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("saya", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    public BroadcastReceiver endChat = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String nik_rekan_chat = intent.getStringExtra("nik_rekan");
            String nama_rekan_chat = intent.getStringExtra("nama_rekan");

            String shortName;
            String[] shortNameArray = nama_rekan_chat.split(" ");

            if(shortNameArray.length>1){
                if(shortNameArray[0].length()<3){
                    shortName = shortNameArray[1];
                } else {
                    shortName = shortNameArray[0];
                }
            } else {
                shortName = shortNameArray[0];
            }

            try {
                new KAlertDialog(ListChatMateActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Apakah anda yakin untuk menghapus percakapan dengan "+shortName+"?")
                        .setCancelText("TIDAK")
                        .setConfirmText("   YA   ")
                        .showCancelButton(true)
                        .setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismiss();
                            }
                        })
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismiss();

                                pDialog = new KAlertDialog(ListChatMateActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1300, 800) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
                                            case 0:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (ListChatMateActivity.this, R.color.colorGradien));
                                                break;
                                            case 1:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (ListChatMateActivity.this, R.color.colorGradien2));
                                                break;
                                            case 2:
                                            case 6:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (ListChatMateActivity.this, R.color.colorGradien3));
                                                break;
                                            case 3:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (ListChatMateActivity.this, R.color.colorGradien4));
                                                break;
                                            case 4:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (ListChatMateActivity.this, R.color.colorGradien5));
                                                break;
                                            case 5:
                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                        (ListChatMateActivity.this, R.color.colorGradien6));
                                                break;
                                        }
                                    }
                                    public void onFinish() {
                                        i = -1;
                                        endChatFunction(nik_rekan_chat);
                                    }
                                }.start();

                            }
                        })
                        .show();
            }
            catch (WindowManager.BadTokenException e) {
                Log.d("Error", e.toString());
            }

        }
    };


    private void endChatFunction(String nikRekan) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/end_chat";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")) {
                                pDialog.setTitleText("Percakapan Dihapus")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                pDialog.dismiss();
                                                getListChatMate();
                                            }
                                        })
                                        .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        connectionFailed();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("saya", sharedPrefManager.getSpNik());
                params.put("rekan", nikRekan);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(ListChatMateActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListChatMate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}