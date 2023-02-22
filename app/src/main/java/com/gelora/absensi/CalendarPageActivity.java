package com.gelora.absensi;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.gelora.absensi.support.StatusBarColorManager;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarPageActivity extends AppCompatActivity {

    CompactCalendarView compactCalendarView;
    private StatusBarColorManager mStatusBarColorManager;
    TextView eventCalender, yearTV, monthTV, dayDateTV, monthDateTV, yearDateTV;
    LinearLayout prevBTN, nextBTN, backBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_page);

        dayDateTV = findViewById(R.id.day_date);
        monthDateTV = findViewById(R.id.month_date);
        yearDateTV = findViewById(R.id.year_date);
        backBTN = findViewById(R.id.back_btn);

        mStatusBarColorManager = new StatusBarColorManager(this);
        mStatusBarColorManager.setStatusBarColor(Color.BLACK, true, false);

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        calendarPart();

        String bulanName;
        switch (getDateM()) {
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

        dayDateTV.setText(getDateD());
        monthDateTV.setText(bulanName.toUpperCase());
        yearDateTV.setText(getDateY());

    }

    private void calendarPart(){
        monthTV = findViewById(R.id.month_calender);
        yearTV = findViewById(R.id.year_calender);
        eventCalender = findViewById(R.id.event_calender);
        prevBTN = findViewById(R.id.prevBTN);
        nextBTN = findViewById(R.id.nextBTN);
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            Typeface typeface = ResourcesCompat.getFont(CalendarPageActivity.this, R.font.roboto);
            eventCalender.setTypeface(typeface);
        }

        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        String month = String.valueOf(compactCalendarView.getFirstDayOfCurrentMonth()).substring(4,7);

        int maxLengthYear = Integer.parseInt(String.valueOf(String.valueOf(compactCalendarView.getFirstDayOfCurrentMonth()).length()));
        int minLengthYear = maxLengthYear - 4;

        String year = String.valueOf(compactCalendarView.getFirstDayOfCurrentMonth()).substring(minLengthYear,maxLengthYear);
        String bulanName;
        switch (month) {
            case "Jan":
                bulanName = "Januari";
                break;
            case "Feb":
                bulanName = "Februari";
                break;
            case "Mar":
                bulanName = "Maret";
                break;
            case "Apr":
                bulanName = "April";
                break;
            case "May":
                bulanName = "Mei";
                break;
            case "Jun":
                bulanName = "Juni";
                break;
            case "Jul":
                bulanName = "Juli";
                break;
            case "Aug":
                bulanName = "Agustus";
                break;
            case "Sep":
                bulanName = "September";
                break;
            case "Oct":
                bulanName = "Oktober";
                break;
            case "Nov":
                bulanName = "November";
                break;
            case "Dec":
                bulanName = "Desember";
                break;
            default:
                bulanName = "Not found!";
                break;
        }

        monthTV.setText(bulanName);
        yearTV.setText(year);

        // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
        getEventCalender();

        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @SuppressLint("InlinedApi")
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
                if(events.size()<=0){
                    eventCalender.setText("");
                } else {
                    eventCalender.setText(String.valueOf(events.get(0).getData()));
                }
            }

            @SuppressLint("InlinedApi")
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
                String month = String.valueOf(firstDayOfNewMonth).substring(4,7);
                int maxLengthYear = Integer.parseInt(String.valueOf(String.valueOf(firstDayOfNewMonth).length()));
                int minLengthYear = maxLengthYear - 4;

                String year = String.valueOf(firstDayOfNewMonth).substring(minLengthYear,maxLengthYear);
                String bulanName;
                switch (month) {
                    case "Jan":
                        bulanName = "Januari";
                        break;
                    case "Feb":
                        bulanName = "Februari";
                        break;
                    case "Mar":
                        bulanName = "Maret";
                        break;
                    case "Apr":
                        bulanName = "April";
                        break;
                    case "May":
                        bulanName = "Mei";
                        break;
                    case "Jun":
                        bulanName = "Juni";
                        break;
                    case "Jul":
                        bulanName = "Juli";
                        break;
                    case "Aug":
                        bulanName = "Agustus";
                        break;
                    case "Sep":
                        bulanName = "September";
                        break;
                    case "Oct":
                        bulanName = "Oktober";
                        break;
                    case "Nov":
                        bulanName = "November";
                        break;
                    case "Dec":
                        bulanName = "Desember";
                        break;
                    default:
                        bulanName = "Not found!";
                        break;
                }

                monthTV.setText(bulanName);
                yearTV.setText(year);
                eventCalender.setText("");

            }
        });

        prevBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollLeft();
            }
        });

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollRight();
            }
        });

    }

    private void getEventCalender() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        final String url = "https://geloraaksara.co.id/absen-online/api/holiday";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("PaRSE JSON", response + "");
                        try {
                            JSONArray data = response.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject event = data.getJSONObject(i);
                                String nama = event.getString("nama");
                                String tanggal = event.getString("tanggal");

                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = sdf.parse(tanggal);
                                long millis = date.getTime();
                                Event ev1 = new Event(Color.RED, millis, nama);
                                compactCalendarView.addEvent(ev1);
                            }


                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                connectionFailed();
            }
        });

        requestQueue.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void connectionFailed(){
        CookieBar.build(CalendarPageActivity.this)
                .setTitle("Perhatian")
                .setMessage("Koneksi anda terputus!")
                .setTitleColor(R.color.colorPrimaryDark)
                .setMessageColor(R.color.colorPrimaryDark)
                .setBackgroundColor(R.color.warningBottom)
                .setIcon(R.drawable.warning_connection_mini)
                .setCookiePosition(CookieBar.BOTTOM)
                .show();
    }

    private String getDateD() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateM() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDateY() {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

}