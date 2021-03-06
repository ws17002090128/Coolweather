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

public class CountyActivity extends AppCompatActivity {
    private ListView listView;
    private String[] data={"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""};
    private TextView textView;
    private String[] weatherid={"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Intent intent=getIntent();
        final int cid=intent.getIntExtra("cid",0);
        final int pid=intent.getIntExtra("pid",0);
        Log.i("我们接收到的id",""+cid);
       this.textView=(TextView)findViewById(R.id.main3);

        this.listView=(ListView)findViewById(R.id.listview);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("点击了哪一个",""+position+":"+CountyActivity.this.weatherid[position]+":"+CountyActivity.this.data[position]);
                Intent intent=new Intent(CountyActivity.this,WeatherActivity.class);
                intent.putExtra("weatherid",CountyActivity.this.weatherid[position]);
                intent.putExtra("cid",cid);
                intent.putExtra("pid",pid);
                startActivity(intent);
            }
        });
        String weatherUrl = "http://guolin.tech/api/china/"+pid+"/"+cid;
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
                        textView.setText(responseText);
                    }
                });

            }
        });
    }
    private void parseJSONObject(String responseText) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(responseText);
            String[] result = new String[jsonArray.length()];
            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject jsonObject =null ;
                jsonObject=jsonArray.getJSONObject(i);
                this.data[i]=jsonObject.getString("name");
                this.weatherid[i]=jsonObject.getString("weather_id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
