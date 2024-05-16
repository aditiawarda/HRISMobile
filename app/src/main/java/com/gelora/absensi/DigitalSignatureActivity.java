package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.kalert.KAlertDialog;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DigitalSignatureActivity extends AppCompatActivity {

    LinearLayout actionBar, redPen, bluePen, blackPen, cancelBTN, loadingPart, showSignaturePart, creatSignaturePart, changeBTN, backBTN, saveBTN, removeBTN;
    SignaturePad mSignaturePad;
    SharedPrefManager sharedPrefManager;
    Bitmap bitmapFixSize;
    private int i = -1;
    Uri destinationUri;
    KAlertDialog pDialog;
    ImageView signatureIMG;
    String kodeString = "";
    View rootview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_signature);

        sharedPrefManager = new SharedPrefManager(this);
        rootview = findViewById(android.R.id.content);
        backBTN = findViewById(R.id.back_btn);
        saveBTN = findViewById(R.id.save_btn);
        changeBTN = findViewById(R.id.change_btn);
        removeBTN = findViewById(R.id.remove_btn);
        mSignaturePad = findViewById(R.id.signature_pad);
        signatureIMG = findViewById(R.id.signature_img);
        showSignaturePart = findViewById(R.id.show_signature_part);
        creatSignaturePart = findViewById(R.id.creat_signature_part);
        loadingPart = findViewById(R.id.loading_part);
        cancelBTN = findViewById(R.id.cancel_btn);
        redPen = findViewById(R.id.pen_red);
        bluePen = findViewById(R.id.pen_blue);
        blackPen = findViewById(R.id.pen_black);
        actionBar = findViewById(R.id.action_bar);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        kodeString = getIntent().getExtras().getString("kode");

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        redPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redPen.setBackground(ContextCompat.getDrawable(DigitalSignatureActivity.this, R.drawable.shape_pen_red_choice));
                bluePen.setBackground(ContextCompat.getDrawable(DigitalSignatureActivity.this, R.drawable.shape_pen_blue));
                blackPen.setBackground(ContextCompat.getDrawable(DigitalSignatureActivity.this, R.drawable.shape_pen_black));
                mSignaturePad.setPenColor(Color.parseColor("#D32525"));
            }
        });

        bluePen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redPen.setBackground(ContextCompat.getDrawable(DigitalSignatureActivity.this, R.drawable.shape_pen_red));
                bluePen.setBackground(ContextCompat.getDrawable(DigitalSignatureActivity.this, R.drawable.shape_pen_blue_choice));
                blackPen.setBackground(ContextCompat.getDrawable(DigitalSignatureActivity.this, R.drawable.shape_pen_black));
                mSignaturePad.setPenColor(Color.parseColor("#0D2E80"));
            }
        });

        blackPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redPen.setBackground(ContextCompat.getDrawable(DigitalSignatureActivity.this, R.drawable.shape_pen_red));
                bluePen.setBackground(ContextCompat.getDrawable(DigitalSignatureActivity.this, R.drawable.shape_pen_blue));
                blackPen.setBackground(ContextCompat.getDrawable(DigitalSignatureActivity.this, R.drawable.shape_pen_black_choice));
                mSignaturePad.setPenColor(Color.parseColor("#000000"));
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(DigitalSignatureActivity.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Yakin untuk menyimpan tanda tangan?")
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
                                Bitmap bitmap = getBitmapFromView(mSignaturePad);
                                bitmapFixSize = Bitmap.createScaledBitmap(bitmap, 300, 300, false);

                                if(mSignaturePad.isEmpty()){
                                    new KAlertDialog(DigitalSignatureActivity.this, KAlertDialog.ERROR_TYPE)
                                            .setTitleText("Perhatian")
                                            .setContentText("Tanda tangan belum terisi!")
                                            .setConfirmText("    OK    ")
                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog sDialog) {
                                                    sDialog.dismiss();
                                                }
                                            })
                                            .show();
                                } else {
                                    uploadDigitalSignature();
                                }
                            }
                        })
                        .show();
            }
        });

        removeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
            }
        });

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
                creatSignaturePart.setVisibility(View.GONE);
                showSignaturePart.setVisibility(View.VISIBLE);
            }
        });

        changeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
                creatSignaturePart.setVisibility(View.VISIBLE);
                showSignaturePart.setVisibility(View.GONE);
            }
        });

        checkSignature();

    }

    private Bitmap getBitmapFromView(View view) {
        if(view.getWidth()==0){
            if(view.getHeight()==0){
                Bitmap returnedBitmap = Bitmap.createBitmap(300, 300,Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(returnedBitmap);
                Drawable bgDrawable = view.getBackground();
                if (bgDrawable!=null) {
                    bgDrawable.draw(canvas);
                } else {
                    canvas.drawColor(Color.WHITE);
                }
                view.draw(canvas);
                return returnedBitmap;
            } else {
                Bitmap returnedBitmap = Bitmap.createBitmap(view.getHeight(), view.getHeight(),Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(returnedBitmap);
                Drawable bgDrawable = view.getBackground();
                if (bgDrawable!=null) {
                    bgDrawable.draw(canvas);
                } else {
                    canvas.drawColor(Color.WHITE);
                }
                view.draw(canvas);
                return returnedBitmap;
            }
        } else {
            if(view.getHeight()==0){
                Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getWidth(),Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(returnedBitmap);
                Drawable bgDrawable = view.getBackground();
                if (bgDrawable!=null) {
                    bgDrawable.draw(canvas);
                } else {
                    canvas.drawColor(Color.WHITE);
                }
                view.draw(canvas);
                return returnedBitmap;
            } else {
                Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(returnedBitmap);
                Drawable bgDrawable = view.getBackground();
                if (bgDrawable!=null) {
                    bgDrawable.draw(canvas);
                } else {
                    canvas.drawColor(Color.WHITE);
                }
                view.draw(canvas);
                return returnedBitmap;
            }
        }

    }

    public void uploadDigitalSignature() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapFixSize.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        if (encodedImage == null) {
            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            pDialog = new KAlertDialog(DigitalSignatureActivity.this, KAlertDialog.PROGRESS_TYPE)
                    .setTitleText("Loading");
            pDialog.show();
            pDialog.setCancelable(false);
            new CountDownTimer(2000, 1000) {
                public void onTick(long millisUntilFinished) {
                    i++;
                    switch (i) {
                        case 0:
                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                    (DigitalSignatureActivity.this, R.color.colorGradien));
                            break;
                        case 1:
                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                    (DigitalSignatureActivity.this, R.color.colorGradien2));
                            break;
                        case 2:
                        case 6:
                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                    (DigitalSignatureActivity.this, R.color.colorGradien3));
                            break;
                        case 3:
                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                    (DigitalSignatureActivity.this, R.color.colorGradien4));
                            break;
                        case 4:
                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                    (DigitalSignatureActivity.this, R.color.colorGradien5));
                            break;
                        case 5:
                            pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                    (DigitalSignatureActivity.this, R.color.colorGradien6));
                            break;
                    }
                }
                public void onFinish() {
                    i = -1;
                    uploadSignature(encodedImage);
                }
            }.start();

        }

    }

    private void uploadSignature(String file){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/upload_digital_signature";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            if (status.equals("Success")){
                                checkSignature();
                                if (kodeString.equals("form")){
                                    pDialog.setTitleText("Berhasil Disimpan")
                                            .setContentText("Tanda tangan digital berhasil disimpan")
                                            .setConfirmText("    OK    ")
                                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                                @Override
                                                public void onClick(KAlertDialog sDialog) {
                                                    sDialog.dismiss();
                                                    onBackPressed();
                                                    finish();
                                                }
                                            })
                                            .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                } else {
                                    pDialog.setTitleText("Berhasil Disimpan")
                                            .setContentText("Tanda tangan digital berhasil disimpan")
                                            .setConfirmText("    OK    ")
                                            .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                }
                            } else {
                                pDialog.setTitleText("Gagal Disimpan")
                                        .setContentText("Tanda tangan digital gagal disimpan")
                                        .setConfirmText("    OK    ")
                                        .changeAlertType(KAlertDialog.ERROR_TYPE);
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
                params.put("NIK", sharedPrefManager.getSpNik());
                params.put("file", file);

                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void checkSignature(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/cek_ttd_digital";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            Log.d("Success.Response", response.toString());
                            JSONObject data = new JSONObject(response);
                            String status = data.getString("status");

                            loadingPart.setVisibility(View.GONE);

                            if (status.equals("Available")){
                                String signature = data.getString("data");
                                String url = "https://hrisgelora.co.id/upload/digital_signature/"+signature;

                                Picasso.get().load(url).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .into(signatureIMG);

                                showSignaturePart.setVisibility(View.VISIBLE);
                                creatSignaturePart.setVisibility(View.GONE);

                            } else {
                                cancelBTN.setVisibility(View.GONE);
                                showSignaturePart.setVisibility(View.GONE);
                                creatSignaturePart.setVisibility(View.VISIBLE);
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
                params.put("NIK", sharedPrefManager.getSpNik());

                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        // Banner.make(rootview, DigitalSignatureActivity.this, Banner.WARNING, "Koneksi anda terputus!", Banner.BOTTOM, 3000).show();

        CookieBar.build(DigitalSignatureActivity.this)
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