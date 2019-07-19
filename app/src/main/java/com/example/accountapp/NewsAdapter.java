package com.example.accountapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsAdapter extends BaseAdapter {

    private List<NewsData> datas = new ArrayList<>();

    private Context context;
    private LayoutInflater inflater;

    public NewsAdapter(Context context,List<NewsData> datas){
        this.datas = datas;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            convertView = inflater.inflate(R.layout.cell_list_new,null);
            convertView.setTag(new NewsHolder(convertView));
        }

        initViews((NewsData) getItem(position),(NewsHolder)convertView.getTag());


        return convertView;
    }

    private void initViews(NewsData data,NewsHolder holder){

        holder.ivImg.setTag(data.getNewsImgUrl());
        holder.tvTitle.setText(data.getNewsTitle());
        holder.tvData.setText(data.getNewsDate());

        getImage(this.context,data.getNewsImgUrl(),holder.ivImg);


    }




    protected class NewsHolder{
        private ImageView ivImg;
        private TextView tvTitle;
        private TextView tvData;


        public NewsHolder(View view){
            ivImg = view.findViewById(R.id.iv_img);
            tvTitle = view.findViewById(R.id.tv_title);
            tvData = view.findViewById(R.id.tv_date);
        }
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
        /**
         * 检测图片的Tag值 ,如果根请求的地址相同 才做图片的网络请求.
         */
        if (imageView.getTag().toString().equals(imgUrl)) {
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
}



