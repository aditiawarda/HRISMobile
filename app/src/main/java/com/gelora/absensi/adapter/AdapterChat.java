package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.MonitoringAbsensiBagianActivity;
import com.gelora.absensi.PersonalChatActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.Bagian;
import com.gelora.absensi.model.ChatModel;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyViewHolder> {

    private ChatModel[] data;
    private Context mContext;
    SharedPrefManager sharedPrefManager;
    KAlertDialog pDialog;
    int h = -1;

    public AdapterChat(ChatModel[] data, PersonalChatActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ChatModel chat = data[i];

        if(chat.getFirst_chat().equals("1")){
            myViewHolder.dateChatPart.setVisibility(View.VISIBLE);

            if (chat.getTimestamp().substring(0,7).equals(getDate().substring(0,7))){
                int selisih_hari = Integer.parseInt(getDate().substring(8,10))-Integer.parseInt(chat.getTimestamp().substring(8,10));
                if(selisih_hari==0){
                    myViewHolder.dateChat.setText("Hari ini");
                } else if (selisih_hari==1) {
                    myViewHolder.dateChat.setText("Kemarin");
                } else {
                    myViewHolder.dateChat.setText(chat.getTimestamp().substring(8,10)+"/"+chat.getTimestamp().substring(5,7)+"/"+chat.getTimestamp().substring(2,4));
                }
            } else {
                myViewHolder.dateChat.setText(chat.getTimestamp().substring(8,10)+"/"+chat.getTimestamp().substring(5,7)+"/"+chat.getTimestamp().substring(2,4));
            }

        } else {
            myViewHolder.dateChatPart.setVisibility(View.GONE);
        }

        if (chat.getReceiver().equals(sharedPrefManager.getSpNik())) {
            //Pesan diterima
            myViewHolder.recieverPart.setVisibility(View.VISIBLE);
            myViewHolder.senderPart.setVisibility(View.GONE);

            myViewHolder.textReciever.setText(StringEscapeUtils.unescapeJava(String.valueOf(chat.getMessage())));
            myViewHolder.timeReciever.setText(chat.getTimestamp().substring(11,16));

        } else {
            //Pesan dikirim
            myViewHolder.senderPart.setVisibility(View.VISIBLE);
            myViewHolder.recieverPart.setVisibility(View.GONE);

            myViewHolder.textSender.setText(StringEscapeUtils.unescapeJava(String.valueOf(chat.getMessage())));
            myViewHolder.timeSender.setText(chat.getTimestamp().substring(11,16));

            if(chat.getRead_status().equals("1")){
                myViewHolder.sendMark.setVisibility(View.GONE);
                myViewHolder.readMark.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.sendMark.setVisibility(View.VISIBLE);
                myViewHolder.readMark.setVisibility(View.GONE);
            }

            myViewHolder.senderPart.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new KAlertDialog(mContext, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Hapus Pesan?")
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

                                    pDialog = new KAlertDialog(mContext, KAlertDialog.PROGRESS_TYPE).setTitleText("Loading");
                                    pDialog.show();
                                    pDialog.setCancelable(false);
                                    new CountDownTimer(1000, 800) {
                                        public void onTick(long millisUntilFinished) {
                                            h++;
                                            switch (h) {
                                                case 0:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (mContext, R.color.colorGradien));
                                                    break;
                                                case 1:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (mContext, R.color.colorGradien2));
                                                    break;
                                                case 2:
                                                case 6:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (mContext, R.color.colorGradien3));
                                                    break;
                                                case 3:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (mContext, R.color.colorGradien4));
                                                    break;
                                                case 4:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (mContext, R.color.colorGradien5));
                                                    break;
                                                case 5:
                                                    pDialog.getProgressHelper().setBarColor(ContextCompat.getColor
                                                            (mContext, R.color.colorGradien6));
                                                    break;
                                            }
                                        }
                                        public void onFinish() {
                                            h = -1;
                                            pDialog.dismiss();
                                            Intent intent = new Intent("remove_chat");
                                            intent.putExtra("id_chat",String.valueOf(chat.getId()));
                                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                        }
                                    }.start();

                                }
                            })
                            .show();

                    return false;
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout senderPart, recieverPart, dateChatPart, sendMark, readMark;
        TextView textSender, textReciever, timeSender, timeReciever, dateChat;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            senderPart = itemView.findViewById(R.id.sender_part);
            recieverPart = itemView.findViewById(R.id.reciever_part);
            textSender = itemView.findViewById(R.id.text_sender);
            textReciever = itemView.findViewById(R.id.text_reciver);
            timeSender = itemView.findViewById(R.id.time_sender);
            timeReciever = itemView.findViewById(R.id.time_reciever);
            dateChatPart = itemView.findViewById(R.id.date_chat_part);
            dateChat = itemView.findViewById(R.id.date_chat);
            sendMark = itemView.findViewById(R.id.send_mark);
            readMark = itemView.findViewById(R.id.read_mark);
        }
    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
