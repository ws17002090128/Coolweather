package com.example.admin.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProvinceActivity extends AppCompatActivity {

    public static final String CITY = "city";
    public static final String PROVINCE = "province";
    public static final String COUNTY = "county";
    private String currentlevel=PROVINCE;
    private int pid=0;
    private int cid=0;
    private List<Integer> pids=new ArrayList<>();
    private List<String> weatherids=new ArrayList<>();
    private List<String> data= new ArrayList<>();
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.listView=(ListView)findViewById(R.id.listview);
        final ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("点击了哪一个",""+position+":"+ProvinceActivity.this.pids.get(position)+":"+ProvinceActivity.this.data.get(position));
                if(currentlevel==PROVINCE){
                    pid=ProvinceActivity.this.pids.get(position);
                    currentlevel= CITY;
                }else if(currentlevel==CITY){
                    cid=ProvinceActivity.this.pids.get(position);
                    currentlevel=COUNTY;
                }else{
                    Intent intent=new Intent(ProvinceActivity.this,WeatherActivity.class);
                    intent.putExtra("weatherid",ProvinceActivity.this.weatherids.get(position));
                    startActivity(intent);
                }

                getData(adapter);
            }
        });
        getData(adapter);
    }
    private void getData(final ArrayAdapter<String> adapter){
        String weatherUrl =currentlevel== PROVINCE?"http://guolin.tech/api/china":(currentlevel==CITY ?"http://guolin.tech/api/china/"+pid:"http://guolin.tech/api/china/"+pid+"/"+cid);
        HttpUtil.sendOkHttpRequest(weatherUrl,new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                parseJSONObject(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void parseJSONObject(String responseText) {
        JSONArray jsonArray = null;
        this.data.clear();
        this.pids.clear();
        try {
            jsonArray = new JSONArray(responseText);
            String[] result = new String[jsonArray.length()];
            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonObject =null ;
                jsonObject=jsonArray.getJSONObject(i);
                this.data.add(jsonObject.getString("name"));
                    this.pids.add(jsonObject.getInt("id"));
                   if(jsonObject.has("weather_id")){
                        this.weatherids.add(jsonObject.getString("weather_id"));
                };
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}