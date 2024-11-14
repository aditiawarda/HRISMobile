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
import androidx.recyclerview.widget.RecyclerView;

import com.gelora.absensi.DataFormSdmActivity;
import com.gelora.absensi.DetailFormSdmActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.model.DataFormSDM;

public class AdapterListDataFormSDM extends RecyclerView.Adapter<AdapterListDataFormSDM.MyViewHolder> {

    private DataFormSDM[] data;
    private Context mContext;
    SharedPrefManager sharedPrefManager;

    public AdapterListDataFormSDM(DataFormSDM[] data, DataFormSdmActivity context) {
        this.data = data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        sharedPrefManager = new SharedPrefManager(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_data_form_sdm,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DataFormSDM dataFormSDM = data[i];

        if(dataFormSDM.getKeterangan().equals("1")){
            myViewHolder.keteranganTV.setText("PENERIMAAN");
            myViewHolder.keteranganTV.setTextColor(Color.parseColor("#438cd6"));
            myViewHolder.atasNamaTV.setTextColor(Color.parseColor("#438cd6"));
            myViewHolder.point1.setVisibility(View.VISIBLE);
            myViewHolder.bagianTV.setText(dataFormSDM.getBagian().toUpperCase());
        } else if(dataFormSDM.getKeterangan().equals("2")){
            myViewHolder.keteranganTV.setText("PENGANGKATAN");
            myViewHolder.keteranganTV.setTextColor(Color.parseColor("#239fae"));
            myViewHolder.atasNamaTV.setTextColor(Color.parseColor("#239fae"));
            myViewHolder.point2.setVisibility(View.VISIBLE);
            myViewHolder.bagianTV.setText(dataFormSDM.getBagian_lama().toUpperCase());
        } else if(dataFormSDM.getKeterangan().equals("3")){
            myViewHolder.keteranganTV.setText("PENUGASAN KEMBALI");
            myViewHolder.keteranganTV.setTextColor(Color.parseColor("#9570f0"));
            myViewHolder.atasNamaTV.setTextColor(Color.parseColor("#9570f0"));
            myViewHolder.point3.setVisibility(View.VISIBLE);
            myViewHolder.bagianTV.setText(dataFormSDM.getBagian_lama().toUpperCase());
        } else if(dataFormSDM.getKeterangan().equals("4")){
            if(String.valueOf(dataFormSDM.getSub_keterangan()).equals("1")){
                myViewHolder.keteranganTV.setText("PENSIUN");
            } else if(String.valueOf(dataFormSDM.getSub_keterangan()).equals("2")){
                myViewHolder.keteranganTV.setText("PHK");
            } else {
                myViewHolder.keteranganTV.setText("PENSIUN/PHK");
            }
            myViewHolder.keteranganTV.setTextColor(Color.parseColor("#e85588"));
            myViewHolder.atasNamaTV.setTextColor(Color.parseColor("#e85588"));
            myViewHolder.point4.setVisibility(View.VISIBLE);
            myViewHolder.bagianTV.setText(dataFormSDM.getBagian_lama().toUpperCase());
        } else if(dataFormSDM.getKeterangan().equals("5")){
            if(String.valueOf(dataFormSDM.getSub_keterangan()).equals("1")){
                myViewHolder.keteranganTV.setText("PROMOSI");
            } else if(String.valueOf(dataFormSDM.getSub_keterangan()).equals("2")){
                myViewHolder.keteranganTV.setText("MUTASI");
            } else {
                myViewHolder.keteranganTV.setText("PROMOSI/MUTASI");
            }
            myViewHolder.keteranganTV.setTextColor(Color.parseColor("#db7a33"));
            myViewHolder.atasNamaTV.setTextColor(Color.parseColor("#db7a33"));
            myViewHolder.point5.setVisibility(View.VISIBLE);
            myViewHolder.bagianTV.setText(dataFormSDM.getBagian_lama().toUpperCase());
        } else if(dataFormSDM.getKeterangan().equals("6")){
            myViewHolder.keteranganTV.setText("PENYESUAIAN GAJI");
            myViewHolder.keteranganTV.setTextColor(Color.parseColor("#37a464"));
            myViewHolder.atasNamaTV.setTextColor(Color.parseColor("#37a464"));
            myViewHolder.point6.setVisibility(View.VISIBLE);
            myViewHolder.bagianTV.setText(dataFormSDM.getBagian_lama().toUpperCase());
        } else if(dataFormSDM.getKeterangan().equals("7")){
            myViewHolder.keteranganTV.setText("LAIN-LAIN");
            myViewHolder.keteranganTV.setTextColor(Color.parseColor("#ff6666"));
            myViewHolder.atasNamaTV.setTextColor(Color.parseColor("#ff6666"));
            myViewHolder.point7.setVisibility(View.VISIBLE);
            myViewHolder.bagianTV.setText(dataFormSDM.getBagian_lama().toUpperCase());
        }

        if(dataFormSDM.getKeterangan().equals("1")){
            myViewHolder.atasNamaTV.setText(dataFormSDM.getPenerimaan_jabatan().toUpperCase());
        } else {
            myViewHolder.atasNamaTV.setText(dataFormSDM.getNama().toUpperCase());
        }

        if(dataFormSDM.getStatus_approve_kabag().equals("0")){
            myViewHolder.detailTV.setText("Menunggu verifikasi Atasan Bagian");
            myViewHolder.accMark.setVisibility(View.GONE);
            myViewHolder.rejMark.setVisibility(View.GONE);
            myViewHolder.proMark.setVisibility(View.VISIBLE);
            if(sharedPrefManager.getSpIdJabatan().equals("3")||sharedPrefManager.getSpIdJabatan().equals("11")||sharedPrefManager.getSpIdJabatan().equals("25")||sharedPrefManager.getSpNik().equals("3294031022")||sharedPrefManager.getSpNik().equals("0113010500")||sharedPrefManager.getSpNik().equals("0687260508")||sharedPrefManager.getSpNik().equals("0132020401")||sharedPrefManager.getSpNik().equals("0121010900")||sharedPrefManager.getSpNik().equals("0015141287") || (sharedPrefManager.getSpNik().equals("1280270910")||sharedPrefManager.getSpNik().equals("1090080310")||sharedPrefManager.getSpNik().equals("2840071116")||sharedPrefManager.getSpNik().equals("1332240111")||sharedPrefManager.getSpNik().equals("0057010793")||sharedPrefManager.getSpNik().equals("1504060711"))){
                myViewHolder.waitingMark.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.waitingMark.setVisibility(View.GONE);
            }
        } else if(dataFormSDM.getStatus_approve_kabag().equals("1")){
            if(sharedPrefManager.getSpNik().equals("0132020401")||sharedPrefManager.getSpNik().equals("0113010500")||sharedPrefManager.getSpNik().equals("0829030809")){
                if(dataFormSDM.getStatus_approve_astkadept().equals("0")){
                    myViewHolder.detailTV.setText("Menunggu verifikasi Ast.Ka.Dept");
                    myViewHolder.accMark.setVisibility(View.GONE);
                    myViewHolder.rejMark.setVisibility(View.GONE);
                    myViewHolder.proMark.setVisibility(View.VISIBLE);
                    myViewHolder.waitingMark.setVisibility(View.VISIBLE);
                } else if(dataFormSDM.getStatus_approve_astkadept().equals("1")){
                    if(dataFormSDM.getStatus_approve_kadept().equals("0")){
                        myViewHolder.detailTV.setText("Menunggu verifikasi Atasan Departemen");
                        myViewHolder.accMark.setVisibility(View.GONE);
                        myViewHolder.rejMark.setVisibility(View.GONE);
                        myViewHolder.proMark.setVisibility(View.VISIBLE);
                        if(sharedPrefManager.getSpIdJabatan().equals("41")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("90")){
                            myViewHolder.waitingMark.setVisibility(View.VISIBLE);
                        } else {
                            myViewHolder.waitingMark.setVisibility(View.GONE);
                        }
                    }
                    else if(dataFormSDM.getStatus_approve_kadept().equals("1")){
                        if(dataFormSDM.getKeterangan().equals("1")||dataFormSDM.getKeterangan().equals("2")){
                            if(dataFormSDM.getStatus_approve_direktur().equals("0")){
                                myViewHolder.detailTV.setText("Menunggu verifikasi Direksi");
                                myViewHolder.accMark.setVisibility(View.GONE);
                                myViewHolder.rejMark.setVisibility(View.GONE);
                                myViewHolder.proMark.setVisibility(View.VISIBLE);
                                if(sharedPrefManager.getSpIdJabatan().equals("8")){
                                    myViewHolder.waitingMark.setVisibility(View.VISIBLE);
                                } else {
                                    myViewHolder.waitingMark.setVisibility(View.GONE);
                                }
                            } else if(dataFormSDM.getStatus_approve_direktur().equals("1")){
                                if(dataFormSDM.getStatus_approve_hrd().equals("0")){
                                    myViewHolder.detailTV.setText("Menunggu verifikasi HRD");
                                    myViewHolder.accMark.setVisibility(View.GONE);
                                    myViewHolder.rejMark.setVisibility(View.GONE);
                                    myViewHolder.proMark.setVisibility(View.VISIBLE);
                                } else if(dataFormSDM.getStatus_approve_hrd().equals("1")){
                                    myViewHolder.detailTV.setText("Pengajuan telah diverifikasi HRD");
                                    myViewHolder.accMark.setVisibility(View.VISIBLE);
                                    myViewHolder.rejMark.setVisibility(View.GONE);
                                    myViewHolder.proMark.setVisibility(View.GONE);
                                } else if(dataFormSDM.getStatus_approve_hrd().equals("2")){
                                    myViewHolder.detailTV.setText("Pengajuan ditolak HRD");
                                    myViewHolder.accMark.setVisibility(View.GONE);
                                    myViewHolder.rejMark.setVisibility(View.VISIBLE);
                                    myViewHolder.proMark.setVisibility(View.GONE);
                                }
                            } else if(dataFormSDM.getStatus_approve_direktur().equals("2")){
                                myViewHolder.detailTV.setText("Pengajuan ditolak Direksi");
                                myViewHolder.accMark.setVisibility(View.GONE);
                                myViewHolder.rejMark.setVisibility(View.VISIBLE);
                                myViewHolder.proMark.setVisibility(View.GONE);
                            }
                        } else {
                            if(dataFormSDM.getStatus_approve_hrd().equals("0")){
                                myViewHolder.detailTV.setText("Menunggu verifikasi HRD");
                                myViewHolder.accMark.setVisibility(View.GONE);
                                myViewHolder.rejMark.setVisibility(View.GONE);
                                myViewHolder.proMark.setVisibility(View.VISIBLE);
                            } else if(dataFormSDM.getStatus_approve_hrd().equals("1")){
                                myViewHolder.detailTV.setText("Pengajuan telah diverifikasi HRD");
                                myViewHolder.accMark.setVisibility(View.VISIBLE);
                                myViewHolder.rejMark.setVisibility(View.GONE);
                                myViewHolder.proMark.setVisibility(View.GONE);
                            } else if(dataFormSDM.getStatus_approve_hrd().equals("2")){
                                myViewHolder.detailTV.setText("Pengajuan ditolak HRD");
                                myViewHolder.accMark.setVisibility(View.GONE);
                                myViewHolder.rejMark.setVisibility(View.VISIBLE);
                                myViewHolder.proMark.setVisibility(View.GONE);
                            }
                        }
                    }
                    else if(dataFormSDM.getStatus_approve_kadept().equals("2")){
                        myViewHolder.detailTV.setText("Pengajuan ditolak Atasan Departemen");
                        myViewHolder.accMark.setVisibility(View.GONE);
                        myViewHolder.rejMark.setVisibility(View.VISIBLE);
                        myViewHolder.proMark.setVisibility(View.GONE);
                    }
                } else if(dataFormSDM.getStatus_approve_astkadept().equals("2")){
                    myViewHolder.detailTV.setText("Pengajuan ditolak Ast.Ka.Dept");
                    myViewHolder.accMark.setVisibility(View.GONE);
                    myViewHolder.rejMark.setVisibility(View.VISIBLE);
                    myViewHolder.proMark.setVisibility(View.GONE);
                }
            } else {
                if(dataFormSDM.getStatus_approve_kadept().equals("0")){
                    myViewHolder.detailTV.setText("Menunggu verifikasi Atasan Departemen");
                    myViewHolder.accMark.setVisibility(View.GONE);
                    myViewHolder.rejMark.setVisibility(View.GONE);
                    myViewHolder.proMark.setVisibility(View.VISIBLE);
                    if(sharedPrefManager.getSpIdJabatan().equals("41")||sharedPrefManager.getSpIdJabatan().equals("10")||sharedPrefManager.getSpIdJabatan().equals("90")||sharedPrefManager.getSpIdJabatan().equals("3")){
                        myViewHolder.waitingMark.setVisibility(View.VISIBLE);
                    } else {
                        myViewHolder.waitingMark.setVisibility(View.GONE);
                    }
                }
                else if(dataFormSDM.getStatus_approve_kadept().equals("1")){
                    if(dataFormSDM.getKeterangan().equals("1")||dataFormSDM.getKeterangan().equals("2")){
                        if(dataFormSDM.getStatus_approve_direktur().equals("0")){
                            myViewHolder.detailTV.setText("Menunggu verifikasi Direksi");
                            myViewHolder.accMark.setVisibility(View.GONE);
                            myViewHolder.rejMark.setVisibility(View.GONE);
                            myViewHolder.proMark.setVisibility(View.VISIBLE);
                            if(sharedPrefManager.getSpIdJabatan().equals("8")){
                                myViewHolder.waitingMark.setVisibility(View.VISIBLE);
                            } else {
                                myViewHolder.waitingMark.setVisibility(View.GONE);
                            }
                        } else if(dataFormSDM.getStatus_approve_direktur().equals("1")){
                            if(dataFormSDM.getStatus_approve_hrd().equals("0")){
                                myViewHolder.detailTV.setText("Menunggu verifikasi HRD");
                                myViewHolder.accMark.setVisibility(View.GONE);
                                myViewHolder.rejMark.setVisibility(View.GONE);
                                myViewHolder.proMark.setVisibility(View.VISIBLE);
                            } else if(dataFormSDM.getStatus_approve_hrd().equals("1")){
                                myViewHolder.detailTV.setText("Pengajuan telah diverifikasi HRD");
                                myViewHolder.accMark.setVisibility(View.VISIBLE);
                                myViewHolder.rejMark.setVisibility(View.GONE);
                                myViewHolder.proMark.setVisibility(View.GONE);
                            } else if(dataFormSDM.getStatus_approve_hrd().equals("2")){
                                myViewHolder.detailTV.setText("Pengajuan ditolak HRD");
                                myViewHolder.accMark.setVisibility(View.GONE);
                                myViewHolder.rejMark.setVisibility(View.VISIBLE);
                                myViewHolder.proMark.setVisibility(View.GONE);
                            }
                        } else if(dataFormSDM.getStatus_approve_direktur().equals("2")){
                            myViewHolder.detailTV.setText("Pengajuan ditolak Direksi");
                            myViewHolder.accMark.setVisibility(View.GONE);
                            myViewHolder.rejMark.setVisibility(View.VISIBLE);
                            myViewHolder.proMark.setVisibility(View.GONE);
                        }
                    } else {
                        if(dataFormSDM.getStatus_approve_hrd().equals("0")){
                            myViewHolder.detailTV.setText("Menunggu verifikasi HRD");
                            myViewHolder.accMark.setVisibility(View.GONE);
                            myViewHolder.rejMark.setVisibility(View.GONE);
                            myViewHolder.proMark.setVisibility(View.VISIBLE);
                        } else if(dataFormSDM.getStatus_approve_hrd().equals("1")){
                            myViewHolder.detailTV.setText("Pengajuan telah diverifikasi HRD");
                            myViewHolder.accMark.setVisibility(View.VISIBLE);
                            myViewHolder.rejMark.setVisibility(View.GONE);
                            myViewHolder.proMark.setVisibility(View.GONE);
                        } else if(dataFormSDM.getStatus_approve_hrd().equals("2")){
                            myViewHolder.detailTV.setText("Pengajuan ditolak HRD");
                            myViewHolder.accMark.setVisibility(View.GONE);
                            myViewHolder.rejMark.setVisibility(View.VISIBLE);
                            myViewHolder.proMark.setVisibility(View.GONE);
                        }
                    }
                }
                else if(dataFormSDM.getStatus_approve_kadept().equals("2")){
                    myViewHolder.detailTV.setText("Pengajuan ditolak Atasan Departemen");
                    myViewHolder.accMark.setVisibility(View.GONE);
                    myViewHolder.rejMark.setVisibility(View.VISIBLE);
                    myViewHolder.proMark.setVisibility(View.GONE);
                }
            }
        } else if(dataFormSDM.getStatus_approve_kabag().equals("2")){
            myViewHolder.detailTV.setText("Pengajuan ditolak Atasan Bagian");
            myViewHolder.accMark.setVisibility(View.GONE);
            myViewHolder.rejMark.setVisibility(View.VISIBLE);
            myViewHolder.proMark.setVisibility(View.GONE);
        }

        String tanggal_buat = dataFormSDM.getCreated_at();
        String dayDate = tanggal_buat.substring(8,10);
        String yearDate = tanggal_buat.substring(0,4);
        String bulanValue = tanggal_buat.substring(5,7);
        String bulanName;

        switch (bulanValue) {
            case "01":
                bulanName = "Jan";
                break;
            case "02":
                bulanName = "Feb";
                break;
            case "03":
                bulanName = "Mar";
                break;
            case "04":
                bulanName = "Apr";
                break;
            case "05":
                bulanName = "Mei";
                break;
            case "06":
                bulanName = "Jun";
                break;
            case "07":
                bulanName = "Jul";
                break;
            case "08":
                bulanName = "Agu";
                break;
            case "09":
                bulanName = "Sep";
                break;
            case "10":
                bulanName = "Okt";
                break;
            case "11":
                bulanName = "Nov";
                break;
            case "12":
                bulanName = "Des";
                break;
            default:
                bulanName = "Not found!";
                break;
        }

        myViewHolder.timestampTV.setText("Dibuat pada "+dayDate+" "+bulanName+" "+yearDate);

        myViewHolder.parrentPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailFormSdmActivity.class);
                intent.putExtra("id_data",dataFormSDM.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parrentPart, point1, point2, point3, point4, point5, point6, point7, waitingMark;
        TextView keteranganTV, timestampTV, detailTV, rejMark, accMark, proMark, atasNamaTV, bagianTV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            parrentPart = itemView.findViewById(R.id.parent_part);
            keteranganTV = itemView.findViewById(R.id.keterangan_data_tv);
            point1 = itemView.findViewById(R.id.point_1);
            point2 = itemView.findViewById(R.id.point_2);
            point3 = itemView.findViewById(R.id.point_3);
            point4 = itemView.findViewById(R.id.point_4);
            point5 = itemView.findViewById(R.id.point_5);
            point6 = itemView.findViewById(R.id.point_6);
            point7 = itemView.findViewById(R.id.point_7);
            detailTV = itemView.findViewById(R.id.detail_tv);
            timestampTV = itemView.findViewById(R.id.timestamp_data_tv);
            waitingMark = itemView.findViewById(R.id.waiting_mark);
            accMark = itemView.findViewById(R.id.acc_mark);
            rejMark = itemView.findViewById(R.id.rej_mark);
            proMark = itemView.findViewById(R.id.pro_mark);
            atasNamaTV = itemView.findViewById(R.id.atas_nama_tv);
            bagianTV = itemView.findViewById(R.id.bagain_tv);
        }
    }

}