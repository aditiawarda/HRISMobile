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
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailProjectActivity;
import com.gelora.absensi.ProjectViewActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.ProjectData;
import com.gelora.absensi.model.TaskData;

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

        myViewHolder.picTV.setText(taskData.getPic());
        myViewHolder.taskTV.setText(taskData.getTaskName());
        myViewHolder.progressPercent.setText(taskData.getProgressPercent());

        if (Integer.parseInt(taskData.getProgressPercent()) >= 0 && Integer.parseInt(taskData.getProgressPercent()) < 25) {
            myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_0_25));
        } else if (Integer.parseInt(taskData.getProgressPercent()) >= 25 && Integer.parseInt(taskData.getProgressPercent()) < 50) {
            myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_25_50));
        } else if (Integer.parseInt(taskData.getProgressPercent()) >= 50 && Integer.parseInt(taskData.getProgressPercent()) < 75) {
            myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_50_75));
        } else if (Integer.parseInt(taskData.getProgressPercent()) >= 75 && Integer.parseInt(taskData.getProgressPercent()) <= 100) {
            myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_75_100));
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Test", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parentPart, progressPart;
        TextView picTV, taskTV, progressPercent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            picTV = itemView.findViewById(R.id.pic_tv);
            taskTV = itemView.findViewById(R.id.task_tv);
            progressPercent = itemView.findViewById(R.id.progress_percent);
            progressPart = itemView.findViewById(R.id.progress_part);
        }
    }


}