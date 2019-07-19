package com.example.accountapp;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    Button searchBtn;
    EditText inputCity;
    TextView textCity;
    TextView textTemperature;
    TextView textInfo;
    TextView textDirect;
    TextView textPower;
    TextView textAqi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        searchBtn = findViewById(R.id.btn_weather);
        inputCity = findViewById(R.id.input_city);
        textCity = findViewById(R.id.text_city);
        textTemperature = findViewById(R.id.text_temperature);
        textInfo = findViewById(R.id.text_info);
        textDirect = findViewById(R.id.text_direct);
        textPower = findViewById(R.id.text_power);
        textAqi = findViewById(R.id.text_aqi);

        net1();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                net1();
            }
        });
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    JSONObject resultJson = (JSONObject) msg.obj;
                    try {
                        String city = resultJson.getString("city");
                        JSONObject realtime = resultJson.getJSONObject("realtime");


                        String temperature = realtime.getString("temperature");
                        String info = realtime.getString("info");
                        String direct = realtime.getString("direct");
                        String power = realtime.getString("power");
                        String aqi = realtime.getString("aqi");

                        textCity.setText(city);
                        textTemperature.setText(temperature + " ℃");
                        textInfo.setText(info);
                        textDirect.setText(direct);
                        textPower.setText(power);
                        textAqi.setText(aqi);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;


            }
        }
    };



    /**get请求方式*/
    public void net1() {
        //创建网络处理的对象


        OkHttpClient client = new OkHttpClient.Builder()
                //设置读取数据的时间
                .readTimeout(5, TimeUnit.SECONDS)
                //对象的创建
                .build();

        String city = "桂林";
        if (!inputCity.getText().toString().equals("")){
            city = inputCity.getText().toString();
        }



        //创建一个网络请求的对象，如果没有写请求方式，默认的是get
        //在请求对象里面传入链接的URL地址
        Request request = new Request.Builder()
                .url("http://apis.juhe.cn/simpleWeather/query?city="+city+"&key=f8876714587565432f5f414e4bafffd9").build();

        //call就是我们可以执行的请求类
        Call call = client.newCall(request);
        //异步方法，来执行任务的处理，一般都是使用异步方法执行的
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {


                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject resultJson = jsonObject.getJSONObject("result");

                    Message message = handler.obtainMessage();
                    message.what = 1;
                    message.obj = resultJson;
                    handler.sendMessage(message);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("请求失败");
            }
        });
    }

}
