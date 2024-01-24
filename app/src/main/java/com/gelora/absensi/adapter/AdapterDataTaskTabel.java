package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailProjectActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.TabelProjectViewActivity;
import com.gelora.absensi.model.TaskData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterDataTaskTabel extends RecyclerView.Adapter<AdapterDataTaskTabel.MyViewHolder> {

    private TaskData[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterDataTaskTabel(TaskData[] data, TabelProjectViewActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager = new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_task_tabel,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final TaskData taskData = data[i];

        myViewHolder.taskTV.setText(taskData.getTaskName());
        myViewHolder.startDateTV.setText(taskData.getTimeline().substring(3,5)+"/"+taskData.getTimeline().substring(0,2)+"/"+taskData.getTimeline().substring(6,10));
        myViewHolder.endDateTV.setText(taskData.getTimeline().substring(16,18)+"/"+taskData.getTimeline().substring(13,15)+"/"+taskData.getTimeline().substring(19,23));

        if(taskData.getStatus().equals("5")){
            myViewHolder.statusTV.setText("Done");
            myViewHolder.statusPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_75_100));
        } else if(taskData.getStatus().equals("4")){
            myViewHolder.statusTV.setText("On Hold");
            myViewHolder.statusPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_0_25));
        } else if(taskData.getStatus().equals("3")){
            myViewHolder.statusTV.setText("Waiting Acc");
            myViewHolder.statusPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_25_50));
        } else if(taskData.getStatus().equals("2")){
            myViewHolder.statusTV.setText("On Progress");
            myViewHolder.statusPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_50_75));
        } else if(taskData.getStatus().equals("1")){
            myViewHolder.statusTV.setText("Waiting");
            myViewHolder.statusPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_25_50));
        } else {
            myViewHolder.statusTV.setText("Undefined");
            myViewHolder.statusPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_0));
        }

        myViewHolder.progressTV.setText(taskData.getProgressDate()+"%");

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView taskTV, statusTV, progressTV, startDateTV, endDateTV;
        LinearLayout statusPart;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTV = itemView.findViewById(R.id.task_tv);
            statusTV = itemView.findViewById(R.id.status_tv);
            progressTV = itemView.findViewById(R.id.progress_tv);
            startDateTV = itemView.findViewById(R.id.start_date_tv);
            endDateTV = itemView.findViewById(R.id.end_date_tv);
            statusPart = itemView.findViewById(R.id.status_part);
        }
    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

}