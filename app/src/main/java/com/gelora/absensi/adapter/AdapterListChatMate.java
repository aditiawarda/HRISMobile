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

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailTidakHadirActivity;
import com.gelora.absensi.ListChatMateActivity;
import com.gelora.absensi.PersonalChatActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataIzin;
import com.gelora.absensi.model.ListChatMate;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListChatMate extends RecyclerView.Adapter<AdapterListChatMate.MyViewHolder> {

    private ListChatMate[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterListChatMate(ListChatMate[] data, ListChatMateActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager = new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_mate,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ListChatMate chatMate = data[i];

        Picasso.get().load(chatMate.getAvatar()).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(myViewHolder.avatarChatmate);

        myViewHolder.namaChatmate.setText(chatMate.getNama());
        myViewHolder.lastMessage.setText(chatMate.getMessage());

        if (chatMate.getTime().substring(0,7).equals(getDate().substring(0,7))){
            int selisih_hari = Integer.parseInt(getDate().substring(8,10))-Integer.parseInt(chatMate.getTime().substring(8,10));
            if(selisih_hari==0){
                myViewHolder.lastTimeChat.setText(chatMate.getTime().substring(11,16));
            } else if (selisih_hari==1) {
                myViewHolder.lastTimeChat.setText("Kemarin");
            } else {
                myViewHolder.lastTimeChat.setText(chatMate.getTime().substring(8,10)+"/"+chatMate.getTime().substring(5,7)+"/"+chatMate.getTime().substring(2,4));
            }
        } else {
            myViewHolder.lastTimeChat.setText(chatMate.getTime().substring(8,10)+"/"+chatMate.getTime().substring(5,7)+"/"+chatMate.getTime().substring(2,4));
        }

        if(chatMate.getLast_sender().equals(sharedPrefManager.getSpNik())){
            if(chatMate.getRead_status().equals("1")){
                myViewHolder.markRead.setVisibility(View.VISIBLE);
                myViewHolder.markSend.setVisibility(View.GONE);
            } else {
                myViewHolder.markRead.setVisibility(View.GONE);
                myViewHolder.markSend.setVisibility(View.VISIBLE);
            }
        } else {
            myViewHolder.markRead.setVisibility(View.GONE);
            myViewHolder.markSend.setVisibility(View.GONE);
        }

        if(chatMate.getBelum_dibaca().equals("0")){
            myViewHolder.lastMessage.setPadding(0,0,0,0);
            myViewHolder.countYetReadPart.setVisibility(View.GONE);
            myViewHolder.countYetRead.setText(chatMate.getBelum_dibaca());
            myViewHolder.lastTimeChat.setTextColor(Color.parseColor("#6D6D6D"));
        } else {
            float scale = mContext.getResources().getDisplayMetrics().density;
            int right = (int) (25*scale + 0.5f);
            myViewHolder.lastMessage.setPadding(0,0,right,0);

            myViewHolder.countYetReadPart.setVisibility(View.VISIBLE);
            myViewHolder.countYetRead.setText(chatMate.getBelum_dibaca());
            myViewHolder.lastTimeChat.setTextColor(Color.parseColor("#A6441F"));
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PersonalChatActivity.class);
                intent.putExtra("chat_mate",chatMate.getNik());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatarChatmate;
        TextView namaChatmate, lastMessage, lastTimeChat, countYetRead;
        LinearLayout countYetReadPart, parentPart, markSend, markRead;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            avatarChatmate = itemView.findViewById(R.id.avatar_chatmate);
            namaChatmate = itemView.findViewById(R.id.name_chatname);
            lastMessage = itemView.findViewById(R.id.last_chat);
            lastTimeChat = itemView.findViewById(R.id.time_last_chat);
            countYetRead = itemView.findViewById(R.id.count_yet_read);
            countYetReadPart = itemView.findViewById(R.id.count_yet_read_part);
            markSend = itemView.findViewById(R.id.send_mark);
            markRead = itemView.findViewById(R.id.read_mark);
        }
    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

}