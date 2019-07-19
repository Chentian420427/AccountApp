package com.example.accountapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //新闻
        final View NewsView = findViewById(R.id.menu_news);
        NewsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });

        //笑话大全
        final View HappyView = findViewById(R.id.menu_happy);
        HappyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, HappyActivity.class);
                startActivity(intent);
            }
        });

        //天气查询
        final View WeatherView = findViewById(R.id.menu_weather);
        WeatherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, WeatherActivity.class);
                startActivity(intent);
            }
        });


        //历史今天
        final View TodayView = findViewById(R.id.menu_today);
        TodayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, TodayActivity.class);
                startActivity(intent);
            }
        });


        //关于
        final View AboutView = findViewById(R.id.menu_about);
        AboutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        //更多
        final View MoreView = findViewById(R.id.menu_more);
        MoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"即将到来 ...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
