package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.DetailPermohonanCutiActivity;
import com.gelora.absensi.DetailReportSumaActivity;
import com.gelora.absensi.PersonalNotificationActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataPersonalNotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdapterPersonalNotification extends RecyclerView.Adapter<AdapterPersonalNotification.MyViewHolder> {

    private DataPersonalNotification[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterPersonalNotification(DataPersonalNotification[] data, PersonalNotificationActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_personal_notifications,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataPersonalNotification dataPersonalNotification = data[i];

        if(dataPersonalNotification.getType().equals("1")){
            myViewHolder.typeNotifikasi.setText("CUTI");
            myViewHolder.statusNotifikasiTV.setText(dataPersonalNotification.getNotif_from_name()+" mencantumkan anda sebagai pengganti selama cuti");
            String input_date = dataPersonalNotification.getCreated_at().substring(0,10);
            String dayDate = input_date.substring(8,10);
            String yearDate = input_date.substring(0,4);
            String bulanValue = input_date.substring(5,7);
            String bulanName;

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date dt1= null;
            try {
                dt1 = format.parse(input_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            @SuppressLint("SimpleDateFormat")
            DateFormat format2 = new SimpleDateFormat("EEE");
            String finalDay = format2.format(dt1);
            String hariName = "";

            if (finalDay.equals("Mon") || finalDay.equals("Sen")) {
                hariName = "Senin";
            } else if (finalDay.equals("Tue") || finalDay.equals("Sel")) {
                hariName = "Selasa";
            } else if (finalDay.equals("Wed") || finalDay.equals("Rab")) {
                hariName = "Rabu";
            } else if (finalDay.equals("Thu") || finalDay.equals("Kam")) {
                hariName = "Kamis";
            } else if (finalDay.equals("Fri") || finalDay.equals("Jum")) {
                hariName = "Jumat";
            } else if (finalDay.equals("Sat") || finalDay.equals("Sab")) {
                hariName = "Sabtu";
            } else if (finalDay.equals("Sun") || finalDay.equals("Min")) {
                hariName = "Minggu";
            }

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

            String time = dataPersonalNotification.getCreated_at().substring(10,16);
            myViewHolder.tanggalNotifikasi.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate+" "+time);
            if (dataPersonalNotification.getStatus_read().equals("1")){
                myViewHolder.statusNotifikasiTV.setTextColor(Color.parseColor("#7d7d7d"));
                myViewHolder.statusNotifikasiTV.setTypeface(myViewHolder.statusNotifikasiTV.getTypeface(), Typeface.NORMAL);
                myViewHolder.tanggalNotifikasi.setTextColor(Color.parseColor("#7d7d7d"));
                myViewHolder.typeNotifikasi.setTextColor(Color.parseColor("#7d7d7d"));
                myViewHolder.lineLimit.setBackgroundColor(Color.parseColor("#EAEAEA"));
            } else {
                myViewHolder.statusNotifikasiTV.setTypeface(myViewHolder.statusNotifikasiTV.getTypeface(), Typeface.BOLD);
            }
            myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionRead(dataPersonalNotification.getId());
                    Intent intent = new Intent(mContext, DetailPermohonanCutiActivity.class);
                    intent.putExtra("kode", "delegation");
                    intent.putExtra("id_izin",dataPersonalNotification.getDirect_id());
                    mContext.startActivity(intent);
                }
            });
        } else if(dataPersonalNotification.getType().equals("2")){
            myViewHolder.typeNotifikasi.setText("LHS");
            if(dataPersonalNotification.getSubType().equals("1")){
                if(dataPersonalNotification.getData_owner().equals(sharedPrefManager.getSpNik())){
                    myViewHolder.statusNotifikasiTV.setText(dataPersonalNotification.getNotif_from_name()+" mengomentari rencana kunjungan anda");
                } else {
                    if(dataPersonalNotification.getData_owner().equals(dataPersonalNotification.getNotif_from())){
                        myViewHolder.statusNotifikasiTV.setText(dataPersonalNotification.getNotif_from_name()+" membalas komentar anda di data rencana kunjungannya");
                    } else {
                        myViewHolder.statusNotifikasiTV.setText(dataPersonalNotification.getNotif_from_name()+" mengomentari rencana kunjungan "+dataPersonalNotification.getNotif_from_name());
                    }
                }
            } else if(dataPersonalNotification.getSubType().equals("2")){
                if(dataPersonalNotification.getData_owner().equals(sharedPrefManager.getSpNik())){
                    myViewHolder.statusNotifikasiTV.setText(dataPersonalNotification.getNotif_from_name()+" mengomentari laporan kunjungan anda");
                } else {
                    if(dataPersonalNotification.getData_owner().equals(dataPersonalNotification.getNotif_from())){
                        myViewHolder.statusNotifikasiTV.setText(dataPersonalNotification.getNotif_from_name()+" membalas komentar laporan kunjungannya");
                    } else {
                        myViewHolder.statusNotifikasiTV.setText(dataPersonalNotification.getNotif_from_name()+" mengomentari laporan kunjungan "+dataPersonalNotification.getNotif_from_name());
                    }
                }
            }
            String input_date = dataPersonalNotification.getCreated_at().substring(0,10);
            String dayDate = input_date.substring(8,10);
            String yearDate = input_date.substring(0,4);
            String bulanValue = input_date.substring(5,7);
            String bulanName;

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date dt1= null;
            try {
                dt1 = format.parse(input_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            @SuppressLint("SimpleDateFormat")
            DateFormat format2 = new SimpleDateFormat("EEE");
            String finalDay = format2.format(dt1);
            String hariName = "";

            if (finalDay.equals("Mon") || finalDay.equals("Sen")) {
                hariName = "Senin";
            } else if (finalDay.equals("Tue") || finalDay.equals("Sel")) {
                hariName = "Selasa";
            } else if (finalDay.equals("Wed") || finalDay.equals("Rab")) {
                hariName = "Rabu";
            } else if (finalDay.equals("Thu") || finalDay.equals("Kam")) {
                hariName = "Kamis";
            } else if (finalDay.equals("Fri") || finalDay.equals("Jum")) {
                hariName = "Jumat";
            } else if (finalDay.equals("Sat") || finalDay.equals("Sab")) {
                hariName = "Sabtu";
            } else if (finalDay.equals("Sun") || finalDay.equals("Min")) {
                hariName = "Minggu";
            }

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

            String time = dataPersonalNotification.getCreated_at().substring(10,16);
            myViewHolder.tanggalNotifikasi.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate+" "+time);
            if (dataPersonalNotification.getStatus_read().equals("1")){
                myViewHolder.statusNotifikasiTV.setTextColor(Color.parseColor("#7d7d7d"));
                myViewHolder.statusNotifikasiTV.setTypeface(myViewHolder.statusNotifikasiTV.getTypeface(), Typeface.NORMAL);
                myViewHolder.tanggalNotifikasi.setTextColor(Color.parseColor("#7d7d7d"));
                myViewHolder.typeNotifikasi.setTextColor(Color.parseColor("#7d7d7d"));
                myViewHolder.lineLimit.setBackgroundColor(Color.parseColor("#EAEAEA"));
            } else {
                myViewHolder.statusNotifikasiTV.setTypeface(myViewHolder.statusNotifikasiTV.getTypeface(), Typeface.BOLD);
            }
            myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionRead(dataPersonalNotification.getId());
                    Intent intent = new Intent(mContext, DetailReportSumaActivity.class);
                    intent.putExtra("report_id",dataPersonalNotification.getDirect_id());
                    mContext.startActivity(intent);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView statusNotifikasiTV, tanggalNotifikasi, lineLimit, typeNotifikasi;
        LinearLayout parentPart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            statusNotifikasiTV = itemView.findViewById(R.id.status_notifikasi_tv);
            tanggalNotifikasi = itemView.findViewById(R.id.tanggal_notifikasi);
            typeNotifikasi = itemView.findViewById(R.id.type_notifikasi);
            lineLimit = itemView.findViewById(R.id.line_limit);
            parentPart = itemView.findViewById(R.id.parent_part);
        }
    }

    private void actionRead(String id) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        final String url = "https://hrisgelora.co.id/api/read_personal_notification";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");
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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

}