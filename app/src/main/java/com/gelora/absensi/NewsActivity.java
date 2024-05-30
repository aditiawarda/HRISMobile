package com.gelora.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.adapter.AdapterDataNews;
import com.gelora.absensi.model.NewsData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewsActivity extends AppCompatActivity {

    LinearLayout sourceLabelPart, actionBar, categoryBTN, emptyDataNews, loadingNewsPart, backBTN;
    LinearLayout cat1BTN, cat2BTN, cat3BTN, cat4BTN, cat5BTN, cat6BTN, cat7BTN, cat8BTN, cat9BTN, cat10BTN, cat11BTN, cat12BTN;
    LinearLayout markCat1BTN, markCat2BTN, markCat3BTN, markCat4BTN, markCat5BTN, markCat6BTN, markCat7BTN, markCat8BTN, markCat9BTN, markCat10BTN, markCat11BTN, markCat12BTN;
    TextView categoryChoiceTV;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refreshLayout;
    BottomSheetLayout bottomSheet;
    RequestQueue requestQueue;
    View rootview;
    String linkApi = "", categoryNewsLabel = "", categoryNews = ""; //business,health,politics,sports,technology,entertainment,environment,food,science,top,tourism,world

    private RecyclerView dataNewsRV;
    private NewsData[] dataNews;
    private AdapterDataNews adapterDataNews;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        sharedPrefManager = new SharedPrefManager(this);
        requestQueue = Volley.newRequestQueue(this);
        rootview = findViewById(android.R.id.content);
        bottomSheet = findViewById(R.id.bottom_sheet_layout);
        backBTN = findViewById(R.id.back_btn);
        refreshLayout = findViewById(R.id.swipe_to_refresh_layout);
        categoryChoiceTV = findViewById(R.id.category_choice_tv);
        loadingNewsPart = findViewById(R.id.loading_data_part_news);
        emptyDataNews = findViewById(R.id.no_data_part_news);
        actionBar = findViewById(R.id.action_bar);
        categoryBTN = findViewById(R.id.choice_category);
        sourceLabelPart = findViewById(R.id.source_label_part);

        dataNewsRV = findViewById(R.id.data_news_rv);

        dataNewsRV.setLayoutManager(new LinearLayoutManager(this));
        dataNewsRV.setHasFixedSize(true);
        dataNewsRV.setNestedScrollingEnabled(false);
        dataNewsRV.setItemAnimator(new DefaultItemAnimator());

        linkApi = getIntent().getExtras().getString("api_url");
        categoryNews = getIntent().getExtras().getString("defaut_news_category");

        if(categoryNews.equals("top")){
            categoryNewsLabel = "Teratas";
        } else if(categoryNews.equals("business")){
            categoryNewsLabel = "Bisnis";
        } else if(categoryNews.equals("health")){
            categoryNewsLabel = "Kesehatan";
        } else if(categoryNews.equals("politics")){
            categoryNewsLabel = "Politik";
        } else if(categoryNews.equals("sports")){
            categoryNewsLabel = "Olahraga";
        } else if(categoryNews.equals("technology")){
            categoryNewsLabel = "Teknologi";
        } else if(categoryNews.equals("entertainment")){
            categoryNewsLabel = "Hiburan";
        } else if(categoryNews.equals("environment")){
            categoryNewsLabel = "Lingkungan";
        } else if(categoryNews.equals("food")){
            categoryNewsLabel = "Kuliner";
        } else if(categoryNews.equals("science")){
            categoryNewsLabel = "Sains";
        } else if(categoryNews.equals("tourism")){
            categoryNewsLabel = "Pariwisata";
        } else if(categoryNews.equals("world")){
            categoryNewsLabel = "Dunia";
        }
        categoryChoiceTV.setText(categoryNewsLabel);

        actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        refreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sourceLabelPart.setVisibility(View.GONE);
                dataNewsRV.setVisibility(View.GONE);
                loadingNewsPart.setVisibility(View.VISIBLE);
                emptyDataNews.setVisibility(View.GONE);

                handler.postDelayed(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        getNews(categoryNews);
                    }
                }, 500);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        categoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryChoice();
            }
        });

        getNews(categoryNews);
        newsVisitor();

    }

    private void getNews(String category) {
        //RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = linkApi+category;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response.toString());
                            data = response;
                            String status = data.getString("status").toString();
                            if (status.equals("success")) {
                                String totalResults = data.getString("totalResults");

                                if (totalResults.equals("0")) {
                                    sourceLabelPart.setVisibility(View.GONE);
                                    emptyDataNews.setVisibility(View.VISIBLE);
                                    dataNewsRV.setVisibility(View.GONE);
                                    loadingNewsPart.setVisibility(View.GONE);
                                } else {
                                    sourceLabelPart.setVisibility(View.VISIBLE);
                                    emptyDataNews.setVisibility(View.GONE);
                                    dataNewsRV.setVisibility(View.VISIBLE);
                                    loadingNewsPart.setVisibility(View.GONE);
                                    String results = data.getString("results");

                                    GsonBuilder builder = new GsonBuilder();
                                    Gson gson = builder.create();
                                    dataNews = gson.fromJson(results, NewsData[].class);
                                    adapterDataNews = new AdapterDataNews(dataNews, NewsActivity.this);
                                    dataNewsRV.setAdapter(adapterDataNews);
                                }

                            } else {
                                sourceLabelPart.setVisibility(View.GONE);
                                emptyDataNews.setVisibility(View.VISIBLE);
                                dataNewsRV.setVisibility(View.GONE);
                                loadingNewsPart.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                sourceLabelPart.setVisibility(View.GONE);
                emptyDataNews.setVisibility(View.VISIBLE);
                dataNewsRV.setVisibility(View.GONE);
                loadingNewsPart.setVisibility(View.GONE);
            }
        });

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);

        requestQueue.add(request);

    }

    private void categoryChoice(){
        bottomSheet.showWithSheetView(LayoutInflater.from(NewsActivity.this).inflate(R.layout.layout_news_category, bottomSheet, false));
        cat1BTN = findViewById(R.id.cat_1);   //top
        cat2BTN = findViewById(R.id.cat_2);   //business
        cat3BTN = findViewById(R.id.cat_3);   //environment
        cat4BTN = findViewById(R.id.cat_4);   //entertainment
        cat5BTN = findViewById(R.id.cat_5);   //food
        cat6BTN = findViewById(R.id.cat_6);   //health
        cat7BTN = findViewById(R.id.cat_7);   //politics
        cat8BTN = findViewById(R.id.cat_8);   //science
        cat9BTN = findViewById(R.id.cat_9);   //sports
        cat10BTN = findViewById(R.id.cat_10); //technology
        cat11BTN = findViewById(R.id.cat_11); //tourism
        cat12BTN = findViewById(R.id.cat_12); //world

        markCat1BTN = findViewById(R.id.mark_cat_1);
        markCat2BTN = findViewById(R.id.mark_cat_2);
        markCat3BTN = findViewById(R.id.mark_cat_3);
        markCat4BTN = findViewById(R.id.mark_cat_4);
        markCat5BTN = findViewById(R.id.mark_cat_5);
        markCat6BTN = findViewById(R.id.mark_cat_6);
        markCat7BTN = findViewById(R.id.mark_cat_7);
        markCat8BTN = findViewById(R.id.mark_cat_8);
        markCat9BTN = findViewById(R.id.mark_cat_9);
        markCat10BTN = findViewById(R.id.mark_cat_10);
        markCat11BTN = findViewById(R.id.mark_cat_11);
        markCat12BTN = findViewById(R.id.mark_cat_12);

        handler.postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if (categoryNews.equals("top")){
                    cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                    cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    markCat1BTN.setVisibility(View.VISIBLE);
                    markCat2BTN.setVisibility(View.GONE);
                    markCat3BTN.setVisibility(View.GONE);
                    markCat4BTN.setVisibility(View.GONE);
                    markCat5BTN.setVisibility(View.GONE);
                    markCat6BTN.setVisibility(View.GONE);
                    markCat7BTN.setVisibility(View.GONE);
                    markCat8BTN.setVisibility(View.GONE);
                    markCat9BTN.setVisibility(View.GONE);
                    markCat10BTN.setVisibility(View.GONE);
                    markCat11BTN.setVisibility(View.GONE);
                    markCat12BTN.setVisibility(View.GONE);
                }
                else if (categoryNews.equals("business")){
                    cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                    cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    markCat1BTN.setVisibility(View.GONE);
                    markCat2BTN.setVisibility(View.VISIBLE);
                    markCat3BTN.setVisibility(View.GONE);
                    markCat4BTN.setVisibility(View.GONE);
                    markCat5BTN.setVisibility(View.GONE);
                    markCat6BTN.setVisibility(View.GONE);
                    markCat7BTN.setVisibility(View.GONE);
                    markCat8BTN.setVisibility(View.GONE);
                    markCat9BTN.setVisibility(View.GONE);
                    markCat10BTN.setVisibility(View.GONE);
                    markCat11BTN.setVisibility(View.GONE);
                    markCat12BTN.setVisibility(View.GONE);
                }
                else if (categoryNews.equals("environment")){
                    cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                    cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    markCat1BTN.setVisibility(View.GONE);
                    markCat2BTN.setVisibility(View.GONE);
                    markCat3BTN.setVisibility(View.VISIBLE);
                    markCat4BTN.setVisibility(View.GONE);
                    markCat5BTN.setVisibility(View.GONE);
                    markCat6BTN.setVisibility(View.GONE);
                    markCat7BTN.setVisibility(View.GONE);
                    markCat8BTN.setVisibility(View.GONE);
                    markCat9BTN.setVisibility(View.GONE);
                    markCat10BTN.setVisibility(View.GONE);
                    markCat11BTN.setVisibility(View.GONE);
                    markCat12BTN.setVisibility(View.GONE);
                }
                else if (categoryNews.equals("entertainment")){
                    cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                    cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    markCat1BTN.setVisibility(View.GONE);
                    markCat2BTN.setVisibility(View.GONE);
                    markCat3BTN.setVisibility(View.GONE);
                    markCat4BTN.setVisibility(View.VISIBLE);
                    markCat5BTN.setVisibility(View.GONE);
                    markCat6BTN.setVisibility(View.GONE);
                    markCat7BTN.setVisibility(View.GONE);
                    markCat8BTN.setVisibility(View.GONE);
                    markCat9BTN.setVisibility(View.GONE);
                    markCat10BTN.setVisibility(View.GONE);
                    markCat11BTN.setVisibility(View.GONE);
                    markCat12BTN.setVisibility(View.GONE);
                }
                else if (categoryNews.equals("food")){
                    cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                    cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    markCat1BTN.setVisibility(View.GONE);
                    markCat2BTN.setVisibility(View.GONE);
                    markCat3BTN.setVisibility(View.GONE);
                    markCat4BTN.setVisibility(View.GONE);
                    markCat5BTN.setVisibility(View.VISIBLE);
                    markCat6BTN.setVisibility(View.GONE);
                    markCat7BTN.setVisibility(View.GONE);
                    markCat8BTN.setVisibility(View.GONE);
                    markCat9BTN.setVisibility(View.GONE);
                    markCat10BTN.setVisibility(View.GONE);
                    markCat11BTN.setVisibility(View.GONE);
                    markCat12BTN.setVisibility(View.GONE);
                }
                else if (categoryNews.equals("health")){
                    cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                    cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    markCat1BTN.setVisibility(View.GONE);
                    markCat2BTN.setVisibility(View.GONE);
                    markCat3BTN.setVisibility(View.GONE);
                    markCat4BTN.setVisibility(View.GONE);
                    markCat5BTN.setVisibility(View.GONE);
                    markCat6BTN.setVisibility(View.VISIBLE);
                    markCat7BTN.setVisibility(View.GONE);
                    markCat8BTN.setVisibility(View.GONE);
                    markCat9BTN.setVisibility(View.GONE);
                    markCat10BTN.setVisibility(View.GONE);
                    markCat11BTN.setVisibility(View.GONE);
                    markCat12BTN.setVisibility(View.GONE);
                }
                else if (categoryNews.equals("politics")){
                    cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                    cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    markCat1BTN.setVisibility(View.GONE);
                    markCat2BTN.setVisibility(View.GONE);
                    markCat3BTN.setVisibility(View.GONE);
                    markCat4BTN.setVisibility(View.GONE);
                    markCat5BTN.setVisibility(View.GONE);
                    markCat6BTN.setVisibility(View.GONE);
                    markCat7BTN.setVisibility(View.VISIBLE);
                    markCat8BTN.setVisibility(View.GONE);
                    markCat9BTN.setVisibility(View.GONE);
                    markCat10BTN.setVisibility(View.GONE);
                    markCat11BTN.setVisibility(View.GONE);
                    markCat12BTN.setVisibility(View.GONE);
                }
                else if (categoryNews.equals("science")){
                    cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                    cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    markCat1BTN.setVisibility(View.GONE);
                    markCat2BTN.setVisibility(View.GONE);
                    markCat3BTN.setVisibility(View.GONE);
                    markCat4BTN.setVisibility(View.GONE);
                    markCat5BTN.setVisibility(View.GONE);
                    markCat6BTN.setVisibility(View.GONE);
                    markCat7BTN.setVisibility(View.GONE);
                    markCat8BTN.setVisibility(View.VISIBLE);
                    markCat9BTN.setVisibility(View.GONE);
                    markCat10BTN.setVisibility(View.GONE);
                    markCat11BTN.setVisibility(View.GONE);
                    markCat12BTN.setVisibility(View.GONE);
                }
                else if (categoryNews.equals("sports")){
                    cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                    cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    markCat1BTN.setVisibility(View.GONE);
                    markCat2BTN.setVisibility(View.GONE);
                    markCat3BTN.setVisibility(View.GONE);
                    markCat4BTN.setVisibility(View.GONE);
                    markCat5BTN.setVisibility(View.GONE);
                    markCat6BTN.setVisibility(View.GONE);
                    markCat7BTN.setVisibility(View.GONE);
                    markCat8BTN.setVisibility(View.GONE);
                    markCat9BTN.setVisibility(View.VISIBLE);
                    markCat10BTN.setVisibility(View.GONE);
                    markCat11BTN.setVisibility(View.GONE);
                    markCat12BTN.setVisibility(View.GONE);
                }
                else if (categoryNews.equals("technology")){
                    cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                    cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    markCat1BTN.setVisibility(View.GONE);
                    markCat2BTN.setVisibility(View.GONE);
                    markCat3BTN.setVisibility(View.GONE);
                    markCat4BTN.setVisibility(View.GONE);
                    markCat5BTN.setVisibility(View.GONE);
                    markCat6BTN.setVisibility(View.GONE);
                    markCat7BTN.setVisibility(View.GONE);
                    markCat8BTN.setVisibility(View.GONE);
                    markCat9BTN.setVisibility(View.GONE);
                    markCat10BTN.setVisibility(View.VISIBLE);
                    markCat11BTN.setVisibility(View.GONE);
                    markCat12BTN.setVisibility(View.GONE);
                }
                else if (categoryNews.equals("tourism")){
                    cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                    cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    markCat1BTN.setVisibility(View.GONE);
                    markCat2BTN.setVisibility(View.GONE);
                    markCat3BTN.setVisibility(View.GONE);
                    markCat4BTN.setVisibility(View.GONE);
                    markCat5BTN.setVisibility(View.GONE);
                    markCat6BTN.setVisibility(View.GONE);
                    markCat7BTN.setVisibility(View.GONE);
                    markCat8BTN.setVisibility(View.GONE);
                    markCat9BTN.setVisibility(View.GONE);
                    markCat10BTN.setVisibility(View.GONE);
                    markCat11BTN.setVisibility(View.VISIBLE);
                    markCat12BTN.setVisibility(View.GONE);
                }
                else if (categoryNews.equals("world")){
                    cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                    markCat1BTN.setVisibility(View.GONE);
                    markCat2BTN.setVisibility(View.GONE);
                    markCat3BTN.setVisibility(View.GONE);
                    markCat4BTN.setVisibility(View.GONE);
                    markCat5BTN.setVisibility(View.GONE);
                    markCat6BTN.setVisibility(View.GONE);
                    markCat7BTN.setVisibility(View.GONE);
                    markCat8BTN.setVisibility(View.GONE);
                    markCat9BTN.setVisibility(View.GONE);
                    markCat10BTN.setVisibility(View.GONE);
                    markCat11BTN.setVisibility(View.GONE);
                    markCat12BTN.setVisibility(View.VISIBLE);
                }
                else {
                    cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                    markCat1BTN.setVisibility(View.GONE);
                    markCat2BTN.setVisibility(View.GONE);
                    markCat3BTN.setVisibility(View.GONE);
                    markCat4BTN.setVisibility(View.GONE);
                    markCat5BTN.setVisibility(View.GONE);
                    markCat6BTN.setVisibility(View.GONE);
                    markCat7BTN.setVisibility(View.GONE);
                    markCat8BTN.setVisibility(View.GONE);
                    markCat9BTN.setVisibility(View.GONE);
                    markCat10BTN.setVisibility(View.GONE);
                    markCat11BTN.setVisibility(View.GONE);
                    markCat12BTN.setVisibility(View.GONE);
                }

                cat1BTN.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                        cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        markCat1BTN.setVisibility(View.VISIBLE);
                        markCat2BTN.setVisibility(View.GONE);
                        markCat3BTN.setVisibility(View.GONE);
                        markCat4BTN.setVisibility(View.GONE);
                        markCat5BTN.setVisibility(View.GONE);
                        markCat6BTN.setVisibility(View.GONE);
                        markCat7BTN.setVisibility(View.GONE);
                        markCat8BTN.setVisibility(View.GONE);
                        markCat9BTN.setVisibility(View.GONE);
                        markCat10BTN.setVisibility(View.GONE);
                        markCat11BTN.setVisibility(View.GONE);
                        markCat12BTN.setVisibility(View.GONE);
                        categoryNews = "top";
                        categoryNewsLabel = "Teratas";
                        categoryChoiceTV.setText(categoryNewsLabel);

                        sourceLabelPart.setVisibility(View.GONE);
                        dataNewsRV.setVisibility(View.GONE);
                        loadingNewsPart.setVisibility(View.VISIBLE);
                        emptyDataNews.setVisibility(View.GONE);

                        handler.postDelayed(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                getNews(categoryNews);
                            }
                        }, 500);

                    }
                });

                cat2BTN.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                        cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        markCat1BTN.setVisibility(View.GONE);
                        markCat2BTN.setVisibility(View.VISIBLE);
                        markCat3BTN.setVisibility(View.GONE);
                        markCat4BTN.setVisibility(View.GONE);
                        markCat5BTN.setVisibility(View.GONE);
                        markCat6BTN.setVisibility(View.GONE);
                        markCat7BTN.setVisibility(View.GONE);
                        markCat8BTN.setVisibility(View.GONE);
                        markCat9BTN.setVisibility(View.GONE);
                        markCat10BTN.setVisibility(View.GONE);
                        markCat11BTN.setVisibility(View.GONE);
                        markCat12BTN.setVisibility(View.GONE);
                        categoryNews = "business";
                        categoryNewsLabel = "Bisnis";
                        categoryChoiceTV.setText(categoryNewsLabel);

                        sourceLabelPart.setVisibility(View.GONE);
                        dataNewsRV.setVisibility(View.GONE);
                        loadingNewsPart.setVisibility(View.VISIBLE);
                        emptyDataNews.setVisibility(View.GONE);

                        handler.postDelayed(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                getNews(categoryNews);
                            }
                        }, 500);

                    }
                });

                cat3BTN.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                        cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        markCat1BTN.setVisibility(View.GONE);
                        markCat2BTN.setVisibility(View.GONE);
                        markCat3BTN.setVisibility(View.VISIBLE);
                        markCat4BTN.setVisibility(View.GONE);
                        markCat5BTN.setVisibility(View.GONE);
                        markCat6BTN.setVisibility(View.GONE);
                        markCat7BTN.setVisibility(View.GONE);
                        markCat8BTN.setVisibility(View.GONE);
                        markCat9BTN.setVisibility(View.GONE);
                        markCat10BTN.setVisibility(View.GONE);
                        markCat11BTN.setVisibility(View.GONE);
                        markCat12BTN.setVisibility(View.GONE);
                        categoryNews = "environment";
                        categoryNewsLabel = "Lingkungan";
                        categoryChoiceTV.setText(categoryNewsLabel);

                        sourceLabelPart.setVisibility(View.GONE);
                        dataNewsRV.setVisibility(View.GONE);
                        loadingNewsPart.setVisibility(View.VISIBLE);
                        emptyDataNews.setVisibility(View.GONE);

                        handler.postDelayed(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                getNews(categoryNews);
                            }
                        }, 500);

                    }
                });

                cat4BTN.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                        cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        markCat1BTN.setVisibility(View.GONE);
                        markCat2BTN.setVisibility(View.GONE);
                        markCat3BTN.setVisibility(View.GONE);
                        markCat4BTN.setVisibility(View.VISIBLE);
                        markCat5BTN.setVisibility(View.GONE);
                        markCat6BTN.setVisibility(View.GONE);
                        markCat7BTN.setVisibility(View.GONE);
                        markCat8BTN.setVisibility(View.GONE);
                        markCat9BTN.setVisibility(View.GONE);
                        markCat10BTN.setVisibility(View.GONE);
                        markCat11BTN.setVisibility(View.GONE);
                        markCat12BTN.setVisibility(View.GONE);
                        categoryNews = "entertainment";
                        categoryNewsLabel = "Hiburan";
                        categoryChoiceTV.setText(categoryNewsLabel);

                        sourceLabelPart.setVisibility(View.GONE);
                        dataNewsRV.setVisibility(View.GONE);
                        loadingNewsPart.setVisibility(View.VISIBLE);
                        emptyDataNews.setVisibility(View.GONE);

                        handler.postDelayed(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                getNews(categoryNews);
                            }
                        }, 500);

                    }
                });

                cat5BTN.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                        cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        markCat1BTN.setVisibility(View.GONE);
                        markCat2BTN.setVisibility(View.GONE);
                        markCat3BTN.setVisibility(View.GONE);
                        markCat4BTN.setVisibility(View.GONE);
                        markCat5BTN.setVisibility(View.VISIBLE);
                        markCat6BTN.setVisibility(View.GONE);
                        markCat7BTN.setVisibility(View.GONE);
                        markCat8BTN.setVisibility(View.GONE);
                        markCat9BTN.setVisibility(View.GONE);
                        markCat10BTN.setVisibility(View.GONE);
                        markCat11BTN.setVisibility(View.GONE);
                        markCat12BTN.setVisibility(View.GONE);
                        categoryNews = "food";
                        categoryNewsLabel = "Kuliner";
                        categoryChoiceTV.setText(categoryNewsLabel);

                        sourceLabelPart.setVisibility(View.GONE);
                        dataNewsRV.setVisibility(View.GONE);
                        loadingNewsPart.setVisibility(View.VISIBLE);
                        emptyDataNews.setVisibility(View.GONE);

                        handler.postDelayed(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                getNews(categoryNews);
                            }
                        }, 500);

                    }
                });

                cat6BTN.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                        cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        markCat1BTN.setVisibility(View.GONE);
                        markCat2BTN.setVisibility(View.GONE);
                        markCat3BTN.setVisibility(View.GONE);
                        markCat4BTN.setVisibility(View.GONE);
                        markCat5BTN.setVisibility(View.GONE);
                        markCat6BTN.setVisibility(View.VISIBLE);
                        markCat7BTN.setVisibility(View.GONE);
                        markCat8BTN.setVisibility(View.GONE);
                        markCat9BTN.setVisibility(View.GONE);
                        markCat10BTN.setVisibility(View.GONE);
                        markCat11BTN.setVisibility(View.GONE);
                        markCat12BTN.setVisibility(View.GONE);
                        categoryNews = "health";
                        categoryNewsLabel = "Kesehatan";
                        categoryChoiceTV.setText(categoryNewsLabel);

                        sourceLabelPart.setVisibility(View.GONE);
                        dataNewsRV.setVisibility(View.GONE);
                        loadingNewsPart.setVisibility(View.VISIBLE);
                        emptyDataNews.setVisibility(View.GONE);

                        handler.postDelayed(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                getNews(categoryNews);
                            }
                        }, 500);

                    }
                });

                cat7BTN.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                        cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        markCat1BTN.setVisibility(View.GONE);
                        markCat2BTN.setVisibility(View.GONE);
                        markCat3BTN.setVisibility(View.GONE);
                        markCat4BTN.setVisibility(View.GONE);
                        markCat5BTN.setVisibility(View.GONE);
                        markCat6BTN.setVisibility(View.GONE);
                        markCat7BTN.setVisibility(View.VISIBLE);
                        markCat8BTN.setVisibility(View.GONE);
                        markCat9BTN.setVisibility(View.GONE);
                        markCat10BTN.setVisibility(View.GONE);
                        markCat11BTN.setVisibility(View.GONE);
                        markCat12BTN.setVisibility(View.GONE);
                        categoryNews = "politics";
                        categoryNewsLabel = "Politik";
                        categoryChoiceTV.setText(categoryNewsLabel);

                        sourceLabelPart.setVisibility(View.GONE);
                        dataNewsRV.setVisibility(View.GONE);
                        loadingNewsPart.setVisibility(View.VISIBLE);
                        emptyDataNews.setVisibility(View.GONE);

                        handler.postDelayed(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                getNews(categoryNews);
                            }
                        }, 500);

                    }
                });

                cat8BTN.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                        cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        markCat1BTN.setVisibility(View.GONE);
                        markCat2BTN.setVisibility(View.GONE);
                        markCat3BTN.setVisibility(View.GONE);
                        markCat4BTN.setVisibility(View.GONE);
                        markCat5BTN.setVisibility(View.GONE);
                        markCat6BTN.setVisibility(View.GONE);
                        markCat7BTN.setVisibility(View.GONE);
                        markCat8BTN.setVisibility(View.VISIBLE);
                        markCat9BTN.setVisibility(View.GONE);
                        markCat10BTN.setVisibility(View.GONE);
                        markCat11BTN.setVisibility(View.GONE);
                        markCat12BTN.setVisibility(View.GONE);
                        categoryNews = "science";
                        categoryNewsLabel = "Sains";
                        categoryChoiceTV.setText(categoryNewsLabel);

                        sourceLabelPart.setVisibility(View.GONE);
                        dataNewsRV.setVisibility(View.GONE);
                        loadingNewsPart.setVisibility(View.VISIBLE);
                        emptyDataNews.setVisibility(View.GONE);

                        handler.postDelayed(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                getNews(categoryNews);
                            }
                        }, 500);

                    }
                });

                cat9BTN.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                        cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        markCat1BTN.setVisibility(View.GONE);
                        markCat2BTN.setVisibility(View.GONE);
                        markCat3BTN.setVisibility(View.GONE);
                        markCat4BTN.setVisibility(View.GONE);
                        markCat5BTN.setVisibility(View.GONE);
                        markCat6BTN.setVisibility(View.GONE);
                        markCat7BTN.setVisibility(View.GONE);
                        markCat8BTN.setVisibility(View.GONE);
                        markCat9BTN.setVisibility(View.VISIBLE);
                        markCat10BTN.setVisibility(View.GONE);
                        markCat11BTN.setVisibility(View.GONE);
                        markCat12BTN.setVisibility(View.GONE);
                        categoryNews = "sports";
                        categoryNewsLabel = "Olahraga";
                        categoryChoiceTV.setText(categoryNewsLabel);

                        sourceLabelPart.setVisibility(View.GONE);
                        dataNewsRV.setVisibility(View.GONE);
                        loadingNewsPart.setVisibility(View.VISIBLE);
                        emptyDataNews.setVisibility(View.GONE);

                        handler.postDelayed(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                getNews(categoryNews);
                            }
                        }, 500);

                    }
                });

                cat10BTN.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                        cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        markCat1BTN.setVisibility(View.GONE);
                        markCat2BTN.setVisibility(View.GONE);
                        markCat3BTN.setVisibility(View.GONE);
                        markCat4BTN.setVisibility(View.GONE);
                        markCat5BTN.setVisibility(View.GONE);
                        markCat6BTN.setVisibility(View.GONE);
                        markCat7BTN.setVisibility(View.GONE);
                        markCat8BTN.setVisibility(View.GONE);
                        markCat9BTN.setVisibility(View.GONE);
                        markCat10BTN.setVisibility(View.VISIBLE);
                        markCat11BTN.setVisibility(View.GONE);
                        markCat12BTN.setVisibility(View.GONE);
                        categoryNews = "technology";
                        categoryNewsLabel = "Teknologi";
                        categoryChoiceTV.setText(categoryNewsLabel);

                        sourceLabelPart.setVisibility(View.GONE);
                        dataNewsRV.setVisibility(View.GONE);
                        loadingNewsPart.setVisibility(View.VISIBLE);
                        emptyDataNews.setVisibility(View.GONE);

                        handler.postDelayed(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                getNews(categoryNews);
                            }
                        }, 500);

                    }
                });

                cat11BTN.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                        cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        markCat1BTN.setVisibility(View.GONE);
                        markCat2BTN.setVisibility(View.GONE);
                        markCat3BTN.setVisibility(View.GONE);
                        markCat4BTN.setVisibility(View.GONE);
                        markCat5BTN.setVisibility(View.GONE);
                        markCat6BTN.setVisibility(View.GONE);
                        markCat7BTN.setVisibility(View.GONE);
                        markCat8BTN.setVisibility(View.GONE);
                        markCat9BTN.setVisibility(View.GONE);
                        markCat10BTN.setVisibility(View.GONE);
                        markCat11BTN.setVisibility(View.VISIBLE);
                        markCat12BTN.setVisibility(View.GONE);
                        categoryNews = "tourism";
                        categoryNewsLabel = "Pariwisata";
                        categoryChoiceTV.setText(categoryNewsLabel);

                        sourceLabelPart.setVisibility(View.GONE);
                        dataNewsRV.setVisibility(View.GONE);
                        loadingNewsPart.setVisibility(View.VISIBLE);
                        emptyDataNews.setVisibility(View.GONE);

                        handler.postDelayed(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                getNews(categoryNews);
                            }
                        }, 500);

                    }
                });

                cat12BTN.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        cat1BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat2BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat3BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat4BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat5BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat6BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat7BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat8BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat9BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat10BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat11BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option));
                        cat12BTN.setBackground(ContextCompat.getDrawable(NewsActivity.this, R.drawable.shape_option_choice));
                        markCat1BTN.setVisibility(View.GONE);
                        markCat2BTN.setVisibility(View.GONE);
                        markCat3BTN.setVisibility(View.GONE);
                        markCat4BTN.setVisibility(View.GONE);
                        markCat5BTN.setVisibility(View.GONE);
                        markCat6BTN.setVisibility(View.GONE);
                        markCat7BTN.setVisibility(View.GONE);
                        markCat8BTN.setVisibility(View.GONE);
                        markCat9BTN.setVisibility(View.GONE);
                        markCat10BTN.setVisibility(View.GONE);
                        markCat11BTN.setVisibility(View.GONE);
                        markCat12BTN.setVisibility(View.VISIBLE);
                        categoryNews = "world";
                        categoryNewsLabel = "Dunia";
                        categoryChoiceTV.setText(categoryNewsLabel);

                        sourceLabelPart.setVisibility(View.GONE);
                        dataNewsRV.setVisibility(View.GONE);
                        loadingNewsPart.setVisibility(View.VISIBLE);
                        emptyDataNews.setVisibility(View.GONE);

                        handler.postDelayed(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                bottomSheet.dismissSheet();
                                getNews(categoryNews);
                            }
                        }, 500);

                    }
                });

            }
        }, 100);

    }

    private void newsVisitor() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "https://hrisgelora.co.id/api/news_visitor";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        // response
                        JSONObject data = null;
                        try {
                            Log.d("Success.Response", response);
                            data = new JSONObject(response);
                            String status = data.getString("status");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        connectionFailed();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nik", sharedPrefManager.getSpNik());
                return params;
            }
        };

        requestQueue.add(postRequest);

    }

    private void connectionFailed(){
        CookieBar.build(NewsActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    public void onBackPressed() {
        if(bottomSheet.isSheetShowing()){
            bottomSheet.dismissSheet();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

}