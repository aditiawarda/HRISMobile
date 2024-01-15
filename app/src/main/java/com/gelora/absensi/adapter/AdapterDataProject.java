package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.NewsActivity;
import com.gelora.absensi.ProjectViewActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.model.NewsData;
import com.gelora.absensi.model.ProjectData;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdapterDataProject extends RecyclerView.Adapter<AdapterDataProject.MyViewHolder> {

    private ProjectData[] data;
    private Context mContext;
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    public AdapterDataProject(ProjectData[] data, ProjectViewActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        sharedPrefManager= new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_project,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final ProjectData projectData = data[i];

        myViewHolder.categoryName.setText(projectData.getCategoryName());
        myViewHolder.projectName.setText(projectData.getProjectName());
        myViewHolder.progressPercent.setText(projectData.getProgressPercent());

        if (Integer.parseInt(projectData.getProgressPercent()) >= 0 && Integer.parseInt(projectData.getProgressPercent()) < 25) {
            myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_0_25));
        } else if (Integer.parseInt(projectData.getProgressPercent()) >= 25 && Integer.parseInt(projectData.getProgressPercent()) < 50) {
            myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_25_50));
        } else if (Integer.parseInt(projectData.getProgressPercent()) >= 50 && Integer.parseInt(projectData.getProgressPercent()) < 75) {
            myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_50_75));
        } else if (Integer.parseInt(projectData.getProgressPercent()) >= 75 && Integer.parseInt(projectData.getProgressPercent()) <= 100) {
            myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_75_100));
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Tes", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parentPart, progressPart;
        TextView categoryName, projectName, progressPercent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            categoryName = itemView.findViewById(R.id.nama_kategori);
            projectName = itemView.findViewById(R.id.nama_project);
            progressPercent = itemView.findViewById(R.id.progress_tv);
            progressPart = itemView.findViewById(R.id.progress_part);
        }
    }


}