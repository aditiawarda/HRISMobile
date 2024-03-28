package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailProjectActivity;
import com.gelora.absensi.ProjectViewActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.UpdateTaskActivity;
import com.gelora.absensi.model.ProjectData;
import com.gelora.absensi.model.TaskData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterDataTask extends RecyclerView.Adapter<AdapterDataTask.MyViewHolder> {

    private TaskData[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterDataTask(TaskData[] data, DetailProjectActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager = new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_task,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final TaskData taskData = data[i];

        if(taskData.getPic().equals("") || taskData.getPic().equals(" ") || taskData.getPic().equals("null") || taskData.getPic().equals("-")){
            myViewHolder.picTV.setText("Tidak Diketahui");
        } else {
            myViewHolder.picTV.setText(taskData.getPic().substring(11, taskData.getPic().length()));
        }

        myViewHolder.taskTV.setText(taskData.getTaskname());

        if(taskData.getDate().equals("")){
            myViewHolder.targetDateTV.setText("00/00/0000");
        } else {
            char format1 = '-';
            char format2 = '/';

            if (isCharacterInString(taskData.getDate(), format1)) {
                String targetDate = taskData.getDate().substring(8,10)+"/"+taskData.getDate().substring(5,7)+"/"+taskData.getDate().substring(0,4);
                myViewHolder.targetDateTV.setText(targetDate);
            } else if (isCharacterInString(taskData.getDate(), format2)) {
                String[] tgl_target = taskData.getDate().split("/");
                String targetDate = tgl_target[1]+"/"+tgl_target[0]+"/"+tgl_target[2];
                myViewHolder.targetDateTV.setText(targetDate);
            }
        }

        if(taskData.getStatus().equals("5")){
            myViewHolder.statusTV.setText("Done");
        } else if(taskData.getStatus().equals("4")){
            myViewHolder.statusTV.setText("On Hold");
        } else if(taskData.getStatus().equals("3")){
            myViewHolder.statusTV.setText("Waiting Approval");
        } else if(taskData.getStatus().equals("2")){
            myViewHolder.statusTV.setText("On Progress");
        // } else if(taskData.getStatus().equals("1")){
        //    myViewHolder.statusTV.setText("Waiting");
        } else if(taskData.getStatus().equals("0")){
            myViewHolder.statusTV.setText("On Planning");
            // myViewHolder.statusTV.setText("To Do");
        } else {
            myViewHolder.statusTV.setText("Undefined");
        }

        if(taskData.getProgress().equals("")||taskData.getProgress().equals(" ")||taskData.getProgress().equals("null")||taskData.getProgress() == null){
            myViewHolder.progressPercent.setText("0");
            myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_0_25));
        } else {
            myViewHolder.progressPercent.setText(taskData.getProgress());
            if (Integer.parseInt(taskData.getProgress()) >= 0 && Integer.parseInt(taskData.getProgress()) < 25) {
                myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_0_25));
            } else if (Integer.parseInt(taskData.getProgress()) >= 25 && Integer.parseInt(taskData.getProgress()) < 50) {
                myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_25_50));
            } else if (Integer.parseInt(taskData.getProgress()) >= 50 && Integer.parseInt(taskData.getProgress()) < 75) {
                myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_50_75));
            } else if (Integer.parseInt(taskData.getProgress()) >= 75 && Integer.parseInt(taskData.getProgress()) <= 100) {
                myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_75_100));
            }
        }

        // ProgressBar Part

        myViewHolder.startDateTV.setText(taskData.getScheduleTimeline().substring(3,5)+"/"+taskData.getScheduleTimeline().substring(0,2)+"/"+taskData.getScheduleTimeline().substring(6,10));
        myViewHolder.endDateTV.setText(taskData.getScheduleTimeline().substring(16,18)+"/"+taskData.getScheduleTimeline().substring(13,15)+"/"+taskData.getScheduleTimeline().substring(19,23));

        String startDateString = taskData.getScheduleTimeline().substring(6,10)+"-"+taskData.getScheduleTimeline().substring(0,2)+"-"+taskData.getScheduleTimeline().substring(3,5);
        String endDateString = taskData.getScheduleTimeline().substring(19,23)+"-"+taskData.getScheduleTimeline().substring(13,15)+"-"+taskData.getScheduleTimeline().substring(16,18);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(endDateString);
            date2 = format.parse(startDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long waktu1 = date1.getTime();
        long waktu2 = date2.getTime();
        long selisih_waktu = waktu1 - waktu2;

        long totalDay = (selisih_waktu / (24 * 60 * 60 * 1000)) + 1;

        String startDateString2 = startDateString;
        String endDateString2 = getDate();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1_2 = null;
        Date date2_2 = null;
        try {
            date1_2 = format.parse(endDateString2);
            date2_2 = format.parse(startDateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long waktu1_2 = date1_2.getTime();
        long waktu2_2 = date2_2.getTime();
        long selisih_waktu_2 = waktu1_2 - waktu2_2;

        long progressDay = (selisih_waktu_2 / (24 * 60 * 60 * 1000)) + 1;

        String startDateString3 = endDateString;
        String endDateString3 = getDate();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1_3 = null;
        Date date2_3 = null;
        try {
            date1_3 = format.parse(endDateString3);
            date2_3 = format.parse(startDateString3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long waktu1_3 = date1_3.getTime();
        long waktu2_3 = date2_3.getTime();
        long selisih_waktu_3 = waktu1_3 - waktu2_3;

        long overDay = (selisih_waktu_3 / (24 * 60 * 60 * 1000)) + 1;

        if(overDay >= 1){
            progressDay = totalDay;
        }

        float persentase = ((float) progressDay / (float) totalDay) * 100;

        LinearLayout.LayoutParams layoutParamsProgress = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,0.0f);
        LinearLayout.LayoutParams layoutParamsLeft = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,0.10f);

        float weightProgress = (float) Math.round(persentase) / 100.0f;
        float weightLeft = (float) (100 - Math.round(persentase)) / 100.0f;

        layoutParamsProgress.weight = weightProgress;
        layoutParamsLeft.weight = weightLeft;

        myViewHolder.progressBar.setLayoutParams(layoutParamsProgress);
        myViewHolder.leftBar.setLayoutParams(layoutParamsLeft);

        // End ProgressBar Part

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                Intent intent = new Intent(mContext, UpdateTaskActivity.class);
                intent.putExtra("id_project",sharedPrefAbsen.getSpProjectOpen());
                intent.putExtra("taskname",taskData.getTaskname());
                intent.putExtra("pic",taskData.getPic());
                intent.putExtra("date",taskData.getDate());
                intent.putExtra("status",taskData.getStatus());
                intent.putExtra("scheduleTimeline",taskData.getScheduleTimeline());
                if(taskData.getStatus().equals("5")){
                    intent.putExtra("actualTimeline",taskData.getActualTimeline());
                }
                if(taskData.getProgress().equals("")||taskData.getProgress().equals(" ")||taskData.getProgress().equals("null")||taskData.getProgress() == null){
                    intent.putExtra("progress","0");
                } else {
                    intent.putExtra("progress",taskData.getProgress());
                }
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parentPart, progressPart, progressBar, leftBar;
        TextView picTV, taskTV, progressPercent, statusTV, targetDateTV, startDateTV, endDateTV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            picTV = itemView.findViewById(R.id.pic_tv);
            taskTV = itemView.findViewById(R.id.task_tv);
            progressPercent = itemView.findViewById(R.id.progress_percent);
            progressPart = itemView.findViewById(R.id.progress_part);
            statusTV = itemView.findViewById(R.id.status_tv);
            targetDateTV = itemView.findViewById(R.id.target_date_tv);
            progressBar = itemView.findViewById(R.id.progress_bar);
            leftBar = itemView.findViewById(R.id.left_bar);
            startDateTV = itemView.findViewById(R.id.start_date_tv);
            endDateTV = itemView.findViewById(R.id.end_date_tv);
        }
    }

    private boolean isCharacterInString(String string, char character) {
        return string.indexOf(character) != -1;
    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

}