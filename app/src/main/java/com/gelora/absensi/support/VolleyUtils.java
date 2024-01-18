package com.gelora.absensi.support;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyUtils {

    private static final String BASE_URL = "https://contoh.com/api/";

    private static RequestQueue requestQueue;

    private VolleyUtils() {
        // konstruktor privat untuk mencegah instansiasi
    }

    public static void init(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static void makeAuthenticatedRequest(String endpoint, String token, final VolleyResponseListener listener) {
        String url = BASE_URL + endpoint;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    if (listener != null) {
                        listener.onResponse(response);
                    }
                },
                error -> {
                    if (listener != null) {
                        listener.onError(error);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        getRequestQueue().add(request);
    }

    private static RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            throw new IllegalStateException("RequestQueue belum diinisialisasi. Panggil VolleyUtils.init() terlebih dahulu.");
        }
        return requestQueue;
    }

    public interface VolleyResponseListener {
        void onResponse(JSONObject response);
        void onError(Exception error);
    }
}
