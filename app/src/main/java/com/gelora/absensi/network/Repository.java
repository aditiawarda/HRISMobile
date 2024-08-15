package com.gelora.absensi.network;

import android.content.Context;
import android.content.Intent;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gelora.absensi.ListIzinKeluarKantor;
import com.gelora.absensi.model.ApprovalAtasanResponse;
import com.gelora.absensi.model.ApprovalSatpamResponse;
import com.gelora.absensi.model.DetailKaryawanKeluar;
import com.gelora.absensi.model.KaryawanKeluar;
import com.gelora.absensi.model.PostIzinResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Repository {

    private static final String BASE_URL = "https://hrisgelora.co.id/api/";
    private static final String FILE_URL = "https://hrisgelora.co.id/";

    public static final String GET_ALL_DATA_FOR_SATPAM_ENDPOINT = BASE_URL + "all_list_izin_keluar_kantor_satpam?nik=";
    public static final String GET_ALL_DATA_ENDPOINT = BASE_URL + "all_list_izin_keluar_kantor?nik=";
    public static final String GET_DATA_ENDPOINT = BASE_URL + "my_list_izin_keluar_kantor?nik=";
    public static final String POST_DATA_ENDPOINT = BASE_URL + "input_izin_keluar_kantor";
    public static final String UPDATE_APPROVE_ENDPOINT = BASE_URL + "approval_atasan_izin_keluar_kantor";
    public static final String UPDATE_APPROVE_SATPAM_ENDPOINT = BASE_URL + "approval_satpam_izin_keluar_kantor";
    public static final String PUT_DATA_ENDPOINT = BASE_URL + "put";
    public static final String DELETE_DATA_ENDPOINT = BASE_URL + "delete";
    public static final String GET_DETAIL_ENDPOINT = BASE_URL + "detail_izin_keluar_kantor?id=";
    public static final String GET_SIGNATURE_ENDPOINT = FILE_URL + "upload/digital_signature/";
    public static final String DELETE_REQUEST_ENDPOINT = BASE_URL + "cancel_permohonan_izin_keluar_kantor";

    private final RequestQueue requestQueue;
    private final ApiClient apiClient;
    private final Gson gson;

    private ApiService apiService;

    public Repository(Context context) {
        apiClient = ApiClient.getInstance(context);
        requestQueue = Volley.newRequestQueue(context);
        gson = new Gson();
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void cancelRequests(String tag, Context context) {
        if (tag == null) return;

        RequestQueue queue = Volley.newRequestQueue(context);
        if (queue != null) {
            queue.cancelAll(tag);
        }
    }

    public void getUsers(String currentNik, Response.Listener<List<KaryawanKeluar>> listener, Response.Listener<String> codeListener,  Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_DATA_ENDPOINT + currentNik, null,
                response -> {
                    try {
                        // Extract the 'data' array from the JSON response
                        JSONArray dataArray = response.getJSONArray("data");

                        // Convert the JSON Array to a List of User objects
                        Type userListType = new TypeToken<List<KaryawanKeluar>>() {
                        }.getType();
                        List<KaryawanKeluar> users = gson.fromJson(dataArray.toString(), userListType);

                        // Notify listener with the list of users
                        codeListener.onResponse(response.getString("status"));
                        listener.onResponse(users);
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

    public void getAllUsers(String currentNik, Response.Listener<List<KaryawanKeluar>> listener, Response.Listener<String> codeListener,  Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_ALL_DATA_ENDPOINT + currentNik, null,
                response -> {
                    try {
                        JSONArray dataArray = response.getJSONArray("data");
                        Type userListType = new TypeToken<List<KaryawanKeluar>>() {
                        }.getType();
                        List<KaryawanKeluar> users = gson.fromJson(dataArray.toString(), userListType);
                        codeListener.onResponse(response.getString("status"));
                        listener.onResponse(users);
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
        jsonObjectRequest.setTag("get_all_users");
        apiClient.addToRequestQueue(jsonObjectRequest);
    }


    public void getAllUsersForSatpam(String currentNik, Response.Listener<List<KaryawanKeluar>> listener, Response.Listener<String> codeListener, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_ALL_DATA_FOR_SATPAM_ENDPOINT + currentNik, null,
                response -> {
                    try {
                        // Extract the 'data' array from the JSON response
                        JSONArray dataArray = response.getJSONArray("data");

                        // Convert the JSON Array to a List of User objects
                        Type userListType = new TypeToken<List<KaryawanKeluar>>() {
                        }.getType();
                        List<KaryawanKeluar> users = gson.fromJson(dataArray.toString(), userListType);

                        // Notify listener with the list of users
                        codeListener.onResponse(response.getString("status"));
                        listener.onResponse(users);
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
        jsonObjectRequest.setTag("get_all_users_satpam");
        apiClient.addToRequestQueue(jsonObjectRequest);
    }




    public String getSignature(String signatureName) {
        return GET_SIGNATURE_ENDPOINT + signatureName;
    }

    public void getDetail(int requestId, Response.Listener<DetailKaryawanKeluar> listener, Response.Listener<String> codeListener, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_DETAIL_ENDPOINT + requestId, null,
                response -> {
                    try {
                        JSONObject dataObject = response.getJSONObject("data");
                        String responseCode = response.getString("status");
                        Type userListType = new TypeToken<DetailKaryawanKeluar>() {
                        }.getType();
                        DetailKaryawanKeluar users = gson.fromJson(dataObject.toString(), userListType);
                        codeListener.onResponse(responseCode);
                        listener.onResponse(users);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("Error parsing JSON"));
                    }
                },
                error -> {
                    error.printStackTrace();
                    errorListener.onErrorResponse(error);
                }
        );
        apiClient.addToRequestQueue(jsonObjectRequest);
    }


    public void cancelIzinKeluarKantor(String id,  final Context context, final Callback<Void> callback) {
        Call<Void> call = apiService.cancelIzinKeluarKantor(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    callback.onResponse(call, response);
                } else {
                    // Handle error response
                    callback.onFailure(call, new Throwable("Failed to delete item"));
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle network errors or exceptions
                callback.onFailure(call, t);
            }
        });

}


public void postData(PostIzinResponse karyawanKeluar, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nik", karyawanKeluar.getNik());
            jsonBody.put("jam_keluar", karyawanKeluar.getJamKeluar());
            jsonBody.put("durasi", karyawanKeluar.getDurasi());
            jsonBody.put("keperluan", karyawanKeluar.getKeperluan());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, POST_DATA_ENDPOINT, jsonBody,
                response -> {
                    try {
                        String message = response.getString("status");
                        String id = response.getString("id");
                        listener.onResponse(message+"-"+id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("Error parsing response JSON"));
                    }
                },
                error -> {
                    error.printStackTrace();
                    errorListener.onErrorResponse(error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }


    public void updateApproveAtasan(ApprovalAtasanResponse approvalAtasanResponse, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nik", approvalAtasanResponse.getNik());
            jsonBody.put("id", approvalAtasanResponse.getId());
            jsonBody.put("status_approve", approvalAtasanResponse.getStatusApprove());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, UPDATE_APPROVE_ENDPOINT, jsonBody,
                response -> {
                    try {
                        String message = response.getString("status");
                        listener.onResponse(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("Error parsing response JSON"));
                    }
                },
                error -> {
                    error.printStackTrace();
                    errorListener.onErrorResponse(error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void updateApproveSatpam(ApprovalSatpamResponse approvalAtasanResponse, Response.Listener<String> listener, Response.ErrorListener errorListener) {

        // Convert KaryawanKeluar object to JSON
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nik", approvalAtasanResponse.getNik());
            jsonBody.put("id", approvalAtasanResponse.getId());
            jsonBody.put("status_approve_satpam", approvalAtasanResponse.getStatusApproveSatpam());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, UPDATE_APPROVE_SATPAM_ENDPOINT, jsonBody,
                response -> {
                    try {
                        String message = response.getString("status");
                        listener.onResponse(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(new VolleyError("Error parsing response JSON"));
                    }
                },
                error -> {
                    error.printStackTrace();
                    errorListener.onErrorResponse(error);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

}
