package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.ExitClearanceActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.ListDataExitClearanceIn;
import com.gelora.absensi.model.ListDataExitClearanceOut;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterExitClearanceIn extends RecyclerView.Adapter<AdapterExitClearanceIn.MyViewHolder> {

    private ListDataExitClearanceIn[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterExitClearanceIn(ListDataExitClearanceIn[] data, ExitClearanceActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_data_masuk_exit_clearance,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ListDataExitClearanceIn listDataExitClearanceIn = data[i];

        Picasso.get().load(listDataExitClearanceIn.getAvatar()).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .resize(80, 80)
                .into(myViewHolder.profileImage);

        myViewHolder.namaKaryawanTV.setText(listDataExitClearanceIn.getNama().toUpperCase());
        myViewHolder.nikKaryawanTV.setText("NIK "+listDataExitClearanceIn.getNik());
        myViewHolder.deskrisiExitClearanceTV.setText("Serah Terima "+listDataExitClearanceIn.getSerah_terima()+" ");

        String input_date_keluar = listDataExitClearanceIn.getTgl_keluar();
        String dayDateKeluar = input_date_keluar.substring(8,10);
        String yearDateKeluar = input_date_keluar.substring(0,4);
        String bulanValueKeluar = input_date_keluar.substring(5,7);
        String bulanNameKeluar;

        switch (bulanValueKeluar) {
            case "01":
                bulanNameKeluar = "Jan";
                break;
            case "02":
                bulanNameKeluar = "Feb";
                break;
            case "03":
                bulanNameKeluar = "Mar";
                break;
            case "04":
                bulanNameKeluar = "Apr";
                break;
            case "05":
                bulanNameKeluar = "Mei";
                break;
            case "06":
                bulanNameKeluar = "Jun";
                break;
            case "07":
                bulanNameKeluar = "Jul";
                break;
            case "08":
                bulanNameKeluar = "Agu";
                break;
            case "09":
                bulanNameKeluar = "Sep";
                break;
            case "10":
                bulanNameKeluar = "Okt";
                break;
            case "11":
                bulanNameKeluar = "Nov";
                break;
            case "12":
                bulanNameKeluar = "Des";
                break;
            default:
                bulanNameKeluar = "Not found";
                break;
        }

        myViewHolder.tglKeluarTV.setText("Keluar : "+String.valueOf(Integer.parseInt(dayDateKeluar))+" "+bulanNameKeluar+" "+yearDateKeluar);

        myViewHolder.statusDataTV.setText(listDataExitClearanceIn.getStatus_data()+" ");
        if(listDataExitClearanceIn.getStatus_data().equals("Menunggu verifikasi")) {
            myViewHolder.statusDataTV.setTextColor(Color.parseColor("#C32424"));
        } else if(listDataExitClearanceIn.getStatus_data().equals("Sudah diverifikasi")){
            myViewHolder.statusDataTV.setTextColor(Color.parseColor("#089b02"));
            myViewHolder.namaKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
            myViewHolder.nikKaryawanTV.setTextColor(Color.parseColor("#7d7d7d"));
            myViewHolder.tglKeluarTV.setTextColor(Color.parseColor("#7d7d7d"));
            myViewHolder.deskrisiExitClearanceTV.setTextColor(Color.parseColor("#7d7d7d"));
            myViewHolder.tglBuatTV.setTextColor(Color.parseColor("#7d7d7d"));
        }

        String input_date = listDataExitClearanceIn.getTime_notifikasi().substring(0,10);
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

        String time = listDataExitClearanceIn.getTime_notifikasi().substring(10,16);

        myViewHolder.tglBuatTV.setText(hariName+", "+String.valueOf(Integer.parseInt(dayDate))+" "+bulanName+" "+yearDate+" "+time);

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, String.valueOf(listDataExitClearanceIn.getId_data()), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView namaKaryawanTV, nikKaryawanTV, deskrisiExitClearanceTV, statusDataTV, tglBuatTV, tglKeluarTV;
        LinearLayout parentPart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            namaKaryawanTV = itemView.findViewById(R.id.nama_karyawan_tv);
            nikKaryawanTV = itemView.findViewById(R.id.nik_karyawan_tv);
            deskrisiExitClearanceTV = itemView.findViewById(R.id.deskrisi_data_tv);
            statusDataTV = itemView.findViewById(R.id.status_data_tv);
            tglKeluarTV = itemView.findViewById(R.id.tgl_keluar_tv);
            tglBuatTV = itemView.findViewById(R.id.tanggal_kirim_permohonan);
            parentPart = itemView.findViewById(R.id.parent_part);
        }
    }

}