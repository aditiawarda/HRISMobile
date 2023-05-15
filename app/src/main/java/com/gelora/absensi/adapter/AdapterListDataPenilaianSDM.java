package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DataPenilaianSdmActivity;
import com.gelora.absensi.DetailPenilaianKaryawanActivity;
import com.gelora.absensi.FormPenilaianKaryawanActivity;
import com.gelora.absensi.HumanResourceActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.ResumeKaryawanActivity;
import com.gelora.absensi.model.DataPenilaianSDM;
import com.gelora.absensi.model.HumanResource;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListDataPenilaianSDM extends RecyclerView.Adapter<AdapterListDataPenilaianSDM.MyViewHolder> {

    private DataPenilaianSDM[] data;
    private Context mContext;

    public AdapterListDataPenilaianSDM(DataPenilaianSDM[] data, DataPenilaianSdmActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_karyawan_list_penilaian,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataPenilaianSDM dataPenilaianSDM = data[i];

        if(dataPenilaianSDM.getAvatar()!=null && !dataPenilaianSDM.getAvatar().equals("null")){
            Picasso.get().load("https://geloraaksara.co.id/absen-online/upload/avatar/"+dataPenilaianSDM.getAvatar()).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize(100, 100)
                    .into(myViewHolder.profileImage);
        }

        myViewHolder.namaTV.setText(dataPenilaianSDM.getNama().toUpperCase());
        myViewHolder.nikTV.setText(dataPenilaianSDM.getNIK());
        myViewHolder.detailTV.setText(dataPenilaianSDM.getJabatan()+" | "+dataPenilaianSDM.getBagian()+" | "+dataPenilaianSDM.getDepartemen());
        myViewHolder.predikatTV.setText(dataPenilaianSDM.getPredikat());

        String tanggal_buat = dataPenilaianSDM.getTanggal_buat();
        String dayDate = tanggal_buat.substring(8,10);
        String yearDate = tanggal_buat.substring(0,4);;
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

        myViewHolder.tanggalBuatTV.setText("Dinilai pada "+dayDate+" "+bulanName+" "+yearDate);

        myViewHolder.parrentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailPenilaianKaryawanActivity.class);
                intent.putExtra("id_penilaian",dataPenilaianSDM.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parrentPart;
        TextView namaTV, nikTV, detailTV, predikatTV, tanggalBuatTV;
        CircleImageView profileImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parrentPart = itemView.findViewById(R.id.parent_part);
            namaTV = itemView.findViewById(R.id.nama_tv);
            nikTV = itemView.findViewById(R.id.nik_tv);
            detailTV = itemView.findViewById(R.id.detail_tv);
            predikatTV = itemView.findViewById(R.id.predikat);
            profileImage = itemView.findViewById(R.id.profile_image);
            tanggalBuatTV = itemView.findViewById(R.id.tanggal_buat_tv);
        }
    }
}