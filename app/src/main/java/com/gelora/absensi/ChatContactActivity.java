package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.gelora.absensi.adapter.AdapterChat;
import com.gelora.absensi.adapter.AdapterListChatMate;
import com.gelora.absensi.adapter.AdapterListContactSearch;
import com.gelora.absensi.model.ChatModel;
import com.gelora.absensi.model.ContactSearch;
import com.gelora.absensi.model.ListChatMate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChatContactActivity extends AppCompatActivity {

    SharedPrefManager sharedPrefManager;
    private RecyclerView listContactRV;
    private ContactSearch[] contactSearches;
    private AdapterListContactSearch adapterListContactSearch;
    EditText keywordContact;
    LinearLayout backBTN, noDataPart, loadingPart, actionBar;
    boolean canEnterSpace = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_contact);

        sharedPrefManager = new SharedPrefManager(this);
        listContactRV = findViewById(R.id.data_list_contact_rv);
        keywordContact = findViewById(R.id.keyword_contact_ed);
        backBTN = findViewById(R.id.back_btn);
        noDataPart = findViewById(R.id.no_data_part);
        loadingPart = findViewById(R.id.loading_data_part);
        actionBar = findViewById(R.id.action_bar);

        listContactRV.setLayoutManager(new LinearLayoutManager(this));
        listContactRV.setHasFixedSize(true);
        listContactRV.setItemAnimator(new DefaultItemAnimator());

        keywordContact.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

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

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                String blockCharacterSet = ".,+×÷=/_€£¥₩!@#$%^&*()-'\":;,?`~\\|<>{}[]°•○●□■♤♡◇♧☆▪︎¤《》¡¿";

                if(keywordContact.getText().toString().equals("")) {
                    canEnterSpace = false;
                }

                StringBuilder builder = new StringBuilder();

                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);

                    if (Character.isLetterOrDigit(currentChar) || blockCharacterSet.contains(("" + source))) {
                        builder.append(currentChar);
                        canEnterSpace = true;
                    }

                    if(Character.isWhitespace(currentChar) && canEnterSpace) {
                        builder.append(currentChar);
                    }

                }
                return builder.toString();
            }

        };

        keywordContact.setFilters(new InputFilter[]{filter});

        keywordContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = keywordContact.getText().toString();
                if(canEnterSpace){
                    if(keyword.equals("")){
                        listContactRV.setVisibility(View.GONE);
                        loadingPart.setVisibility(View.VISIBLE);
                    } else {
                        listContactRV.setVisibility(View.VISIBLE);
                        loadingPart.setVisibility(View.GONE);
                        searchContact(keyword);
                    }
                } else {
                    listContactRV.setVisibility(View.GONE);
                    loadingPart.setVisibility(View.VISIBLE);
                }

            }

        });

        keywordContact.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return false;
            }
        });

    }

    private void searchContact(String keyword) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/cari_kontak";
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

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        noDataPart.setVisibility(View.GONE);
                                        loadingPart.setVisibility(View.GONE);
                                        listContactRV.setVisibility(View.VISIBLE);
                                    }
                                }, 500);

                                String data_contact = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                contactSearches = gson.fromJson(data_contact, ContactSearch[].class);
                                adapterListContactSearch = new AdapterListContactSearch(contactSearches, ChatContactActivity.this);
                                listContactRV.setAdapter(adapterListContactSearch);
                            } else {
                                noDataPart.setVisibility(View.VISIBLE);
                                loadingPart.setVisibility(View.GONE);
                                listContactRV.setVisibility(View.GONE);
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
                        noDataPart.setVisibility(View.VISIBLE);
                        loadingPart.setVisibility(View.GONE);
                        listContactRV.setVisibility(View.GONE);
                        connectionFailed();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("saya", sharedPrefManager.getSpNik());
                params.put("nama_saya", sharedPrefManager.getSpNama());
                params.put("keyword", keyword);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        // Banner.make(rootview, SearchKaryawanBagianActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 3000).show();

        CookieBar.build(ChatContactActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

}