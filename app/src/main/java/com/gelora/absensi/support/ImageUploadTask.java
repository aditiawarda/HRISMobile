package com.gelora.absensi.support;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageUploadTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "ImageUploadTask";
    private final String url;
    private final String idReport;
    private final File imageFile;
    private final OkHttpClient client;

    public ImageUploadTask(String url, String idReport, File imageFile) {
        this.url = url;
        this.idReport = idReport;
        this.imageFile = imageFile;
        this.client = new OkHttpClient();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id_report", idReport)
                    .addFormDataPart("image", imageFile.getName(),
                            RequestBody.create(MediaType.parse("image/jpeg"), imageFile))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .method("POST",requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            return response.body().string();
        } catch (IOException e) {
            Log.e(TAG, "Error during image upload", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // Handle the response here
        if (result != null) {
            Log.d(TAG, "Response: " + result);
            // Process the response accordingly
        } else {
            Log.e(TAG, "Failed to upload image");
            // Handle failure scenario
        }
    }
}