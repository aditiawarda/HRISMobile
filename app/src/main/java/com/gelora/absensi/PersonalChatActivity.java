package com.gelora.absensi;

import static com.yalantis.ucrop.UCropFragment.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.adapter.AdapterChat;
import com.gelora.absensi.adapter.AdapterDataHadir;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.ChatModel;
import com.gelora.absensi.model.DataHadir;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalChatActivity extends AppCompatActivity {

    EditText messageED;
    ImageButton sendBTN, backBTN, moreBTN;
    String repeat = "true";
    String keyboard = "hide";
    String state = "0", removeChatLast = "";
    String rekanChat, partnerNameString;
    TextView partnerName, partnerDetail;
    CircleImageView partnerPic;
    LinearLayout sendBTNPart, emojiBTN;
    KAlertDialog pDialog;
    private int i = -1;
    LinearLayout loadingProgressBar;

    private RecyclerView chatRV;
    private ChatModel[] dataChat;
    private AdapterChat adapterChat;
    boolean canEnterSpace = false;

    SharedPrefManager sharedPrefManager;
    View constraintLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);

        sharedPrefManager = new SharedPrefManager(this);
        chatRV = findViewById(R.id.chat_rv);
        messageED = findViewById(R.id.message_et);
        sendBTN = findViewById(R.id.send_message_btn);
        backBTN = findViewById(R.id.back_btn);
        partnerName = findViewById(R.id.partner_name);
        partnerPic = findViewById(R.id.partner_pic);
        sendBTNPart = findViewById(R.id.send_btn_part);
        moreBTN = findViewById(R.id.more_btn);
        emojiBTN = findViewById(R.id.emoji_btn);
        partnerDetail = findViewById(R.id.partner_detail);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        rekanChat = getIntent().getExtras().getString("chat_mate");

        constraintLayout = findViewById(android.R.id.content);
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                constraintLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = constraintLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    keyboard = "show";
                } else {
                    keyboard = "hide";
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.getReverseLayout();
        chatRV.setLayoutManager(linearLayoutManager);
        chatRV.setHasFixedSize(true);
        chatRV.setItemAnimator(new DefaultItemAnimator());

        LocalBroadcastManager.getInstance(this).registerReceiver(removeChat,
                new IntentFilter("remove_chat"));

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                String blockCharacterSet = ".,+×÷=/_€£¥₩!@#$%^&*()-'\":;,?`~\\|<>{}[]°•○●□■♤♡◇♧☆▪︎¤《》¡¿";

                if(messageED.getText().toString().equals("")) {
                    canEnterSpace = false;
                }

                StringBuilder builder = new StringBuilder();

                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);
                    int type = Character.getType(source.charAt(i));

                    if (Character.isLetterOrDigit(currentChar) || blockCharacterSet.contains(("" + source)) || type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
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

        messageED.setFilters(new InputFilter[]{filter});

        messageED.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(canEnterSpace){
                    if(messageED.getText().toString().equals("")){
                        sendBTNPart.setVisibility(View.GONE);
                    } else {
                        sendBTNPart.setVisibility(View.VISIBLE);
                    }
                } else {
                    sendBTNPart.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(canEnterSpace){
                    if(messageED.getText().toString().equals("")){
                        sendBTNPart.setVisibility(View.GONE);
                    } else {
                        sendBTNPart.setVisibility(View.VISIBLE);
                    }
                } else {
                    sendBTNPart.setVisibility(View.GONE);
                }
            }

        });

        sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canEnterSpace){
                    sendBTN.setVisibility(View.GONE);
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    sendChat();
                }
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        moreBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), moreBTN);
                dropDownMenu.getMenuInflater().inflate(R.menu.menu_option, dropDownMenu.getMenu());
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        String shortName;
                        String[] shortNameArray = partnerNameString.split(" ");

                        if(shortNameArray.length>1){
                            if(shortNameArray[0].length()<3){
                                shortName = shortNameArray[1];
                            } else {
                                shortName = shortNameArray[0];
                            }
                        } else {
                            shortName = shortNameArray[0];
                        }

                        // String shortName = partnerNameString;
                        // if(shortName.contains(" ")){
                        //    shortName = shortName.substring(0, shortName.indexOf(" "));
                        // }

                        if (id == R.id.dropdown_end_chat) {
                            if(!partnerName.getText().toString().equals("")){
                                new KAlertDialog(PersonalChatActivity.this, KAlertDialog.WARNING_TYPE)
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

                                                pDialog = new KAlertDialog(PersonalChatActivity.this, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                                pDialog.show();
                                                pDialog.setCancelable(false);
                                                new CountDownTimer(1300, 800) {
                                                    public void onTick(long millisUntilFinished) {
                                                        i++;
                                                        switch (i) {
                                                            case 0:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (PersonalChatActivity.this, R.color.colorGradien));
                                                                break;
                                                            case 1:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (PersonalChatActivity.this, R.color.colorGradien2));
                                                                break;
                                                            case 2:
                                                            case 6:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (PersonalChatActivity.this, R.color.colorGradien3));
                                                                break;
                                                            case 3:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (PersonalChatActivity.this, R.color.colorGradien4));
                                                                break;
                                                            case 4:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (PersonalChatActivity.this, R.color.colorGradien5));
                                                                break;
                                                            case 5:
                                                                pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                                        (PersonalChatActivity.this, R.color.colorGradien6));
                                                                break;
                                                        }
                                                    }
                                                    public void onFinish() {
                                                        i = -1;
                                                        repeat = "false";
                                                        endChatFunction();
                                                    }
                                                }.start();

                                            }
                                        })
                                        .show();
                            }
                            return true;
                        } else if(id == R.id.dropdown_contact_it){
                            getContactIT();
                        }
                        return true;
                    }
                });
                dropDownMenu.show();
            }
        });

        getChatMate();
        getPersonalChat();

    }

    private void getContactIT() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://geloraaksara.co.id/absen-online/api/get_contact_it";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            String bagian = response.getString("bagian");
                            String nama = response.getString("nama");
                            String whatsapp = response.getString("whatsapp");

                            Intent webIntent = new Intent(Intent.ACTION_VIEW); webIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=+"+whatsapp+"&text="));
                            try {
                                startActivity(webIntent);
                            } catch (SecurityException e) {
                                e.printStackTrace();
                                new KAlertDialog(PersonalChatActivity.this, KAlertDialog.WARNING_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Tidak dapat terhubung ke Whatsapp, anda bisa hubungi secara langsung ke 0"+whatsapp.substring(2, whatsapp.length())+" atas nama Bapak "+nama+" bagian IT/EDP")
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
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                connectionFailed();
            }
        });

        requestQueue.add(request);

    }

    static private boolean isMaxScrollReached(RecyclerView recyclerView) {
        int maxScroll = recyclerView.computeVerticalScrollRange();
        int currentScroll = recyclerView.computeVerticalScrollOffset() + recyclerView.computeVerticalScrollExtent();
        return currentScroll >= maxScroll;
    }

    private void getPersonalChat() {
        if(String.valueOf(isMaxScrollReached(chatRV)).equals("true")){
            if(keyboard.equals("hide")){
                if(repeat.equals("true")){
                    getChat();
                }
            }
        } else {
            if(keyboard.equals("show") && state.equals("0")){
                if(repeat.equals("true")){
                    getChat();
                }
                state = "1";
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getPersonalChat();
            }
        }, 3000);
    }

    private void getChatMate() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_chat_mate";
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
                            if (status.equals("Success")) {
                                String partner = data.getString("nama");
                                String avatar = data.getString("avatar");
                                String jabatan = data.getString("jabatan");
                                String bagian = data.getString("bagian");
                                String departemen = data.getString("departemen");
                                partnerName.setText(partner);
                                partnerDetail.setText(jabatan+" | "+bagian+" | "+departemen);
                                partnerNameString = partner;

                                Picasso.get().load(avatar).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .resize(80, 80)
                                        .into(partnerPic);

                                partnerPic.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(PersonalChatActivity.this, ViewImageActivity.class);
                                        intent.putExtra("url", avatar);
                                        intent.putExtra("kode", "chat_mate");
                                        intent.putExtra("name_chat_mate", partner);
                                        startActivity(intent);
                                    }
                                });

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
                params.put("rekan", rekanChat);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void sendChat() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/send_chat";
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
                                getChat();
                                sendBTN.setVisibility(View.VISIBLE);
                                loadingProgressBar.setVisibility(View.GONE);
                                messageED.setText("");
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
                params.put("rekan", rekanChat);
                params.put("timestamp", getTime());
                params.put("message", StringEscapeUtils.escapeJava(messageED.getText().toString()));
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(postRequest);

    }

    private void getChat() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/get_personal_chat";
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
                                removeChatLast = "";
                                chatRV.setVisibility(View.VISIBLE);
                                String data_chat = data.getString("data");
                                GsonBuilder builder = new GsonBuilder();
                                Gson gson = builder.create();
                                dataChat = gson.fromJson(data_chat, ChatModel[].class);
                                adapterChat = new AdapterChat(dataChat, PersonalChatActivity.this);
                                chatRV.setAdapter(adapterChat);
                            } else {
                                if(removeChatLast.equals("on")){
                                    removeChatLast = "";
                                    onBackPressed();
                                } else {
                                    chatRV.setVisibility(View.GONE);
                                }
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
                params.put("rekan", rekanChat);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    public BroadcastReceiver removeChat = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            removeChatLast = "on";
            String id_chat = intent.getStringExtra("id_chat");
            removeChatFunction(id_chat);
        }
    };

    private void removeChatFunction(String id_chat) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/remove_chat";
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
                                getChat();
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
                params.put("nik", sharedPrefManager.getSpNik());
                params.put("id_chat", id_chat);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void endChatFunction() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://geloraaksara.co.id/absen-online/api/end_chat";
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
                                                onBackPressed();
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
                params.put("rekan", rekanChat);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private String getTime() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void connectionFailed(){
        CookieBar.build(PersonalChatActivity.this)
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
    public void onBackPressed() {
        repeat = "false";
        try{
            super.onBackPressed();
        } catch (NullPointerException nullPointerException){
            System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
        }
    }

}