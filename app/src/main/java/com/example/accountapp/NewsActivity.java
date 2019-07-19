package com.example.accountapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NewsActivity extends AppCompatActivity {

    private String TAG = "NewsActivity";

    private ListView listView;
    private List<NewsData> datas;

    private NewsAdapter adapter;

    public int SUCCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        listView = findViewById(R.id.listView_news);
        datas = new ArrayList<>();

        net1();

        adapter = new NewsAdapter(this,datas);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(NewsActivity.this,NewsInfoActivity.class);

                intent.putExtra("newsTitle",datas.get(position).getNewsTitle());
                intent.putExtra("newsDate",datas.get(position).getNewsDate());
                intent.putExtra("newsImgUrl",datas.get(position).getNewsImgUrl());
                intent.putExtra("newsUrl",datas.get(position).getNewsUrl());

                startActivity(intent);
            }
        });

    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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
                .url("http://v.juhe.cn/toutiao/index?type=top&key=112114c805ceeefd2b76e3dae2d7be67").build();

        //call就是我们可以执行的请求类
        Call call = client.newCall(request);
        //异步方法，来执行任务的处理，一般都是使用异步方法执行的
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               // Log.d(TAG, response.body().string());
                //String data = response.body().string();

                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject jsonObject2 = jsonObject.getJSONObject("result");
                    JSONArray jsonArray = jsonObject2.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        NewsData news = new NewsData();
                        news.setNewsTitle(item.getString("title"));
                        news.setNewsDate(item.getString("date"));
                        news.setNewsImgUrl(item.getString("thumbnail_pic_s"));
                        news.setNewsUrl(item.getString("url"));

                        datas.add(news);

                    }

                    Message message = handler.obtainMessage();
                    message.what = 1;
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








