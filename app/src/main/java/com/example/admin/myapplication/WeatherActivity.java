package com.example.admin.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        this.textview=(TextView)findViewById(R.id.textview);
        String weatherId=getIntent().getStringExtra("weatherid");

        String weatherUrl = "http://guolin.tech/api/weather?cityid="+weatherId;
        HttpUtil.sendOkHttpRequest(weatherUrl,new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textview.setText(responseText);
                    }
                });

            }


        });
    }
}
