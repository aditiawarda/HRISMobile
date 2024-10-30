package com.gelora.absensi.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.model.CurrentUser;
import com.gelora.absensi.model.DetailPkResponse;
import com.gelora.absensi.model.PinjamKendaraanResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KendaraanRepository {
    private static final String BASE_URL = "https://hrisgelora.co.id/api/";
    private static final String FILE_URL = "https://hrisgelora.co.id/";

    private static final String GAP_PORTAL_BASE = "https://family.geloraaksara.co.id/gap-f/api/";
    private static final String POST_PINJAM_KENDARAAN = "proses_buat_form_pk";

    public static final String GET_ALL_USER = BASE_URL + "get_all_user";
    public static final String GET_CURRENT_USER = BASE_URL + "get_karyawan?nik=";

    public static final String LIST_PK = GAP_PORTAL_BASE + "get_list_pk/";
    public static final String DETAIL_PK = GAP_PORTAL_BASE + "get_detail_pk/";

    public static final String APPROVAL_PK1 = GAP_PORTAL_BASE + "update_approval_pk1/";
    public static final String APPROVAL_PK2 = GAP_PORTAL_BASE + "update_approval_pk2/";
    public static final String APPROVAL_PK3 = GAP_PORTAL_BASE + "update_approval_pk3/";
    public static final String APPROVAL_PK4 = GAP_PORTAL_BASE + "update_approval_pk4/";

    public static final String KONDISI_PK = GAP_PORTAL_BASE + "update_kondisi_pk/";

    private final RequestQueue requestQueue;
    private final ApiClient apiClient;
    private final Gson gson;

    private ApiService apiService;

    public KendaraanRepository(Context context) {
        apiClient = ApiClient.getInstance(context);
        requestQueue = Volley.newRequestQueue(context);
        gson = new Gson();
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getCurrentUsers(String currentNik, Response.Listener<CurrentUser> listener, Response.Listener<String> codeListener, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_CURRENT_USER + currentNik, null,
                response -> {
                    try {
                        JSONObject dataObject = response.getJSONObject("data");
                        CurrentUser user = gson.fromJson(((JSONObject) dataObject).toString(), CurrentUser.class);
                        codeListener.onResponse(response.getString("status"));
                        listener.onResponse(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("Error parsing JSON"));
                    }
                },
                error -> {
                    error.printStackTrace();
                    errorListener.onErrorResponse(error);
                }
        );
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);
        jsonObjectRequest.setTag("get_users");
        apiClient.addToRequestQueue(jsonObjectRequest);
    }

    public void postPK(String requestNik, String requestNoSurat, String requestBulanSurat, String requestTahunSurat, String tglKeluar, String reqTujuan, String reqKeperluan, String idKendaraan, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GAP_PORTAL_BASE + POST_PINJAM_KENDARAAN,
            response -> {
                try {
                    String message = new JSONObject(response).getString("status");
                    Log.d("POST WOI", message.toString());
                    listener.onResponse(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    errorListener.onErrorResponse(new VolleyError("Error parsing response JSON"));
                }
            },
            error -> {
                error.printStackTrace();
                errorListener.onErrorResponse(error);
            }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nik", requestNik);
                params.put("no_surat", requestNoSurat);
                params.put("bulan_surat", requestBulanSurat);
                params.put("tahun_surat", requestTahunSurat);
                params.put("tanggal_keluar", tglKeluar);
                params.put("tujuan", reqTujuan);
                params.put("keperluan", reqKeperluan);
                params.put("id_kendaraan", idKendaraan);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getListMasuk(String nik, Response.Listener<List<PinjamKendaraanResponse>> listener, Response.Listener<String> codeListener, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, LIST_PK + nik, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray permohonanMasukArray = response.getJSONObject("data").getJSONArray("permohonan_masuk");
                            Type listType = new TypeToken<List<PinjamKendaraanResponse>>() {
                            }.getType();
                            List<PinjamKendaraanResponse> pk = gson.fromJson(permohonanMasukArray.toString(), listType);
                            Log.d("getmasuk", pk.toString());
                            codeListener.onResponse("success");
                            listener.onResponse(pk);
                        } else {
                            errorListener.onErrorResponse(new VolleyError("Status not successful"));
                        }
                    } catch (JSONException | JsonSyntaxException e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("Error parsing JSON: " + e.getMessage()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("Unexpected error: " + e.getMessage()));
                    }
                },
                error -> {
                    error.printStackTrace();
                    errorListener.onErrorResponse(error);
                }
        );

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);
        jsonObjectRequest.setTag("get_list_masuk");
        apiClient.addToRequestQueue(jsonObjectRequest);
    }

    public void getListSaya(String nik, Response.Listener<List<PinjamKendaraanResponse>> listener, Response.Listener<String> codeListener, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, LIST_PK + nik, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray permohonanSayaArray = response.getJSONObject("data").getJSONArray("permohonan_saya");
                            Type listType = new TypeToken<List<PinjamKendaraanResponse>>() {
                            }.getType();
                            List<PinjamKendaraanResponse> pk = gson.fromJson(permohonanSayaArray.toString(), listType);
                            codeListener.onResponse("success");
                            listener.onResponse(pk);
                        } else {
                            errorListener.onErrorResponse(new VolleyError("Status not successful"));
                        }
                    } catch (JSONException | JsonSyntaxException e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("Error parsing JSON: " + e.getMessage()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("Unexpected error: " + e.getMessage()));
                    }
                },
                error -> {
                    error.printStackTrace();
                    errorListener.onErrorResponse(error);
                }
        );
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);
        jsonObjectRequest.setTag("get_list_saya");
        apiClient.addToRequestQueue(jsonObjectRequest);
    }

    public void getDetailPk(String idPk, Response.Listener<DetailPkResponse> listener, Response.Listener<String> codeListener, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, DETAIL_PK + idPk, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONObject dataObject = response.getJSONObject("data");
                            Log.d("getpk", dataObject.toString());
                            DetailPkResponse pk = gson.fromJson(dataObject.toString(), DetailPkResponse.class);
                            codeListener.onResponse("success");
                            listener.onResponse(pk);
                        } else {
                            errorListener.onErrorResponse(new VolleyError("Status not successful"));
                        }
                    } catch (JSONException | JsonSyntaxException e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("Error parsing JSON: " + e.getMessage()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("Unexpected error: " + e.getMessage()));
                    }
                },
                error -> {
                    error.printStackTrace();
                    errorListener.onErrorResponse(error);
                }
        );
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);
        jsonObjectRequest.setTag("get_list_saya");
        apiClient.addToRequestQueue(jsonObjectRequest);
    }

    public void updateApprovalPk1(String idPK, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APPROVAL_PK1 + idPK,
                response -> {
                    try {
                        String message = new JSONObject(response).getString("status");
                        listener.onResponse(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("Error parsing response JSON"));
                    }
                },
                error -> {
                    error.printStackTrace();
                    errorListener.onErrorResponse(error);
                }) {
        };
        requestQueue.add(stringRequest);
    }

    public void updateApprovalPk2(String idPK, String requestNik, String aksi, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APPROVAL_PK2 + idPK,
                response -> {
                    try {
                        String message = new JSONObject(response).getString("status");
                        listener.onResponse(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("Error parsing response JSON"));
                    }
                },
                error -> {
                    error.printStackTrace();
                    errorListener.onErrorResponse(error);
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("approved_2_nik", requestNik);
                params.put("aksi", aksi);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void updateApprovalPk3(String idPK, String requestNik, String aksi, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APPROVAL_PK3 + idPK,
            response -> {
                try {
                    String message = new JSONObject(response).getString("status");
                    listener.onResponse(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    errorListener.onErrorResponse(new VolleyError("Error parsing response JSON"));
                }
            },
            error -> {
                error.printStackTrace();
                errorListener.onErrorResponse(error);
            }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("approved_3_nik", requestNik);
                params.put("aksi", aksi);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void updateApprovalPk4(String idPK, String requestNik, String aksi, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, APPROVAL_PK4 + idPK,
            response -> {
                try {
                    String message = new JSONObject(response).getString("status");
                    listener.onResponse(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    errorListener.onErrorResponse(new VolleyError("Error parsing response JSON"));
                }
            },
            error -> {
                error.printStackTrace();
                errorListener.onErrorResponse(error);
            }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("approved_4_nik", requestNik);
                params.put("aksi", aksi);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void updateKondisiPk(String idPK, String aksi, String bk, String bbm, String km, String jam, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, KONDISI_PK + idPK,
                response -> {
                    try {
                        String message = new JSONObject(response).getString("status");
                        listener.onResponse(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("Error parsing response JSON"));
                    }
                },
                error -> {
                    error.printStackTrace();
                    errorListener.onErrorResponse(error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (aksi.equals("0")) {
                    params.put("bk_keluar", bk);
                    params.put("km_keluar", km);
                    params.put("jam_keluar", jam);
                    params.put("bbm_keluar", bbm);
                } else if (aksi.equals("1")) {
                    params.put("bk_masuk", bk);
                    params.put("km_masuk", km);
                    params.put("jam_masuk", jam);
                    params.put("bbm_masuk", bbm);
                }

                params.put("aksi", aksi);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

}
