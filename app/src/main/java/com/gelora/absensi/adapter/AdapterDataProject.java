package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailProjectActivity;
import com.gelora.absensi.ProjectViewActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.ProjectData;

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

        if(projectData.getPic().contains("-")){
            String[] namaPIC = projectData.getPic().split("-");
            myViewHolder.pimproTV.setText(namaPIC[1]);
        } else {
            myViewHolder.pimproTV.setText(projectData.getPic());
        }

        myViewHolder.progressPercent.setText(String.valueOf(Math.round(Float.parseFloat(projectData.getPersentaseProgress()))));

        int floatValue = Math.round(Float.parseFloat(projectData.getPersentaseProgress()));

        if (floatValue >= 0 && floatValue < 25) {
            myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_0_25));
        } else if (floatValue >= 25 && floatValue < 50) {
            myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_25_50));
        } else if (floatValue >= 50 && floatValue < 75) {
            myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_50_75));
        } else if (floatValue >= 75 && floatValue <= 100) {
            myViewHolder.progressPart.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_progress_75_100));
        }

        myViewHolder.parentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailProjectActivity.class);
                intent.putExtra("id_project",projectData.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parentPart, progressPart;
        TextView categoryName, projectName, progressPercent, pimproTV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parentPart = itemView.findViewById(R.id.parent_part);
            categoryName = itemView.findViewById(R.id.nama_kategori);
            projectName = itemView.findViewById(R.id.nama_project);
            progressPercent = itemView.findViewById(R.id.progress_tv);
            progressPart = itemView.findViewById(R.id.progress_part);
            pimproTV = itemView.findViewById(R.id.pimpro_tv);
        }
    }

}