package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailReportSumaActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.ReportSumaActivity;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.DataInvoicePiutang;
import com.gelora.absensi.model.ReportComment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterReportComment extends RecyclerView.Adapter<AdapterReportComment.MyViewHolder> {

    private ReportComment[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterReportComment(ReportComment[] data, DetailReportSumaActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_report_suma_comment,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ReportComment reportComment = data[i];

        if(reportComment.getFoto()!=null){
            Picasso.get().load("https://geloraaksara.co.id/absen-online/upload/avatar/"+reportComment.getFoto()).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize(80, 80)
                    .into(myViewHolder.profilePict);
        } else {
            Picasso.get().load("https://geloraaksara.co.id/absen-online/upload/avatar/default_profile.jpg").networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize(80, 80)
                    .into(myViewHolder.profilePict);
        }

        myViewHolder.namaJabatanTV.setText(reportComment.getNama()+" | "+reportComment.getJabatan()+" "+reportComment.getBagian());
        myViewHolder.komentarTV.setText(reportComment.getKomentar());
        myViewHolder.waktuKomentarTV.setText(reportComment.getCreatedAt());

        if (reportComment.getCreatedAt().substring(0,7).equals(getTimeStamp().substring(0,7))){
            int selisih_hari = Integer.parseInt(getTimeStamp().substring(8,10))-Integer.parseInt(reportComment.getCreatedAt().substring(8,10));
            if(selisih_hari==0){
                if(Integer.parseInt(reportComment.getCreatedAt().substring(11,13)) == Integer.parseInt(getTimeStamp().substring(11,13))){
                    if(Integer.parseInt(reportComment.getCreatedAt().substring(14,16)) == Integer.parseInt(getTimeStamp().substring(14,16))){
                        myViewHolder.waktuKomentarTV.setText("Baru saja");
                    } else {
                        int selisih_menit = Integer.parseInt(getTimeStamp().substring(14,16))-Integer.parseInt(reportComment.getCreatedAt().substring(14,16));
                        myViewHolder.waktuKomentarTV.setText(String.valueOf(selisih_menit)+" menit yang lalu");
                    }
                } else {
                    int selisih_jam = Integer.parseInt(getTimeStamp().substring(11,13))-Integer.parseInt(reportComment.getCreatedAt().substring(11,13));
                    if(selisih_jam <= 5){
                        myViewHolder.waktuKomentarTV.setText(String.valueOf(selisih_jam)+" jam yang lalu");
                    } else {
                        myViewHolder.waktuKomentarTV.setText("Hari ini, "+reportComment.getCreatedAt().substring(11,13)+":"+reportComment.getCreatedAt().substring(14,16));
                    }
                }
            } else if (selisih_hari==1) {
                myViewHolder.waktuKomentarTV.setText("Kemarin, "+reportComment.getCreatedAt().substring(11,13)+":"+reportComment.getCreatedAt().substring(14,16));
            } else {
                myViewHolder.waktuKomentarTV.setText(reportComment.getCreatedAt().substring(8,10)+"/"+reportComment.getCreatedAt().substring(5,7)+"/"+reportComment.getCreatedAt().substring(2,4)+" "+reportComment.getCreatedAt().substring(11,13)+":"+reportComment.getCreatedAt().substring(14,16));
            }
        } else {
            myViewHolder.waktuKomentarTV.setText(reportComment.getCreatedAt().substring(8,10)+"/"+reportComment.getCreatedAt().substring(5,7)+"/"+reportComment.getCreatedAt().substring(2,4)+" "+reportComment.getCreatedAt().substring(11,13)+":"+reportComment.getCreatedAt().substring(14,16));
        }

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView namaJabatanTV, komentarTV, waktuKomentarTV;
        CircleImageView profilePict;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            namaJabatanTV = itemView.findViewById(R.id.komentator_nama_jabatan);
            komentarTV = itemView.findViewById(R.id.komentator_komentar);
            waktuKomentarTV = itemView.findViewById(R.id.komentator_waktu);
            profilePict = itemView.findViewById(R.id.komentator_profile_pict);
        }
    }

    private String getTimeStamp() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
