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

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.R;
import com.gelora.absensi.ResumeKaryawanActivity;
import com.gelora.absensi.SearchSdmActivity;
import com.gelora.absensi.model.HumanResource;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListSDMSearch extends RecyclerView.Adapter<AdapterListSDMSearch.MyViewHolder> {

    private HumanResource[] data;
    private Context mContext;

    public AdapterListSDMSearch(HumanResource[] data, SearchSdmActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_karyawan_list,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final HumanResource humanResource = data[i];

        if(humanResource.getAvatar()!=null){
            Picasso.get().load("https://geloraaksara.co.id/absen-online/upload/avatar/"+humanResource.getAvatar()).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize(100, 100)
                    .into(myViewHolder.profileImage);
        }

        myViewHolder.namaTV.setText(humanResource.getNama().toUpperCase());
        myViewHolder.nikTV.setText(humanResource.getNIK());
        myViewHolder.jabatanTV.setText(humanResource.getJabatan());
        myViewHolder.detailTV.setText(humanResource.getBagian()+" | "+humanResource.getDepartemen());

        myViewHolder.parrentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentClose = new Intent("close_keyboard");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intentClose);

                Intent intent = new Intent(mContext, ResumeKaryawanActivity.class);
                intent.putExtra("NIK",humanResource.getNIK());
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
        TextView namaTV, nikTV, jabatanTV, detailTV;
        CircleImageView profileImage;
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch controlSwitch;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parrentPart = itemView.findViewById(R.id.parent_part);
            namaTV = itemView.findViewById(R.id.nama_tv);
            nikTV = itemView.findViewById(R.id.nik_tv);
            jabatanTV = itemView.findViewById(R.id.jabatan_tv);
            detailTV = itemView.findViewById(R.id.detail_tv);
            profileImage = itemView.findViewById(R.id.profile_image);
        }
    }

}
