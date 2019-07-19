package com.example.accountapp;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class HappyActivity extends AppCompatActivity {

    Button reloadBtn;
    TextView happyContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy);

        reloadBtn = findViewById(R.id.reload_btn);
        happyContent = findViewById(R.id.happy_content);

        net1();

        reloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                net1();
                System.out.println("刷新");
            }
        });

    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String content = (String) msg.obj;
                    happyContent.setText(content);
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
        //创建一个网络请求的对象，如果没有写请求方式，默认的是get
        //在请求对象里面传入链接的URL地址
        Request request = new Request.Builder()
                .url("http://v.juhe.cn/joke/randJoke.php?key=209d9c24f67f712c54a02e7fa63a37ce").build();

        //call就是我们可以执行的请求类
        Call call = client.newCall(request);
        //异步方法，来执行任务的处理，一般都是使用异步方法执行的
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {


                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    JSONObject data = jsonArray.getJSONObject(0);
                    String content = data.getString("content");

                    Message message = handler.obtainMessage();
                    message.what = 1;
                    message.obj = content;
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
