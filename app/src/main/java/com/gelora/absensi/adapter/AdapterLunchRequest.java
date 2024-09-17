package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.EditLunchRequestActivity;
import com.gelora.absensi.ListDataLunchRequestActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.model.DataListLunchRequest;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterLunchRequest extends RecyclerView.Adapter<AdapterLunchRequest.MyViewHolder> {

    private DataListLunchRequest[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterLunchRequest(DataListLunchRequest[] data, ListDataLunchRequestActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_data_lunch_request,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataListLunchRequest dataList = data[i];

        try {
            String dayDate = dataList.getTanggal().substring(8,10);
            String yearDate = dataList.getTanggal().substring(0,4);
            String bulanValue = dataList.getTanggal().substring(5,7);
            String bulanName;

            switch (bulanValue) {
                case "01":
                    bulanName = "JAN";
                    break;
                case "02":
                    bulanName = "FEB";
                    break;
                case "03":
                    bulanName = "MAR";
                    break;
                case "04":
                    bulanName = "APR";
                    break;
                case "05":
                    bulanName = "MEI";
                    break;
                case "06":
                    bulanName = "JUN";
                    break;
                case "07":
                    bulanName = "JUL";
                    break;
                case "08":
                    bulanName = "AGU";
                    break;
                case "09":
                    bulanName = "SEP";
                    break;
                case "10":
                    bulanName = "OKT";
                    break;
                case "11":
                    bulanName = "NOV";
                    break;
                case "12":
                    bulanName = "DES";
                    break;
                default:
                    bulanName = "Not found";
                    break;
            }

            myViewHolder.tglTV.setText(dayDate+" "+bulanName+" "+yearDate);
            myViewHolder.bagianTV.setText(dataList.getBagian());
            myViewHolder.requesterTV.setText(dataList.getNama_requester());
            myViewHolder.createdAtTV.setText(dataList.getCreated_at());
            myViewHolder.jumlah1TV.setText(dataList.getPusat_siang_k());
            myViewHolder.jumlah2TV.setText(dataList.getPusat_siang_s());
            myViewHolder.jumlah3TV.setText(dataList.getPusat_sore());
            myViewHolder.jumlah4TV.setText(dataList.getPusat_malam());
            myViewHolder.jumlah5TV.setText(dataList.getGapprint_siang());
            myViewHolder.jumlah6TV.setText(dataList.getGapprint_sore());
            myViewHolder.jumlah7TV.setText(dataList.getGapprint_malam());

            myViewHolder.expandBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(myViewHolder.expandableLayout.isExpanded()){
                        myViewHolder.expandableLayout.collapse();
                    } else {
                        myViewHolder.expandableLayout.expand();
                    }
                }
            });

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            Date date2 = null;
            try {
                date = sdf.parse(dataList.getTanggal());
                date2 = sdf.parse(getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long pilih = date.getTime();
            long sekarang = date2.getTime();

            if(pilih<sekarang) {
                myViewHolder.updateBTN.setVisibility(View.GONE);
            } else if (pilih==sekarang){
                String jamString = getTimeNow();
                String batasString = sharedPrefAbsen.getSpLunchRequestTimeout();

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                try {
                    Date jam = format.parse(jamString);
                    Date batas = format.parse(batasString);

                    if (jam.before(batas)) {
                        myViewHolder.updateBTN.setVisibility(View.VISIBLE);
                        myViewHolder.updateBTN.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, EditLunchRequestActivity.class);
                                intent.putExtra("id", dataList.getId());
                                mContext.startActivity(intent);
                            }
                        });
                    } else {
                        myViewHolder.updateBTN.setVisibility(View.GONE);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else if (pilih>sekarang){
                myViewHolder.updateBTN.setVisibility(View.VISIBLE);
                myViewHolder.updateBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, EditLunchRequestActivity.class);
                        intent.putExtra("id", dataList.getId());
                        mContext.startActivity(intent);
                    }
                });
            }
        } catch (StringIndexOutOfBoundsException e){
            Log.e("Error",e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout expandBTN, updateBTN;
        ExpandableLayout expandableLayout;
        TextView tglTV, bagianTV, requesterTV, createdAtTV, jumlah1TV, jumlah2TV, jumlah3TV, jumlah4TV, jumlah5TV, jumlah6TV, jumlah7TV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            expandBTN = itemView.findViewById(R.id.expand_btn);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            tglTV = itemView.findViewById(R.id.tgl_tv);
            bagianTV = itemView.findViewById(R.id.bagian_tv);
            requesterTV = itemView.findViewById(R.id.requester_tv);
            createdAtTV = itemView.findViewById(R.id.created_at_tv);
            jumlah1TV = itemView.findViewById(R.id.jumlah_1_tv);
            jumlah2TV = itemView.findViewById(R.id.jumlah_2_tv);
            jumlah3TV = itemView.findViewById(R.id.jumlah_3_tv);
            jumlah4TV = itemView.findViewById(R.id.jumlah_4_tv);
            jumlah5TV = itemView.findViewById(R.id.jumlah_5_tv);
            jumlah6TV = itemView.findViewById(R.id.jumlah_6_tv);
            jumlah7TV = itemView.findViewById(R.id.jumlah_7_tv);
            updateBTN = itemView.findViewById(R.id.update_btn);
        }
    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getTimeNow() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}