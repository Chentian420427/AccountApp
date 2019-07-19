package com.example.accountapp;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TodayActivity extends AppCompatActivity {

    TextView textDate;
    TextView textTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        textDate = findViewById(R.id.text_date);
        textTitle = findViewById(R.id.text_title);

        net1();
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    JSONArray array = (JSONArray) msg.obj;
                    try {

                        Random random = new Random();
                        int index = random.nextInt(array.length());

                        JSONObject item = array.getJSONObject(index);

                        String date = item.getString("date");
                        String title = item.getString("title");

                        textDate.setText(date);
                        textTitle.setText(title);



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

        SimpleDateFormat format = new SimpleDateFormat("M/dd");
        String date = format.format(new Date());



        //创建一个网络请求的对象，如果没有写请求方式，默认的是get
        //在请求对象里面传入链接的URL地址
        Request request = new Request.Builder()
                .url("http://v.juhe.cn/todayOnhistory/queryEvent.php?date="+date+"&key=c96cb005e73e3d3e7fb2596fa5e06ed5").build();

        //call就是我们可以执行的请求类
        Call call = client.newCall(request);
        //异步方法，来执行任务的处理，一般都是使用异步方法执行的
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {


                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray resultArray = jsonObject.getJSONArray("result");




                    Message message = handler.obtainMessage();
                    message.what = 1;
                    message.obj = resultArray;
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
