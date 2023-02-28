package com.gelora.absensi.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.ComingSoonActivity;
import com.gelora.absensi.DetailPengumumanActivity;
import com.gelora.absensi.LoginActivity;
import com.gelora.absensi.R;
import com.gelora.absensi.SharedPrefAbsen;
import com.gelora.absensi.SharedPrefManager;
import com.gelora.absensi.UserActivity;
import com.gelora.absensi.UserDetailActivity;
import com.gelora.absensi.ViewImageActivity;
import com.gelora.absensi.kalert.KAlertDialog;
import com.gelora.absensi.support.FilePathimage;
import com.gelora.absensi.support.ImagePickerActivity;
import com.gelora.absensi.support.Preferences;
import com.github.jinatonic.confetti.CommonConfetti;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;
import net.gotev.uploadservice.MultipartUploadRequest;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FragmentProfile extends Fragment {

    LinearLayout removeAvatarBTN, updateAvatarBTN, viewAvatarBTN, emptyAvatarBTN, availableAvatarBTN, avatarBTN, logoutPart, logoutBTN, uploadFileImage, editFileImage, availableAvatarPart, emptyAvatarPart;
    LinearLayout infoPersonalBTN, infoPekerjaanBTN, infoKontakDaruratBTN, infoKeluargaBTN, infoPendidikanDanPengalamanBTN, infoPayrollBTN;
    TextView nameOfUser, positionOfUser;
    ImageView avatarUser;
    SwipeRefreshLayout refreshLayout;
    SharedPrefManager sharedPrefManager;
    SharedPrefAbsen sharedPrefAbsen;
    ProgressBar loadingProgressBarLogout;
    ExpandableLayout avatarSetting;
    Context mContext;
    Activity mActivity;
    int REQUEST_IMAGE = 100;
    private Uri uri;
    private int i = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = getContext();
        mActivity = getActivity();

        sharedPrefManager = new SharedPrefManager(mContext);
        sharedPrefAbsen = new SharedPrefAbsen(mContext);
        refreshLayout = view.findViewById(R.id.swipe_to_refresh_layout);
        nameOfUser = view.findViewById(R.id.name_of_user);
        positionOfUser = view.findViewById(R.id.position_of_user);
        avatarUser = view.findViewById(R.id.avatar_user);
        avatarBTN = view.findViewById(R.id.avatar_btn);
        logoutPart = view.findViewById(R.id.logout_part);
        logoutBTN = view.findViewById(R.id.logout_btn);
        loadingProgressBarLogout = view.findViewById(R.id.loadingProgressBar_logout);
        avatarSetting = view.findViewById(R.id.avatar_setting);
        uploadFileImage = view.findViewById(R.id.upload_file);
        editFileImage = view.findViewById(R.id.edit_file);
        availableAvatarPart = view.findViewById(R.id.available_avatar_part);
        emptyAvatarPart = view.findViewById(R.id.empty_avatar_part);
        availableAvatarBTN = view.findViewById(R.id.available_avatar_btn);
        emptyAvatarBTN = view.findViewById(R.id.empty_avatar_btn);
        viewAvatarBTN = view.findViewById(R.id.view_avatar_btn);
        updateAvatarBTN = view.findViewById(R.id.update_avatar_btn);
        removeAvatarBTN = view.findViewById(R.id.hapus_avatar_btn);
        infoPersonalBTN = view.findViewById(R.id.info_personal_btn);
        infoPekerjaanBTN = view.findViewById(R.id.info_pekerjaan_btn);
        infoPayrollBTN = view.findViewById(R.id.info_payroll_btn);
        infoPendidikanDanPengalamanBTN = view.findViewById(R.id.info_pendidikan_dan_pengalaman_btn);
        infoKeluargaBTN = view.findViewById(R.id.info_keluarga_btn);
        infoKontakDaruratBTN = view.findViewById(R.id.info_kontak_darurat_btn);

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getDataKaryawan();
                    }
                }, 1000);
            }
        });

        avatarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avatarSetting.isExpanded()){
                    avatarSetting.collapse();
                } else {
                    avatarSetting.expand();
                }
            }
        });

        infoPekerjaanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ComingSoonActivity.class);
                startActivity(intent);
            }
        });

        infoKontakDaruratBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ComingSoonActivity.class);
                startActivity(intent);
            }
        });

        infoKeluargaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ComingSoonActivity.class);
                startActivity(intent);
            }
        });

        infoPendidikanDanPengalamanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ComingSoonActivity.class);
                startActivity(intent);
            }
        });

        infoPayrollBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ComingSoonActivity.class);
                startActivity(intent);
            }
        });

        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new KAlertDialog(mContext, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Perhatian")
                        .setContentText("Apakah anda yakin untuk logout?")
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
                                loadingProgressBarLogout.setVisibility(View.VISIBLE);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        logoutFunction();
                                    }
                                }, 500);

                            }
                        })
                        .show();
            }
        });

        nameOfUser.setText(sharedPrefManager.getSpNama());
        getDataKaryawan();

        return view;
    }

    private void getDataKaryawan() {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        final String url = "https://geloraaksara.co.id/absen-online/api/data_karyawan";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                String tanggal_masuk = data.getString("tanggal_masuk");
                                String status_karyawan = data.getString("status_karyawan");
                                String sisa_cuti = data.getString("sisa_cuti");
                                String periode_mulai = data.getString("periode_mulai");
                                String periode_akhir = data.getString("periode_akhir");
                                String department = data.getString("departemen");
                                String bagian = data.getString("bagian");
                                String jabatan = data.getString("jabatan");
                                String avatar = data.getString("avatar");
                                String weather_key = data.getString("weather_key");
                                String info_covid = data.getString("info_covid");
                                String logout_part = data.getString("logout_part");
                                String web_btn = data.getString("web_btn");
                                String warning_email = data.getString("warning_email");
                                String email_karyawan = data.getString("email_karyawan");
                                String nohp_karyawan = data.getString("nohp_karyawan");
                                String fitur_pengumuman = data.getString("fitur_pengumuman");
                                String join_reminder = data.getString("join_reminder");
                                String pengumuman_date = data.getString("pengumuman_date");
                                String pengumuman_title = data.getString("pengumuman_title");
                                String pengumuman_desc = data.getString("pengumuman_desc");
                                String pengumuman_image = data.getString("pengumuman_image");
                                String pengumuman_time = data.getString("pengumuman_time");

                                String id_cab = data.getString("id_cab");
                                String id_dept = data.getString("id_dept");
                                String id_bagian = data.getString("id_bagian");
                                String id_jabatan = data.getString("id_jabatan");

                                if(!sharedPrefManager.getSpIdCab().equals(id_cab)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_CAB, id_cab);
                                }
                                if(!sharedPrefManager.getSpIdHeadDept().equals(id_dept)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_HEAD_DEPT, id_dept);
                                }
                                if(!sharedPrefManager.getSpIdDept().equals(id_bagian)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_DEPT, id_bagian);
                                }
                                if(!sharedPrefManager.getSpIdJabatan().equals(id_jabatan)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_JABATAN, id_jabatan);
                                }
                                if(!sharedPrefManager.getSpTglBergabung().equals(tanggal_masuk)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_TGL_BERGABUNG, tanggal_masuk);
                                }
                                if(!sharedPrefManager.getSpStatusKaryawan().equals(status_karyawan)){
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS_KARYAWAN, status_karyawan);
                                }

                                if(!avatar.equals("default_profile.jpg") && !avatar.equals("null")){
                                    uploadFileImage.setVisibility(View.GONE);
                                    editFileImage.setVisibility(View.VISIBLE);
                                    String avatarPath = "https://geloraaksara.co.id/absen-online/upload/avatar/"+avatar;
                                    Picasso.get().load(avatarPath).networkPolicy(NetworkPolicy.NO_CACHE)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .into(avatarUser);

                                    emptyAvatarPart.setVisibility(View.GONE);
                                    availableAvatarPart.setVisibility(View.VISIBLE);
                                    emptyAvatarBTN.setVisibility(View.GONE);
                                    availableAvatarBTN.setVisibility(View.VISIBLE);

                                    viewAvatarBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            avatarSetting.collapse();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent = new Intent(mContext, ViewImageActivity.class);
                                                    intent.putExtra("url", avatarPath);
                                                    intent.putExtra("kode", "profile");
                                                    startActivity(intent);
                                                }
                                            }, 400);
                                        }
                                    });

                                    updateAvatarBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            avatarSetting.collapse();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dexterCall();
                                                }
                                            }, 400);
                                        }
                                    });

                                    removeAvatarBTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            avatarSetting.collapse();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    new KAlertDialog(mContext, KAlertDialog.WARNING_TYPE)
                                                            .setTitleText("Perhatian")
                                                            .setContentText("Yakin untuk menghapus foto profil?")
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
                                                                    removePic();
                                                                }
                                                            })
                                                            .show();
                                                }
                                            }, 400);
                                        }
                                    });

                                } else {
                                    if(avatar.equals("default_profile.jpg")){
                                        uploadFileImage.setVisibility(View.VISIBLE);
                                        editFileImage.setVisibility(View.GONE);
                                        String avatarPath = "https://geloraaksara.co.id/absen-online/upload/avatar/"+avatar;
                                        Picasso.get().load(avatarPath).networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .into(avatarUser);

                                        emptyAvatarPart.setVisibility(View.VISIBLE);
                                        availableAvatarPart.setVisibility(View.GONE);
                                        emptyAvatarBTN.setVisibility(View.VISIBLE);
                                        availableAvatarBTN.setVisibility(View.GONE);

                                        emptyAvatarBTN.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                avatarSetting.collapse();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dexterCall();
                                                    }
                                                }, 400);
                                            }
                                        });

                                    } else {
                                        uploadFileImage.setVisibility(View.VISIBLE);
                                        editFileImage.setVisibility(View.GONE);

                                        emptyAvatarPart.setVisibility(View.VISIBLE);
                                        availableAvatarPart.setVisibility(View.GONE);
                                        emptyAvatarBTN.setVisibility(View.VISIBLE);
                                        availableAvatarBTN.setVisibility(View.GONE);

                                        emptyAvatarBTN.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                avatarSetting.collapse();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dexterCall();
                                                    }
                                                }, 400);
                                            }
                                        });
                                    }
                                }

                                infoPersonalBTN.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(mContext, UserDetailActivity.class);
                                        intent.putExtra("avatar", avatar);
                                        intent.putExtra("jabatan", jabatan);
                                        intent.putExtra("bagian", bagian);
                                        intent.putExtra("departemen", department);
                                        intent.putExtra("tanggal_bergabung", tanggal_masuk);
                                        startActivity(intent);
                                    }
                                });

                                positionOfUser.setText(jabatan+" | "+bagian+" | "+department);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_department", sharedPrefManager.getSpIdHeadDept());
                params.put("id_bagian", sharedPrefManager.getSpIdDept());
                params.put("id_jabatan", sharedPrefManager.getSpIdJabatan());
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void dexterCall(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(mActivity)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                showImagePickerOptions();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        } else {
            Dexter.withActivity(mActivity)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                showImagePickerOptions();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }
                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(mContext, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }
            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        }, "avatar");
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 500);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 500);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
        intent.setData(uri);
        String file_directori = getRealPathFromURIPath(uri, mActivity);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        @SuppressLint("Recycle")
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getParcelableExtra("path");
                String stringUri = String.valueOf(uri);
                String extension = stringUri.substring(stringUri.lastIndexOf("."));
                try {
                    if(extension.equals(".jpg")||extension.equals(".JPG")||extension.equals(".jpeg")||extension.equals(".png")||extension.equals(".PNG")){
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), uri);
                        String file_directori = getRealPathFromURIPath(uri, mActivity);
                        String a = "File Directory : "+file_directori+" URI: "+String.valueOf(uri);
                        Log.e("PaRSE JSON", a);
                        uploadMultipart();
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new KAlertDialog(mContext, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Perhatian")
                                        .setContentText("Format file tidak sesuai!")
                                        .setConfirmText("    OK    ")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog sDialog) {
                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        }, 800);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void uploadMultipart() {
        String UPLOAD_URL = "https://geloraaksara.co.id/absen-online/api/update_profilepic";
        String path1 = FilePathimage.getPath(mContext, uri);
        if (path1 == null) {
            Toast.makeText(mContext, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            try {
                String uploadId = UUID.randomUUID().toString();
                new MultipartUploadRequest(mContext, uploadId, UPLOAD_URL)
                        .addFileToUpload(path1, "file") //Adding file
                        .addParameter("NIK", sharedPrefManager.getSpNik()) //Adding text parameter to the request
                        .setMaxRetries(1)
                        .startUpload();
            } catch (Exception exc) {
                Log.e("PaRSE JSON", "Oke");
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final KAlertDialog pDialog = new KAlertDialog(mContext, KAlertDialog.PROGRESS_TYPE)
                        .setTitleText("Loading");
                pDialog.show();
                pDialog.setCancelable(false);
                new CountDownTimer(2000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        i++;
                        switch (i) {
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
                        i = -1;
                        pDialog.setTitleText("Unggah Berhasil")
                                .setConfirmText("    OK    ")
                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                        getDataKaryawan();
                    }
                }.start();

            }
        }, 1);
    }

    private void removePic() {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        final String url = "https://geloraaksara.co.id/absen-online/api/remove_pic";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = new JSONObject(response);
                            String status = data.getString("status");
                            if (status.equals("Success")){
                                final KAlertDialog pDialog = new KAlertDialog(mContext, KAlertDialog.PROGRESS_TYPE)
                                        .setTitleText("Loading");
                                pDialog.show();
                                pDialog.setCancelable(false);
                                new CountDownTimer(1300, 800) {
                                    public void onTick(long millisUntilFinished) {
                                        i++;
                                        switch (i) {
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
                                        i = -1;
                                        pDialog.setTitleText("Foto Berhasil Dihapus")
                                                .setConfirmText("    OK    ")
                                                .changeAlertType(KAlertDialog.SUCCESS_TYPE);
                                        getDataKaryawan();
                                    }
                                }.start();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        connectionFailed();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("NIK", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(mActivity)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    private void logoutFunction(){
        Preferences.setLoggedInStatus(mContext,false);
        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_NIK, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_CAB, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_HEAD_DEPT, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_DEPT, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_JABATAN, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS_USER, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS_AKTIF, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_HALAMAN, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_TGL_BERGABUNG, "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_STATUS_KARYAWAN, "");
        sharedPrefAbsen.saveSPString(SharedPrefAbsen.SP_ID_STATUS, "");
        Preferences.clearLoggedInUser(mContext);
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        mActivity.finish();
        mActivity.finishAffinity();
    }

}
