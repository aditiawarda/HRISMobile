package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.ChatContactActivity;
import com.gelora.absensi.ListChatMateActivity;
import com.gelora.absensi.PersonalChatActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.ContactSearch;
import com.gelora.absensi.model.ListChatMate;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListContactSearch extends RecyclerView.Adapter<AdapterListContactSearch.MyViewHolder> {

    private ContactSearch[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterListContactSearch(ContactSearch[] data, ChatContactActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager = new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_contact,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ContactSearch contactSearch = data[i];

        Picasso.get().load(contactSearch.getAvatar()).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(myViewHolder.avatarContact);

        myViewHolder.namaContact.setText(contactSearch.getNama());
        myViewHolder.nikContact.setText(contactSearch.getNIK());
        myViewHolder.detailContact.setText(contactSearch.getJabatan()+" | "+contactSearch.getBagian()+" | "+contactSearch.getDepartemen());

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, PersonalChatActivity.class);
                intent.putExtra("chat_mate",String.valueOf(contactSearch.getNIK()));
                ((Activity)mContext).finish();
                mContext.startActivity(intent);

            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatarContact;
        TextView namaContact, nikContact, detailContact;
        LinearLayout parentPart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            avatarContact = itemView.findViewById(R.id.avatar_contact);
            namaContact = itemView.findViewById(R.id.name_contact);
            nikContact = itemView.findViewById(R.id.nik_contact);
            detailContact = itemView.findViewById(R.id.detail_contact);
        }
    }

}