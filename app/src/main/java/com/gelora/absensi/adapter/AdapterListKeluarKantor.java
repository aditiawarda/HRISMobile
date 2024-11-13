package com.gelora.absensi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DetailIzinKeluar;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.databinding.AdapterListKeluarKantorBinding;
import com.gelora.absensi.model.KaryawanKeluar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AdapterListKeluarKantor extends RecyclerView.Adapter<AdapterListKeluarKantor.MyViewHolder> {
    private Context context;

    private AdapterListKeluarKantorBinding _binding = null;
    private AdapterListKeluarKantorBinding getBinding() {
        return _binding;
    }
    SharedPrefAbsen sharedPrefAbsen;
    SharedPrefManager sharedPrefManager;

    private List<KaryawanKeluar> itemList;

    public void getData(Context context, List<KaryawanKeluar> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        _binding = AdapterListKeluarKantorBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        sharedPrefAbsen = new SharedPrefAbsen(context);
        sharedPrefManager = new SharedPrefManager(context);
        View view = getBinding().getRoot();

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        KaryawanKeluar item = itemList.get(position);
        getBinding().tvNama.setText(item.getNamaKaryawan());
        getBinding().tvNik.setText(item.getNik());

        if(item.getTanggal().equals(getDate())){
            if (Objects.equals(item.getStatusApproval(), "1") && Objects.equals(item.getStatusApprovalSatpam(), "1")){
                getBinding().detailStatus.setText("Accepted");
                getBinding().imageRequest.setImageResource(R.drawable.ic_request_green);
                if (!item.getVerifikatorKembali().equals("1")){
                    getBinding().cardDetailProses.setText("Disetujui & belum kembali");
                    getBinding().cardDetailProses.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heavyRed));
                } else {
                    getBinding().cardDetailProses.setText("Sudah kembali");
                    getBinding().cardDetailProses.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heavyGreen));
                }
                getBinding().detailStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heavyGreen));
            }
            else if (Objects.equals(item.getStatusApproval(), "2") || Objects.equals(item.getStatusApproval(), "1") && Objects.equals(item.getStatusApprovalSatpam(), "2")){
                getBinding().detailStatus.setText("Rejected");
                getBinding().imageRequest.setImageResource(R.drawable.ic_request_red);
                if(Objects.equals(item.getStatusApproval(), "2")){
                    getBinding().cardDetailProses.setText("Permohonan ditolak Atasan");
                } else if(Objects.equals(item.getStatusApprovalSatpam(), "2")){
                    getBinding().cardDetailProses.setText("Permohonan ditolak Satpam");
                }
                getBinding().cardDetailProses.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heavyRed));
                getBinding().detailStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heavyRed));
            }
            else if (Objects.equals(item.getStatusApproval(), "1") || Objects.equals(item.getStatusApproval(), "0") && Objects.equals(item.getStatusApprovalSatpam(), "0")){
                getBinding().detailStatus.setText("Pending");
                getBinding().imageRequest.setImageResource(R.drawable.ic_request_yellow);
                getBinding().detailStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.selected_yellow));
                if(Objects.equals(item.getStatusApproval(), "0")){
                    getBinding().cardDetailProses.setText("Menunggu persetujuan Atasan");
                    if ((sharedPrefManager.getSpIdJabatan().equals("41")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("90")||sharedPrefManager.getSpIdJabatan().equals("3")) || (sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25")) || (sharedPrefManager.getSpNik().equals("1280270910")||sharedPrefManager.getSpNik().equals("1090080310")||sharedPrefManager.getSpNik().equals("2840071116")||sharedPrefManager.getSpNik().equals("1332240111"))){
                        if(sharedPrefManager.getSpNik().equals(item.getNik())){
                            getBinding().batasStatus.setVisibility(View.GONE);
                            getBinding().waitingMark.setVisibility(View.GONE);
                        } else {
                            getBinding().batasStatus.setVisibility(View.VISIBLE);
                            getBinding().waitingMark.setVisibility(View.VISIBLE);
                        }
                    } else {
                        getBinding().batasStatus.setVisibility(View.GONE);
                        getBinding().waitingMark.setVisibility(View.GONE);
                    }
                } else if(Objects.equals(item.getStatusApproval(), "1")){
                    getBinding().cardDetailProses.setText("Menunggu persetujuan Satpam");
                    if (sharedPrefManager.getSpIdDept().equals("21")){
                        if(sharedPrefManager.getSpNik().equals(item.getNik())){
                            getBinding().batasStatus.setVisibility(View.GONE);
                            getBinding().waitingMark.setVisibility(View.GONE);
                        } else {
                            if(sharedPrefManager.getSpNik().equals("000112092023")){
                                getBinding().batasStatus.setVisibility(View.GONE);
                                getBinding().waitingMark.setVisibility(View.GONE);
                            } else {
                                getBinding().batasStatus.setVisibility(View.VISIBLE);
                                getBinding().waitingMark.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        getBinding().batasStatus.setVisibility(View.GONE);
                        getBinding().waitingMark.setVisibility(View.GONE);
                    }
                }
                getBinding().cardDetailProses.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.selected_yellow));
            }
            else {
                getBinding().detailStatus.setText("Pending");
                getBinding().detailStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.selected_yellow));
            }
        } else {
            getBinding().detailStatus.setText("Expired");
            getBinding().imageRequest.setImageResource(R.drawable.ic_request);
            getBinding().cardDetailProses.setText("Tidak berlaku");
            getBinding().cardDetailProses.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heavy_gray));
            getBinding().detailStatus.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.heavy_gray));
        }

        getBinding().card.setOnClickListener(view-> {
            Intent intent = new Intent(view.getContext(), DetailIzinKeluar.class);
            intent.putExtra("current_id",item.getId());
            intent.putExtra("nik_pemohon",item.getNik());
            view.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

}

