package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DataFormSdmActivity;
import com.gelora.absensi.DataPenilaianSdmActivity;
import com.gelora.absensi.DetailFormSdmActivity;
import com.gelora.absensi.DetailPenilaianKaryawanActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataFormSDM;
import com.gelora.absensi.model.DataPenilaianSDM;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListDataFormSDM extends RecyclerView.Adapter<AdapterListDataFormSDM.MyViewHolder> {

    private DataFormSDM[] data;
    private Context mContext;
    SharedPrefManager sharedPrefManager;

    public AdapterListDataFormSDM(DataFormSDM[] data, DataFormSdmActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefManager = new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_data_form_sdm,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataFormSDM dataFormSDM = data[i];

        if(dataFormSDM.getKeterangan().equals("1")){
            myViewHolder.keteranganTV.setText("PENERIMAAN");
            myViewHolder.keteranganTV.setTextColor(Color.parseColor("#438cd6"));
            myViewHolder.point1.setVisibility(View.VISIBLE);
        } else if(dataFormSDM.getKeterangan().equals("2")){
            myViewHolder.keteranganTV.setText("PENGANGKATAN");
            myViewHolder.keteranganTV.setTextColor(Color.parseColor("#2bc3d4"));
            myViewHolder.point2.setVisibility(View.VISIBLE);
        } else if(dataFormSDM.getKeterangan().equals("3")){
            myViewHolder.keteranganTV.setText("PENUGASAN KEMBALI");
            myViewHolder.keteranganTV.setTextColor(Color.parseColor("#BAA2F5"));
            myViewHolder.point3.setVisibility(View.VISIBLE);
        } else if(dataFormSDM.getKeterangan().equals("4")){
            myViewHolder.keteranganTV.setText("PENSIUN/PHK");
            myViewHolder.keteranganTV.setTextColor(Color.parseColor("#F199B8"));
            myViewHolder.point4.setVisibility(View.VISIBLE);
        } else if(dataFormSDM.getKeterangan().equals("5")){
            myViewHolder.keteranganTV.setText("PROMOSI/MUTASI");
            myViewHolder.keteranganTV.setTextColor(Color.parseColor("#ECB994"));
            myViewHolder.point5.setVisibility(View.VISIBLE);
        } else if(dataFormSDM.getKeterangan().equals("6")){
            myViewHolder.keteranganTV.setText("PENYESUAIAN GAJI");
            myViewHolder.keteranganTV.setTextColor(Color.parseColor("#8AD8AA"));
            myViewHolder.point6.setVisibility(View.VISIBLE);
        } else if(dataFormSDM.getKeterangan().equals("7")){
            myViewHolder.keteranganTV.setText("LAIN-LAIN");
            myViewHolder.keteranganTV.setTextColor(Color.parseColor("#FF8B8B"));
            myViewHolder.point7.setVisibility(View.VISIBLE);
        }

        if(dataFormSDM.getStatus_approve_kabag().equals("0")){
            myViewHolder.detailTV.setText("Menunggu verifikasi Kepala Bagian");
        } else if(dataFormSDM.getStatus_approve_kabag().equals("1")){
            if(dataFormSDM.getStatus_approve_kadept().equals("0")){
                myViewHolder.detailTV.setText("Menunggu verifikasi Kepala Departemen");
            } else if(dataFormSDM.getStatus_approve_kadept().equals("1")){
                if(dataFormSDM.getStatus_approve_direktur().equals("0")){
                    myViewHolder.detailTV.setText("Menunggu verifikasi Direksi");
                } else if(dataFormSDM.getStatus_approve_direktur().equals("1")){
                    if(dataFormSDM.getStatus_approve_hrd().equals("0")){
                        myViewHolder.detailTV.setText("Menunggu verifikasi HRD");
                    } else if(dataFormSDM.getStatus_approve_hrd().equals("1")){
                        myViewHolder.detailTV.setText("Pengajuan telah diverifikasi HRD");
                    } else if(dataFormSDM.getStatus_approve_hrd().equals("2")){
                        myViewHolder.detailTV.setText("Pengajuan ditolak HRD");
                    }
                } else if(dataFormSDM.getStatus_approve_hrd().equals("2")){
                    myViewHolder.detailTV.setText("Pengajuan ditolak Direksi");
                }
            } else if(dataFormSDM.getStatus_approve_hrd().equals("2")){
                myViewHolder.detailTV.setText("Pengajuan ditolak Kepala Departemen");
            }
        } else if(dataFormSDM.getStatus_approve_hrd().equals("2")){
            myViewHolder.detailTV.setText("Pengajuan ditolak Kepala Bagian");
        }

        String tanggal_buat = dataFormSDM.getCreated_at();
        String dayDate = tanggal_buat.substring(8,10);
        String yearDate = tanggal_buat.substring(0,4);
        String bulanValue = tanggal_buat.substring(5,7);
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
                bulanName = "Not found!";
                break;
        }

        myViewHolder.timestampTV.setText("Dibuat pada "+dayDate+" "+bulanName+" "+yearDate);

        myViewHolder.parrentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailFormSdmActivity.class);
                intent.putExtra("id_data",dataFormSDM.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parrentPart, point1, point2, point3, point4, point5, point6, point7;
        TextView keteranganTV, timestampTV, detailTV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parrentPart = itemView.findViewById(R.id.parent_part);
            keteranganTV = itemView.findViewById(R.id.keterangan_data_tv);
            point1 = itemView.findViewById(R.id.point_1);
            point2 = itemView.findViewById(R.id.point_2);
            point3 = itemView.findViewById(R.id.point_3);
            point4 = itemView.findViewById(R.id.point_4);
            point5 = itemView.findViewById(R.id.point_5);
            point6 = itemView.findViewById(R.id.point_6);
            point7 = itemView.findViewById(R.id.point_7);
            detailTV = itemView.findViewById(R.id.detail_tv);
            timestampTV = itemView.findViewById(R.id.timestamp_data_tv);
        }
    }
}