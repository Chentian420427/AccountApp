package com.example.accountapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsInfoActivity extends AppCompatActivity {



    private WebView webView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_info);

        initViews();
    }

    /**
     * 初始化数据
     */
    public void initViews(){


        webView = (WebView) this.findViewById(R.id.wv_content);

        /**
         * 获得传递过来的数据
         */
        Intent intent = this.getIntent();

        String newsUrl = intent.getStringExtra("newsUrl");


        //getImage(this, newsImgUrl, ivImg);

        /**
         * 显示新闻信息
         */

        webView.loadUrl(newsUrl);

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    HashMap<String,Object> map = (HashMap<String, Object>) msg.obj;
                    byte[] picture = (byte[]) map.get("picture");
                    ImageView imageView = (ImageView) map.get("imageView");
                    Bitmap bitmap = BitmapFactory.decodeByteArray(picture,0,picture.length);

                    imageView.setImageBitmap(bitmap);


            }
        }
    };


    public void getImage(Context context,String imgUrl,final ImageView imageView){

        OkHttpClient client = new OkHttpClient.Builder()
                //设置读取数据的时间
                .readTimeout(5, TimeUnit.SECONDS)
                //对象的创建
                .build();
        //创建一个网络请求的对象，如果没有写请求方式，默认的是get
        //在请求对象里面传入链接的URL地址
        Request request = new Request.Builder()
                .url(imgUrl).build();

        //call就是我们可以执行的请求类
        Call call = client.newCall(request);
        //异步方法，来执行任务的处理，一般都是使用异步方法执行的
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Log.d(TAG, response.body().string());
                //String data = response.body().string();

                try {
                    byte[] picture_bt = response.body().bytes();

                    Message message = handler.obtainMessage();
                    Map<String,Object> map = new HashMap<>();
                    map.put("picture",picture_bt);
                    map.put("imageView",imageView);
                    message.obj = map;

                    message.what = 1;
                    handler.sendMessage(message);



                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

}
