package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.FormInputTaskActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.UpdateTaskActivity;
import com.gelora.absensi.model.StatusTask;

public class AdapterStatusTaskUpdate extends RecyclerView.Adapter<AdapterStatusTaskUpdate.MyViewHolder> {

    private StatusTask[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;

    public AdapterStatusTaskUpdate(StatusTask[] data, UpdateTaskActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_task_status,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final StatusTask statusTask = data[i];

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            Typeface typeface = ResourcesCompat.getFont(mContext, R.font.roboto);
            myViewHolder.statusTV.setTypeface(typeface);
        }

        myViewHolder.statusTV.setText(statusTask.getStatusName());

        if (sharedPrefAbsen.getSpStatusTask().equals(statusTask.getStatusId())) {
            myViewHolder.markStatus.setVisibility(View.VISIBLE);
            myViewHolder.parentPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option_choice));
        } else {
            myViewHolder.markStatus.setVisibility(View.GONE);
            myViewHolder.parentPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_option));
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {

                notifyDataSetChanged();

                Intent intent = new Intent("task_status_broad_update");
                intent.putExtra("id_status", statusTask.getStatusId());
                intent.putExtra("nama_status", statusTask.getStatusName());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_STATUS_TASK, statusTask.getStatusId());

            }

        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView statusTV;
        LinearLayout parentPart, markStatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            statusTV = itemView.findViewById(R.id.status_tv);
            markStatus = itemView.findViewById(R.id.mark_status);
        }
    }

}
