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

public class CityActivity extends AppCompatActivity {
    private int[] cids=new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private ListView listView;
    private String[] data={"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""};
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        final int pid=intent.getIntExtra("pid",0);
        Log.i("我们接收到的id",""+pid);
        this.textView=(TextView)findViewById(R.id.main1);
        this.listView=(ListView)findViewById(R.id.listview);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("点击了哪一个",""+position+":"+CityActivity.this.cids[position]+":"+CityActivity.this.data[position]);
                Intent intent=new Intent(CityActivity.this,CountyActivity.class);
                intent.putExtra("cid",CityActivity.this.cids[position]);
                intent.putExtra("pid",pid);
                startActivity(intent);
            }
        });
        String weatherId="CN101210501";
        String weatherUrl = "http://guolin.tech/api/china/"+pid;
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
                this.cids[i]=jsonObject.getInt("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
