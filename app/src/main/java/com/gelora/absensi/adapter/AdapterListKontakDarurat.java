package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.DetailPengumumanActivity;
import com.gelora.absensi.FormKontakDaruratActivity;
import com.gelora.absensi.InfoKontakDaruratActivity;
import com.gelora.absensi.ListAllPengumumanActivity;
import com.gelora.absensi.LoginActivity;
import com.gelora.absensi.PersonalChatActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.DataKontakDarurat;
import com.gelora.absensi.model.DataPengumumanAll;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterListKontakDarurat extends RecyclerView.Adapter<AdapterListKontakDarurat.MyViewHolder> {

    private DataKontakDarurat[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterListKontakDarurat(DataKontakDarurat[] data, InfoKontakDaruratActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_kontak_darurat,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataKontakDarurat dataKontakDarurat = data[i];

        myViewHolder.namaKontak.setText(dataKontakDarurat.getNama_kontak());

        if(dataKontakDarurat.getHubungan().equals("Lainnya")){
            myViewHolder.hubunganKontak.setText(dataKontakDarurat.getHubungan_lainnya());
        } else {
            myViewHolder.hubunganKontak.setText(dataKontakDarurat.getHubungan());
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        myViewHolder.noKontak.setText(dataKontakDarurat.getNotelp());
        myViewHolder.kontakBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", dataKontakDarurat.getNotelp(), null));
                mContext.startActivity(intent);
            }
        });

        myViewHolder.menuBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myViewHolder.expandableLayout.isExpanded()){
                    myViewHolder.expandableLayout.collapse();
                } else {
                    myViewHolder.expandableLayout.expand();
                }
            }
        });

        myViewHolder.hapusBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("delete_kontak");
                intent.putExtra("id_kontak",String.valueOf(dataKontakDarurat.getId()));
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        });

        myViewHolder.editBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FormKontakDaruratActivity.class);
                intent.putExtra("tipe","edit");
                intent.putExtra("id_kontak",dataKontakDarurat.getId());
                mContext.startActivity(intent);
            }
        });

        if(dataKontakDarurat.getStatus_action().equals("1")){
            myViewHolder.menuBTN.setVisibility(View.VISIBLE);
        } else if(dataKontakDarurat.getStatus_action().equals("0")) {
            myViewHolder.menuBTN.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout kontakBTN, menuBTN, parentPart, editBTN, hapusBTN;
        TextView namaKontak, hubunganKontak, noKontak;
        ExpandableLayout expandableLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            parentPart = itemView.findViewById(R.id.parent_part);
            namaKontak = itemView.findViewById(R.id.nama_kontak);
            hubunganKontak = itemView.findViewById(R.id.hubungan);
            noKontak = itemView.findViewById(R.id.no_kontak);
            kontakBTN = itemView.findViewById(R.id.phone_btn);
            menuBTN = itemView.findViewById(R.id.menu_btn);
            editBTN = itemView.findViewById(R.id.edit_btn);
            hapusBTN = itemView.findViewById(R.id.hapus_btn);
        }
    }

}